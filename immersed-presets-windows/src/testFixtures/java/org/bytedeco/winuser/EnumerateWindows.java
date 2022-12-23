package org.bytedeco.winuser;

import static org.bytedeco.global.winuser.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.bytedeco.global.winuser;
import org.bytedeco.javacpp.CharPointer;
import org.bytedeco.javacpp.Pointer;

public class EnumerateWindows
{
    public static void main(String... args)
    {
        AtomicLong window = new AtomicLong(0L);
        final int size = 1024;
        final CharPointer lpString = new CharPointer(size);

        WNDENUMPROC lpEnumFunc = new WNDENUMPROC()
        {
            @Override
            public boolean call(Pointer hwnd, long lParam)
            {
                lpString.zero();
                GetWindowTextW(hwnd, lpString, size);

                System.out.format("%d %s\n", window.incrementAndGet(), lpString.getString()
                                                                               .trim());

                return true;
            }
        };

        long now = System.nanoTime();
        System.out.println(winuser.EnumWindows(lpEnumFunc, 0));
        System.out.format("%d ms", TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - now));
    }
}
