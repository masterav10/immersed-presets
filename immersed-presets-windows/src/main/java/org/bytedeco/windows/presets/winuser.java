package org.bytedeco.windows.presets;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
  inherit = windef.class, 
  names = {"windows-x86_64"}, 
  target = "org.bytedeco.winuser",
  global = "org.bytedeco.global.winuser",
  value = {
      @Platform(
          include = {
              "winuser-stub.h"
          }
      )
  }
)
@NoException
public class winuser implements InfoMapper
{
    @Override
    public void map(InfoMap infoMap)
    {
        infoMap.put(new Info("LPARAM").cast().valueTypes("long").pointerTypes("SizeTPointer"));
    }
    
}
//@formatter:on