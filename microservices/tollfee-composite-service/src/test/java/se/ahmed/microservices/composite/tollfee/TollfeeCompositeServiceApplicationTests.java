package se.ahmed.microservices.composite.tollfee;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import se.ahmed.api.composite.tollfee.request.TollFeeRequest;
import se.ahmed.api.composite.tollfee.request.TollFeeTaxRequest;
import se.ahmed.api.composite.tollfee.response.TollFeeResponse;
import se.ahmed.api.composite.tollfee.response.TollFeeTaxResponse;
import se.ahmed.api.core.tollfee.TaxRules;
import se.ahmed.api.core.tollfee.TollFee;
import se.ahmed.api.core.tollfee.TollFeeTime;
import se.ahmed.api.core.tollfeecalculator.TollFeeTax;
import se.ahmed.microservices.composite.tollfee.services.TollFeeCompositeIntegration;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
public class TollfeeCompositeServiceApplicationTests {
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

	@Before
	public void setup(){
		setupTollFeeCompositIntegration();
		setupTollFeeCalculation();
		setupTollFeeTaxCalculation();
	}

	private void setupTollFeeCompositIntegration(){
		testTollFeeData = getTollFeeGothenborg();

		when(compositeIntegration.getTollFee("gothenburg")).
				thenReturn(testTollFeeData);
	}

	private void setupTollFeeCalculation(){
		TollFeeTax tollFeeTaxRequestBody = TollFeeTax.builder()
				.vehicle("Car")
				.dates(new ArrayList<String>(){{add("2022-06-13 06:00:00");}})
				.tollFee(testTollFeeData)
				.build();

		TollFeeResponse tollFee = new TollFeeResponse();
		tollFee.setFee(8);
		when(compositeIntegration.getTollFeeCalculation(tollFeeTaxRequestBody))
		.thenReturn(tollFee);
	}

	private void setupTollFeeTaxCalculation(){
		TollFeeTax tollFeeTaxRequestBody = TollFeeTax.builder()
				.vehicle("Car")
				.dates(new ArrayList<String>(){{add("2013-01-14 07:30:00");add("2013-01-14 08:00:00");}})
				.tollFee(testTollFeeData)
				.build();

		TollFeeTaxResponse tollFeeTax = new TollFeeTaxResponse();
		tollFeeTax.setTax(18);
		when(compositeIntegration.getTollFeeTaxCalculation(tollFeeTaxRequestBody)).
				thenReturn(tollFeeTax);
	}

	@Autowired
	private WebTestClient client;

	@MockBean
	private TollFeeCompositeIntegration compositeIntegration;

	@Test
	public void getTollFeeOk() {
		String candidateDate = "2022-06-13 06:00:00";
		TollFeeRequest tollFeeTaxRequestBody = TollFeeRequest.builder()
				.vehicle("Car")
				.city("gothenburg")
				.date(candidateDate)
				.build();

		client.post()
				.uri("/tollfee-composite/fee")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.fee").isNotEmpty()
				.jsonPath("$.fee").isNumber()
				.jsonPath("$.fee").isEqualTo("8");
	}

	@Test
	public void getTollFeeWrongDateFormatBAD_REQUEST() {
		String wrongDateformat = "2022-06/13 06:00:00";
		TollFeeRequest tollFeeTaxRequestBody = TollFeeRequest.builder()
				.vehicle("Car")
				.city("gothenburg")
				.date(wrongDateformat)
				.build();

		client.post()
				.uri("/tollfee-composite/fee")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType("application/json")
				.expectBody();
	}

	@Test
	public void getTollFeeWrongVehicleBAD_REQUEST() {
		String wrongVehicle = "Airplane";
		TollFeeRequest tollFeeTaxRequestBody = TollFeeRequest.builder()
				.vehicle(wrongVehicle)
				.city("gothenburg")
				.date("2022-06-13 06:00:00")
				.build();

		client.post()
				.uri("/tollfee-composite/fee")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType("application/json")
				.expectBody();
	}

	@Test
	public void getTollFeeTaxOk() {
		TollFeeTaxRequest tollFeeTaxRequestBody = TollFeeTaxRequest.builder()
				.vehicle("Car")
				.city("gothenburg")
				.dates(new ArrayList<String>(){{add("2013-01-14 07:30:00");add("2013-01-14 08:00:00");}})
				.build();

		client.post()
				.uri("/tollfee-composite/tax")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.tax").isNotEmpty()
				.jsonPath("$.tax").isNumber()
				.jsonPath("$.tax").isEqualTo("18");
	}

	@Test
	public void getTollFeeTaxWrongDateFormatBAD_REQUEST() {
		String wrongDateFormat = "2013-01/14 08:00:00";
		TollFeeTaxRequest tollFeeTaxRequestBody = TollFeeTaxRequest.builder()
				.vehicle("Car")
				.city("gothenburg")
				.dates(new ArrayList<String>(){{add("2013-01-14 07:30:00");add(wrongDateFormat);}})
				.build();

		client.post()
				.uri("/tollfee-composite/tax")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType("application/json")
				.expectBody();
	}

	@Test
	public void getTollFeeTaxWrongVehicleBAD_REQUEST() {
		String wrongVehicle = "Airplane";
		TollFeeTaxRequest tollFeeTaxRequestBody = TollFeeTaxRequest.builder()
				.vehicle(wrongVehicle)
				.city("gothenburg")
				.dates(new ArrayList<String>(){{add("2013-01-14 07:30:00");add("2013-01-14 08:00:00");}})
				.build();

		client.post()
				.uri("/tollfee-composite/tax")
				.body(BodyInserters.fromValue(tollFeeTaxRequestBody))
				.exchange()
				.expectStatus().isEqualTo(BAD_REQUEST)
				.expectHeader().contentType("application/json")
				.expectBody();
	}
}
