/*
 * Copyright 2013  Séven Le Mesle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package fr.xebia.extras.selma.codegen;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MappingRegistry {

    final Map<InOutType, MappingBuilder> registryMap;
    final MapperGeneratorContext context;


    public MappingRegistry(MapperGeneratorContext context) {
        this.registryMap = new HashMap<InOutType, MappingBuilder>();
        this.context = context;
    }

    public MappingBuilder findMappingFor(InOutType inOutType) {

        MappingBuilder res;
        // First look in registry
        if (registryMap.get(inOutType) != null) {
            res = registryMap.get(inOutType);
        } else { // look in chain

            res = MappingBuilder.getBuilderFor(context, inOutType);
            if (res != null && !inOutType.areDeclared()) {
                registryMap.put(inOutType, res);
            }

        }

        return res;
    }

    /**
     * Adds a custom mapping method to the registry for later use at codegen.
     *
     * @param customMapper
     * @param method
     */
    public void pushCustomMapper(final String customMapper, final MethodWrapper method) {


        InOutType inOutType = method.inOutType();
        MappingBuilder res = MappingBuilder.newCustomMapper(inOutType, String.format("%s.%s", customMapper, method.getSimpleName()));

        registryMap.put(inOutType, res);
    }
}
