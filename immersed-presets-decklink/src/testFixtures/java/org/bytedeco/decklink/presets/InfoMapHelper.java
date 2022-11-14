package org.bytedeco.decklink.presets;

import static java.lang.String.*;
import static java.lang.System.*;

import java.io.IOException;

import org.bytedeco.decklink.DecklinkHelper.IDecklinkBase;
import org.bytedeco.decklink.DecklinkHelper.IDecklinkCallback;

public class InfoMapHelper
{
    public static void main(String[] args) throws IOException
    {
        FileLists.deprecated()
                 .reduce((t1, t2) -> String.join(",", t1, t2))
                 .ifPresent(types ->
                 {
                 //@formatter:off
                     out.println(       "void deprecated(InfoMap infoMap) {");
                     out.println(format("     infoMap.put(new Info(%s)", types));
                     out.println(       "            .skip());");
                     out.println(       "}");
                   //@formatter:on
                 });

        out.println();

        FileLists.callbacks()
                 .reduce((t1, t2) -> String.join(",", t1, t2))
                 .ifPresent(types ->
                 {
                     final String name = IDecklinkCallback.class.getSimpleName();

                 //@formatter:off
                     out.println(       "void callbacks(InfoMap infoMap) {");
                     out.println(format("     infoMap.put(new Info(%s)", types));
                     out.println(format("            .purify(false).virtualize().base(\"%s\"));", name));
                     out.println(       "}");
                   //@formatter:on
                 });

        out.println();

        FileLists.commObjects()
                 .reduce((t1, t2) -> String.join(",", t1, t2))
                 .ifPresent(types ->
                 {
                     final String name = IDecklinkBase.class.getSimpleName();

                 //@formatter:off
                     out.println(       "void commObjects(InfoMap infoMap) {");
                     out.println(format("     infoMap.put(new Info(%s)", types));
                     out.println(format("            .base(\"%s\"));", name));
                     out.println(       "}");
                     //@formatter:on
                 });
    }
}
