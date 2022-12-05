package org.bytedeco.xinput.preset;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
    inherit = {
        windows.class,
    },
    names = {"windows-x86_64"}, 
    target = "org.bytedeco.xinput", 
    global = "org.bytedeco.xinput.global.xinput",
    value = {
        @Platform(
            include = {
                "XInput.h"
            },
            link = {
                "Xinput", "Xinput9_1_0"
            }
        )
    }
)
@NoException
//@formatter:on
public class xinput implements InfoMapper
{
    @Override
    public void map(InfoMap infoMap)
    {
        // no-op
    }

}
