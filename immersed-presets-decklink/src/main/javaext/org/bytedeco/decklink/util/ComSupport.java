package org.bytedeco.decklink.util;

import static org.bytedeco.global.windef.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bytedeco.com.IUnknown;
import org.bytedeco.global.com;
import org.bytedeco.global.decklink;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.systems.windows.GUID;

/**
 * Provides utility functions for creating or querying COM objects.
 * 
 * @author Dan Avila
 *
 */
public class ComSupport
{
    private static final Map<Class<?>, GUID> IIDS = new HashMap<>();
    private static final Map<Class<?>, Method> CLSIDS = new HashMap<>();

    static
    {
        final Properties properties = org.bytedeco.decklink.presets.decklink.class.getAnnotation(Properties.class);
        final String packageName = properties.target();

        ReflectionUtils.doWithMethods(decklink.class, method ->
        {
            final String name = method.getName();
            final boolean noParams = method.getParameterCount() == 0;
            final boolean returnsGuid = GUID.class.equals(method.getReturnType());
            final boolean isIID = name.startsWith("IID_");

            if (returnsGuid && noParams && isIID)
            {
                try
                {
                    final String typeName = name.replace("IID_", "");
                    final String className = String.format("%s.%s", packageName, typeName);
                    final Class<?> type = Class.forName(className);

                    final Object guid = ReflectionUtils.invokeMethod(method, null);
                    IIDS.put(type, (GUID) guid);
                }
                catch (ClassNotFoundException e)
                {
                    // skip, no associated class
                }
            }
        });

        ReflectionUtils.doWithMethods(decklink.class, method ->
        {
            final boolean noParams = method.getParameterCount() == 0;
            final boolean returnsGuid = GUID.class.equals(method.getReturnType());

            final String name = method.getName();
            final boolean isCLSID = name.startsWith("CLSID_C");

            if (returnsGuid && noParams && isCLSID)
            {
                final String typeName = name.replace("CLSID_C", "I");
                final String methodName = name.replace("CLSID_C", "Get");
                final String className = String.format("%s.%s", packageName, typeName);

                try
                {
                    final Class<?> type = Class.forName(className);
                    final Method getter = ReflectionUtils.findMethod(decklink.class, methodName, PointerPointer.class);

                    CLSIDS.put(type, getter);
                }
                catch (ClassNotFoundException e)
                {
                    // skip, no associated class
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
            }
        });
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
     * @param  <T>  the type of object we wish to create.
     * @param  type the class representing the type.
     * @return      a reference to the loaded object.
     */
    public static <T extends Pointer> T create(Class<T> type)
    {
        final Method rootMethod = CLSIDS.get(type);

        try (PointerPointer<T> ref = new PointerPointer<>(1L))
        {
            ReflectionUtils.invokeMethod(rootMethod, null, ref);
            return ref.get(type);
        }
    }

    static GUID lookupIID(Class<?> type)
    {
        return IIDS.get(type);
    }
}
