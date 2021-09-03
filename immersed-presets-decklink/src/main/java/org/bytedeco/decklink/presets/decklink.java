package org.bytedeco.decklink.presets;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.BuildEnabled;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.javacpp.tools.Logger;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
        inherit = windows.class, 
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.decklink", 
        global = "org.bytedeco.global.decklink",
        value = {
            @Platform(
                includepath = {
                    "C:/Tools/Blackmagic DeckLink SDK 12.1/Win/include",
                    //"C:/Program Files (x86)/Windows Kits/8.1/Include/um"
                }, 
                include = {
                    "combaseapi.h"
                    //"Unknwnbase.h", 
                    //"DeckLinkAPI_h.h"
                },
                link = {
                    "comsuppw", 
                    "ole32", 
                    "oleaut32", 
                    "uuid"
                }
            )
        }
)
@NoException
public class decklink implements BuildEnabled, InfoMapper
{   
    @Override
    public void init(Logger logger, java.util.Properties properties, String encoding)
    {
        // new windows().init(logger, properties, encoding);
    }
    @Override
    public void map(InfoMap infoMap)
    {   
        // new windows().map(infoMap);
        
        combaseapi(infoMap);
        
        // infoMap.put(new Info("Unknwnbase.h").linePatterns("#define [a-zA-Z0-9]+ +_[a-zA-Z0-9_]+").skip());
        // infoMap.put(new Info("Unknwnbase.h").linePatterns("#define [a-zA-Z0-9]+ +[a-zA-Z0-9_]+W").skip());
        
        infoMap.put(new Info("Unknwnbase.h").linePatterns("MIDL_INTERFACE[(].+[)]").skip());
        infoMap.put(new Info("IUnknown").cppTypes().cppText("struct IUnknown"));
        
        infoMap.put(new Info(
                "(_MSC_VER >= 1100) && defined(__cplusplus) && !defined(CINTERFACE)")
                .define(true));
        
        
        infoMap.put(new Info("EXTERN_C", "MIDL_INTERFACE", "__clrcall", "extern \"C++\"")
                .annotations().cppTypes());
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
    }

    private static final void combaseapi(InfoMap infoMap)
    {
        // ideally, we would parse these enumerations
        infoMap.put(new Info("APTTYPE", "APTTYPEQUALIFIER")
                .valueTypes("int"));
        
        // skip these until we figure out how to deal with COM
        infoMap.put(new Info("IUnknown", "LPSTREAM", "LPMALLOC", "LPUNKNOWN", "LPSURROGATE", 
                             "SOLE_AUTHENTICATION_SERVICE", "IActivationFilter", "RPC_AUTHZ_HANDLE")
                .skip());
        
        infoMap.put(new Info("REFCLSID", "CONST_VTBL", "__STRUCT__", "interface", "PURE", "THIS",
                             "CLSCTX_INPROC", "CLSCTX_ALL", "CLSCTX_SERVER", "_Outptr_opt_result_buffer_",
                             "_Outptr_result_buffer_", "_Pre_maybenull_", "__drv_allocatesMem")
                .annotations().cppTypes());
        
        infoMap.put(new Info("CLSID")
                .valueTypes("GUID"));
        infoMap.put(new Info("LPCLSID")
                .pointerTypes("GUID"));
        infoMap.put(new Info("ULONG")
                .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("LPCOLESTR", "LPOLESTR")
                .pointerTypes("CharPointer", "CharBuffer", "char[]"));
        infoMap.put(new Info("OLECHAR")
                .cast().valueTypes("char").pointerTypes("CharPointer", "CharBuffer", "char[]"));
    }
}
