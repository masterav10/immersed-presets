package org.bytedeco.decklink.presets;

import java.util.List;

import org.bytedeco.decklink.DecklinkHelper.IDecklinkCallback;
import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;
import org.bytedeco.windows.presets.com;
import org.bytedeco.windows.presets.windef;

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
        helper = "DecklinkHelper",
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
//@formatter:on
@NoException
public class decklink implements InfoMapper, LoadEnabled
{
    @Override
    public void map(InfoMap infoMap)
    {
      //@formatter:off
        infoMap.put(new Info("REFIID")
                .cppTypes("const GUID &"));
        
        infoMap.put(new Info("IID", "CLSID")
                .cppTypes("GUID"));
        
        infoMap.put(new Info("__IID_DEFINED__")
                .define(true));
        
        infoMap.put(new Info("BSTR")
                .cast().valueTypes("CharPointer", "CharBuffer", "char[]").pointerTypes("PointerPointer"));
      //@formatter:on       

        deprecated(infoMap);
        callbacks(infoMap);
        commObjects(infoMap);
    }

    @Override
    public void init(ClassProperties properties)
    {
        final String srcs = System.getProperty("decklink.src");

        List<String> includePaths = properties.get("platform.includepath");

        if (!includePaths.contains(srcs))
        {
            includePaths.add(srcs);
        }
    }

    /**
     * Populates the provided info with deprecated interfaces.
     * 
     * <p>
     * The contents of this method are automatically generated. Regenerate the
     * bindings using gradle and then run InfoMapHelper in the test fixtures.
     * </p>
     * 
     * @param infoMap the map we are populating.
     */
    void deprecated(InfoMap infoMap)
    {
        infoMap.put(new Info("CBMDStreamingDiscovery_v10_8", "CDeckLinkDiscovery_v10_11", "CDeckLinkDiscovery_v10_8",
                "CDeckLinkGLScreenPreviewHelper_v7_6", "CDeckLinkIterator_v10_11", "CDeckLinkIterator_v10_8",
                "CDeckLinkIterator_v8_0", "CDeckLinkVideoConversion_v7_6", "IDeckLinkAttributes_v10_11",
                "IDeckLinkAudioInputPacket_v7_1", "IDeckLinkConfiguration_v10_11", "IDeckLinkConfiguration_v10_2",
                "IDeckLinkConfiguration_v10_4", "IDeckLinkConfiguration_v10_9", "IDeckLinkConfiguration_v7_6",
                "IDeckLinkDeckControlStatusCallback_v8_1", "IDeckLinkDeckControl_v7_9", "IDeckLinkDeckControl_v8_1",
                "IDeckLinkDisplayModeIterator_v7_1", "IDeckLinkDisplayModeIterator_v7_6", "IDeckLinkDisplayMode_v7_1",
                "IDeckLinkDisplayMode_v7_6", "IDeckLinkEncoderConfiguration_v10_5", "IDeckLinkEncoderInput_v10_11",
                "IDeckLinkGLScreenPreviewHelper_v7_6", "IDeckLinkInputCallback_v11_5_1", "IDeckLinkInputCallback_v7_1",
                "IDeckLinkInputCallback_v7_3", "IDeckLinkInputCallback_v7_6", "IDeckLinkInput_v10_11",
                "IDeckLinkInput_v11_4", "IDeckLinkInput_v11_5_1", "IDeckLinkInput_v7_1", "IDeckLinkInput_v7_3",
                "IDeckLinkInput_v7_6", "IDeckLinkInput_v9_2", "IDeckLinkIterator_v8_0",
                "IDeckLinkMutableVideoFrame_v7_6", "IDeckLinkNotification_v10_11", "IDeckLinkOutput_v10_11",
                "IDeckLinkOutput_v11_4", "IDeckLinkOutput_v7_1", "IDeckLinkOutput_v7_3", "IDeckLinkOutput_v7_6",
                "IDeckLinkOutput_v9_9", "IDeckLinkScreenPreviewCallback_v7_6", "IDeckLinkTimecode_v7_6",
                "IDeckLinkVideoConversion_v7_6", "IDeckLinkVideoFrameMetadataExtensions_v11_5",
                "IDeckLinkVideoFrame_v7_1", "IDeckLinkVideoFrame_v7_6", "IDeckLinkVideoInputFrame_v7_1",
                "IDeckLinkVideoInputFrame_v7_3", "IDeckLinkVideoInputFrame_v7_6", "IDeckLinkVideoOutputCallback_v7_1",
                "IDeckLinkVideoOutputCallback_v7_6", "IDeckLink_v8_0").skip());
    }

