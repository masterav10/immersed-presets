package org.bytedeco.decklink.windows;

import org.bytedeco.com.IUnknown;
import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

public class Utility
{
    private static int error = -1;

    public static int error()
    {
        return error;
    }

    public static final <T extends Pointer> T QueryInterface(IUnknown unknown, GUID guid, Class<T> type)
    {
        try (PointerPointer<T> pointer = new PointerPointer<>(1L))
        {
            error = unknown.QueryInterface(guid, pointer);
            return pointer.get(type);
        }
    }

    public static final boolean GetFlag(int key, IDeckLinkProfileAttributes attributes)
    {
        try (IntPointer pointer = new IntPointer(1L))
        {
            error = attributes.GetFlag(key, pointer);
            return pointer.get() != 0;
        }
    }

    public static final long GetInt(int key, IDeckLinkProfileAttributes attributes)
    {
        try (LongPointer pointer = new LongPointer(1L))
        {
            error = attributes.GetInt(key, pointer);
            return pointer.get();
        }
    }

    public static final String GetString(int key, IDeckLinkProfileAttributes attributes)
    {
        try (PointerPointer<CharPointer> pointer = new PointerPointer<>(1L))
        {
            error = attributes.GetString(key, pointer);
            CharPointer ptr = pointer.get(CharPointer.class);
            return ptr.getString();
        }
    }
}
