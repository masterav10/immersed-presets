package org.bytedeco.decklink.presets;

import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
      inherit = windows.class, 
      names = {"windows-x86_64"}, 
      target = "org.bytedeco.com",
      global = "org.bytedeco.global.com",
      value = {
          @Platform(
              include = {
                  "combaseapi.h",
                  "Unknwnbase.h",
                  "windef.h"
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
public class com implements InfoMapper
{
    @Override
    public void map(InfoMap infoMap)
    {   
        infoMap.put(new Info("combaseapi.h", "Unknwnbase.h")
                .linePatterns(".*__MIDL_itf.*").skip());
        
        // skip these until we figure out how to deal with COM
        infoMap.put(new Info("LPSTREAM", "LPMALLOC", "LPSURROGATE", 
                             "SOLE_AUTHENTICATION_SERVICE", "IActivationFilter", "RPC_AUTHZ_HANDLE",
                             "DllGetClassObject", "DllCanUnloadNow", "COSERVERINFO")
                .skip());
        
        infoMap.put(new Info("WINOLEAPI", "FARSTRUCT", "REFCLSID", "CONST_VTBL", "__STRUCT__", "interface", 
                             "PURE", "THIS", "CLSCTX_INPROC", "CLSCTX_ALL", "CLSCTX_SERVER", "_Outptr_opt_result_buffer_",
                             "_Outptr_result_buffer_", "_Pre_maybenull_", "__drv_allocatesMem", "__RPC__in", "_COM_Outptr_",
                             "__RPC__deref_out_opt", "HFILE_ERROR")
                .annotations().cppTypes());
        
        infoMap.put(new Info("CLSID")
                .valueTypes("GUID"));
        infoMap.put(new Info("LPCLSID")
                .cast().pointerTypes("GUID"));
        
        infoMap.put(new Info("RPC_AUTH_IDENTITY_HANDLE")
                .cppTypes().valueTypes("void").pointerTypes("Pointer"));
        
        infoMap.put(new Info("ULONG", "APTTYPE", "APTTYPEQUALIFIER")
                .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("LPCOLESTR", "LPOLESTR")
                .cast().pointerTypes("CharPointer", "CharBuffer", "char[]"));
        infoMap.put(new Info("OLECHAR")
                .cast().valueTypes("char").pointerTypes("CharPointer", "CharBuffer", "char[]"));
        
        infoMap.put(new Info("LPHANDLE", "LPUNKNOWN").cast().valueTypes("PointerPointer"));
        
        infoMap.put(new Info(
                "(_MSC_VER >= 1100) && defined(__cplusplus) && !defined(CINTERFACE)",
                "defined(__cplusplus) && !defined(CINTERFACE)",
                "WINAPI_FAMILY_PARTITION(WINAPI_PARTITION_APP)")
                .define(false));
        
        infoMap.put(new Info("EXTERN_C", "MIDL_INTERFACE", "__clrcall", "extern \"C++\"")
                .annotations().cppTypes());
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
    }
}