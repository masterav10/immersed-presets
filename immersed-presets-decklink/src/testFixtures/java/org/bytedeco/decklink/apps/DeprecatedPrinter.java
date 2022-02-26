package org.bytedeco.decklink.apps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.bytedeco.decklink.presets.decklink;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;

public class DeprecatedPrinter
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
             .filter(name -> name.contains("_v"))
             .reduce((a, b) -> a + "," + b)
             .ifPresent(s ->
             {
                 System.out.format("infoMap.put(new Info(%s)\n", s);
                 System.out.println(".skip();");
             });
    }
}
