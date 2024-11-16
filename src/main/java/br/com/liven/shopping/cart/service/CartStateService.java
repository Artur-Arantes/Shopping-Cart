package br.com.liven.shopping.cart.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class CartStateService {

    @Value("${time.to.cart.expire}")
    private long timeToExpire;


    public boolean isCartExpired(ZonedDateTime updatedAt) {
        ZonedDateTime now = ZonedDateTime.now(updatedAt.getZone());
        Duration duration = Duration.between(updatedAt, now);

        return duration.toMinutes() > new BigDecimal(timeToExpire).longValue();
    }
}
