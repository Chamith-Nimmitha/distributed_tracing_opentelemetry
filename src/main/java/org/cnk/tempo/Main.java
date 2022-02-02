package org.cnk.tempo;


import io.opentelemetry.api.trace.Span;

import java.io.IOException;

/**
 * @created: 01/02/2022
 * @project: tempo
 * @author: Chamith Nimmitha
 */


class AnotherService extends Thread{

    private Span parent;
    public AnotherService(Span parent){
        this.parent = parent;
    }

    @Override
    public void run(){
        TraceHelper traceHelper = new TraceHelper();
        this.parent.makeCurrent();
        Span anotherService = traceHelper.createWithParent("anotherService", this.parent);
        traceHelper.endSpan(anotherService);
        traceHelper.closeTracer();
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException {

        TraceHelper traceHelper = new TraceHelper();

        /* start parent */
        Span first = traceHelper.createScopedSpan("first");

        /* called to child method*/
        Span child = traceHelper.createScopedSpan("child");

        /* called to another service*/
        AnotherService anotherService = new AnotherService(child);
        traceHelper.endSpan(child);
        traceHelper.endSpan(first);
        Thread.sleep(5000);
        anotherService.start();
        traceHelper.closeTracer();

    }
}
