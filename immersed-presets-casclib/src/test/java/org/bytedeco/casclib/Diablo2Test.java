package org.bytedeco.casclib;

import static org.bytedeco.casclib.global.casclib.*;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.SizeTPointer;
import org.bytedeco.systems.global.windows;
import org.bytedeco.systems.windows.HANDLE;

public class Diablo2Test
{
    private static final void check(boolean success)
    {
        if (!success)
        {
            throw new IllegalStateException("" + windows.GetLastError());
        }
    }

    private static void readData(Pointer hFind, CASC_FIND_DATA data)
    {
        int bytes = (int) data.FileSize();

        try (BytePointer pointer = new BytePointer(bytes))
        {
            System.out.println(data.szFileName()
                                   .getString());
            check(CascReadFile(hFind, pointer, bytes, (int[]) null));
        }
    }

    public static void main(String[] args)
    {
        String name = "C:\\Program Files (x86)\\Diablo II Resurrected\\";

        try (PointerPointer<HANDLE> handlePtr = new PointerPointer<>(1L))
        {
            check(CascOpenStorage(name.toCharArray(), 0, handlePtr));

            final HANDLE hStorage = handlePtr.get(HANDLE.class);
            final int InfoClass = CascStorageProduct;
            final CASC_STORAGE_PRODUCT pvStorageInfo = new CASC_STORAGE_PRODUCT();
            final long cbStorageInfo = pvStorageInfo.sizeof();
            final SizeTPointer pcbLengthNeeded = new SizeTPointer(1L);

            check(CascGetStorageInfo(hStorage, InfoClass, pvStorageInfo, cbStorageInfo, pcbLengthNeeded));

            System.out.println(pvStorageInfo.szCodeName()
                                            .getString());

            String szMask = null;
            CASC_FIND_DATA pFindData = new CASC_FIND_DATA();
            char[] szListFile = null;

            Pointer hFind = CascFindFirstFile(hStorage, szMask, pFindData, szListFile);
            readData(hFind, pFindData);

            while (CascFindNextFile(hFind, pFindData))
            {
                readData(hFind, pFindData);
            }

            check(CascCloseStorage(hStorage));
        }
    }

}
