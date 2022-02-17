package org.bytedeco.decklink.windows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.bytedeco.decklink.presets.decklink;
import org.bytedeco.javacpp.annotation.Properties;

public class CallbackPrinter
{
    public static void main(String[] args) throws ClassNotFoundException, IOException
    {
        final Properties properties = decklink.class.getAnnotation(Properties.class);
        final String packageName = properties.target()
                                             .replace('.', File.separatorChar);

        final Path root = Paths.get("build", "javacpp", "java");

        Arrays.stream(packageName.split("."));

        Files.walk(root)
             .filter(Files::isRegularFile)
             .map(Path::getFileName)
             .map(Path::toString)
             .map(name -> name.substring(0, name.lastIndexOf('.')))
             .map(name -> "\"" + name + "\"")
             .filter(name -> name.contains("Callback"))
             .reduce((a, b) -> a + "," + b)
             .ifPresent(System.out::println);
    }
}