    /**
     * Populates the provided info map with callback COM objects, which need to be
     * extended by the client to be used. A utility superclass was created to
     * simplify behavior.
     * 
     * <p>
     * The contents of this method are automatically generated. Regenerate the
     * bindings using gradle and then run InfoMapHelper in the test fixtures.
     * </p>
     * 
     * @param infoMap the map we are populating.
     * @see           IDecklinkCallback
     */
    void callbacks(InfoMap infoMap)
    {
        infoMap.put(new Info("IBMDStreamingDeviceNotificationCallback", "IBMDStreamingH264InputCallback",
                "IDeckLinkAudioOutputCallback", "IDeckLinkDeckControlStatusCallback",
                "IDeckLinkDeviceNotificationCallback", "IDeckLinkEncoderInputCallback", "IDeckLinkInputCallback",
                "IDeckLinkNotificationCallback", "IDeckLinkProfileCallback", "IDeckLinkScreenPreviewCallback",
                "IDeckLinkVideoOutputCallback").purify(false)
                                               .virtualize()
                                               .base("IDecklinkCallback"));
    }

    /**
     * Populates the provided info map with COM objects. A utility superclass was
     * created to improve usability.
     * 
     * <p>
     * The contents of this method are automatically generated. Regenerate the
     * bindings using gradle and then run InfoMapHelper in the test fixtures.
     * </p>
     * 
     * @param infoMap the map we are populating.
     * @see           IDecklinkCallback
     */
    void commObjects(InfoMap infoMap)
    {
        infoMap.put(new Info("IBMDStreamingAudioPacket", "IBMDStreamingDeviceInput",
                "IBMDStreamingDeviceNotificationCallback", "IBMDStreamingDiscovery", "IBMDStreamingH264InputCallback",
                "IBMDStreamingH264NALPacket", "IBMDStreamingH264NALParser", "IBMDStreamingMPEG2TSPacket",
                "IBMDStreamingVideoEncodingMode", "IBMDStreamingVideoEncodingModePresetIterator", "IDeckLink",
                "IDeckLinkAPIInformation", "IDeckLinkAncillaryPacket", "IDeckLinkAncillaryPacketIterator",
                "IDeckLinkAudioInputPacket", "IDeckLinkAudioOutputCallback", "IDeckLinkConfiguration",
                "IDeckLinkDX9ScreenPreviewHelper", "IDeckLinkDeckControl", "IDeckLinkDeckControlStatusCallback",
                "IDeckLinkDeviceNotificationCallback", "IDeckLinkDiscovery", "IDeckLinkDisplayMode",
                "IDeckLinkDisplayModeIterator", "IDeckLinkEncoderConfiguration", "IDeckLinkEncoderInput",
                "IDeckLinkEncoderInputCallback", "IDeckLinkEncoderPacket", "IDeckLinkGLScreenPreviewHelper",
                "IDeckLinkHDMIInputEDID", "IDeckLinkInput", "IDeckLinkInputCallback", "IDeckLinkIterator",
                "IDeckLinkKeyer", "IDeckLinkMemoryAllocator", "IDeckLinkNotification", "IDeckLinkNotificationCallback",
                "IDeckLinkOutput", "IDeckLinkProfile", "IDeckLinkProfileAttributes", "IDeckLinkProfileCallback",
                "IDeckLinkProfileIterator", "IDeckLinkProfileManager", "IDeckLinkScreenPreviewCallback",
                "IDeckLinkStatus", "IDeckLinkTimecode", "IDeckLinkVideoConversion", "IDeckLinkVideoFrame",
                "IDeckLinkVideoFrame3DExtensions", "IDeckLinkVideoFrameAncillary",
                "IDeckLinkVideoFrameAncillaryPackets", "IDeckLinkVideoFrameMetadataExtensions",
                "IDeckLinkVideoOutputCallback", "IDeckLinkWPFDX9ScreenPreviewHelper").base("IDecklinkBase"));
    }

}
