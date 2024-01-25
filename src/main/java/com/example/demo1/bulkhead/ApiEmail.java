package com.example.demo1.bulkhead;

import io.github.resilience4j.bulkhead.Bulkhead;
import io.github.resilience4j.bulkhead.BulkheadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class ApiEmail {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiEmail.class);
    private final Bulkhead bulkhead;
    public ApiEmail(){
        BulkheadConfig config = BulkheadConfig.custom()
                .maxConcurrentCalls(2)
                .maxWaitDuration(Duration.ofSeconds(1))
                .build();

        this.bulkhead = Bulkhead.of("api1", config);
    }
    public void notificarComBulkhead(int id) {

        bulkhead.executeRunnable( () -> {

            LOGGER.info("Notificação via email iniciada");
            try {
                Thread.sleep(Duration.ofMinutes(1).toMillis());
            } catch (InterruptedException e) {
            }
            LOGGER.info("Notificação via email concluída");
        });

    }

    public void notificarSemBulkHead(int id) {

        LOGGER.info("Notificação via email iniciada");
        try {
            Thread.sleep(Duration.ofMinutes(1).toMillis());
        } catch (InterruptedException e) {
        }
        LOGGER.info("Notificação via email concluída");
    }
}
