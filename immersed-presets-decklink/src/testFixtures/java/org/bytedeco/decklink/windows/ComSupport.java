package org.bytedeco.decklink.windows;

import static org.bytedeco.decklink.windows.Utility.*;
import static org.bytedeco.global.com.*;
import static org.bytedeco.global.windef.*;

import java.util.HashMap;
import java.util.Map;

import org.bytedeco.com.IUnknown;
import org.bytedeco.global.com;
import org.bytedeco.global.decklink;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.systems.windows.GUID;
import org.springframework.util.ReflectionUtils;

/**
 * Provides utility functions for creating or querying COM objects.
 * 
 * @author Dan Avila
 *
 */
public class ComSupport
{
    private static final Map<Class<?>, GUID> IIDS = new HashMap<>();
    private static final Map<Class<?>, GUID> CLSIDS = new HashMap<>();

    static
    {
        final Properties properties = org.bytedeco.decklink.presets.decklink.class.getAnnotation(Properties.class);
        final String packageName = properties.target();

        ReflectionUtils.doWithMethods(decklink.class, method ->
        {
            final String name = method.getName();
            final boolean noParams = method.getParameterCount() == 0;
            final boolean returnsGuid = GUID.class.equals(method.getReturnType());

            if (returnsGuid && noParams)
            {
                final Map<Class<?>, GUID> map = name.startsWith("IID_") ? IIDS : CLSIDS;
                final String typeName = name.replace("CLSID_C", "I")
                                            .replace("IID_", "");

                final String className = String.format("%s.%s", packageName, typeName);

                try
                {
                    final Object guid = ReflectionUtils.invokeMethod(method, null);
                    final Class<?> type = Class.forName(className);

                    if (map != null)
                    {
                        map.put(type, (GUID) guid);
                    }
                }
                catch (ClassNotFoundException e)
                {
                    // skip, no associated class
                }
            }
        });
    }

    /**
     * Gets a sub-object via the base's QueryInterface method.
     * 
     * @param <T>  the type of object we are querying.
     * @param base the object holding the subtype
     * @param type the class for the subtype
     * @return the object we are querying for, or null if that object could not be
     *         found.
     */
    public static <T extends Pointer> T find(IUnknown base, Class<T> type)
    {
        final GUID iid = IIDS.get(type);

        try (PointerPointer<T> pointer = new PointerPointer<>(1L))
        {
            if (base.QueryInterface(iid, pointer) == S_OK)
            {
                return pointer.get(type);
            }

            return null;
        }
    }

    /**
     * Creates a new COM object using
     * {@link com#CoCreateInstance(GUID, PointerPointer, int, GUID, PointerPointer)}.
     * 
     * @param <T>  the type of object we wish to create.
     * @param type the class representing the type.
     * @return a reference to the loaded object.
     */
    public static <T extends Pointer> T create(Class<T> type)
    {
        final GUID clsid = CLSIDS.get(type);
        final GUID iid = IIDS.get(type);
        final PointerPointer<?> pUnkOuter = null;
        final int dwClsContext = CLSCTX_ALL;

        try (PointerPointer<T> ref = new PointerPointer<>(1L))
        {
            check(CoCreateInstance(clsid, pUnkOuter, dwClsContext, iid, ref));
            return ref.get(type);
        }
    }

    static GUID lookupIID(Class<?> type)
    {
        return IIDS.get(type);
    }
}
