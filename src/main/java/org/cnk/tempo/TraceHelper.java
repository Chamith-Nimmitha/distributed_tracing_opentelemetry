package org.cnk.tempo;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.zipkin.ZipkinSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import zipkin2.reporter.okhttp3.OkHttpSender;
import java.io.Closeable;
import java.io.IOException;

/**
 * @created: 02/02/2022
 * @project: tempo
 * @author: Chamith Nimmitha
 */
public class TraceHelper implements Closeable {

    private SdkTracerProvider sdkTracerProvider;
    private Tracer tracer;

    public TraceHelper(){
        ZipkinSpanExporter exporter = ZipkinSpanExporter.builder()
                .setSender(OkHttpSender.create("http://127.0.0.1:9411/api/v2/spans"))
                .build();

        this.sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(exporter)).build();
        this.tracer = sdkTracerProvider.tracerBuilder("Main").build();
    }

    public Span createSpan(String name){
        Span span = this.tracer.spanBuilder(name).startSpan();
        return span;
    }

    public Span createScopedSpan(String name){
        Span span = this.createSpan(name);
        span.makeCurrent();
        return span;
    }

    public Span createWithParent(String name, Span parent){
        parent.makeCurrent();
        return this.createScopedSpan(name);

    }

    public SpanBuilder getSpanBuilder(String name){
        return tracer.spanBuilder(name);
    }

    public Context getCurrentContext(){
        return Context.current();
    }

    public Span getCurrentSpan(){
        return Span.current();
    }

    public Span getCurrentSpanFromContext(Context context){
        return Span.fromContextOrNull(context);
    }

    public void endSpan(Span span){
        span.end();
    }

    public void closeTracer(){
        try {
            this.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("Tracer Closed");
        this.sdkTracerProvider.close();
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("Tracer finalize");
        this.sdkTracerProvider.close();
    }
}
