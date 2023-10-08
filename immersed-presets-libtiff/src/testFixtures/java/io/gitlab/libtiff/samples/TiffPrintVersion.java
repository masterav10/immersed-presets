package io.gitlab.libtiff.samples;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.libtiff.global.libtiff;

public class TiffPrintVersion
{
    public static void main(String... args)
    {
        BytePointer pointer = libtiff.TIFFGetVersion();
        System.out.println(pointer.getString());
    }
}
