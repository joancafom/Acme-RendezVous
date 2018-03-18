
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Manager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private ManagerService	managerService;

	// Fixtures

	@PersistenceContext
	private EntityManager	entityManager;


	// -------------------------------------------------------------------------------
	// [UC-2-001] Registrarse como Manager.
	// 
	// Requisitos relacionados:
	//   · REQ 1: There's a new kind of actor in the system: managers, who have a VAT
	//            number that is a string of letters, numbers, and dashes.
	//   · REQ 3.1: An actor who is not authenticated must be able to register to the
	//              system as a manager.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverManagerRegister() {

		// testingData[i][0] -> username of the actor who tries to register.
		// testingData[i][1] -> the name of the manager.
		// testingData[i][2] -> the surnames of the manager.
		// testingData[i][3] -> the address of the manager.
		// testingData[i][4] -> the phoneNumber of the manager.
		// testingData[i][5] -> the email of the manager.
		// testingData[i][6] -> the vat of the manager.
		// testingData[i][7] -> the the username/password of the manager.
		// testingData[i][8] -> the expected exception.

		final Object testingData[][] = {

			{
				// 1 - (+) An unauthenticated actor registers as a manager
				null, "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", null
			}, {
				// 2 - (-) An unauthenticated actor registers with a null name
				null, null, "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", ConstraintViolationException.class
			}, {
				// 3 - (-) An unauthenticated actor registers with a blank name
				null, "", "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", ConstraintViolationException.class
			}, {
				// 4 - (+) An unauthenticated actor registers without an address
				null, "Carrie", "Frances Fisher", null, "612421465", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", null
			}, {
				// 5 - (+) An unauthenticated actor registers without a phone number
				null, "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", null, "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", null
			}, {
				// 6 - (+) An unauthenticated actor registers without an address neither a phone number
				null, "Carrie", "Frances Fisher", null, null, "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", null
			}, {
				// 7 - (-) An unauthenticated actor register with an incorrect phone number
				null, "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", "imnotaphonenumber", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", ConstraintViolationException.class
			}, {
				// 8 - (-) An unauthenticated actor registers with a null email
				null, "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", null, "US-32493673-W", "carriefisher1", ConstraintViolationException.class
			}, {
				// 9 - (-) An unauthenticated actor registers with an incorrect email
				null, "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", "imnotanemail", "US-32493673-W", "carriefisher1", ConstraintViolationException.class
			}, {
				// 10 - (-) An unauthenticated actor registers as a null actor
				null, null, null, null, null, null, null, null, IllegalArgumentException.class
			}, {
				// 11 - (-) An unauthenticated actor registers as an already registered manager
				null, null, null, null, null, null, null, "manager1", IllegalArgumentException.class
			}, {
				// 12 - (-) A manager tries to register
				"manager1", "Carrie", "Frances Fisher", "Heaven 16 8ºA, 28000", "612421465", "carriefisher@gmail.com", "US-32493673-W", "carriefisher1", RuntimeException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			this.startTransaction();

			this.templateManagerRegister((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (Class<?>) testingData[i][8]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}

	protected void templateManagerRegister(final String loggedActor, final String name, final String surnames, final String address, final String phoneNumber, final String email, final String vat, final String credentials, final Class<?> expected) {

		Class<?> caught = null;

		// 1. Loggearse (o como null)
		this.authenticate(loggedActor);

		try {

			// 2. Listar todos los managers existentes
			final Collection<Manager> allManagers = this.managerService.findAll();

			// 3. Registrarse como Manager

			Manager manager = null;

			if (!(credentials == null || credentials.contains("manager"))) {
				manager = this.managerService.create();

				manager.setName(name);
				manager.setSurnames(surnames);
				manager.setPostalAddress(address);
				manager.setPhoneNumber(phoneNumber);
				manager.setEmail(email);
				manager.setVat(vat);
				manager.getUserAccount().setUsername(credentials);
				manager.getUserAccount().setPassword(credentials);
			} else if (credentials != null)
				manager = this.managerService.findOne(super.getEntityId(credentials));

			final Manager savedManager = this.managerService.save(manager);

			// Flush
			this.managerService.flush();

			// 4. Loggearse como el nuevo manager

			this.unauthenticate();
			this.authenticate(credentials);

			// 5. Listar los managers y comprobar que está el creado

			final Collection<Manager> newAllManagers = this.managerService.findAll();

			Assert.isTrue(!allManagers.containsAll(newAllManagers));
			Assert.isTrue(this.managerService.findAll().contains(savedManager));

		} catch (final Throwable oops) {
			caught = oops.getClass();

		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);
	}
}
