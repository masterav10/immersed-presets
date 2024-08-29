package org.bytedeco.dng;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HeaderPrinter
{
    private static final class Header
    {
        private final String source;
        private final List<String> dependencies;

        private Header(Path file) throws IOException
        {
            this.dependencies = new ArrayList<>();
            this.source = file.getFileName()
                              .toString();

            final Path root = file.getParent();

            addAll(file);

            for (int i = 0; i < dependencies.size(); i++)
            {
                String next = dependencies.get(i);
                Path path = root.resolve(next);

                if (Files.exists(path))
                {
                    addAll(path);
                }
            }
        }

        private void addAll(Path file) throws IOException
        {
            final List<String> lines = Files.readAllLines(file);

            for (String line : lines)
            {
                if (line.contains("#include") && !line.contains("<"))
                {
                    String header = line.replace("\"", "")
                                        .replace("#include ", "");

                    if (!dependencies.contains(header))
                    {
                        dependencies.add(header);
                    }
                }
            }
        }

        private boolean imports(Header header)
        {
            return dependencies.contains(header.source);
        }

        @Override
        public String toString()
        {
            return source;
        }
    }

    public static void main(String[] args) throws IOException
    {
        final Path relativePath = Paths.get("build", "unpacked", "dist", "dng_sdk_1_6", "dng_sdk", "source");

        Path source = Paths.get("")
                           .toAbsolutePath()
                           .resolve(relativePath);

        List<Path> files = Files.walk(source)
                                .filter(Files::isRegularFile)
                                .filter(p -> p.toString()
                                              .endsWith(".h"))
                                .collect(Collectors.toList());

        List<Header> headers = new ArrayList<>();
        List<Header> sortedList = new ArrayList<>();

        for (Path file : files)
        {
            Header header = new Header(file);
            headers.add(header);
        }

        for (Header header : headers)
        {
            int indexOfLastDependency = 0;

            for (int i = 0; i < sortedList.size(); i++)
            {
                Header sortedHeader = sortedList.get(i);

                if (header.imports(sortedHeader))
                {
                    indexOfLastDependency = i + 1;
                }
            }

            sortedList.add(indexOfLastDependency, header);
        }

        // printCompleteHeaders(source, files, sortedList);
        printHeadersFor(source, "dng_image_writer.h", sortedList);

        // Paths.get("C:\\Users\\Dan
        // Avila\\git\\immersed-presets\\immersed-presets-dngsdk\\build\\unpacked\\dist\\dng_sdk_1_6\\dng_sdk\\source");
    }

    private static void printCompleteHeaders(List<Header> sortedList) throws IOException
    {
        String out = sortedList.stream()
                               .map(s -> String.format("\"%s\"", s))
                               .reduce((s1, s2) -> String.format("%s,%n%s", s1, s2))
                               .orElse("");

        System.out.println(out);
    }

    private static void printHeadersFor(Path source, String headerName, List<Header> sortedList) throws IOException
    {
        Header header = new Header(source.resolve(headerName));

        List<String> realHeaders = new ArrayList<>(header.dependencies);
        realHeaders.removeIf(h -> !Files.exists(source.resolve(h)));
        realHeaders.add(header.source);

        String out = sortedList.stream()
                               .filter(h -> realHeaders.contains(h.source))
                               .map(s -> String.format("\"%s\"", s))
                               .reduce((s1, s2) -> String.format("%s,%n%s", s1, s2))
                               .orElse("");

        System.out.println(out);
    }
}
