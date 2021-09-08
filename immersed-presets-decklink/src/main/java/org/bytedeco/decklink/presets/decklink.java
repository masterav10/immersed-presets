package org.bytedeco.decklink.presets;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
        inherit = {
            com.class,
            windef.class
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.decklink", 
        global = "org.bytedeco.global.decklink",
        value = {
            @Platform(
                includepath = {
                    "C:/Tools/Blackmagic DeckLink SDK 12.1/Win/include"
                }, 
                include = {
                    "DeckLinkAPI_h.h"
                }
            )
        }
)
@NoException
public class decklink implements InfoMapper
{   
    @Override
    public void map(InfoMap infoMap)
    {           
        infoMap.put(new Info("EXTERN_C", "MIDL_INTERFACE", "__clrcall", "extern \"C++\"")
                .annotations().cppTypes());
        
        infoMap.put(new Info("BSTR")
                .cast().valueTypes("CharPointer", "CharBuffer", "char[]").pointerTypes("PointerPointer"));
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
    }
}
