package org.bytedeco.dng.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
        inherit = {
            javacpp.class
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.dng", 
        global = "org.bytedeco.global.dng",
        value = {
            @Platform(
                include = {
                    "dng_image_writer.h"
                },
                link = {
                    "dng_validate"
                }
            )
        }
)
//@formatter:on
@NoException
public class dng implements InfoMapper, LoadEnabled
{
    @Override
    public void map(InfoMap infoMap)
    {

    }

    @Override
    public void init(ClassProperties properties)
    {
        final String srcs = System.getProperty("dngsdk.src");

        List<String> includePaths = properties.get("platform.includepath");

        if (!includePaths.contains(srcs))
        {
            includePaths.add(srcs);
        }

        final List<String> linkPaths = properties.get("platform.linkpath");

        for (Object keyObj : System.getProperties()
                                   .keySet())
        {
            final String key = keyObj.toString();

            if (key.startsWith("dngsdk.bin"))
            {
                final String value = System.getProperty(key);

                if (!linkPaths.contains(value))
                {
                    linkPaths.add(value);
                }
            }
        }
    }
}
