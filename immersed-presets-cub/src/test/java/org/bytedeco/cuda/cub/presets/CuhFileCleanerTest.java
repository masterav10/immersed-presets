package org.bytedeco.cuda.cub.presets;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.bytedeco.cuda.presets.cudart;
import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.Loader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class CuhFileCleanerTest
{
    private ClassProperties properties;
    private Path testDir;

    @BeforeEach
    public void createProperties(@TempDir Path testDir)
    {
        final Properties props = Loader.loadProperties();

        this.properties = new ClassProperties(props);
        this.properties.load(cudart.class, true);
        this.properties.get("platform.include")
                       .add("<cub/device/device_adjacent_difference.cuh>");

        System.setProperty("cub.src", testDir.toAbsolutePath()
                                             .toString());

        this.testDir = testDir;
    }

    @AfterEach
    public void removeSystemProperty()
    {
        System.clearProperty("cub.src");
    }

    @Test
    public void loadInFile() throws IOException
    {
        CuhFileCleaner.clean(properties);

        List<String> expectedNames = Arrays.asList("device_adjacent_difference_modified.cuh");

        List<String> headers = expectedNames.stream()
                                            .map(s -> "<" + s + ">")
                                            .collect(Collectors.toList());

        assertThat(properties.get("platform.include")).containsAll(headers);

        assertThat(properties.get("platform.includepath")).contains(testDir.toString());

        List<String> headerNames = Files.walk(testDir)
                                        .filter(Files::isRegularFile)
                                        .map(f -> f.getFileName()
                                                   .toString())
                                        .collect(Collectors.toList());

        assertThat(headerNames).containsExactlyInAnyOrderElementsOf(expectedNames);
    }
}
