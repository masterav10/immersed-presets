package org.bytedeco.decklink.presets;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
    inherit = windows.class, 
    names = {"windows-x86_64"}, 
    target = "org.bytedeco.windef",
    global = "org.bytedeco.global.windef",
    value = {
        @Platform(
            include = {
                "windef.h",
                "winerror.h"
            }
        )
    }
)
@NoException
public class windef implements InfoMapper
{
    @Override
    public void map(InfoMap infoMap)
    {
        infoMap.put(new Info("DECLARE_HANDLE").skip());
    }
}
