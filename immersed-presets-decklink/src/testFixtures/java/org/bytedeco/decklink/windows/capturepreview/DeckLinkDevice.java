package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.global.com.*;
import static org.bytedeco.global.decklink.*;
import static org.bytedeco.global.windef.*;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkAudioInputPacket;
import org.bytedeco.decklink.IDeckLinkDisplayMode;
import org.bytedeco.decklink.IDeckLinkHDMIInputEDID;
import org.bytedeco.decklink.IDeckLinkInput;
import org.bytedeco.decklink.IDeckLinkInputCallback;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.decklink.IDeckLinkVideoInputFrame;
import org.bytedeco.decklink.windows.Utility;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

public class DeckLinkDevice extends IDeckLinkInputCallback
{
    private static enum DeviceError
    {
        NoError, EnableVideoInputFailed, StartStreamsFailed, ReenableVideoInputFailed,
    };

    private Consumer<DeviceError> m_errorListener;
    private IntConsumer m_videoFormatChangedCallback;

    private int m_refCount = 1;
    private IDeckLink m_deckLink;
    private IDeckLinkInput m_deckLinkInput;
    private IDeckLinkHDMIInputEDID m_deckLinkHDMIInputEDID;

    private boolean m_supportsFormatDetection = false;
    private boolean m_currentlyCapturing = false;
    private boolean m_applyDetectedInputMode = false;
    private String m_deviceName = null;

    public DeckLinkDevice(IDeckLink device)
    {
        this.m_deckLink = device;
        this.m_deckLinkInput = Utility.QueryInterface(device, IID_IDeckLinkInput(), IDeckLinkInput.class);

        this.m_supportsFormatDetection = false;

        if (m_deckLinkInput.isNull())
        {
            throw new IllegalArgumentException("DeckLink device does not have an input interface.");
        }
    }

    @Override
    public int QueryInterface(GUID iid, PointerPointer ppv)
    {
        int result = (int) E_NOINTERFACE;

        if (ppv == null || ppv.isNull())
        {
            return (int) E_INVALIDARG;
        }

        if (iid.equals(IID_IUnknown()))
        {
            ppv.put(this);
            AddRef();
            result = (int) S_OK;
        }
        else if (iid.equals(IID_IDeckLinkInputCallback()))
        {
            ppv.put(this);
            AddRef();
            result = (int) S_OK;
        }

        return result;
    }

    @Override
    public int AddRef()
    {
        return ++m_refCount;
    }

    @Override
    public int Release()
    {
        int newRefValue = --m_refCount;
        if (newRefValue == 0)
        {
            deallocate();
        }
        return newRefValue;
    }

    public boolean init()
    {

        IDeckLinkProfileAttributes deckLinkAttributes = Utility.QueryInterface(m_deckLink,
                IID_IDeckLinkProfileAttributes(), IDeckLinkProfileAttributes.class);

        if (deckLinkAttributes == null || deckLinkAttributes.isNull())
            return false;

        // Check if input mode detection is supported by the device.
        m_supportsFormatDetection = Utility.GetFlag(BMDDeckLinkSupportsInputFormatDetection, deckLinkAttributes);
        if (Utility.error() != S_OK)
            m_supportsFormatDetection = false;

        // Enable all EDID functionality if possible
        if (m_deckLinkHDMIInputEDID != null && !m_deckLinkHDMIInputEDID.isNull())
        {
            int allKnownRanges = bmdDynamicRangeSDR | bmdDynamicRangeHDRStaticPQ | bmdDynamicRangeHDRStaticHLG;
            m_deckLinkHDMIInputEDID.SetInt(bmdDeckLinkHDMIInputEDIDDynamicRange, allKnownRanges);
            m_deckLinkHDMIInputEDID.WriteToEDID();
        }

        // Get device name

        try (PointerPointer<CharPointer> pointer = new PointerPointer<CharPointer>(1L))
        {
            if (m_deckLink.GetDisplayName(pointer) == S_OK)
            {
                CharPointer tmp = pointer.get(CharPointer.class);
                this.m_deviceName = tmp.getString();
            }
            else
            {
                this.m_deviceName = "Decklink";
            }
        }

        return true;
    }

    @Override
    public int VideoInputFormatChanged(int notificationEvents, IDeckLinkDisplayMode newMode, int detectedSignalFlags)
    {
        int pixelFormat;

        // Restart capture with the new video mode if told to
        if (!m_applyDetectedInputMode)
            return (int) S_OK;

        if ((detectedSignalFlags & bmdDetectedVideoInputRGB444) == 1)
        {
            if ((detectedSignalFlags & bmdDetectedVideoInput8BitDepth) == 1)
                pixelFormat = bmdFormat8BitARGB;
            else if ((detectedSignalFlags & bmdDetectedVideoInput10BitDepth) == 1)
                pixelFormat = bmdFormat10BitRGB;
            else if ((detectedSignalFlags & bmdDetectedVideoInput12BitDepth) == 1)
                pixelFormat = bmdFormat12BitRGB;
            else
                // Invalid color depth for RGB
                return (int) E_INVALIDARG;
        }
        else if ((detectedSignalFlags & bmdDetectedVideoInputYCbCr422) == 1)
        {
            if ((detectedSignalFlags & bmdDetectedVideoInput8BitDepth) == 1)
                pixelFormat = bmdFormat8BitYUV;
            else if ((detectedSignalFlags & bmdDetectedVideoInput10BitDepth) == 1)
                pixelFormat = bmdFormat10BitYUV;
            else
                // Invalid color depth for YUV
                return (int) E_INVALIDARG;
        }
        else
            // Unexpected detected video input format flags
            return (int) E_INVALIDARG;

        if (((notificationEvents & bmdVideoInputDisplayModeChanged) == 1)
                || ((notificationEvents & bmdVideoInputColorspaceChanged) == 1))
        {
            // Stop the capture
            m_deckLinkInput.StopStreams();

            // Set the video input mode
            if (m_deckLinkInput.EnableVideoInput(newMode.GetDisplayMode(), pixelFormat,
                    bmdVideoInputEnableFormatDetection) != S_OK)
            {
                // Let the UI know we couldnt restart the capture with the detected input mode
                if (m_errorListener != null)
                    m_errorListener.accept(DeviceError.ReenableVideoInputFailed);

                return (int) E_FAIL;
            }

            // Start the capture
            if (m_deckLinkInput.StartStreams() != S_OK)
            {
                // Let the UI know we couldnt restart the capture with the detected input mode
                if (m_errorListener != null)
                    m_errorListener.accept(DeviceError.ReenableVideoInputFailed);

                return (int) E_FAIL;
            }

            // Update the UI with detected display mode
            if (m_videoFormatChangedCallback != null)
                m_videoFormatChangedCallback.accept(newMode.GetDisplayMode());
        }

        return (int) S_OK;
    }

    @Override
    public int VideoInputFrameArrived(IDeckLinkVideoInputFrame videoFrame, IDeckLinkAudioInputPacket audioPacket)
    {
        boolean isVideoFrameNull = videoFrame == null || videoFrame.isNull();
        System.out.println(videoFrame.GetRowBytes());

        return (int) S_OK;
    }
}
