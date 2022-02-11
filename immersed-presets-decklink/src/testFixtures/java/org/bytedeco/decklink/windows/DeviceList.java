package org.bytedeco.decklink.windows;

import static org.bytedeco.decklink.windows.Utility.*;
import static org.bytedeco.global.com.*;
import static org.bytedeco.global.decklink.*;

import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkAPIInformation;
import org.bytedeco.decklink.IDeckLinkIterator;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.global.decklink;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.PointerPointer;

/**
 * Recreation of the DeviceList project.
 * 
 * @author Dan Avila
 * 
 * @see C:\Tools\Blackmagic DeckLink SDK 12.1\Win\Samples\DeviceList
 */
public final class DeviceList
{
    private static final void check(int error)
    {
        if (error < 0)
        {
            throw new IllegalStateException(String.format("%s", Integer.toHexString(error)));
        }
    }

    public static int GetDeckLinkIterator(PointerPointer<IDeckLinkIterator> deckLinkIterator)
    {
        return CoCreateInstance(CLSID_CDeckLinkIterator(), null, CLSCTX_ALL, IID_IDeckLinkIterator(), deckLinkIterator);
    }

    public static void main(String... args)
    {
        int printFlags = 0;

        // Initialize COM on this thread
        check(CoInitializeEx(null, COINIT_MULTITHREADED));

        // Create an IDeckLinkIterator object to enumerate all DeckLink cards in the
        // system
        PointerPointer<IDeckLinkIterator> deckLinkIteratorPtr = new PointerPointer<>(1L);
        check(GetDeckLinkIterator(deckLinkIteratorPtr));
        IDeckLinkIterator deckLinkIterator = deckLinkIteratorPtr.get(IDeckLinkIterator.class);

        PointerPointer<IDeckLinkAPIInformation> deckLinkAPIInformationPtr = new PointerPointer<>(1L);
        check(deckLinkIterator.QueryInterface(IID_IDeckLinkAPIInformation(), deckLinkAPIInformationPtr));

        // We can get the version of the API like this:
        LongPointer deckLinkVersionPtr = new LongPointer(1L);
        IDeckLinkAPIInformation deckLinkAPIInformation = deckLinkAPIInformationPtr.get(IDeckLinkAPIInformation.class);

        // We can also use the BMDDeckLinkAPIVersion flag with GetString
        deckLinkAPIInformation.GetInt(BMDDeckLinkAPIVersion, deckLinkVersionPtr);

        long deckLinkVersion = deckLinkVersionPtr.get();
        long dlVerMajor = (deckLinkVersion & 0xFF000000) >> 24;
        long dlVerMinor = (deckLinkVersion & 0x00FF0000) >> 16;
        long dlVerPoint = (deckLinkVersion & 0x0000FF00) >> 8;

        System.out.printf("DeckLinkAPI version: %d.%d.%d\n", dlVerMajor, dlVerMinor, dlVerPoint);

        deckLinkAPIInformation.Release();

        // Enumerate all cards in this system

        int numDevices = 0;
        PointerPointer<IDeckLink> deckLinkPtr = new PointerPointer<>(1L);

        while (deckLinkIterator.Next(deckLinkPtr) == 0)
        {
            // Increment the total number of DeckLink cards found
            numDevices++;
            if (numDevices > 1)
            {
                System.out.printf("\n\n");
            }

            IDeckLink deckLink = deckLinkPtr.get(IDeckLink.class);

            // *** Print the model name of the DeckLink card
            try (PointerPointer<CharPointer> deviceNameStringPtr = new PointerPointer<>(1L))
            {
                check(deckLink.GetModelName(deviceNameStringPtr));
                CharPointer deviceName = deviceNameStringPtr.get(CharPointer.class);

                System.out.printf("=============== %s ===============\n\n", deviceName.getString());
                deviceName.deallocate();
            }

            // Products with multiple subdevices might not be usable if a subdevice is
            // inactive for the current profile
            boolean showIOInfo = true;

            PointerPointer<IDeckLinkProfileAttributes> deckLinkAttributesPtr = new PointerPointer<>(1L);
            check(deckLink.QueryInterface(IID_IDeckLinkProfileAttributes(), deckLinkAttributesPtr));
            IDeckLinkProfileAttributes deckLinkAttributes = deckLinkAttributesPtr.get(IDeckLinkProfileAttributes.class);

            LongPointer duplexModePtr = new LongPointer(1L);
            check(deckLinkAttributes.GetInt(BMDDeckLinkDuplex, duplexModePtr));

            if (duplexModePtr.get() == bmdDuplexInactive)
            {
                System.out.printf("Sub-device has no active connectors for current profile\n\n");
                showIOInfo = false;
            }

            LongPointer videoIOSupportPtr = new LongPointer(1L);
            check(deckLinkAttributes.GetInt(BMDDeckLinkVideoIOSupport, videoIOSupportPtr));

            deckLinkAttributes.Release();

            print_attributes(deckLink, showIOInfo);

            if (showIOInfo)
            {
                if ((videoIOSupportPtr.get() & bmdDeviceSupportsPlayback) == 1)
                {
                    // ** List the video output display modes supported by the card
                    print_output_modes(deckLink, printFlags);
                }

                if ((videoIOSupportPtr.get() & bmdDeviceSupportsCapture) == 1)
                {
                    // ** List the video output display modes supported by the card
                    print_input_modes(deckLink, printFlags);
                }
            }

            // Release the IDeckLink instance when we've finished with it to prevent leaks
            deckLink.Release();
        }

        // Uninitalize COM on this thread
        CoUninitialize();

        // If no DeckLink cards were found in the system, inform the user
        if (numDevices == 0)
        {
            System.out.printf("No Blackmagic Design devices were found.\n");
        }

        System.out.printf("\n");
    }

