package org.bytedeco.decklink.windows;

import org.bytedeco.decklink.IDeckLinkProfileAttributes;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.PointerPointer;

public class Utility
{
    private static int error = -1;

    public static int error()
    {
        return error;
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

    public static final void check(int error)
    {
        if (error < 0)
        {
            throw new IllegalStateException(String.format("%s", Integer.toHexString(error)));
        }
    }
}
