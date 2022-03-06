package org.bytedeco.decklink.windows;

import org.bytedeco.com.IUnknown;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

/**
 * Utility class that handles most of the generic COM stuff associated with
 * reference counting and interface querying.
 * 
 * @author Dan Avila
 *
 */
public interface IUnknownSupport
{
    /**
     * Creates a new {@link IUnknownSupport} that will manage reference counting for
     * the parent.
     * 
     * @param parent
     * @return
     */
    static IUnknownSupport create(IUnknown parent)
    {
        return new IUnknownImpl(parent);
    }

    long AddRef();

    int QueryInterface(GUID riid, PointerPointer ppv);

    long Release();
}
