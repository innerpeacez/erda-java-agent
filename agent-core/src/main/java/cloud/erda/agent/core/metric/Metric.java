/*
 * Copyright (c) 2021 Terminus, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cloud.erda.agent.core.metric;

import org.apache.skywalking.apm.agent.core.util.Strings;

import java.util.HashMap;
import java.util.Map;

public class Metric {
    private String name;
    private long timestamp;
    private Map<String, Object> fields;
    private Map<String, String> tags;

    private Metric(String name, long timestamp) {
        if (Strings.isEmpty(name)) {
            throw new IllegalArgumentException("Metric name cannot be empty or null.");
        }
        this.name = name.replace('.', '_').replace('-', '_');
        this.timestamp = timestamp;
        this.fields = new HashMap<String, Object>();
        this.tags = new HashMap<String, String>();
    }

    public Metric addTag(String key, String value) {
        if (Strings.isEmpty(key) || Strings.isEmpty(value)) {
            return this;
        }

        this.tags.put(key.replace('.', '_').replace('-', '_'), value);
        return this;
    }

    public Metric addField(String key, Object value) {
        if (Strings.isEmpty(key) || value == null) {
            return this;
        }
        this.fields.put(key.replace('.', '_').replace('-', '_'), value);
        return this;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public static Metric New(String name, long timestamp) {
        return new Metric(name, timestamp);
    }
}
