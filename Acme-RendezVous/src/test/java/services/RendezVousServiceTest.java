
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import security.LoginService;
import utilities.AbstractTest;
import domain.RendezVous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class RendezVousServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private RendezVousService	rendezVousService;

	//Fixtures 

	@Autowired
	private UserService			userService;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 5.4
	 * An actor who is authenticated as a user must be able to
	 * RSVP a rendezvous.When a user RSVPs a rendezvous,
	 * he or she is assumed to attend it.
	 * 
	 * Test Cases (6; 1+ 5-):
	 * 
	 * + 1) A user actor provides a correct rendezVous and successfully RSVPd it
	 * 
	 * - 2) An unauthenticated actor tries to RSVPd a correct rendezVous
	 * 
	 * - 3) A manager tries to RSVPd a correct rendezVous
	 * 
	 * - 4) A user actor provides an expired rendezVous
	 * 
	 * - 5) A underaged user actor tries to join a +18 rendezVous
	 * 
	 * - 6) A user actor who is currently attending the rendezVous tries to RSVP
	 */
	@Test
	public void driverAcceptRSVP() {

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {
			{
				"user1", "rendezVous5", futureDate, null
			}, {
				null, "rendezVous5", futureDate, IllegalArgumentException.class
			}, {
				"manager1", "rendezVous5", futureDate, IllegalArgumentException.class
			}, {
				"user3", "rendezVous6", null, IllegalArgumentException.class
			}, {
				"user5", "rendezVous5", futureDate, IllegalArgumentException.class
			}, {
				"user2", "rendezVous6", futureDate, IllegalArgumentException.class
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				//If we are provided a Date to change the rendezVous' one
				if (testingData[i][2] != null) {
					//We ensure that the OrganizationDate is valid by updating it

					this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
					rendezVous.setOrgDate(futureDate.toDate());
					rendezVous = this.rendezVousService.save(rendezVous);
					this.unauthenticate();
				}
			} else
				rendezVous = null;

			this.templateAcceptRSVP((String) testingData[i][0], rendezVous, (Class<?>) testingData[i][3]);
		}

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 5.4
	 * An actor who is authenticated as a user must be able to
	 * cancel a RSVPd rendezvous. When a user RSVPs a rendezvous,
	 * he or she is assumed to attend it.
	 * 
	 * Test Cases (6; 1+ 5-):
	 * 
	 * + 1) A user actor provides a rendezVous he/she was going to attend and successfully cancels it
	 * 
	 * - 2) An unauthenticated tries to cancel a rendezVous
	 * 
	 * - 3) A manager tries to cancel a rendezVous
	 * 
	 * - 4) A user actor provides a rendezVous he/she's not attending and tries to cancel it
	 * 
	 * - 5) A user actor provides a rendezVous that is Deleted and tries to cancel it
	 * 
	 * - 6) A user actor provides a null rendezVous and tries to cancel it
	 */

	@Test
	public void driverCancelRSVP() {

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {
			{
				"user2", "rendezVous6", futureDate, null
			}, {
				null, "rendezVous6", futureDate, IllegalArgumentException.class
			}, {
				"manager1", "rendezVous6", futureDate, IllegalArgumentException.class
			}, {
				"user3", "rendezVous6", futureDate, IllegalArgumentException.class
			}, {
				"user5", "rendezVous4", futureDate, IllegalArgumentException.class
			}, {
				"user2", null, futureDate, IllegalArgumentException.class
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {

			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				//If we are provided a Date to change the rendezVous' one
				if (testingData[i][2] != null) {
					//We ensure that the OrganizationDate is valid by updating it

					this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
					rendezVous.setOrgDate(futureDate.toDate());
					rendezVous = this.rendezVousService.save(rendezVous);
					this.unauthenticate();
				}
			} else
				rendezVous = null;

			this.templateCancelRSVP((String) testingData[i][0], rendezVous, (Class<?>) testingData[i][3]);
		}

	}

	/*
	 * v1.0 - josembell
	 * 
	 * Req to Test: 5.3
	 * Update the rendezvouses that he or she's created
	 * 
	 * Test Cases; (5; +1 -4)
	 * 
	 * + 1) An actor updates a rendezvous created by him/herself, not-deleted and saved in draft mode.
	 * 
	 * - 2) An user tries to update a rendezvous which is not created by him/herself
	 * 
	 * - 3) An user tries to update a rendezvous created by him/herself but is already virtually deleted
	 * 
	 * - 4) An user tries to update a rendezvous created by him/herself but in final mode
	 * 
	 * - 5) An user tries to update a null rendezvous
	 */

	@Test
	public void driverUpdateRendezVous() {
		final Object testingData[][] = {
			{
				"user5", "rendezVous5", null
			}, {
				"user1", "rendezVous2", IllegalArgumentException.class
			}, {
				"user4", "rendezVous4", IllegalArgumentException.class
			}, {
				"user1", "rendezVous1", IllegalArgumentException.class
			}, {
				"user2", null, IllegalArgumentException.class
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {
			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
			} else
				rendezVous = null;

			this.templateUpdateRendezVous((String) testingData[i][0], rendezVous, (Class<?>) testingData[i][2]);
		}

	}
	// Test Templates

	protected void templateAcceptRSVP(final String username, final RendezVous rendezVous, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		final User currentUser;

		try {

			final RendezVous savedRendezVous = this.rendezVousService.acceptRSVP(rendezVous);

			//Force the transaction to happen
			this.rendezVousService.flush();

			Assert.notNull(savedRendezVous);
			Assert.isTrue(savedRendezVous.getId() != 0);
			Assert.isTrue(rendezVous.equals(savedRendezVous));

			currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());

			Assert.isTrue(savedRendezVous.getAttendants().contains(currentUser));
			Assert.isTrue(currentUser.getAttendedRendezVouses().contains(savedRendezVous));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateCancelRSVP(final String username, final RendezVous rendezVous, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		final User currentUser;

		try {

			final RendezVous savedRendezVous = this.rendezVousService.cancelRSVP(rendezVous);

			//Force the transaction to happen
			this.rendezVousService.flush();

			Assert.notNull(savedRendezVous);
			Assert.isTrue(savedRendezVous.getId() != 0);
			Assert.isTrue(rendezVous.equals(savedRendezVous));

			currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());

			Assert.isTrue(!savedRendezVous.getAttendants().contains(currentUser));
			Assert.isTrue(!currentUser.getAttendedRendezVouses().contains(savedRendezVous));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateUpdateRendezVous(final String username, final RendezVous rendezVous, final Class<?> expected) {
		/* v1.0 - josembell */
		Class<?> caught = null;

		/* 1. Logearte como usuario */
		this.authenticate(username);

		final User currentUser;

		try {
			/* 2. Listar mis rendezVouses */
			currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());
			Assert.notNull(currentUser);
			final Collection<RendezVous> myRendezVouses = currentUser.getCreatedRendezVouses();

			/* 3. Seleccionar un rendezVous -> El que entra por parámetros */
			Assert.isTrue(myRendezVouses.contains(rendezVous));
			Assert.isTrue(currentUser.getCreatedRendezVouses().contains(rendezVous));
			Assert.isTrue(rendezVous.getCreator().equals(currentUser));

			/* editar campos */
			if (rendezVous != null)
				rendezVous.setName("Test");

			/* 4. Cambiar datos */
			final RendezVous updatedRendezVous = this.rendezVousService.save(rendezVous);

			//Flush
			this.rendezVousService.flush();

			Assert.notNull(updatedRendezVous);
			Assert.isTrue(updatedRendezVous.getId() != 0);
			Assert.isTrue(updatedRendezVous.getName().equals("Test"));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
