package org.bytedeco.aff3ct.presets;

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
      target = "org.bytedeco.aff3ct", 
      global = "org.bytedeco.aff3ct.global.aff3ct",
      value = {
          @Platform(
              include = {
                  "aff3ct.hpp",
                  "Types/Argument_type.hpp",
                  "Maps/Argument_tag.hpp",
                  "Maps/Argument_info.hpp",
                  "Maps/Argument_links.hpp",
                  "Maps/Argument_map_info.hpp",
                  "Maps/Argument_map_value.hpp",
                  "Maps/Argument_map_group.hpp",
                  "Tools/Interface/Interface_clone.hpp",
                  "Tools/Factory/Header.hpp",
                  "Factory/Factory.hpp",
                  "Factory/Module/Decoder/Decoder.hpp",
                  "Factory/Module/Decoder/RS/Decoder_RS.hpp",
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
        infoMap.put(new Info("std::vector<std::pair<std::string,std::string> >")
               .pointerTypes("HeaderList").define());
        infoMap.put(new Info("std::map<cli::Argument_tag,cli::Argument_info*>")
               .pointerTypes("ArgTagToArgInfoPtrMap").define());
        infoMap.put(new Info("std::map<Argument_tag,Argument_info*>")
               .pointerTypes("MotherT").define());
        infoMap.put(new Info("std::vector<std::string>")
               .pointerTypes("Argument_tag").define());
        infoMap.put(new Info("std::vector<cli::Argument_link>")
               .pointerTypes("ArgumentLinkList").define());
        infoMap.put(new Info("std::map<cli::Argument_tag,std::pair<std::string,cli::Argument_info*> >")
               .pointerTypes("ArgTagToPairOfStringArgInfoPtrMap").define());
        infoMap.put(new Info("std::map<std::string,std::string>")
                .pointerTypes("StringToStringMap").define());
        infoMap.put(new Info("std::map<std::string,aff3ct::tools::header_list>")
                .pointerTypes("StringToHeaderListMap").define());
        
        infoMap.put(new Info("aff3ct::tools::header_list").pointerTypes("HeaderList"));
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