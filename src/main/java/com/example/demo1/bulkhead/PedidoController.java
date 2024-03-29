package com.example.demo1.bulkhead;

import com.example.demo1.Price;
import com.example.demo1.PriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PedidoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PriceRepository priceRepository;

    @GetMapping(path = "/pedido/{id}")
    public Price getPrice(@PathVariable("id") String productId) {
        LOGGER.info("Getting Price details for Product Id {}", productId);
        return priceRepository.getPrice(productId);
    }
}