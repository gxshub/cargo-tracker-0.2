package csci318.demo.cargotracker.client;

import csci318.demo.cargotracker.client.dto.BookCargoResource;
import csci318.demo.cargotracker.client.dto.BookingIdDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Random;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    final String url = "http://localhost:8787/cargobooking";

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        return args -> {
            Random rand = new Random();
            int upperbound = 24;
            String[] cities = {"SYD", "SG", "HK", "NY"};
            int numCities = cities.length;
            LocalDate day = LocalDate.now();
            //int i = 0;
            while (true) {
                int bookAmount = rand.nextInt(upperbound) + 1;
                int r1 = rand.nextInt(numCities);
                int r2 = rand.nextInt(numCities - 1);
                String ori = cities[r1];
                String des = cities[(r1 + r2 + 1) % numCities];
                BookCargoResource entry = new BookCargoResource(bookAmount, ori, des, day);
                BookingIdDto bookingId = restTemplate.postForObject(url, entry, BookingIdDto.class);
                System.out.println("******" + bookingId + entry + "*****");
                //i++;
                Thread.sleep(500);
            }

        };
    }


}
