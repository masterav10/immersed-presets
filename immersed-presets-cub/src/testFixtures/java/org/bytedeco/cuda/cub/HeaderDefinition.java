package org.bytedeco.cuda.cub;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import org.inferred.freebuilder.FreeBuilder;

@FreeBuilder
public interface HeaderDefinition
{
    class Builder extends HeaderDefinition_Builder
    {

    }

    default String includeName(Path includeName)
    {
        Path relativePath = includeName.relativize(location());
        String headerDef = relativePath.toString()
                                       .replace(File.separatorChar, '/');

        return "\"<" + headerDef + ">\"";
    }

    default String getFileName()
    {
        return location().getFileName()
                         .toString();
    }

    Path location();

    List<FunctionDefinition> functions();
}
