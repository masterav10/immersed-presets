package org.bytedeco.decklink.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Consumer;

final class ReflectionUtils
{
    static void doWithMethods(Class<?> type, Consumer<Method> method)
    {
        for (Method m : type.getDeclaredMethods())
        {
            method.accept(m);
        }
    }

    static Method findMethod(Class<?> reference, String name, Class<?>... params)
    {
        try
        {
            return reference.getMethod(name, params);
        }
        catch (NoSuchMethodException | SecurityException e)
        {
            throw new IllegalArgumentException(e);
        }
    }

    static Object invokeMethod(Method method, Object obj, Object... args)
    {
        try
        {
            return method.invoke(obj, args);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            throw new IllegalArgumentException(e);
        }
    }
}
