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

    /**
     * An implementation of a decklink callback. This class implements the querying
     * and reference handling so the client can simply focus on extending the
     * intended functionality.
     * 
     * @author Dan Avila
     */
    public static class IUnknownCallback extends IUnknown
    {
        private final GUID iid;
        private int refCount = 1;

        public IUnknownCallback(Pointer p)
        {
            super(p);

            Class<?> settings = decklink.class;
            Properties props = settings.getAnnotation(Properties.class);
            String global = props.global();

            try
            {
                Class<?> decklink = Class.forName(global);
                Class<?> parent = getClass().getSuperclass();

                this.iid = GUID_CACHE.computeIfAbsent(parent, t ->
                {
                    try
                    {
                        String name = "IID_" + parent.getSimpleName();
                        Method method = decklink.getMethod(name);
                        return (GUID) method.invoke(null);
                    }
                    catch (Exception e)
                    {
                        throw new IllegalStateException(e);
                    }

                });
            }
            catch (ClassNotFoundException | SecurityException | IllegalArgumentException e)
            {
                throw new IllegalStateException(e);
            }
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
