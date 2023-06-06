package org.immersed.tempgen;

import java.io.File;
import java.nio.file.Path;
import java.util.Set;

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

    Set<FunctionDefinition> functions();
}
