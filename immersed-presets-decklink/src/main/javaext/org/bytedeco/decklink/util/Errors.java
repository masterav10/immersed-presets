package org.bytedeco.decklink.util;

import static org.bytedeco.global.windef.*;

import org.bytedeco.global.windef;

/**
 * Utilty method to check for errors.
 * 
 * @author Dan Avila
 *
 */
public class Errors
{
    /**
     * Throws an IllegalStateException when the value does not equal
     * {@link windef#S_OK}
     * 
     * <p>
     * Usage: {@code check(nativeMethodCall(...));}
     * </p>
     * 
     * @param error the error code
     */
    public static final void check(long error)
    {
        if (error != S_OK)
        {
            throw new IllegalStateException(String.format("%d: %s", error, Long.toHexString(error)));
        }
    }

    private Errors()
    {
    }
}
