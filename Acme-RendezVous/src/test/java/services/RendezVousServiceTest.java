
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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

import security.LoginService;
import utilities.AbstractTest;
import domain.GPSCoordinates;
import domain.Answer;
import domain.Question;
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

	@PersistenceContext
	private EntityManager		entityManager;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private QuestionService		questionService;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-005: RSVP a RendezVous, List my RSVPd RendezVouses and Display info about the reservation
	 * 1. Log in as a User
	 * 2. List all RendezVouses
	 * 3. Select one RendezVous that has not been RSVPd
	 * 4. Answer all the questions
	 * 5. List my RSVPd RendezVouses
	 * 6. Select one RendezVous that has been RSVP
	 * 7. Show the answers to the questions
	 * 
	 * Involved REQs: 5.4, 5.5, 21.2, 20.1, 14
	 * 
	 * Test Cases (9; 2+ 7-):
	 * 
	 * - 1) A User selects a rendezVous that has not been RSVPd and does not answer all the questions (they are required)
	 * 
	 * - 2) A User selects a rendezVous that has not been RSVPd but is expired (It makes no sense to confirm your attendance to an expired event)
	 * 
	 * + 3) A User selects a rendezVous that has not been RSVPd, answers the questions correctly and displays them
	 * 
	 * - 4) An unauthenticated actor tries to RSVP a rendezVous (Only Users can do so)
	 * 
	 * - 5) A manager tries to RSVPd a rendezVous (Only Users can do so)
	 * 
	 * - 6) A User below 18 selects a rendezVous for adults and tries to RSVP (under 18 Users cannot go to +18 RendezVouses)
	 * 
	 * - 7) A User selects a rendezVous that has already been RSVPd (it makes no sense to confirm twice)
	 * 
	 * - 8) A User selects a null rendezVous
	 * 
	 * + 9) A User selects a rendezVous that has not been RSVPd and does not have questions, RSPVs it and then he/she displays it
	 */
	@Test
	public void driverAcceptRSVP() {

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {
			{
				"user3", "rendezVous6", false, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user3", "rendezVous3", true, null, IllegalArgumentException.class
			}, {
				"user1", "rendezVous6", true, futureDate.toDate(), null
			}, {
				null, "rendezVous5", true, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"manager1", "rendezVous5", true, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user5", "rendezVous5", true, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user2", "rendezVous6", true, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user1", null, false, null, IllegalArgumentException.class
			}, {
				"user1", "rendezVous5", true, futureDate.toDate(), null
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {
			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				//If we are provided a Date to change the rendezVous' one
				if (testingData[i][3] != null) {
					//We ensure that the OrganizationDate is valid by updating it

					this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
					rendezVous.setOrgDate((Date) testingData[i][3]);
					rendezVous = this.rendezVousService.save(rendezVous);
					this.unauthenticate();
				}

			} else
				rendezVous = null;

			this.templateAcceptRSVP((String) testingData[i][0], rendezVous, (Boolean) testingData[i][2], (Class<?>) testingData[i][4]);
		}

	}
	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-006: Cancel a RSVP, List my RSVPd RendezVouses
	 * 1. Log in as a User
	 * 2. List RSVPd RendezVouses
	 * 3. Select one RendezVous from the list and cancel it
	 * 4. List my RSVPd RendezVouses
	 * 
	 * Involved REQs: 5.4, 5.5
	 * 
	 * Test Cases (7; 1+ 6-):
	 * 
	 * - 1) A User provides an expired rendezVous and tries to cancel it
	 * 
	 * - 2) An unauthenticated tries to cancel a RSVPd rendezVous
	 * 
	 * - 3) A manager tries to cancel a RSVPd rendezVous
	 * 
	 * - 4) A User provides a rendezVous he/she's not attending and tries to cancel it
	 * 
	 * - 5) A User provides a rendezVous that is flagged as deleted and tries to cancel it
	 * 
	 * - 6) A User provides a null rendezVous and tries to cancel it
	 * 
	 * + 7) A User provides a rendezVous he/she was going to attend and successfully cancels it
	 */

	@Test
	public void driverCancelRSVP() {

		//A date in the future
		final LocalDate futureDate = new LocalDate().plusDays(1);

		final Object testingData[][] = {
			{
				"user2", "rendezVous6", null, IllegalArgumentException.class
			}, {
				null, "rendezVous6", futureDate.toDate(), IllegalArgumentException.class
			}, {
				"manager1", "rendezVous6", futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user3", "rendezVous6", futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user5", "rendezVous4", futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user2", null, futureDate.toDate(), IllegalArgumentException.class
			}, {
				"user2", "rendezVous6", futureDate.toDate(), null
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {
			System.out.println(i);
			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				///If we are provided a Date to change the rendezVous' one
				if (testingData[i][2] != null) {
					//We ensure that the OrganizationDate is valid by updating it

					this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
					rendezVous.setOrgDate((Date) testingData[i][2]);
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

	protected void templateAcceptRSVP(final String username, final RendezVous rendezVous, final boolean answersAllQ, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		//1. Log in as a User
		this.authenticate(username);

		User currentUser = null;
		Collection<RendezVous> myRVBeforeRSVP = null;

		try {

			//2. List all RendezVouses

			final Collection<RendezVous> allBeforeRSVP = new ArrayList<RendezVous>(this.rendezVousService.findAll());

			//3. Select one RendezVous that has not been RSVPd (given as a parameter)

			//4. Answer all the questions

			final List<Question> questions = new ArrayList<Question>();
			if (rendezVous != null) {
				questions.addAll(rendezVous.getQuestions());
				Question q;

				for (int i = 0; i < questions.size(); i++) {

					q = questions.get(i);

					//If we don't want to answer them all...
					if (answersAllQ || i != questions.size() - 1) {
						final Answer a = this.answerService.create(q);
						a.setText("Sample Response");
						this.answerService.save(a);
					}
				}
			}
			//Obtain the list of my RendezVouses before the save (if we are Users, if not, it will fail)
			if (username != null && username.contains("user")) {
				currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());

				Assert.notNull(currentUser);
				myRVBeforeRSVP = new ArrayList<RendezVous>(currentUser.getAttendedRendezVouses());
			}

			final RendezVous savedRendezVous = this.rendezVousService.acceptRSVP(rendezVous);

			//Force the transaction to happen
			this.rendezVousService.flush();

			Assert.notNull(savedRendezVous);
			Assert.isTrue(savedRendezVous.getId() != 0);
			Assert.isTrue(rendezVous.equals(savedRendezVous));

			Assert.notNull(currentUser);

			Assert.isTrue(savedRendezVous.getAttendants().contains(currentUser));
			Assert.isTrue(currentUser.getAttendedRendezVouses().contains(savedRendezVous));

			//5. List my RSVPd RendezVouses
			final Collection<RendezVous> myRVAfterRSVP = new ArrayList<RendezVous>(currentUser.getAttendedRendezVouses());

			Assert.notNull(myRVAfterRSVP);
			Assert.notNull(myRVBeforeRSVP);
			Assert.isTrue(!myRVAfterRSVP.equals(myRVBeforeRSVP));
			Assert.isTrue(myRVAfterRSVP.containsAll(myRVBeforeRSVP));
			Assert.isTrue(myRVAfterRSVP.size() == myRVBeforeRSVP.size() + 1);
			Assert.isTrue(!myRVBeforeRSVP.contains(savedRendezVous));
			Assert.isTrue(myRVAfterRSVP.contains(savedRendezVous));

			//-- Assert that no new RendezVous was added

			final Collection<RendezVous> allAfterRSVP = new ArrayList<RendezVous>(this.rendezVousService.findAll());

			Assert.notNull(allAfterRSVP);
			Assert.isTrue(allAfterRSVP.equals(allBeforeRSVP));

			//6. Select one RendezVous that has been RSVP

			final RendezVous retrievedRV = this.rendezVousService.findOne(savedRendezVous.getId());

			Assert.notNull(retrievedRV);
			Assert.isTrue(rendezVous.equals(savedRendezVous));

			//7. Show the answers to the questions

			final Collection<Answer> rvAnswers = new ArrayList<Answer>(this.answerService.findAllByRendezVousAndUser(retrievedRV, currentUser));

			if (answersAllQ) {
				Assert.notNull(rvAnswers);
				Assert.isTrue(rvAnswers.size() == questions.size());

				for (final Answer a : rvAnswers)
					Assert.isTrue(a.getText().equals("Sample Response"));
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateCancelRSVP(final String username, final RendezVous rendezVous, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		//1. Log in
		this.authenticate(username);

		User currentUser = null;

		try {

			//2. List RSVPd RendezVouses (only if you are a User)
			final Collection<RendezVous> rvBeforeDelete = new ArrayList<RendezVous>();
			if (username != null && username.contains("user")) {
				currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());
				rvBeforeDelete.addAll(currentUser.getAttendedRendezVouses());
				Assert.notNull(rvBeforeDelete);
			}

			//3. Select one of the rendezVouses (given) and cancel it
			final RendezVous savedRendezVous = this.rendezVousService.cancelRSVP(rendezVous);

			//Force the transaction to happen
			this.rendezVousService.flush();

			Assert.isTrue(savedRendezVous.getId() != 0);
			Assert.isTrue(rendezVous.equals(savedRendezVous));

			currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());

			//4. List my RSVPd RendezVouses

			final Collection<RendezVous> rvAfterDelete = currentUser.getAttendedRendezVouses();

			Assert.isTrue(!rvBeforeDelete.equals(rvAfterDelete));
			Assert.isTrue(!savedRendezVous.getAttendants().contains(currentUser));
			Assert.isTrue(!currentUser.getAttendedRendezVouses().contains(savedRendezVous));

			//We check that the answers the user provided to the questions of the rendezVous are removed
			final Collection<Answer> rvAnswers = this.answerService.findAllByRendezVousAndUser(savedRendezVous, currentUser);

			Assert.isTrue(rvAnswers.isEmpty());

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


	// -------------------------------------------------------------------------------
	// [UC-002] Listar RendezVouses, crear un nuevo RendezVous y mostrar un
	// RendezVous.
	// 
	// Requisitos relacionados:
	//   · REQ 2: Users can create rendezvouses. For each rendezvous, the system must
	//            store its name, its description, the moment when it's going to be 
	//            organised, an optional picture, optional GPS coordinates, and the
	//            creator and the list of attendants.
	//   · REQ 4.3: An actor who is not authenticated must be able to list the
	//              rendezvouses in the system and navigate to the profiles of the
	//              corresponding creators and attendants.
	//   · REQ 5.1: An actor who is authenticated as a user must be able to list the
	//              rendezvouses in the system and navigate to the profiles of the
	//              corresponding creators and attendants.
	//   · REQ 5.2: An actor who is authenticated as a user must be able to create a
	//              rendezvous, which he's implicitly assumed to attend. Note that a
	//              user may edit his or her rendezvouses as long as they aren't saved
	//              them in final mode. Once a rendezvous is saved in final mode, it
	//              cannot be edited or deleted by the creator.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListCreateAndDisplayRendezVous() {

		final Object testingData[][] = {
			{
				// (+) Un usuario no loggeado lista rendezVouses para menores de 18
				null, "listUnder18", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, null
			}, {
				// (-) Un usuario no loggeado lista rendezVouses para mayores de 18
				null, "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// (+) Un usuario mayor de 18 lista rendezVouses para mayores de 18
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, null
			}, {
				// (-) Un usuario menor de 18 lista rendezVouses para mayores de 18
				"user5", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario crea un rendezVous con el nombre a null
				"user1", "listAdults", null, "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// (-) Un usuario crea un rendezVous con el nombre en blanco
				"user1", "listAdults", "", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// (-) Un usuario crea un rendezVous con la descripción a null
				"user1", "listAdults", "firstRendezVous", null, "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// (-) Un usuario crea un rendezVous con la descripción en blanco
				"user1", "listAdults", "firstRendezVous", "", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// (-) Un usuario crea un rendezVous con la orgDate a null
				"user1", "listAdults", "firstRendezVous", "completeDescription", null, "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario crea un rendezVous con la orgDate en pasado
				"user1", "listAdults", "firstRendezVous", "completeDescription", "past", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario crea un rendezVous en el que la picture no es una URL
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// (-) Un usuario crea un rendezVous en el que la latitud es null y la longitud no
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", null, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario crea un rendezVous en el que la longitud es null y la latitud no
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, null, false, false, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario crea un rendezVous en el que isDeleted es true
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, true, false, IllegalArgumentException.class
			}, {
				// (-) Un usuario menor de 18 crea un rendezVous con isForAdults a true
				"user5", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, true, IllegalArgumentException.class
			}, {
				// (+) Un usuario crea un rendezVous sin picture
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", null, 37.3546759, -5.9779805, false, false, true, null
			}, {
				// (+) Un usuario crea un rendezVous sin GPSCoordinates
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", null, null, false, false, true, null
			}, {
				// (+) Un usuario mayor de 18 crea un rendezVous con isForAdults a true
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, true, null
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			Date rendezVousDate = null;

			if (testingData[i][4] != null)
				if ((String) testingData[i][4] == "future")
					rendezVousDate = new LocalDate().plusDays(1).toDate();
				else if ((String) testingData[i][4] == "past")
					rendezVousDate = new LocalDate().minusDays(1).toDate();

			this.templateListCreateAndDisplayRendezVous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], rendezVousDate, (String) testingData[i][5], (Double) testingData[i][6],
				(Double) testingData[i][7], (boolean) testingData[i][8], (boolean) testingData[i][9], (boolean) testingData[i][10], (Class<?>) testingData[i][11]);

		}

	}

	protected void templateListCreateAndDisplayRendezVous(final String username, final String listOption, final String rendezVousName, final String rendezVousDescription, final Date rendezVousDate, final String rendezVousPicture,
		final Double rendezVousLatitude, final Double rendezVousLongitude, final boolean rendezVousIsFinal, final boolean rendezVousIsDeleted, final boolean rendezVousIsForAdults, final Class<?> expected) {

		// 1. Loggearse como Usuario (o como null)
		super.authenticate(username);

		Class<?> caught = null;

		try {

			// 2. Listar todos los rendezVouses

			User user = null;

			if (username != null)
				user = this.userService.findByUserAccount(LoginService.getPrincipal());

			if (listOption == "listUnder18")
				this.rendezVousService.findAllNotAdult();
			else if (listOption == "listAdults") {
				Assert.isTrue(user != null);
				Assert.isTrue(user.getAge() >= 18);
				this.rendezVousService.findAll();
			}

			if (user != null) {

				// 3. Listar mis rendezVouses

				this.rendezVousService.findAllByUser(user);

				// 4. Crear un nuevo rendezVous

				final GPSCoordinates rendezVousCoordinates = new GPSCoordinates();
				rendezVousCoordinates.setLatitude(rendezVousLatitude);
				rendezVousCoordinates.setLongitude(rendezVousLongitude);

				final RendezVous createdRendezVous = this.rendezVousService.create();

				createdRendezVous.setName(rendezVousName);
				createdRendezVous.setDescription(rendezVousDescription);
				createdRendezVous.setOrgDate(rendezVousDate);
				createdRendezVous.setPicture(rendezVousPicture);
				createdRendezVous.setCoordinates(rendezVousCoordinates);
				createdRendezVous.setIsFinal(rendezVousIsFinal);
				createdRendezVous.setIsDeleted(rendezVousIsDeleted);
				createdRendezVous.setIsForAdults(rendezVousIsForAdults);

				final RendezVous savedRendezVous = this.rendezVousService.save(createdRendezVous);

				//Flush
				this.rendezVousService.flush();

				// 5. Listar todos los rendezVouses y comprobar que contiene al nuevo

				if (listOption == "listUnder18") {
					final Collection<RendezVous> allRendezVous = this.rendezVousService.findAllNotAdult();

					if (rendezVousIsForAdults)
						Assert.isTrue(!allRendezVous.contains(savedRendezVous));
					else
						Assert.isTrue(allRendezVous.contains(savedRendezVous));
				} else if (listOption == "listAdults") {
					final Collection<RendezVous> allRendezVous = this.rendezVousService.findAll();
					Assert.isTrue(allRendezVous.contains(savedRendezVous));
				}

				// 6. Listar mis rendezVouses y comprobar que contiene al nuevo

				Collection<RendezVous> myRendezVouses = null;

				if (user != null && user.getAge() >= 18)
					myRendezVouses = this.rendezVousService.findAllByUser(user);
				else if (user != null && user.getAge() < 18)
					myRendezVouses = this.rendezVousService.findAllNotAdultByUser(user);

				Assert.isTrue(myRendezVouses.contains(savedRendezVous));

			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
