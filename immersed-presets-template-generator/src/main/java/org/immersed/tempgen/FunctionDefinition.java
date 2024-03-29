package org.immersed.tempgen;

import java.util.List;
import java.util.Optional;

import org.inferred.freebuilder.FreeBuilder;

@FreeBuilder
public interface FunctionDefinition
{
    class Builder extends FunctionDefinition_Builder
    {
        public Builder fromTextSignature(String definition)
        {
            this.clearTemplates();

            String clean = definition.replaceAll("\\s{2,}", " ");

            String templates = clean.substring(clean.indexOf("<") + 1, clean.indexOf(">"));
            String[] templateArray = templates.split(",");

            for (String templateString : templateArray)
            {
                templateString = templateString.trim();

                String[] templateStringArray = templateString.split(" ");

                this.addTemplates(templateStringArray[1]);
            }

            String function = clean.substring(clean.indexOf(">") + 2, clean.indexOf("("));
            String[] parts = function.split(" ");

            this.name(parts[parts.length - 1]);

            return this;
        }
    }

    Optional<String> namespace();

    Optional<String> struct();

    List<String> templateTypes();

    List<String> templates();

    String name();

    default String toDefinition()
    {
        StringBuilder builder = new StringBuilder();

        namespace().ifPresent(ns -> builder.append(ns)
                                           .append("::"));

        struct().ifPresent(st -> builder.append(st)
                                        .append("::"));

        builder.append(name());
        builder.append(templates().stream()
                                  .reduce((a, b) -> a + "," + b)
                                  .map(s -> "<" + s + ">")
                                  .orElse(""));

        return builder.toString();
    }
}
