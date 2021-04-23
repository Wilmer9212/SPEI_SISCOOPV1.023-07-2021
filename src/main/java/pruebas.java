
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Elliot
 */
public class pruebas {
    public static void main(String[] args) {
         // 2021-03-24 16:48:05.591
  Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("Timestamp:"+timestamp);

  // 2021-03-24 16:48:05.591
  Date date = new Date();
  Timestamp timestamp2 = new Timestamp(date.getTime());

  // convert Instant to Timestamp
  Timestamp ts = Timestamp.from(Instant.now());


  // convert Timestamp to Instant
  Instant instant = ts.toInstant();
    }
}
