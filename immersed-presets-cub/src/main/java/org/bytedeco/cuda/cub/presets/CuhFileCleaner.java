package org.bytedeco.cuda.cub.presets;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bytedeco.javacpp.ClassProperties;

/**
 * Utility class that purges deprecated methods that cause conflicts in javacpp.
 * 
 * @author Dan Avila
 */
public class CuhFileCleaner
{
    private static final class Definiton
    {
        private int start = -1;
        private int end = -1;
        private boolean deprecated = false;

        @Override
        public String toString()
        {
            return String.format("%d - %d: %s", start, end, deprecated);
        }
    }

    /**
     * This method modifies the incoming properties, and "cleans" header files so
     * that proper bindings can be made.
     * 
     * @param  properties  the current properties to be read and modified.
     * @throws IOException
     */
    public static final void clean(ClassProperties properties) throws IOException
    {
        String prop = System.getProperty("cub.src");

        Path outputLocation = Paths.get(prop)
                                   .toAbsolutePath();

        Files.createDirectories(outputLocation);

        final List<String> includePaths = properties.get("platform.includepath");

        if (includePaths.contains(outputLocation.toString()))
        {
            return;
        }

        final List<String> headers = properties.get("platform.include");

        for (int i = 0; i < headers.size(); i++)
        {
            String header = headers.get(i);

            if (!header.contains(".cuh") || header.contains("modified.cuh"))
            {
                continue;
            }

            Path relativeHeader = Paths.get(header.replaceAll("[<>]", ""));

            String headerInName = relativeHeader.getFileName()
                                                .toString();

            String ext = headerInName.substring(headerInName.lastIndexOf('.'));
            String headerOutName = headerInName.replace(ext, "_modified" + ext);
            Path headerOut = outputLocation.resolve(headerOutName);

            for (String includePath : includePaths)
            {
                Path headerIn = Paths.get(includePath)
                                     .resolve(relativeHeader);

                if (Files.exists(headerIn))
                {
                    clean(headerIn, headerOut);
                }
            }

            headers.set(i, String.format("<%s>", headerOutName));
        }

        includePaths.add(outputLocation.toString());
    }

    private static void clean(Path in, Path out) throws IOException
    {
        List<String> lines = Files.readAllLines(in);

        final List<Definiton> definitions = new ArrayList<>();
        Definiton definiton = new Definiton();

        int parenCount = 0;
        int parenOpenCount = 0;
        int parenClosedCount = 0;

        boolean isPublic = true;

        for (int i = 0; i < lines.size(); i++)
        {
            String line = lines.get(i);

            if (line.contains("private:"))
            {
                isPublic = false;
            }

            if (line.contains("public:"))
            {
                isPublic = true;
            }

            if (!isPublic || isComment(line))
            {
                continue;
            }

            // start of function
            if (line.contains("template"))
            {
                definiton.start = i;
            }

            if (definiton.start < 0)
            {
                continue;
            }

            if (line.contains("CUB_DETAIL_RUNTIME_DEBUG_SYNC_IS_NOT_SUPPORTED"))
            {
                definiton.deprecated = true;
            }

            int openCount = count(line, "{");
            int closedCount = count(line, "}");

            parenCount = parenCount + openCount + closedCount;
            parenOpenCount = parenOpenCount + openCount;
            parenClosedCount = parenClosedCount + closedCount;

            // we have found a complete function
            if (parenCount > 0 && parenOpenCount == parenClosedCount)
            {
                definiton.end = i;
                definitions.add(definiton);

                definiton = new Definiton();
                parenCount = parenOpenCount = parenClosedCount = 0;
            }
        }

        Collections.reverse(definitions);

        for (Definiton def : definitions)
        {
            if (def.deprecated)
            {
                lines.subList(def.start, def.end + 1)
                     .clear();
            }
        }

        Files.write(out, lines, StandardOpenOption.CREATE);
    }

    private static int count(String line, char value)
    {
        int index = -1;
        int count = 0;

        while ((index = line.indexOf(value, index + 1)) >= 0)
        {
            count++;
        }

        return count;
    }

    private static int count(String line, String value)
    {
        if (line.trim()
                .equalsIgnoreCase(value))
        {
            return 1;
        }

        return 0;
    }

    private static boolean isComment(String line)
    {
        String trimmed = line.trim();

        return trimmed.startsWith("/**") || trimmed.startsWith("*") || trimmed.endsWith("*/");
    }
}
