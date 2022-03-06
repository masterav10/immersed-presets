package org.bytedeco.decklink.windows;

import static org.bytedeco.decklink.windows.ComSupport.*;
import static org.bytedeco.global.com.*;
import static org.bytedeco.global.windef.*;

import org.bytedeco.com.IUnknown;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.systems.windows.GUID;

final class IUnknownImpl extends IUnknown implements IUnknownSupport
{
    private final IUnknown m_parent;
    private final GUID m_iid;
    private int m_refCount = 1;

    public IUnknownImpl(IUnknown parent)
    {
        this.m_parent = parent;
        this.m_iid = lookupIID(parent.getClass());
    }

    @Override
    public long AddRef()
    {
        return ++m_refCount;
    }

    @Override
    public int QueryInterface(GUID iid, PointerPointer ppv)
    {
        int result = (int) E_NOINTERFACE;

        if (ppv == null || ppv.isNull())
        {
            return (int) E_INVALIDARG;
        }

        ppv.setNull();

        if (iid.equals(IID_IUnknown()))
        {
            ppv.put(this);
            AddRef();
            result = (int) S_OK;
        }
        else if (iid.equals(this.m_iid))
        {
            ppv.put(this);
            AddRef();
            result = (int) S_OK;
        }

        return result;
    }

    @Override
    public long Release()
    {
        int newRefValue = --m_refCount;

        if (newRefValue == 0)
            this.m_parent.deallocate();

        return newRefValue;
    }
}
