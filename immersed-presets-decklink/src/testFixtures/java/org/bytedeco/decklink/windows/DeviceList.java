package org.bytedeco.decklink.windows;

import static org.assertj.core.api.Assertions.*;
import static org.bytedeco.global.com.*;
import static org.bytedeco.global.decklink.*;

import org.bytedeco.decklink.IDeckLinkAPIInformation;
import org.bytedeco.decklink.IDeckLinkIterator;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.PointerPointer;
import org.junit.jupiter.api.Test;

class DeviceList
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
        final int CLSCTX_ALL = (1 | 2 | 4 | 16);
        return CoCreateInstance(CLSID_CDeckLinkIterator(), null, CLSCTX_ALL, IID_IDeckLinkIterator(), deckLinkIterator);
    }

    public static void main(String... args)
    {
        check(CoInitializeEx(null, COINIT_MULTITHREADED));

        PointerPointer<IDeckLinkIterator> ref = new PointerPointer<>(1L);
        check(GetDeckLinkIterator(ref));

        IDeckLinkIterator deckLinkIterator = ref.get(IDeckLinkIterator.class);

        PointerPointer<IDeckLinkAPIInformation> deckLinkAPIInformationPtr = new PointerPointer<>(1L);
        check(deckLinkIterator.QueryInterface(IID_IDeckLinkAPIInformation(), deckLinkAPIInformationPtr));

        LongPointer pointer = new LongPointer(1L);
        IDeckLinkAPIInformation deckLinkAPIInformation = deckLinkAPIInformationPtr.get(IDeckLinkAPIInformation.class);
        deckLinkAPIInformation.GetInt(BMDDeckLinkAPIVersion, pointer);

        long deckLinkVersion = pointer.get();
        long dlVerMajor = (deckLinkVersion & 0xFF000000) >> 24;
        long dlVerMinor = (deckLinkVersion & 0x00FF0000) >> 16;
        long dlVerPoint = (deckLinkVersion & 0x0000FF00) >> 8;

        System.out.printf("DeckLinkAPI version: %d.%d.%d\n", dlVerMajor, dlVerMinor, dlVerPoint);
        deckLinkAPIInformation.Release();

        CoUninitialize();
    }
}
