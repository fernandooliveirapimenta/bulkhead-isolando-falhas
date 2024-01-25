package com.example.demo1;

import io.github.resilience4j.bulkhead.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class BulkheadTests {

    @Test
    public void teste(){
        Bulkhead bulkhea = Bulkhead.ofDefaults("testeBulkHead");
        String resultado = bulkhea.executeSupplier(() -> String.join(",", "a",
                "b", "c"));
        Assertions.assertThat(resultado).isEqualTo("a,b,c");
    }

    @Test
    public void teste2() throws InterruptedException {
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(2) //cada maquina vai variar cpu
                .maxWaitDuration(Duration.ofSeconds(1))
                .build();

        Bulkhead bulkhead = Bulkhead.of("testeBulkHead",config);

        Thread primeira = new Thread(() -> {
            bulkhead.executeRunnable(() -> dizerOlaApos(10));
        });

        Thread segunda = new Thread(() -> {
            bulkhead.executeRunnable(() -> dizerOlaApos(10));
        });

        primeira.start();
        segunda.start();

        Thread.sleep(100);

        Assertions.assertThat(bulkhead.getMetrics().getAvailableConcurrentCalls()).isEqualTo(0);

        Thread.sleep(Duration.ofSeconds(1).toMillis());
        bulkhead.executeRunnable(() -> dizerOlaApos(2));

//        Assertions.assertThatExceptionOfType(BulkheadFullException.class)
//                .isThrownBy(() -> bulkhead.executeRunnable(() -> dizerOlaApos(2)));
    }

    @Test
    public void testeThreadPool() throws ExecutionException, InterruptedException {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(2)
                .coreThreadPoolSize(1)
                .queueCapacity(2)
                .keepAliveDuration(Duration.ofSeconds(2)).build();

        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead.of("testeTheadPool",
                config);
        CompletionStage<String> completionStage = threadPoolBulkhead.executeSupplier(()
                -> String.join(",", "a", "b", "c"));

        String resultado = completionStage.toCompletableFuture().get();

        Assertions.assertThat(resultado).isEqualTo("a,b,c");

    }

    @Test
    public void testeThreadPool2() throws ExecutionException, InterruptedException {
        ThreadPoolBulkheadConfig config = ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(2)
                .coreThreadPoolSize(1)
                .queueCapacity(2)
                .keepAliveDuration(Duration.ofSeconds(2)).build();

        ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead.of("testeTheadPool",
                config);

        //Execucao
        threadPoolBulkhead.executeRunnable(() -> dizerOlaApos(10));
        threadPoolBulkhead.executeRunnable(() -> dizerOlaApos(10));

        //Em fila
        threadPoolBulkhead.executeRunnable(() -> dizerOlaApos(10));
        threadPoolBulkhead.executeRunnable(() -> dizerOlaApos(10));

        //Não tem mais espaco na fila
        Assertions.assertThatExceptionOfType(BulkheadFullException.class)
                .isThrownBy(() ->threadPoolBulkhead.executeRunnable(() -> dizerOlaApos(10)));

    }

    private void dizerOlaApos(int s) {
        try {
            Thread.sleep(Duration.ofSeconds(s).toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
