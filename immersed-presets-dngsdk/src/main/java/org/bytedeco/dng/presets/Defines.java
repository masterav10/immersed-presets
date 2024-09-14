package org.bytedeco.dng.presets;

/**
 * Utility class to ensure the load mapper has properly defined things.
 */
public class Defines
{
    public static final String qDNGValidateTarget = "qDNGValidateTarget 1";

    public static boolean qDNGValidateTarget()
    {
        return qDNGValidateTarget.endsWith("1");
    }
}
