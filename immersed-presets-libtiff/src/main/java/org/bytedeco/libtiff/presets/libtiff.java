package org.bytedeco.libtiff.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
      inherit = {
          windows.class,
      },
      names = {"windows-x86_64"}, 
      target = "org.bytedeco.libtiff", 
      global = "org.bytedeco.libtiff.global.libtiff",
      value = {
          @Platform(
              // "tiffconf.h", "tif_config.h",
              include = {
                  "t4.h", "tif_dir.h", "tif_fax3.h", 
                  "tif_hash_set.h", "tif_predict.h", "tiff.h",
                  "tiffio.h", "tiffiop.h", "tiffvers.h", "uvcode.h"
              },
              link = {
                  "tiff", "tiffxx"
              },
              define = {
                  // "_WINSOCKAPI_"
              }
          )
      }
)
@NoException
public class libtiff implements InfoMapper, LoadEnabled
{   
    @Override
    public void map(InfoMap infoMap)
    {   
        infoMap.put(new Info("_TIFFField", "_TIFFFieldArray", "_TIFFRGBAImage")
               .skip());
        
        // do not use directly
        infoMap.put(new Info("FIELD_LAST", "TIFF_NON_EXISTENT_DIR_NUMBER", "_TIFF_off_t")
               .cppTypes("int").translate(false));
        infoMap.put(new Info("TIFF_TMSIZE_T_MAX").cppTypes("long long").translate(false));
        
        infoMap.put(new Info("tmsize_t")
               .cast().valueTypes("long").pointerTypes("LongPointer", "LongBuffer", "long[]"));
        infoMap.put(new Info("tdir_t", "ttag_t", "TIFFDataType")
               .cast().valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int[]"));
        
        infoMap.put(new Info("tiff").pointerTypes("TIFF"));
        infoMap.put(new Info("client_info").pointerTypes("TIFFClientInfoLink"));
    }

    @Override
    public void init(ClassProperties properties)
    {
        final String srcs = System.getProperty("libtiff.src");
        
        List<String> includePaths = properties.get("platform.includepath");
        
        if(!includePaths.contains(srcs))
        {
            includePaths.add(srcs);
        }
        
        final List<String> linkPaths = properties.get("platform.linkpath");
        
        for(Object keyObj : System.getProperties().keySet())
        {
            final String key = keyObj.toString();
            
            if(key.startsWith("libtiff.bin"))
            {
                final String value = System.getProperty(key);
                
                if(!linkPaths.contains(value))
                {
                    linkPaths.add(value);
                }
            } 
        }
    }
    
  //@formatter:on
}