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
                  "Unknwnbase.h",
              },
              
              link = {
                  "ole32",
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
        // whitelist of various COM files
        infoMap.put(new Info("objbase.h").linePatterns(
                "// COM initialization flags; passed to CoInitialize.", 
                "} COINIT;"));
        
        infoMap.put(new Info("Unknwnbase.h").linePatterns(
                "EXTERN_C const IID IID_IUnknown;",
                ".*};"));
        
        infoMap.put(new Info("WTypesbase.h").linePatterns(
                "enum tagCLSCTX",
                ".*CLSCTX;"));
        
//        infoMap.put(new Info("combaseapi.h").linePatterns(
//                "typedef enum tagCOINITBASE", 
//                ".*COINITBASE;",
//                "// With DCOM, CLSCTX_REMOTE_SERVER should be included", 
//                "// class registration flags; passed to CoRegisterClassObject"));
        
        infoMap.put(new Info("combaseapi.h")
                .linePatterns(".*__MIDL_itf.*").skip());
        
        infoMap.put(new Info("IUnknown").purify(false).virtualize());
        
        infoMap.put(new Info("IUnknown::QueryInterface")
                .javaText("@Virtual(true) public native @Cast(\"HRESULT\") int QueryInterface( @Cast(\"const IID*\") @ByRef GUID riid, PointerPointer ppv/**ppvObject*/);"));
        
        infoMap.put(new Info("MIDL_INTERFACE")
                .cppText("#define MIDL_INTERFACE(x) struct"));
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
        
        infoMap.put(new Info(
                "(_MSC_VER >= 1100) && defined(__cplusplus) && !defined(CINTERFACE)",
                "defined(__cplusplus) && !defined(CINTERFACE)")
                .define(true));
        
        // skip these until we figure out how to deal with COM
        infoMap.put(new Info("LPSTREAM", "LPMALLOC", "LPSURROGATE", "LPMARSHAL", "IAgileReference",
                "SOLE_AUTHENTICATION_SERVICE", "IActivationFilter", "RPC_AUTHZ_HANDLE",
                "PRPC_MESSAGE", "DllGetClassObject", "DllCanUnloadNow", "COSERVERINFO", 
                "IClassFactory_CreateInstance_Proxy", "IClassFactory_LockServer_Stub", 
                "IClassFactory_RemoteLockServer_Proxy", "IClassFactory_RemoteCreateInstance_Proxy",
                "IClassFactory_LockServer_Proxy", "IClassFactory_CreateInstance_Stub", 
                "IUnknown_QueryInterface_Proxy", "LPMALLOCSPY", "IMalloc", "LPINITIALIZESPY", 
                "LPMESSAGEFILTER", "IChannelHook", "LPDATAADVISEHOLDER", "IStorage", "ILockBytes", 
                "IFillLockBytes", "IBindCtx", "uCLSSPEC", "QUERYCONTEXT", "LPMONIKER", "BIND_OPTS", "LPBC", 
                "IBindStatusCallback", "LPRUNNINGOBJECTTABLE", "FILETIME", "RPC_IF_HANDLE", "HYPER_SIZEDARR",
                "BYTE_BLOB", "BYTE_SIZEDARR", "FLAGGED_BYTE_BLOB", "SID_AND_ATTRIBUTES")
                .skip());
        
        infoMap.put(new Info("WINOLEAPI", "FARSTRUCT", "REFCLSID", "CONST_VTBL", "__STRUCT__", "interface", "THIS_",
                             "PURE", "THIS", "_Outptr_opt_result_buffer_",
                             "_Outptr_result_buffer_", "_Pre_maybenull_", "__drv_allocatesMem", "__RPC__in", 
                             "_COM_Outptr_", "__RPC__deref_out", "__RPC__deref_out_opt", "HFILE_ERROR", "__RPC_FAR",
                             "In_opt_z_", "_fastcall", "wIsEqualGUID")
                .annotations().cppTypes());
        
        infoMap.put(new Info("CLSID")
                .valueTypes("GUID"));
        infoMap.put(new Info("LPCLSID")
                .cast().pointerTypes("GUID"));
        
        infoMap.put(new Info("void *", "RPC_AUTH_IDENTITY_HANDLE")
                .cast().valueTypes("Pointer").pointerTypes("PointerPointer"));
        
        infoMap.put(new Info("ULONG", "APTTYPE", "APTTYPEQUALIFIER")
                .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("LPCOLESTR", "LPOLESTR", "BSTR")
                .cast().pointerTypes("CharPointer", "CharBuffer", "char[]").pointerTypes("PointerPointer<CharPointer>"));
        
        infoMap.put(new Info("OLECHAR")
                .cast().valueTypes("char").pointerTypes("CharPointer", "CharBuffer", "char[]"));
        
        infoMap.put(new Info("LPHANDLE", "LPUNKNOWN").cast().valueTypes("PointerPointer"));
    }
}
