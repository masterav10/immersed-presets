package org.bytedeco.decklink;

import static org.bytedeco.global.com.*;
import static org.bytedeco.global.windef.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.com.IUnknown;
import org.bytedeco.decklink.presets.decklink;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.systems.windows.GUID;

/**
 * This is a helper class for generating the decklink bindings.
 * 
 * @author Dan Avila
 * @see    https://github.com/bytedeco/javacpp/wiki/Mapping-Recipes#writing-additional-code-in-a-helper-class
 */
public class DecklinkHelper extends decklink
{
    private static final Map<Class<?>, GUID> GUID_CACHE = new HashMap<>();

    private static final GUID lookupGuid(Class<?> type)
    {
        if (GUID_CACHE.containsKey(type))
        {
            return GUID_CACHE.get(type);
        }

        try
        {
            Class<?> settings = decklink.class;
            Properties props = settings.getAnnotation(Properties.class);
            String global = props.global();
            Class<?> decklink = Class.forName(global);

            String name = "IID_" + type.getSimpleName();
            Method method = decklink.getMethod(name);

            GUID value = (GUID) method.invoke(null);
            GUID_CACHE.put(type, value);
            return value;
        }
        catch (Exception e)
        {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Default subclass to be used by decklink com objects. Provides a quick lookup
     * function with automatic IID detection.
     * 
     * @author Dan Avila
     *
     */
    public static class IDecklinkBase extends IUnknown
    {
        /** Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}. */
        public IDecklinkBase(Pointer p)
        {
            super(p);
        }

        /**
         * Gets a sub-object via the base's QueryInterface method.
         * 
         * @param  <T>  the type of object we are querying.
         * @param  base the object holding the subtype
         * @param  type the class for the subtype
         * @return      the object we are querying for, or null if that object could not
         *              be found.
         */
        public <T extends Pointer> T find(Class<T> type)
        {
            final GUID iid = DecklinkHelper.lookupGuid(type);

            try (PointerPointer<T> pointer = new PointerPointer<>(1L))
            {
                if (QueryInterface(iid, pointer) == S_OK)
                {
                    return pointer.get(type);
                }

                return null;
            }
        }

        @Override
        public long AddRef()
        {
            return super.AddRef();
        }

        @Override
        public long Release()
        {
            return super.Release();
        }

        @Override
        public long QueryInterface(GUID riid, PointerPointer ppv)
        {
            return super.QueryInterface(riid, ppv);
        }
    }

    /**
     * An implementation of a decklink callback. This class implements the querying
     * and reference handling so the client can simply focus on extending the
     * intended functionality.
     * 
     * @author Dan Avila
     */
    public static class IDecklinkCallback extends IDecklinkBase
    {
        private final GUID iid;
        private int refCount = 1;

        /**
         * Pointer cast constructor. Invokes {@link Pointer#Pointer(Pointer)}.
         */
        public IDecklinkCallback(Pointer p)
        {
            super(p);

            this.iid = lookupGuid(getClass().getSuperclass());
        }

        @Override
        public long AddRef()
        {
            return ++refCount;
        }

        @Override
        public long QueryInterface(GUID riid, PointerPointer ppv)
        {
            int result = (int) E_NOINTERFACE;

            if (ppv == null || ppv.isNull())
            {
                return (int) E_INVALIDARG;
            }

            ppv.setNull();

            if (riid.equals(IID_IUnknown()) || riid.equals(this.iid))
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
            int newRefValue = --refCount;

            if (newRefValue == 0)
            {
                deallocate();
            }

            return newRefValue;
        }
    }
}
