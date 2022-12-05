package org.bytedeco.vigem.preset;

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
import org.bytedeco.xinput.preset.xinput;

//@formatter:off
@Properties(
      inherit = {
          windows.class, xinput.class
      },
      names = {"windows-x86_64"}, 
      target = "org.bytedeco.vigemclient", 
      global = "org.bytedeco.vigemclient.global.vigemclient",
      value = {
          @Platform(
              includepath = "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30133/include",
              linkpath = "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30133/lib/x64",
              include = {
                  "limits.h", "ViGEm/Common.h", "ViGEm/Client.h", "Internal.h"
              },
              link = {
                  "ViGEmClient"
              }
          )
      }
)
@NoException
//@formatter:on
public class vigemclient implements InfoMapper, LoadEnabled, vigemspec
{
    @Override
    public void init(ClassProperties properties)
    {
        final String prop = System.getProperty("nativelib.src");

        if (prop != null)
        {
            final String[] srcs = prop.split(";");

            List<String> includePaths = properties.get("platform.includepath");

            for (String src : srcs)
            {
                if (!includePaths.contains(src))
                {
                    includePaths.add(src);
                }
            }
        }

        final List<String> linkPaths = properties.get("platform.linkpath");

        for (Object keyObj : System.getProperties()
                                   .keySet())
        {
            final String key = keyObj.toString();

            if (key.startsWith("nativelib.bin"))
            {
                final String value = System.getProperty(key);

                if (!linkPaths.contains(value))
                {
                    linkPaths.add(value);
                }
            }
        }
    }

    @Override
    public void map(InfoMap infoMap)
    {
        //@formatter:off
        infoMap.put(new Info("VIGEM_API").cppTypes())
               .put(new Info("VIGEM_DEPRECATED").annotations("@Deprecated").cppTypes())
               .put(new Info("_I8_MIN", "_I8_MAX", "_UI8_MAX", "_I16_MIN", "_I16_MAX", "_UI16_MAX", "_I32_MIN",
                       "_I32_MAX", "_UI32_MAX", "DEVICE_IO_CONTROL_END", "DEVICE_IO_CONTROL_BEGIN").skip())
               .put(new Info("PVIGEM_TARGET").cast().valueTypes("PointerPointer<VIGEM_TARGET>"))
               .put(new Info("PVIGEM_CLIENT").cast().valueTypes("PointerPointer<VIGEM_CLIENT>"))
               .put(new Info("VIGEM_ERROR").cast().valueTypes("int"))
               .put(new Info("VIGEM_TARGET_TYPE", "VIGEM_TARGET_STATE", "_VIGEM_ERRORS", "VIGEM_ERROR").enumerate());
        //@formatter:on
    }
}