    private static void print_attributes(IDeckLink deckLink, boolean showIOInfo)
    {
        // Query the DeckLink for its attributes interface
        IDeckLinkProfileAttributes deckLinkAttributes = QueryInterface(deckLink, IID_IDeckLinkProfileAttributes(),
                IDeckLinkProfileAttributes.class);

        // List attributes and their value
        System.out.printf("Attribute list:\n");

        long value = GetInt(BMDDeckLinkDeviceInterface, deckLinkAttributes);

        switch ((int) value)
        {
        case bmdDeviceInterfacePCI:
            System.out.printf(" %-40s %s\n", "Device Interface:", "PCI");
            break;
        case bmdDeviceInterfaceUSB:
            System.out.printf(" %-40s %s\n", "Device Interface:", "USB");
            break;
        case bmdDeviceInterfaceThunderbolt:
            System.out.printf(" %-40s %s\n", "Device Interface", "Thunderbolt");
            break;
        }

        printInt("Device Persistent ID:", BMDDeckLinkPersistentID, deckLinkAttributes);
        printInt("Device Topological ID:", BMDDeckLinkTopologicalID, deckLinkAttributes);

        if (printInt("Number of sub-devices:", BMDDeckLinkNumberOfSubDevices, deckLinkAttributes) > 0)
        {
            printInt("Sub-device index:", BMDDeckLinkSubDeviceIndex, deckLinkAttributes);
        }

        if (printFlag("Serial port present:", BMDDeckLinkHasSerialPort, deckLinkAttributes))
        {
            printString("Serial port name:", BMDDeckLinkSerialPortDeviceName, deckLinkAttributes);
        }

        printInt("Number of audio channels:", BMDDeckLinkMaximumAudioChannels, deckLinkAttributes);
        printFlag("Input mode detection supported ?", BMDDeckLinkSupportsInputFormatDetection, deckLinkAttributes);
        printInt("Duplex Mode:", BMDDeckLinkDuplex, deckLinkAttributes);
        printFlag("Internal keying supported ?", BMDDeckLinkSupportsInternalKeying, deckLinkAttributes);
        printFlag("External keying supported ?", BMDDeckLinkSupportsExternalKeying, deckLinkAttributes);
        printFlag("HDMI timecode support:", BMDDeckLinkSupportsHDMITimecode, deckLinkAttributes);
     
        if(deckLinkAttributes != null && !deckLinkAttributes.isNull())
        {
            deckLinkAttributes.Release();
        }
    }

    private static long printInt(String text, int key, IDeckLinkProfileAttributes deckLinkAttributes)
    {
        long value = GetInt(key, deckLinkAttributes);
        boolean supported = error() == 0;
        String result = supported ? String.format("%d", value) : "Not Supported on this device";
        System.out.printf(" %-40s %s\n", text, result);
        return value;
    }

    private static boolean printFlag(String text, int key, IDeckLinkProfileAttributes deckLinkAttributes)
    {
        boolean value = GetFlag(key, deckLinkAttributes);
        boolean supported = error() == 0;
        String result = supported ? (value ? "Yes" : "No") : "Not Supported on this device";
        System.out.printf(" %-40s %s\n", text, result);
        return value;
    }

    private static String printString(String text, int key, IDeckLinkProfileAttributes deckLinkAttributes)
    {
        String value = GetString(key, deckLinkAttributes);
        boolean supported = error() == 0;
        String result = supported ? value : "Not Supported on this device";
        System.out.printf(" %-40s %s\n", text, result);
        return value;
    }

    private static void print_input_modes(IDeckLink deckLink, int printFlags)
    {
        // TODO Auto-generated method stub
    }

    private static void print_output_modes(IDeckLink deckLink, int printFlags)
    {
        // TODO Auto-generated method stub
    }
}
