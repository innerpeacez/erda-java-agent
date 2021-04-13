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

package cloud.erda.agent.core.tracing.propagator;

import cloud.erda.agent.core.tracing.SpanContext;
import cloud.erda.agent.core.tracing.TracerContext;

import java.util.Map;

/**
 * @author liuhaoyang
 * @since 2019-01-07 17:38
 **/
public class BaggageHeader extends Header {

    private static final String Request_Bg = "terminus-request-bg-";

    public BaggageHeader(Header next) {
        super(next);
    }

    @Override
    public void inject(SpanContext context, Carrier carrier) {
        for (Map.Entry<String, String> entry : context.getTracerContext()) {
            if (TracerContext.REQUEST_ID.equals(entry.getKey()) || TracerContext.SAMPLED.equals(entry.getKey())) {
                continue;
            }
            carrier.put(Request_Bg + entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void extract(SpanContext.Builder builder, Carrier carrier) {
        for (Map.Entry<String, String> entry : carrier) {
            if (entry.getKey().startsWith(Request_Bg)) {
                String key = entry.getKey().substring((Request_Bg.length()));
                builder.getTracerContext().put(key, entry.getValue());
            }
        }
    }
}
