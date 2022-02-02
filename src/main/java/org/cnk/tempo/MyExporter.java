package org.cnk.tempo;

import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.util.Collection;

/**
 * @created: 02/02/2022
 * @project: tempo
 * @author: Chamith Nimmitha
 */
public class MyExporter implements SpanExporter {
    @Override
    public CompletableResultCode export(Collection<SpanData> collection) {
        System.out.println("Called1");
        collection.forEach(span -> System.out.println(span));
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        System.out.println("Called2");
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode shutdown() {
        System.out.println("Called3");
        return CompletableResultCode.ofSuccess();
    }
}
