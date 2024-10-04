package org.bytedeco.javacpp.tools;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bytedeco.dng.presets.dng;

public class ReadOnlyPrinter
{
    public static void main(String[] args)
    {
        InfoMap map = new InfoMap();

        new dng().map(map);

        final Set<String> readOnlyTypes = new HashSet<>(Arrays.asList("AutoPtr", "AutoArray"));

        map.forEach((k, v) ->
        {
            if ("AutoPtr<dng_shared>".equals(k))
            {
                // System.out.println(k);
                // System.out.println(" " + v);
            }

            for (Info info : v)
            {

                boolean isFlagged = readOnlyTypes.stream()
                                                 .anyMatch(s -> info.cppNames[0].startsWith(s));
                boolean hasPtrTypes = info.pointerTypes != null;

                if (isFlagged && hasPtrTypes && info.define)
                {
                    System.out.println(k);
                    System.out.println("  " + Arrays.toString(info.pointerTypes));
                }
            }
        });
    }
}
