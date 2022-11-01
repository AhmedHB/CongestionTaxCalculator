package se.ahmed.microservices.core.tollfeecalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.api.core.tollfeecalculator.TollFeeTax;

import java.util.ArrayList;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class TollfeecalculatorServiceApplicationTests {
	private TollFee testTollFeeData;

	private TollFee getTollFeeGothenborg(){
		TollFee api = TollFee.builder()
				.city("göteborg")
				.tollFeeTimes(new ArrayList<TollFeeTime>(){{
					add(new TollFeeTime("06:00", "06:29", 8));
					add(new TollFeeTime("06:30", "06:59", 13));
					add(new TollFeeTime("07:00", "07:59", 18));
					add(new TollFeeTime("08:00", "08:29", 13));
					add(new TollFeeTime("08:30", "14:59", 8));
					add(new TollFeeTime("15:00", "15:29", 13));
					add(new TollFeeTime("15:30", "16:59", 18));
					add(new TollFeeTime("17:00", "17:59", 13));
					add(new TollFeeTime("18:00", "18:29", 8));
				}})
				.taxRules(TaxRules.builder().tollFreeVehicle(new ArrayList<String>(){{
					add("Motorcycle");
					add("Tractor");
					add("Emergency");
					add("Diplomat");
					add("Foreign");
					add("Military");
				}}).useTollFreeDay(true).build())
				.build();

		return api;
	}

	@Autowired
	private WebTestClient client;

	@Before
	public void setup(){
		testTollFeeData = getTollFeeGothenborg();
	}

	@Test
	public void calculateTollFeeOk() {
		TollFeeTax tollFeeTaxRequestBody = TollFeeTax.builder()
				.vehicle("Car")
				.dates(new ArrayList<String>(){{add("2022-06-13 06:00:00");}})
				.tollFee(testTollFeeData)
				.build();

		client.post()
				.uri("/tollfeecalculation/fee")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.fee").isNotEmpty()
				.jsonPath("$.fee").isNumber()
				.jsonPath("$.fee").isEqualTo("8");
	}

	@Test
	public void calculateTollFeeTaxOk() {
		TollFeeTax tollFeeTaxRequestBody = TollFeeTax.builder()
				.vehicle("Car")
				.dates(new ArrayList<String>(){{add("2013-01-14 07:30:00");add("2013-01-14 08:00:00");}})
				.tollFee(testTollFeeData)
				.build();

		client.post()
				.uri("/tollfeecalculation/tax")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.tax").isNotEmpty()
				.jsonPath("$.tax").isNumber()
				.jsonPath("$.tax").isEqualTo("18");
	}
}
