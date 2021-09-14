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
                  "advapi32",
                  "comdlg32",
                  "comsuppw", 
                  "kernel32",
                  "ole32",
                  "oleaut32",
                  "shell32",
                  "user32",
                  "uuid",
                  "winspool",
              },
              preloadpath = {
                  "C:/Program Files (x86)/Microsoft Visual Studio 14.0/VC/lib/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30037/lib/x64/",
                  "C:/Program Files (x86)/Windows Kits/8.1/Lib/winv6.3/um/x64/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30037/lib/x64/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30037/atlmfc/lib/x64/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Auxiliary/VS/lib/x64/",
                  "C:/Program Files (x86)/Windows Kits/10/lib/10.0.10240.0/ucrt/x64/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Auxiliary/VS/UnitTest/lib/",
                  "C:/Program Files (x86)/Windows Kits/8.1/lib/winv6.3/um/x64/",
                  "C:/Program Files (x86)/Windows Kits/NETFXSDK/4.8/Lib/um/x64/",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30037/atlmfc/lib/x64",
                  "C:/Program Files (x86)/Microsoft Visual Studio/2019/Community/VC/Tools/MSVC/14.29.30037/lib/x64/",
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
        infoMap.put(new Info("LPSTREAM", "LPMALLOC", "LPSURROGATE", "LPMARSHAL", "IAgileReference",
                             "SOLE_AUTHENTICATION_SERVICE", "IActivationFilter", "RPC_AUTHZ_HANDLE",
                             "PRPC_MESSAGE", "DllGetClassObject", "DllCanUnloadNow", "COSERVERINFO")
                .skip());
        
        infoMap.put(new Info("WINOLEAPI", "FARSTRUCT", "REFCLSID", "CONST_VTBL", "__STRUCT__", "interface", "THIS_",
                             "PURE", "THIS", "CLSCTX_INPROC", "CLSCTX_ALL", "CLSCTX_SERVER", "_Outptr_opt_result_buffer_",
                             "_Outptr_result_buffer_", "_Pre_maybenull_", "__drv_allocatesMem", "__RPC__in", "_COM_Outptr_",
                             "__RPC__deref_out", "__RPC__deref_out_opt", "HFILE_ERROR")
                .annotations().cppTypes());
        
        infoMap.put(new Info("CLSID")
                .valueTypes("GUID"));
        infoMap.put(new Info("LPCLSID")
                .cast().pointerTypes("GUID"));
        
        infoMap.put(new Info("RPC_AUTH_IDENTITY_HANDLE")
                .cast().valueTypes("Pointer").pointerTypes("PointerPointer"));
        
        infoMap.put(new Info("ULONG", "APTTYPE", "APTTYPEQUALIFIER")
                .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("LPCOLESTR", "LPOLESTR")
                .cast().pointerTypes("CharPointer", "CharBuffer", "char[]"));
        infoMap.put(new Info("OLECHAR")
                .cast().valueTypes("char").pointerTypes("CharPointer", "CharBuffer", "char[]"));
        
        infoMap.put(new Info("LPHANDLE", "LPUNKNOWN").cast().valueTypes("PointerPointer"));
        
        infoMap.put(new Info(
                "(_MSC_VER >= 1100) && defined(__cplusplus) && !defined(CINTERFACE)",
                "defined(__cplusplus) && !defined(CINTERFACE)")
                .define(false));
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
    }
}
