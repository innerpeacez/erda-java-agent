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

/**
 * @author liuhaoyang
 * @since 2019-01-07 16:01
 **/
public class SpanIdHeader extends Header {

    private static final String Request_Span_Id = "terminus-request-spanid";

    public SpanIdHeader(Header next) {
        super(next);
    }

    @Override
    public void inject(SpanContext context, Carrier carrier) {
        String spanId = context.getSpanId();
        carrier.put(Request_Span_Id, spanId);
    }

    @Override
    public void extract(SpanContext.Builder builder, Carrier carrier) {
        String parentSpanId = carrier.get(Request_Span_Id);
        builder.setSpanId(parentSpanId);
    }
}
