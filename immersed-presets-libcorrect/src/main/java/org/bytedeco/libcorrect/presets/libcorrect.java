package org.bytedeco.libcorrect.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
      inherit = {
          javacpp.class,
      },
      names = {"windows-x86_64"}, 
      target = "org.bytedeco.libcorrect", 
      global = "org.bytedeco.libcorrect.global.libcorrect",
      value = {
          @Platform(
              cinclude = {
                  "correct/reed-solomon.h",
                  "correct.h", 
              },
              link = {
                  "correct", 
                  // "kernel32", "user32", "gdi32", "winspool", "shell32", "ole32", "oleaut32", "uuid", "comdlg32", "advapi32"
              },
              define = {
                  "__GNUC__ 0",
                  "HAVE_BUILTINS 0"
              }
          )
      }
)
@NoException
public class libcorrect implements InfoMapper, LoadEnabled
{   
    @Override
    public void map(InfoMap infoMap)
    {   
        infoMap.put(new Info("correct_convolutional")
                .skip());
    }

    @Override
    public void init(ClassProperties properties)
    {
        final Class<? extends libcorrect> type = getClass();
        final String name = type.getSimpleName();
        final String srcPrefix = name.concat(".src");
        final String linkPrefix = name.concat(".bin");

        final List<String> includePaths = properties.get("platform.includepath");
        final List<String> linkPaths = properties.get("platform.linkpath");
        
        for(Object key : System.getProperties().keySet())
        {
            fromEnv(key, srcPrefix, includePaths);
            fromEnv(key, linkPrefix, linkPaths);
        }
    }
    
    private void fromEnv(Object keyObj, String prefix, List<String> paths) 
    {
        final String key = keyObj.toString();
        
        if(key.startsWith(prefix))
        {
            final String value = System.getProperty(key);
            
            if(!paths.contains(value))
            {
                paths.add(value);
            }
        } 
    }
    
  //@formatter:on
}