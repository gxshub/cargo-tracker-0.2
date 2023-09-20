package csci318.demo.cargotracker.client;

import csci318.demo.cargotracker.client.dto.BookCargoResource;
import csci318.demo.cargotracker.client.dto.BookingIdDto;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Random;

public class DemoClient {

    public static void main(String[] args) throws InterruptedException {

        final String url = "http://localhost:8787/cargobooking";

        Random rand = new Random();
        int upperbound = 24;
        String[] cities = {"SYD", "SG", "HK", "NY"};
        int numCities = cities.length;
        LocalDate day = LocalDate.now();
        RestTemplate restTemplate = new RestTemplate();
        while (true) {
            int bookAmount = rand.nextInt(upperbound) + 1;
            int r1 = rand.nextInt(numCities);
            int r2 = rand.nextInt(numCities - 1);
            String ori = cities[r1];
            String des = cities[(r1 + r2 + 1) % numCities];
            BookCargoResource entry = new BookCargoResource(bookAmount, ori, des, day);
            BookingIdDto bookingId = restTemplate.postForObject(url, entry, BookingIdDto.class);
            System.out.println("******" + bookingId + entry + "*****");
            //System.out.println(entry);
            Thread.sleep(500);
        }
    }
}
