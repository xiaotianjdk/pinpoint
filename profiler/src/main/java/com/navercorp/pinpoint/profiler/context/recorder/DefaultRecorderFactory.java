/*
 * Copyright 2017 NAVER Corp.
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

package com.navercorp.pinpoint.profiler.context.recorder;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.navercorp.pinpoint.bootstrap.context.AsyncState;
import com.navercorp.pinpoint.bootstrap.context.SpanRecorder;
import com.navercorp.pinpoint.common.util.Assert;
import com.navercorp.pinpoint.profiler.context.AsyncContextFactory;
import com.navercorp.pinpoint.profiler.context.Span;
import com.navercorp.pinpoint.profiler.metadata.SqlMetaDataService;
import com.navercorp.pinpoint.profiler.metadata.StringMetaDataService;

/**
 * @author Woonduk Kang(emeroad)
 */
public class DefaultRecorderFactory implements RecorderFactory {

    private final StringMetaDataService stringMetaDataService;
    private final SqlMetaDataService sqlMetaDataService;
    private final Provider<AsyncContextFactory> asyncContextFactoryProvider;

    @Inject
    public DefaultRecorderFactory(Provider<AsyncContextFactory> asyncContextFactoryProvider, StringMetaDataService stringMetaDataService, SqlMetaDataService sqlMetaDataService) {
        this.asyncContextFactoryProvider = Assert.requireNonNull(asyncContextFactoryProvider, "asyncContextFactoryProvider must not be null");
        this.stringMetaDataService = Assert.requireNonNull(stringMetaDataService, "stringMetaDataService must not be null");
        this.sqlMetaDataService = Assert.requireNonNull(sqlMetaDataService, "sqlMetaDataService must not be null");
    }

    @Override
    public SpanRecorder newSpanRecorder(Span span, boolean isRoot, boolean sampling) {
        return new DefaultSpanRecorder(span, isRoot, sampling, stringMetaDataService, sqlMetaDataService);
    }

    @Override
    public WrappedSpanEventRecorder newWrappedSpanEventRecorder() {
        final AsyncContextFactory asyncContextFactory = asyncContextFactoryProvider.get();
        return new WrappedSpanEventRecorder(asyncContextFactory, stringMetaDataService, sqlMetaDataService, null);
    }

    @Override
    public WrappedSpanEventRecorder newWrappedSpanEventRecorder(AsyncState asyncState) {
        Assert.requireNonNull(asyncState, "asyncState must not be null");

        final AsyncContextFactory asyncContextFactory = asyncContextFactoryProvider.get();
        return new WrappedSpanEventRecorder(asyncContextFactory, stringMetaDataService, sqlMetaDataService, asyncState);
    }
}
