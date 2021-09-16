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
        inherit = {
            windows.class,
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
                },
                cinclude = {
                    "DeckLinkAPI_i.c"
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
        infoMap.clear();
     
        infoMap.put(new Info("REFIID")
                .cppTypes("const GUID &"));
        
        infoMap.put(new Info("IID")
                .cppTypes("GUID"));
        
        infoMap.put(new Info("__IID_DEFINED__")
                .define(true));
        
        infoMap.put(new Info("EXTERN_C", "MIDL_INTERFACE", "__clrcall", "extern \"C++\"")
                .annotations().cppTypes());
        
        infoMap.put(new Info("BSTR")
                .cast().valueTypes("CharPointer", "CharBuffer", "char[]").pointerTypes("PointerPointer"));
    }
}
