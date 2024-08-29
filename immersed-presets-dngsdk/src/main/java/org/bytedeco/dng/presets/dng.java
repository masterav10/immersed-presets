package org.bytedeco.dng.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
        inherit = {
            windows.class
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.dng", 
        global = "org.bytedeco.global.dng",
        value = {
            @Platform(
                include = {
                    "dng_uncopyable.h",
                    // "dng_deprecated_flags.h",
                    // "dng_flags.h",
                    "dng_types.h",
                    "dng_tag_values.h",
                    "dng_tag_types.h",
                    "dng_sdk_limits.h",
                    "dng_rational.h",
                    // "dng_pthread.h",
                    "dng_mutex.h",
                    "dng_errors.h",
                    "dng_exceptions.h",
                    "dng_safe_arithmetic.h",
                    "dng_classes.h",
                    "dng_memory.h",
                    "dng_utils.h",
                    "dng_string.h",
                    "dng_point.h",
                    "dng_auto_ptr.h",
                    "dng_stream.h",
                    "dng_fingerprint.h",
                    "dng_area_task.h",
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
        infoMap.put(new Info(
                "kMetadataSubset_CopyrightOnly", 
                "kMetadataSubset_CopyrightAndContact",
                "kMetadataSubset_All", 
                "kMetadataSubset_AllExceptLocationInfo", 
                "kMetadataSubset_AllExceptCameraInfo", 
                "kMetadataSubset_AllExceptCameraAndLocation", 
                "kMetadataSubset_AllExceptCameraRawInfo",
                "kMetadataSubset_AllExceptCameraRawInfoAndLocation")
               .cppTypes("int").translate(false));

        infoMap.put(new Info("hypot", "DNG_ALWAYS_INLINE", "MULUH", "MULSH")
               .cppTypes().annotations());
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
