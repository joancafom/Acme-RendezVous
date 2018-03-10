
package services;

import java.sql.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class UserServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private UserService		userService;

	// Fixtures

	@PersistenceContext
	private EntityManager	entityManager;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 4.1
	 * An actor who is not authenticated must be able to
	 * register to the system as a user
	 * 
	 * Test Cases (10; 2+ 8-):
	 * 
	 * + 1) An unauthenticated actor provides correct data an successfully
	 * registers to the system
	 * 
	 * - 2) An unauthenticated actor provides incorrect data (date in Future)
	 * 
	 * - 3) An unauthenticated actor provides incorrect data (blank Name)
	 * 
	 * - 4) An unauthenticated actor provides incorrect data (null Surname)
	 * 
	 * + 5) An unauthenticated actor provides correct data an successfully
	 * registers to the system (null postalAddress and phoneNumber)
	 * 
	 * - 6) An unauthenticated actor provides incorrect data (phoneNumber not matching pattern)
	 * 
	 * - 7) An unauthenticated actor provides incorrect data (null email)
	 * 
	 * - 8) An unauthenticated actor provides incorrect data (not an email)
	 * 
	 * - 9) An unauthenticated actor provides a null User to be saved
	 * 
	 * - 10) An unauthenticated actor provides an already persisted User to be saved
	 */
	@Test
	public void driverRegister() {

		//We will use two patterns for data
		// The longest one simulates regular input (as in the webpage)
		//The second one simulates other scenarios like the User to persist being null, or already persisted

		final int longestTestCaseLength = 9;

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {

			{
				null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha1", null
			}, {
				null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", futureDate.toString(), "donyaconcha2", ConstraintViolationException.class
			}, {
				null, "", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha3", ConstraintViolationException.class
			}, {
				null, "Concepción", null, "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha4", ConstraintViolationException.class
			}, {
				null, "Concepción", "de la Fuente García", null, null, "donyaconcha@gmail.com", "1964-12-31", "donyaconcha5", null
			}, {
				null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "notAPhone", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha6", ConstraintViolationException.class
			}, {
				null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", null, "1964-12-31", "donyaconcha7", ConstraintViolationException.class
			}, {
				null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "sinemail", "1964-12-31", "donyaconcha8", ConstraintViolationException.class
			}, {
				null, null, IllegalArgumentException.class
			}, {
				null, "user1", IllegalArgumentException.class
			}
		};

		User testUser;

		for (int i = 0; i < testingData.length; i++)

			if (testingData[i].length == longestTestCaseLength)
				this.templateRegister((String) testingData[i][0], true, null, (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
					Date.valueOf((String) testingData[i][6]), (String) testingData[i][7], (Class<?>) testingData[i][8]);
			else {

				if (testingData[i][1] != null)
					//We retrieve the User from the DB
					testUser = this.userService.findOne(this.getEntityId((String) testingData[i][1]));
				else
					testUser = null;

				this.templateRegister((String) testingData[i][0], false, testUser, null, null, null, null, null, null, null, (Class<?>) testingData[i][2]);
			}

	}
	// Test Templates

	protected void templateRegister(final String username, final boolean testCreate, final User testUser, final String name, final String surnames, final String postalAddress, final String phoneNumber, final String email, final Date dateOfBirth,
		final String userCredentials, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;
		final User userToSave;

		this.authenticate(username);

		try {

			if (testCreate) {
				userToSave = this.userService.create();

				//Simulates the User entering data
				userToSave.setName(name);
				userToSave.setSurnames(surnames);
				userToSave.setPostalAddress(postalAddress);
				userToSave.setPhoneNumber(phoneNumber);
				userToSave.setEmail(email);
				userToSave.setDateOfBirth(dateOfBirth);
				userToSave.getUserAccount().setUsername(userCredentials);
				userToSave.getUserAccount().setPassword(userCredentials);
			} else
				//Simulates other scenarios: User somehow is null, already persisted...
				userToSave = testUser;

			final User savedUser = this.userService.save(userToSave);

			//Force the transaction to happen
			this.userService.flush();

			Assert.notNull(savedUser);
			Assert.isTrue(savedUser.getId() != 0);
			Assert.isTrue(!userToSave.equals(savedUser));

		} catch (final Throwable oops) {
			caught = oops.getClass();

			//We clear the unsaved/non-persisted entities so as to proceed flawlessly with the tests
			this.entityManager.clear();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}
}
