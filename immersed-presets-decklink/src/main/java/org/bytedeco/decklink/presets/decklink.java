package org.bytedeco.decklink.presets;

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
            windows.class,
            com.class,
            windef.class
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.decklink", 
        global = "org.bytedeco.global.decklink",
        value = {
            @Platform(
                include = {
                    "DeckLinkAPI_h.h", "DeckLinkAPI_i.c", "com.h", "windows-com.cpp"
                },
                define={
                     "NO_JNI_DETACH_THREAD", "JNI_ATTACH_AS_DAEMON"
                }
            )
        }
)
@NoException
public class decklink implements InfoMapper, LoadEnabled
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
        
        // Deprecated interfaces.
        infoMap.put(new Info("CBMDStreamingDiscovery_v10_8","CDeckLinkDiscovery_v10_11","CDeckLinkDiscovery_v10_8","CDeckLinkGLScreenPreviewHelper_v7_6",
                "CDeckLinkIterator_v10_11","CDeckLinkIterator_v10_8","CDeckLinkIterator_v8_0","CDeckLinkVideoConversion_v7_6",
                "IDeckLinkAttributes_v10_11","IDeckLinkAudioInputPacket_v7_1","IDeckLinkConfiguration_v10_11",
                "IDeckLinkConfiguration_v10_2","IDeckLinkConfiguration_v10_4","IDeckLinkConfiguration_v10_9",
                "IDeckLinkConfiguration_v7_6","IDeckLinkDeckControlStatusCallback_v8_1","IDeckLinkDeckControl_v7_9",
                "IDeckLinkDeckControl_v8_1","IDeckLinkDisplayModeIterator_v7_1","IDeckLinkDisplayModeIterator_v7_6",
                "IDeckLinkDisplayMode_v7_1","IDeckLinkDisplayMode_v7_6","IDeckLinkEncoderConfiguration_v10_5",
                "IDeckLinkEncoderInput_v10_11","IDeckLinkGLScreenPreviewHelper_v7_6","IDeckLinkInputCallback_v11_5_1",
                "IDeckLinkInputCallback_v7_1","IDeckLinkInputCallback_v7_3","IDeckLinkInputCallback_v7_6","IDeckLinkInput_v10_11",
                "IDeckLinkInput_v11_4","IDeckLinkInput_v11_5_1","IDeckLinkInput_v7_1","IDeckLinkInput_v7_3","IDeckLinkInput_v7_6",
                "IDeckLinkInput_v9_2","IDeckLinkIterator_v8_0","IDeckLinkMutableVideoFrame_v7_6","IDeckLinkNotification_v10_11",
                "IDeckLinkOutput_v10_11","IDeckLinkOutput_v11_4","IDeckLinkOutput_v7_1","IDeckLinkOutput_v7_3","IDeckLinkOutput_v7_6",
                "IDeckLinkOutput_v9_9","IDeckLinkScreenPreviewCallback_v7_6","IDeckLinkTimecode_v7_6","IDeckLinkVideoConversion_v7_6",
                "IDeckLinkVideoFrameMetadataExtensions_v11_5","IDeckLinkVideoFrame_v7_1","IDeckLinkVideoFrame_v7_6","IDeckLinkVideoInputFrame_v7_1",
                "IDeckLinkVideoInputFrame_v7_3","IDeckLinkVideoInputFrame_v7_6","IDeckLinkVideoOutputCallback_v7_1","IDeckLinkVideoOutputCallback_v7_6",
                "IDeckLink_v8_0")
                .skip());
        
        // Use CallbackPrinter to update this list
        infoMap.put(new Info("IBMDStreamingDeviceNotificationCallback","IBMDStreamingH264InputCallback","IDeckLinkAudioOutputCallback",
                             "IDeckLinkDeckControlStatusCallback","IDeckLinkDeckControlStatusCallback_v8_1","IDeckLinkDeviceNotificationCallback",
                             "IDeckLinkEncoderInputCallback","IDeckLinkInputCallback","IDeckLinkInputCallback_v11_5_1","IDeckLinkInputCallback_v7_1",
                             "IDeckLinkInputCallback_v7_3","IDeckLinkInputCallback_v7_6","IDeckLinkNotificationCallback","IDeckLinkProfileCallback",
                             "IDeckLinkScreenPreviewCallback","IDeckLinkScreenPreviewCallback_v7_6","IDeckLinkVideoOutputCallback",
                             "IDeckLinkVideoOutputCallback_v7_1","IDeckLinkVideoOutputCallback_v7_6")
                .purify(false).virtualize());
    }

    @Override
    public void init(ClassProperties properties)
    {
        final String srcs = System.getProperty("decklink.src");
        
        List<String> includePaths = properties.get("platform.includepath");
        
        if(!includePaths.contains(srcs))
        {
            includePaths.add(srcs);
        }
    }
}
