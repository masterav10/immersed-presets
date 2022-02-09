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
                  "Unknwnbase.h"
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
        infoMap.put(new Info("Unknwnbase.h")
                .linePatterns(".*__MIDL_itf.*").skip());
        
        infoMap.put(new Info("IUnknown::QueryInterface")
                .javaText("public native @Cast(\"HRESULT\") int QueryInterface( @Cast(\"const IID*\") @ByRef GUID riid, PointerPointer arg1/**ppvObject*/);"));
        
        infoMap.put(new Info("MIDL_INTERFACE")
                .cppText("#define MIDL_INTERFACE(x) struct"));
        
        infoMap.put(new Info("BEGIN_INTERFACE").cppText("#define BEGIN_INTERFACE"));
        infoMap.put(new Info("END_INTERFACE").cppText("#define END_INTERFACE"));
        
        infoMap.put(new Info(
                "(_MSC_VER >= 1100) && defined(__cplusplus) && !defined(CINTERFACE)",
                "defined(__cplusplus) && !defined(CINTERFACE)")
                .define(true));

        // skip these until we figure out how to deal with COM
        infoMap.put(new Info(
                "IRpcStubBuffer", 
                "IUnknown_QueryInterface_Proxy", 
                "IClassFactory_CreateInstance_Proxy", 
                "IClassFactory_LockServer_Stub", 
                "IClassFactory_RemoteLockServer_Proxy", 
                "IClassFactory_RemoteCreateInstance_Proxy",
                "IClassFactory_LockServer_Proxy", 
                "IClassFactory_CreateInstance_Stub")
               .skip());
        
        infoMap.put(new Info(
                "__RPC__deref_out", 
                "_COM_Outptr_")
                .annotations().cppTypes());
       
    }
}
