package org.bytedeco.aff3ct.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.presets.javacpp;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

//@formatter:off
@Properties(
      inherit = {
          javacpp.class,
      },
      names = {"windows-x86_64"}, 
      target = "org.bytedeco.aff3ct", 
      global = "org.bytedeco.aff3ct.global.aff3ct",
      value = {
          @Platform(
              include = {
                  "aff3ct.hpp"
              }
          )
      }
)
@NoException
public class aff3ct implements InfoMapper, LoadEnabled
{   
    @Override
    public void map(InfoMap infoMap)
    {  
        
    }

    @Override
    public void init(ClassProperties properties)
    {
        final Class<? extends aff3ct> type = getClass();
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