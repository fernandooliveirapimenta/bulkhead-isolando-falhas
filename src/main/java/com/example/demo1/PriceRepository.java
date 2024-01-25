package com.example.demo1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PriceRepository.class);

    Map<String, Price> priceMap = new HashMap<>();

    public PriceRepository() {
        Price p = new Price();
        p.setPriceAmount(33.3);
        priceMap.put("1", p );
    }


    public Price getPrice(String productId){
        LOGGER.info("Getting Price from Price Repo With Product Id {}", productId);
        if(!priceMap.containsKey(productId)){
            LOGGER.error("Price Not Found for Product Id {}", productId);
            throw new RuntimeException("Not found");
        }
        return priceMap.get(productId);
    }
}
