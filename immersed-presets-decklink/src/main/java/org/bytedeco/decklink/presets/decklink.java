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
                    "DeckLinkAPI_h.h", "DeckLinkAPI_i.c"
                },
                define="NO_JNI_DETACH_THREAD"
            )
        }
)
@NoException
public class decklink implements InfoMapper
{   
    @Override
    public void map(InfoMap infoMap)
    {   
        infoMap.put(new Info("REFIID")
                .cppTypes("const GUID &"));
        
        infoMap.put(new Info("IID", "CLSID")
                .cppTypes("GUID"));
        
        infoMap.put(new Info("__IID_DEFINED__")
                .define(true));
        
        infoMap.put(new Info("BSTR")
                .cast().valueTypes("CharPointer", "CharBuffer", "char[]").pointerTypes("PointerPointer"));
        
        // Use CallbackPrinter to update this list
        infoMap.put(new Info("IBMDStreamingDeviceNotificationCallback","IBMDStreamingH264InputCallback","IDeckLinkAudioOutputCallback",
                             "IDeckLinkDeckControlStatusCallback","IDeckLinkDeckControlStatusCallback_v8_1","IDeckLinkDeviceNotificationCallback",
                             "IDeckLinkEncoderInputCallback","IDeckLinkInputCallback","IDeckLinkInputCallback_v11_5_1","IDeckLinkInputCallback_v7_1",
                             "IDeckLinkInputCallback_v7_3","IDeckLinkInputCallback_v7_6","IDeckLinkNotificationCallback","IDeckLinkProfileCallback",
                             "IDeckLinkScreenPreviewCallback","IDeckLinkScreenPreviewCallback_v7_6","IDeckLinkVideoOutputCallback",
                             "IDeckLinkVideoOutputCallback_v7_1","IDeckLinkVideoOutputCallback_v7_6")
                .purify(false).virtualize());
    }
}
