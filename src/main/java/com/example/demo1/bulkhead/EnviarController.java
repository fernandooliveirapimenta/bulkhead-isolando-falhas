package com.example.demo1.bulkhead;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnviarController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private ApiEmail api1;
    @Autowired
    private ApiEmail api2;

    @PostMapping(path = "/enviar/{id}")
    public int getPrice(@PathVariable("id") String id) {
        LOGGER.info(" Id {}", id);

        api1.notificarSemBulkHead(1);
        api2.notificarSemBulkHead(2);

        return 1;
    }

    @PostMapping(path = "/enviarv2/{id}")
    public int getPriceComBulkhead(@PathVariable("id") String id) {
        LOGGER.info(" Id {}", id);

        api1.notificarComBulkhead(1);
        api2.notificarComBulkhead(2);
        return 1;
    }
}
