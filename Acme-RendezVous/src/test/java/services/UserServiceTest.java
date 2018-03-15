
package services;

import java.sql.Date;
import java.util.Collection;

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
	 * UC-001: Register to the system as a User, List and Retrieve one
	 * 1. Register as a User
	 * 2. (Optional) Log in
	 * 3. Get a list of Users
	 * 4. Select a User to display
	 * 
	 * Involved REQs: 1, 4.1, 4.2, 5.1
	 * 
	 * Test Cases (12; 3+ 9-):
	 * 
	 * + 1) An unauthenticated actor provides correct data an successfully
	 * registers to the system, logs in an sees himself/herself in the listing
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
	 * - 10) An unauthenticated actor provides an already registered User to be saved (You can only register once)
	 * 
	 * + 11) An unauthenticated actor provides correct data an successfully
	 * registers to the system, keeps logged out an sees himself/herself in the listing
	 * 
	 * - 12) A manager provides correct data and tries to register to the system (only unauthenticated Actors can do so)
	 */
	@Test
	public void driverRegister() {

		//We will use two patterns for data
		// The longest one simulates regular input (as in the webpage)
		//The second one simulates other scenarios like the User to persist being null, or already persisted

		final int longestTestCaseLength = 10;

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {

			{
				null, "donyaconcha1", "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha1", null
			}, {
				null, null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", futureDate.toString(), "donyaconcha2", ConstraintViolationException.class
			}, {
				null, null, "", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha3", ConstraintViolationException.class
			}, {
				null, null, "Concepción", null, "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha4", ConstraintViolationException.class
			}, {
				null, null, "Concepción", "de la Fuente García", null, null, "donyaconcha@gmail.com", "1964-12-31", "donyaconcha5", null
			}, {
				null, null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "notAPhone", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha6", ConstraintViolationException.class
			}, {
				null, null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", null, "1964-12-31", "donyaconcha7", ConstraintViolationException.class
			}, {
				null, null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "sinemail", "1964-12-31", "donyaconcha8", ConstraintViolationException.class
			}, {
				null, null, null, IllegalArgumentException.class
			}, {
				null, null, "user1", IllegalArgumentException.class
			}, {
				null, null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha9", null
			}, {
				"manager1", null, "Concepción", "de la Fuente García", "Desengaño 21 1ºA, 28000", "678493048", "donyaconcha@gmail.com", "1964-12-31", "donyaconcha10", RuntimeException.class
			}
		};

		User testUser;

		for (int i = 0; i < testingData.length; i++)

			if (testingData[i].length == longestTestCaseLength)
				this.templateRegister((String) testingData[i][0], (String) testingData[i][1], true, null, (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
					Date.valueOf((String) testingData[i][7]), (String) testingData[i][8], (Class<?>) testingData[i][9]);
			else {

				if (testingData[i][1] != null)
					//We retrieve the User from the DB
					testUser = this.userService.findOne(this.getEntityId((String) testingData[i][2]));
				else
					testUser = null;

				this.templateRegister((String) testingData[i][0], (String) testingData[i][1], false, testUser, null, null, null, null, null, null, null, (Class<?>) testingData[i][3]);
			}

	}
	// Test Templates

	protected void templateRegister(final String performer, final String viewer, final boolean testCreate, final User testUser, final String name, final String surnames, final String postalAddress, final String phoneNumber, final String email,
		final Date dateOfBirth, final String userCredentials, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;
		final User userToSave;

		this.authenticate(performer);

		try {

			final Collection<User> usersBefore = this.userService.findAll();
			Assert.notNull(usersBefore);

			//1. Registers as an User

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

			//2. Log in

			this.unauthenticate();
			this.authenticate(viewer);

			//3. Get the list of Users

			final Collection<User> usersAfter = this.userService.findAll();

			Assert.notNull(usersAfter);
			Assert.isTrue(!usersBefore.equals(usersAfter));
			Assert.isTrue((usersBefore.size() + 1) == usersAfter.size());
			Assert.isTrue(usersAfter.containsAll(usersBefore));
			Assert.isTrue(!usersBefore.contains(savedUser));
			Assert.isTrue(usersAfter.contains(savedUser));

			//4. Select a User to display it

			final User retrivedUser = this.userService.findOne(savedUser.getId());

			Assert.notNull(retrivedUser);
			Assert.isTrue(retrivedUser.equals(savedUser));

		} catch (final Throwable oops) {
			caught = oops.getClass();

			//We clear the unsaved/non-persisted entities so as to proceed flawlessly with the tests
			this.entityManager.clear();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}
}
