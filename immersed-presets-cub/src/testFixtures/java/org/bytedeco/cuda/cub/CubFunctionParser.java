package org.bytedeco.cuda.cub;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bytedeco.cuda.presets.cudart;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;

public class CubFunctionParser
{
    private static final PrintStream out = System.out;

    public static void main(String[] args) throws IOException
    {
        Properties cudaProps = cudart.class.getAnnotation(Properties.class);

        for (Platform platform : cudaProps.value())
        {
            if (Arrays.asList(platform.value())
                      .contains("windows-x86_64"))
            {
                walkFiles(Paths.get(platform.includepath()[0]));
            }
        }
    }

    private static void walkFiles(Path includeDir) throws IOException
    {
        Path pathCubDevice = includeDir.resolve("cub")
                                       .resolve("device");

        if (!Files.exists(pathCubDevice))
        {
            return;
        }

        List<Path> files = Files.walk(pathCubDevice, 1)
                                .filter(p -> p.toString()
                                              .endsWith(".cuh"))
                                .collect(Collectors.toList());

        List<HeaderDefinition> headers = new ArrayList<>();

        for (Path file : files)
        {
            headers.add(processFile(file));
        }

        // print includes for cub.h
        out.println("// include in org.bytedeco.cuda.cub @Properties include");

        headers.stream()
               .map(header -> header.includeName(includeDir))
               .reduce((a, b) -> String.join(",\n", a, b))
               .ifPresent(out::println);

        out.println();
        out.println("// include in org.bytedeco.cuda.cub.presets.cub#map");
        // print comment with method
        headers.forEach(header ->
        {
            String name = header.getFileName();
            String method = name.substring(0, name.lastIndexOf('.'));

            out.println(String.format("%s(infoMap);", method));
        });

        out.println();
        out.println("");

        CubTemplates templates = new CubTemplates();

        final Set<String> ignoreFunctions = new HashSet<>(Arrays.asList("cub::DeviceHistogram::MultiHistogramEven"));
        ignoreFunctions.add("cub::DeviceHistogram::MultiHistogramEven");
        
        headers.forEach(header ->
        {
            String name = header.getFileName();
            String method = name.substring(0, name.lastIndexOf('.'));

            out.println();
            out.format("private static void %s(InfoMap infoMap)", method)
               .println();
            out.print("{");

            for (FunctionDefinition functionObj : header.functions())
            {
                final String definition = functionObj.toDefinition();

                out.println();
                out.format("    // %s", definition);
                out.println();

                templates.walk(functionObj)
                         .forEach(result ->
                         {
                             String output = result.toString();
                             out.format("    %s", output);
                             out.println();
                         });
            }

            out.println("}");
        });
    }

    private static HeaderDefinition processFile(Path cuh) throws IOException
    {
        List<String> lines = Files.readAllLines(cuh);

        HeaderDefinition.Builder headerBuilder = new HeaderDefinition.Builder().location(cuh);
        FunctionDefinition.Builder defBuilder = new FunctionDefinition.Builder().namespace("cub");

        boolean inPublic = true;
        boolean inFuncDef = false;
        StringBuilder builder = new StringBuilder();

        for (String line : lines)
        {
            line = line.trim();

            if (line.startsWith("struct"))
            {
                defBuilder.struct(line.substring(7));
            }

            if (line.equals("private:"))
            {
                inPublic = false;
            }

            if (line.equals("public:"))
            {
                inPublic = true;
            }

            if (!inPublic)
            {
                continue;
            }

            if (line.startsWith("template"))
            {
                inFuncDef = true;
                builder.delete(0, builder.length());
            }

            if (inFuncDef)
            {
                builder.append(line + " ");
            }

            if (inFuncDef && line.contains(")"))
            {
                inFuncDef = false;

                defBuilder.fromTextSignature(builder.toString());
                headerBuilder.addFunctions(defBuilder.build());
            }
        }

        return headerBuilder.build();
    }

}
