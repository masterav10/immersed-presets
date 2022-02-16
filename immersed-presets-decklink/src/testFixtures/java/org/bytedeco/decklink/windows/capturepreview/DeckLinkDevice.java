package org.bytedeco.decklink.windows.capturepreview;

import static org.bytedeco.decklink.windows.ComSupport.*;
import static org.bytedeco.global.com.*;
import static org.bytedeco.global.decklink.*;
import static org.bytedeco.global.windef.*;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkAudioInputPacket;
import org.bytedeco.decklink.IDeckLinkConfiguration;
import org.bytedeco.decklink.IDeckLinkDisplayMode;
import org.bytedeco.decklink.IDeckLinkDisplayModeIterator;
import org.bytedeco.decklink.IDeckLinkHDMIInputEDID;
import org.bytedeco.decklink.IDeckLinkInput;
import org.bytedeco.decklink.IDeckLinkInputCallback;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.decklink.IDeckLinkVideoInputFrame;
import org.bytedeco.decklink.windows.Utility;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

/**
 * A wrapper around a device available in an attached frame pool.
 * 
 * @author Dan Avila
 * @see DeckLinkDevice.cpp
 * @see DeckLinkDevice.h
 *
 */
public class DeckLinkDevice extends IDeckLinkInputCallback
{
    private static enum DeviceError
    {
        NoError, EnableVideoInputFailed, StartStreamsFailed, ReenableVideoInputFailed,
    };

    private Consumer<DeviceError> m_errorListener;
    private Consumer<IDeckLinkVideoInputFrame> m_videoFrameArrivedCallback;
    private IntConsumer m_videoFormatChangedCallback;

    private int m_refCount = 1;
    private IDeckLink m_deckLink;
    private IDeckLinkInput m_deckLinkInput;
    private IDeckLinkConfiguration m_deckLinkConfig;
    private IDeckLinkHDMIInputEDID m_deckLinkHDMIInputEDID;

    private boolean m_supportsFormatDetection = false;
    private boolean m_currentlyCapturing = false;
    private boolean m_applyDetectedInputMode = false;
    private String m_deviceName = null;

    public DeckLinkDevice(IDeckLink device)
    {
        this.m_deckLink = device;
        this.m_deckLinkInput = find(device, IDeckLinkInput.class);
        this.m_deckLinkConfig = find(device, IDeckLinkConfiguration.class);
        this.m_deckLinkHDMIInputEDID = find(device, IDeckLinkHDMIInputEDID.class);

        if (m_deckLinkInput == null || m_deckLinkInput.isNull())
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
        IDeckLinkProfileAttributes deckLinkAttributes = find(m_deckLink, IDeckLinkProfileAttributes.class);

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

        try (PointerPointer<CharPointer> pointer = new PointerPointer<CharPointer>(1L))
        {
            // Get device name
            if (m_deckLink.GetDisplayName(pointer) == S_OK)
            {
                CharPointer tmp = pointer.get(CharPointer.class);
                this.m_deviceName = tmp.getString();
                tmp.deallocate();
            }
            else
            {
                this.m_deviceName = "Decklink";
            }
        }

        return true;
    }

    public boolean startCapture(int displayMode, boolean applyDetectedInputMode)
    {
        int videoInputFlags = bmdVideoInputFlagDefault;

        m_applyDetectedInputMode = applyDetectedInputMode;

        // Enable input video mode detection if the device supports it
        if (m_supportsFormatDetection)
            videoInputFlags |= bmdVideoInputEnableFormatDetection;

        // Set the screen preview
        // m_deckLinkInput.SetScreenPreviewCallback(screenPreviewCallback);

        // Set capture callback
        m_deckLinkInput.SetCallback(this);

        // Set the video input mode
        if (m_deckLinkInput.EnableVideoInput(displayMode, bmdFormat8BitYUV, videoInputFlags) != S_OK)
        {
            if (m_errorListener != null)
                m_errorListener.accept(DeviceError.EnableVideoInputFailed);

            return false;
        }

        // Start the capture
        if (m_deckLinkInput.StartStreams() != S_OK)
        {
            if (m_errorListener != null)
                m_errorListener.accept(DeviceError.StartStreamsFailed);

            return false;
        }

        m_currentlyCapturing = true;

        return true;
    }

    public void stopCapture()
    {
        if (m_deckLinkInput != null && !m_deckLinkInput.isNull())
        {
            // Stop the capture
            m_deckLinkInput.StopStreams();

            // Unregister screen preview callback
            m_deckLinkInput.SetScreenPreviewCallback(null);

            // Delete capture callback
            m_deckLinkInput.SetCallback(null);

            // Disable video input
            m_deckLinkInput.DisableVideoInput();
        }

        m_currentlyCapturing = false;
    }

    public void queryDisplayModes(Consumer<IDeckLinkDisplayMode> func)
    {
        if (func == null)
            return;

        try (PointerPointer<IDeckLinkDisplayMode> displayModePtr = new PointerPointer<>(1L))
        {
            IDeckLinkDisplayModeIterator displayModeIterator = find(m_deckLinkInput,
                    IDeckLinkDisplayModeIterator.class);

            while (displayModeIterator.Next(displayModePtr) == S_OK)
            {
                IDeckLinkDisplayMode displayMode = displayModePtr.get(IDeckLinkDisplayMode.class);
                func.accept(displayMode);
                displayMode.Release();
            }
        }
        catch (RuntimeException e)
        {
            return;
        }
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
        boolean isFrameNull = videoFrame != null && !videoFrame.isNull();

        if (!isFrameNull && (m_videoFrameArrivedCallback != null))
        {
            m_videoFrameArrivedCallback.accept(videoFrame);
        }

        return (int) S_OK;
    }

    /**
     * Defined in the header.
     * 
     * @return the name of the device.
     */
    public String getDeviceName()
    {
        return m_deviceName;
    }

    public boolean isCapturing()
    {
        return this.m_currentlyCapturing;
    }

    public boolean doesSupportFormatDetection()
    {
        return m_supportsFormatDetection;
    }

    public IDeckLink getDeckLinkInstance()
    {
        return m_deckLink;
    }

    public IDeckLinkInput getDeckLinkInput()
    {
        return m_deckLinkInput;
    }

    public IDeckLinkConfiguration getDeckLinkConfiguration()
    {
        return m_deckLinkConfig;
    }

    public void setErrorListener(Consumer<DeviceError> func)
    {
        this.m_errorListener = func;
    }

    public void onVideoFormatChange(IntConsumer callback)
    {
        this.m_videoFormatChangedCallback = callback;
    }

    public void onVideoFrameArrival(Consumer<IDeckLinkVideoInputFrame> callback)
    {
        this.m_videoFrameArrivedCallback = callback;
    }
}
