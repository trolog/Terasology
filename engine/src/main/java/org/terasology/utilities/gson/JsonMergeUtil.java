/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.utilities.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * @author Immortius
 */
public final class JsonMergeUtil {
    private JsonMergeUtil() {
    }

    public static void mergeOnto(JsonObject from, JsonObject to) {
        for (Map.Entry<String, JsonElement> entry : from.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                if (!to.has(entry.getKey())) {
                    to.add(entry.getKey(), entry.getValue());
                }
            } else {
                if (!to.has(entry.getKey())) {
                    to.add(entry.getKey(), entry.getValue());
                }
            }
        }
    }
}
