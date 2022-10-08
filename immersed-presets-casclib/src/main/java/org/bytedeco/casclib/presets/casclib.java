package org.bytedeco.casclib.presets;

import java.util.List;

import org.bytedeco.javacpp.ClassProperties;
import org.bytedeco.javacpp.LoadEnabled;
import org.bytedeco.javacpp.annotation.NoException;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;
import org.bytedeco.systems.presets.windows;

//@formatter:off
@Properties(
        inherit = {
            windows.class,
        },
        names = {"windows-x86_64"}, 
        target = "org.bytedeco.casclib", 
        global = "org.bytedeco.global.casclib",
        value = {
            @Platform(
                include = {
                    "CascLib.h"
                },
                link = {
                    "CascLibRUS"
                },
                define = {
                    "_WINSOCKAPI_"
                }
            )
        }
)
@NoException
public class casclib implements InfoMapper, LoadEnabled
{   
    @Override
    public void map(InfoMap infoMap)
    {   
    }

    @Override
    public void init(ClassProperties properties)
    {
        final String srcs = System.getProperty("casclib.src");
        
        List<String> includePaths = properties.get("platform.includepath");
        
        if(!includePaths.contains(srcs))
        {
            includePaths.add(srcs);
        }
        
        final List<String> linkPaths = properties.get("platform.linkpath");
        
        for(Object keyObj : System.getProperties().keySet())
        {
            final String key = keyObj.toString();
            
            if(key.startsWith("casclib.bin"))
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