package org.bytedeco.cuda;

import static org.assertj.core.api.Assertions.*;

import org.bytedeco.cuda.global.nvml;
import org.junit.jupiter.api.Test;

class NVMLTest
{
    @Test
    void testNvmlLoads()
    {
        try
        {
            assertThat(nvml.nvmlInit_v2()).isZero();
        }
        finally
        {
            assertThat(nvml.nvmlShutdown()).isZero();
        }
    }
}
