/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.terminus.spot.plugin.httpasyncclient.v4.define;

import org.apache.skywalking.apm.agent.core.plugin.interceptor.ConstructorInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.InstanceMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassInstanceMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.MultiClassNameMatch;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * {@link HttpAsyncClientInstrumentation} indicates that the execute method in both
 * org.apache.http.impl.nio.client.MinimalHttpAsyncClient#execute(HttpAsyncRequestProducer, HttpAsyncResponseConsumer,
 * HttpContext, FutureCallback) and InternalHttpAsyncClient#execute(HttpAsyncRequestProducer, HttpAsyncResponseConsumer,
 * HttpContext, FutureCallback) can be instrumented for single request.pipeline is not support now for some complex
 * situation.this is run in main thread.
 *
 * @author lican
 */
public class HttpAsyncClientInstrumentation extends ClassInstanceMethodsEnhancePluginDefine {

    private static final String ENHANCE_CLASS_MINIMAL = "org.apache.http.impl.nio.client.MinimalHttpAsyncClient";
    private static final String ENHANCE_CLASS_INTERNAL = "org.apache.http.impl.nio.client.InternalHttpAsyncClient";

    private static final String METHOD = "execute";
    private static final String FIRST_ARG_TYPE = "org.apache.http.nio.protocol.HttpAsyncRequestProducer";

    private static final String INTERCEPTOR_CLASS
        = "io.terminus.spot.plugin.httpasyncclient.v4.HttpAsyncClientInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        return MultiClassNameMatch.byMultiClassMatch(ENHANCE_CLASS_MINIMAL, ENHANCE_CLASS_INTERNAL);
    }

    @Override
    protected ConstructorInterceptPoint[] getConstructorsInterceptPoints() {
        return null;
    }

    @Override
    protected InstanceMethodsInterceptPoint[] getInstanceMethodsInterceptPoints() {
        return new InstanceMethodsInterceptPoint[] {
            new InstanceMethodsInterceptPoint() {
                @Override
                public ElementMatcher<MethodDescription> getMethodsMatcher() {
                    return named(METHOD).and(takesArguments(4)
                        .and(takesArgument(0, named(FIRST_ARG_TYPE))));
                }

                @Override
                public String getMethodsInterceptor() {
                    return INTERCEPTOR_CLASS;
                }

                @Override
                public boolean isOverrideArgs() {
                    return true;
                }
            }
        };
    }
}
