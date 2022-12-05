package org.bytedeco.vigem.preset;

/**
 * This interface holds things we could not parse.
 * 
 * @author Dan Avila
 *
 */
public interface vigemspec
{
    /**
     * API succeeded.
     */
    public static final int VIGEM_ERROR_NONE = 0x20000000;
    /**
     * A compatible bus driver wasn't found on the system.
     */
    public static final int VIGEM_ERROR_BUS_NOT_FOUND = 0xE0000001;
    /**
     * All device slots are occupied, no new device can be spawned.
     */
    public static final int VIGEM_ERROR_NO_FREE_SLOT = 0xE0000002;
    public static final int VIGEM_ERROR_INVALID_TARGET = 0xE0000003;
    public static final int VIGEM_ERROR_REMOVAL_FAILED = 0xE0000004;
    /**
     * An attempt has been made to plug in an already connected device.
     */
    public static final int VIGEM_ERROR_ALREADY_CONNECTED = 0xE0000005;
    /**
     * The target device is not initialized.
     */
    public static final int VIGEM_ERROR_TARGET_UNINITIALIZED = 0xE0000006;
    /**
     * The target device is not plugged in.
     */
    public static final int VIGEM_ERROR_TARGET_NOT_PLUGGED_IN = 0xE0000007;
    /**
     * It's been attempted to communicate with an incompatible driver version.
     */
    public static final int VIGEM_ERROR_BUS_VERSION_MISMATCH = 0xE0000008;
    /**
     * Bus driver found but failed to open a handle.
     */
    public static final int VIGEM_ERROR_BUS_ACCESS_FAILED = 0xE0000009;
    public static final int VIGEM_ERROR_CALLBACK_ALREADY_REGISTERED = 0xE0000010;
    public static final int VIGEM_ERROR_CALLBACK_NOT_FOUND = 0xE0000011;
    public static final int VIGEM_ERROR_BUS_ALREADY_CONNECTED = 0xE0000012;
    public static final int VIGEM_ERROR_BUS_INVALID_HANDLE = 0xE0000013;
    public static final int VIGEM_ERROR_XUSB_USERINDEX_OUT_OF_RANGE = 0xE0000014;
    public static final int VIGEM_ERROR_INVALID_PARAMETER = 0xE0000015;
    /**
     * The API is not supported by the driver.
     */
    public static final int VIGEM_ERROR_NOT_SUPPORTED = 0xE0000016;
    /**
     * An unexpected Win32 API error occurred. Call GetLastError() for details.
     */
    public static final int VIGEM_ERROR_WINAPI = 0xE0000017;
    /**
     * The specified timeout has been reached.
     */
    public static final int VIGEM_ERROR_TIMED_OUT = 0xE0000018;
}
