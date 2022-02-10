package org.bytedeco.decklink.windows;

import static org.bytedeco.global.com.*;
import static org.bytedeco.global.decklink.*;

import org.bytedeco.com.IUnknown;
import org.bytedeco.decklink.IDeckLink;
import org.bytedeco.decklink.IDeckLinkAPIInformation;
import org.bytedeco.decklink.IDeckLinkIterator;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

/**
 * Recreation of the DeviceList project.
 * 
 * @author Dan Avila
 * 
 * @see C:\Tools\Blackmagic DeckLink SDK 12.1\Win\Samples\DeviceList
 */
public final class DeviceList
{
    private static int error = -1;
    
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
        
        value = GetInt(BMDDeckLinkPersistentID, deckLinkAttributes);
        
        if(error == 0)
        {
            System.out.printf(" %-40s %llx\n", "Device Persistent ID:",  value);
        }
        else
        {
            System.out.printf(" %-40s %s\n", "Device Persistent ID:",  "Not Supported on this device");
        }
    }

    private static void print_input_modes(IDeckLink deckLink, int printFlags)
    {
        // TODO Auto-generated method stub
    }

    private static void print_output_modes(IDeckLink deckLink, int printFlags)
    {
        // TODO Auto-generated method stub
    }

    private static final <T extends Pointer> T QueryInterface(IUnknown unknown, GUID guid, Class<T> type)
    {
        try (PointerPointer<T> pointer = new PointerPointer<>(1L))
        {
            error = unknown.QueryInterface(guid, pointer);
            return pointer.get(type);
        }
    }

    private static final long GetInt(int key, IDeckLinkProfileAttributes attributes)
    {
        try (LongPointer pointer = new LongPointer(1L))
        {
            error = attributes.GetInt(key, pointer);
            return pointer.get();
        }
    }
}
