package es.geeksusma.jwtrestcontroller.time;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class Clock {

    public Date getCurrentDate() {
        return Date.from(Instant.now());
    }

    public Date getCurrentExpirationTime(Date date, Long expirationTime) {
        return Date.from(Instant.ofEpochMilli(date.getTime() + expirationTime));
    }
}
