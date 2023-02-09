package org.bytedeco.vigem;

import static org.bytedeco.vigem.preset.vigemspec.*;
import static org.bytedeco.vigemclient.global.vigemclient.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.bytedeco.global.windef;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.vigemclient.DS4_REPORT;
import org.bytedeco.vigemclient.VIGEM_CLIENT;
import org.bytedeco.vigemclient.VIGEM_TARGET;
import org.bytedeco.xinput.XINPUT_CAPABILITIES;
import org.bytedeco.xinput.global.xinput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadmeSample
{
    private static final Logger LOG = LoggerFactory.getLogger(ReadmeSample.class);

    private static final void check(int error)
    {
        if (error != VIGEM_ERROR_NONE)
        {
            LOG.error("ViGEm Bus connection failed with error code: 0x{}", Integer.toHexString(error));
        }
    }

    public static void main(String... args)
    {
        for (int dwUserIndex = 0; dwUserIndex < 4; dwUserIndex++)
        {
            final int dwFlags = 0;

            try (XINPUT_CAPABILITIES state = new XINPUT_CAPABILITIES())
            {
                if (windef.ERROR_SUCCESS == xinput.XInputGetCapabilities(dwUserIndex, dwFlags, state))
                {
                    LOG.info("{}:{}", dwUserIndex, state.Type());
                }
            }
        }

        PointerPointer<VIGEM_CLIENT> client = vigem_alloc();

        check(vigem_connect(client));

        PointerPointer<VIGEM_TARGET> pad = vigem_target_ds4_alloc();
        check(vigem_target_add(client, pad));

        LOG.info("Continue.");

        DS4_REPORT report = new DS4_REPORT();

        short value = xinput.XINPUT_GAMEPAD_DPAD_RIGHT;
        report.wButtons(value);

        long start = System.nanoTime();
        long timeNanos = TimeUnit.SECONDS.toNanos(30L);

        while (System.nanoTime() - start < timeNanos)
        {
            long sec = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start);
            report.wButtons(sec % 2 == 0 ? value : 0);

            check(vigem_target_ds4_update(client, pad, report));

            LockSupport.parkNanos(TimeUnit.MICROSECONDS.toNanos(50L));
        }

        vigem_target_remove(client, pad);
        vigem_target_free(pad);
        vigem_free(client);
    }
}
