package org.bytedeco.decklink.presets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bytedeco.com.IUnknown;
import org.bytedeco.decklink.DecklinkHelper.IDecklinkBase;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;

public class FileLists
{
    /**
     * Gets all deprecated classes.
     * 
     * @return             a stream of file names, already in quotes.
     * @throws IOException
     */
    public static Stream<String> deprecated() throws IOException
    {
        Stream<String> s1 = walkFiles().filter(name -> name.contains("_v"))
                                       .sorted();
        Stream<String> s2 = existingDeprecated();

        return Stream.concat(s1, s2)
                     .sorted();
    }

    /**
     * Gets all callback classes that aren't deprecated.
     * 
     * @return             a stream of file names, already in quotes.
     * @throws IOException
     */
    public static Stream<String> callbacks() throws IOException
    {
        Set<String> deprecated = deprecated().collect(Collectors.toSet());

        return walkFiles().filter(name -> !deprecated.contains(name) && name.contains("Callback"))
                          .sorted();
    }

    /**
     * Gets remaining comm objects that are not deprecated or callbacks.
     * 
     * @return             a stream of file names, already in quotes.
     * @throws IOException
     */
    public static Stream<String> commObjects() throws IOException
    {
        Set<String> callbacks = deprecated().collect(Collectors.toSet());
        Set<String> deprecated = deprecated().collect(Collectors.toSet());

        return walkFiles(path ->
        {
            try
            {
                String contents = new String(Files.readAllBytes(path));

                final Class<?>[] types =
                { IUnknown.class, IDecklinkBase.class };

                for (Class<?> type : types)
                {
                    if (contents.contains("extends " + type.getSimpleName()))
                    {
                        return true;
                    }
                }

                return false;
            }
            catch (IOException e)
            {
                return false;
            }
        }).filter(name -> !deprecated.contains(name) && !callbacks.contains(name))

          .sorted();
    }

    private static final Stream<String> existingDeprecated()
    {
        decklink decklink = new decklink();

        InfoMap map = new InfoMap();
        decklink.deprecated(map);

        return map.keySet()
                  .stream()
                  .map(s -> String.join(s, "\"", "\""));
    }

    private static final Stream<String> walkFiles() throws IOException
    {
        return walkFiles(path -> true);
    }

    private static final Stream<String> walkFiles(Predicate<Path> customFilter) throws IOException
    {
        final Properties properties = decklink.class.getAnnotation(Properties.class);
        final String packageName = properties.target()
                                             .replace('.', File.separatorChar);

        final Path root = Paths.get("build", "javacpp", "java");

        Arrays.stream(packageName.split("."));

        return Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(customFilter)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(name -> name.substring(0, name.lastIndexOf('.')))
                    .map(name -> "\"" + name + "\"");
    }
}
