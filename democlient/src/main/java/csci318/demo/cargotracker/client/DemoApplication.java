package csci318.demo.cargotracker.client;

import csci318.demo.cargotracker.client.dto.BookCargoResource;
import csci318.demo.cargotracker.client.dto.BookingIdDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.time.LocalDate;

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
		return  args -> {
			int i = 0;
			while (i < 5) {
				LocalDate day = LocalDate.now();
				BookCargoResource entry = new BookCargoResource(10, "HK", "NY", day);
				BookingIdDto bookingId = restTemplate.postForObject(url, entry, BookingIdDto.class);
				System.out.println("******" + bookingId);
				i++;
				Thread.sleep(500);
			}

		};
	}


}
