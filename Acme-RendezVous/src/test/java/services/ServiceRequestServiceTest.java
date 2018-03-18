
package services;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.CreditCard;
import domain.RendezVous;
import domain.Service;
import domain.ServiceRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ServiceRequestServiceTest extends AbstractTest {

	// System Under Test
	@Autowired
	private ServiceRequestService	serviceRequestService;

	@Autowired
	private ServiceService			serviceService;

	@Autowired
	private RendezVousService		rendezVousService;


	// Fixtures

	/*
	 * v1.0 - josembell
	 * 
	 * [UC-2-002] - Request and Display requested Services
	 * 
	 * REQ: 1, 4.2
	 */

	@Test
	public void driverRequestService() {
		final Object testingData[][] = {
			{
				/* usuario - rendezVous - servicio - comments - holderName - brandName - number - CVV - month - year - error */

				/* + 1) Un usuario solicita un servicio para su rendezVous */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, null
			}, {
				/* - 2) Un usuario no identificado solicita un servicio para un rendezVous */
				null, "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 3) Un usuario identificado solicita un servicio para un rendezVous nulo */
				"user1", null, "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 4) Un usuario identificado solicita un servicio para un rendezVous que no es de él */
				"user1", "rendezVous2", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 5) Un usuario identificado solicita un servicio null para un rendezVous suyo */
				"user1", "rendezVous1", null, "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 6) Un usuario identificado solicita un servicio que ya ha solicitado para el mismo rendezVous */
				"user1", "rendezVous1", "service1", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 7) Un usuario identificado solicita un servicio pero no indica el holdername */
				"user1", "rendezVous1", "service3", "This is a test", null, "JJVA", "5370339536264743", 793, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 8) Un usuario identificado solicita un servicio pero no indica el brand name */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", null, "5370339536264743", 793, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 9) Un usuario identificado solicita un servicio pero no indica el número de la tarjeta */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", null, 793, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 10) Un usuario identificado solicita un servicio pero no indica correctamente el número de la tarjeta */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "1", 793, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 11) Un usuario identificado solicita un servicio pero no indica el CVV */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", null, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 12) Un usuario identificado solicita un servicio pero no indica correctamente el CVV */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 1, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 13) Un usuario identificado solicita un servicio pero no indica correctamente el CVV */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 1000, 10, 2020, ConstraintViolationException.class
			}, {
				/* - 14) Un usuario identificado solicita un servicio pero no indica correctamente el mes */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 14, 2020, ConstraintViolationException.class
			}, {
				/* - 15) Un usuario identificado solicita un servicio pero no indica correctamente el año */
				"user1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2017, IllegalArgumentException.class
			}, {
				/* - 16) Un manager intenta solicitar un servicio */
				"manager1", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}, {
				/* - 17) Un admin intenta solicitar un servicio */
				"admin", "rendezVous1", "service2", "This is a test", "John Doe", "JJVA", "5370339536264743", 793, 10, 2020, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++) {
			RendezVous rendezVous = null;
			domain.Service service = null;

			if (testingData[i][1] != null)
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

			if (testingData[i][2] != null)
				service = this.serviceService.findOne(this.getEntityId((String) testingData[i][2]));

			//System.out.println("Test " + (i + 1));
			this.templateRequestService((String) testingData[i][0], rendezVous, service, (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Integer) testingData[i][7],
				(Integer) testingData[i][8], (Integer) testingData[i][9], (Class<?>) testingData[i][10]);
			//System.out.println("Test " + (i + 1) + " - OK");
		}

	}
	/* v1.0 - josembell */
	protected void templateRequestService(final String username, final RendezVous rendezVous, final Service service, final String comments, final String holderName, final String brandName, final String creditCardNumber, final Integer CVV,
		final Integer month, final Integer year, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearse en el sistema */
		this.authenticate(username);

		try {
			/* 2. Listar todos los services */
			final Collection<domain.Service> allServices = this.serviceService.findAll();
			Assert.isTrue(allServices.contains(service));
			final int numberOfRequestsBefore = service.getServiceRequests().size();

			/* 3. Solicitar un servicio -> el que entra por parámetros */
			final ServiceRequest request = this.serviceRequestService.create(service);
			request.setRendezVous(rendezVous);
			request.setComments(comments);

			final CreditCard creditCard = new CreditCard();
			creditCard.setHolderName(holderName);
			creditCard.setBrandName(brandName);
			creditCard.setNumber(creditCardNumber);
			if (CVV != null)
				creditCard.setCVV(CVV);
			if (month != null)
				creditCard.setMonth(month);
			if (year != null)
				creditCard.setYear(year);

			request.setCreditCard(creditCard);

			/* -> Save */
			this.serviceRequestService.save(request);

			/* -> Flush */
			this.serviceRequestService.flush();

			final int numberOfRequestsNow = rendezVous.getServiceRequests().size();
			Assert.isTrue(numberOfRequestsBefore + 1 == numberOfRequestsNow);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
