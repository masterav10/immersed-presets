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
        
        infoMap.put(new Info("Unknwnbase.h").linePatterns(
                "EXTERN_C const IID IID_IUnknown;",
                ".*};"));
        
        // for extending in java
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
        
        infoMap.put(new Info("CLSID")
                .valueTypes("GUID"));
        infoMap.put(new Info("LPCLSID")
                .cast().pointerTypes("GUID"));
    }
}
