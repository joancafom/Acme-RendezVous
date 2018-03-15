
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import domain.Answer;
import domain.GPSCoordinates;
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

	@Autowired
	private AnswerService		answerService;


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
	 * [CU-003] - Listar y Editar un RendezVous
	 * 
	 * Req to Test: 5.3, 5.2, 2
	 */

	@Test
	public void driverUpdateRendezVous() {

		final Date futureDate = LocalDate.now().plusDays(1).toDate();
		final Date pastDate = LocalDate.now().minusDays(2).toDate();

		final Object testingData[][] = {
			{
				/* + 1) Un actor mayor de edad edita un rendezVous validado creado por el */
				"user2", "rendezVous6", 1, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, null
			}, {
				/* + 2) Un actor menor de edad edita un rendezVous validado creado por el */
				"user5", "rendezVous5", 1, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, null
			}, {
				/* - 3) Un usuario no autentificado intenta editar un rendezVous */
				null, "rendezVous4", 0, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 4) Un usuario identificado intenta editar un rendezVous que no es de el */
				"user1", "rendezVous5", 2, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 5) Un usuario identificado intenta editar un rendezVous null */
				"user2", null, 0, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 6) Un usuario identificado intenta editar un rendezVous inválido (sin nombre) */
				"user1", "rendezVous1", 2, null, "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 7) Un usuario identificado intenta editar un rendezVous inválido (sin descripción */
				"user1", "rendezVous1", 2, "nombreOk", null, futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 8) Un usuario identificado intenta editar un rendezVous inválido (sin fecha) */
				"user1", "rendezVous1", 2, "nombreOk", "descripciónOk", null, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 9) Un usuario identificado intenta editar un rendezVous inválido (fecha en el pasado) */
				"user1", "rendezVous1", 2, "nombreOk", "descripciónOk", pastDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 10)Un usuario que es menor de edar intenta establecer un rendezVous para mayores de edad */
				"user5", "rendezVous5", 1, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, true, IllegalArgumentException.class
			}, {
				/* - 11)Un usuario intenta editar un rendezVous que está en versión final */
				"user3", "rendezVous2", 1, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}, {
				/* - 12)Un usuario intenta editar un rendezVous que ya ha sido eliminado */
				"user4", "rendezVous4", 1, "nombreOk", "descripciónOk", futureDate, 1.0, 1.0, false, IllegalArgumentException.class
			}
		};

		RendezVous rendezVous;

		for (int i = 0; i < testingData.length; i++) {
			if (testingData[i][1] != null) {
				rendezVous = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));
				this.authenticate(rendezVous.getCreator().getUserAccount().getUsername());
			} else
				rendezVous = null;
			System.out.println("TEST-" + i);
			this.templateListUpdateRendezVous((String) testingData[i][0], rendezVous, (Integer) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (Double) testingData[i][6], (Double) testingData[i][7],
				(Boolean) testingData[i][8], (Class<?>) testingData[i][9]);
			System.out.println("TEST-" + i + " - OK");
		}

	}

	protected void templateListUpdateRendezVous(final String username, final RendezVous rendezVous, final Integer numOfRendezVouses, final String name, final String description, final Date date, final Double latitude, final Double longitude,
		final Boolean isForAdults, final Class<?> expected) {
		/* v1.0 - josembell */
		Class<?> caught = null;

		/* 1. Logearte como usuario */
		this.authenticate(username);

		User currentUser = null;

		try {
			/* 2. Listar mis rendezVouses */
			if (username != null) {
				currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());
				final Collection<RendezVous> myRendezVouses = currentUser.getCreatedRendezVouses();
				Assert.isTrue(myRendezVouses.size() == numOfRendezVouses);

				/* 3. Seleccionar un rendezVous -> El que entra por parámetros */
				Assert.isTrue(myRendezVouses.contains(rendezVous));
				Assert.isTrue(currentUser.getCreatedRendezVouses().contains(rendezVous));
				Assert.isTrue(rendezVous.getCreator().equals(currentUser));
			}
			/* editar campos */
			if (rendezVous != null) {
				rendezVous.setName(name);
				rendezVous.setDescription(description);
				rendezVous.setOrgDate(date);
				rendezVous.setIsForAdults(isForAdults);
			}
			/* 4. Cambiar datos */
			this.rendezVousService.save(rendezVous);

			//Flush
			this.rendezVousService.flush();

			/* 5. Asegurar que la lista sigue teniendo el mismo tamaño */
			Assert.isTrue(currentUser.getCreatedRendezVouses().size() == numOfRendezVouses);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

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
	//   · REQ 14: Some rendezvouses may be flagged as "adult only", in which case
	//             every attempt to RSVP them by users who are under 18 must be
	//             prohibited. Such rendezvouses must not be displayed unless the user
	//             who is browsing them is at least 18 year old. Obviously, they must
	//             not be shown to unauthenticated users.
	// -------------------------------------------------------------------------------
	// v2.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListCreateAndDisplayRendezVous() {

		// testingData[i][0] -> username del usuario loggeado.
		// testingData[i][1] -> tipo de rendezVouses que se van a listar.
		// testingData[i][2] -> nombre del rendezVous a crear.
		// testingData[i][3] -> descripción del rendezVous a crear.
		// testingData[i][4] -> tipo de fecha del rendezVous a crear.
		// testingData[i][5] -> imagen  del rendezVous a crear.
		// testingData[i][6] -> latitud de las coordenadas del rendezVous a crear.
		// testingData[i][7] -> longitud de las coordenadas del rendezVous a crear.
		// testingData[i][8] -> isFinal del rendezVous a crear.
		// testingData[i][9] -> isDeleted del rendezVous a  crear.
		// testingData[i][10] -> isForAdults del rendezVous a crear.
		// testingData[i][11] -> excepción que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (+) Un usuario no loggeado lista rendezVouses para menores de 18
				null, "listUnder18", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, null
			}, {
				// 2 - (-) Un usuario no loggeado lista rendezVouses para mayores de 18
				null, "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// 3 - (+) Un usuario mayor de 18 lista rendezVouses para mayores de 18
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, null
			}, {
				// 4 - (-) Un usuario menor de 18 lista rendezVouses para mayores de 18
				"user5", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// 5 - (-) Un usuario crea un rendezVous con el nombre a null
				"user1", "listAdults", null, "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// 6 - (-) Un usuario crea un rendezVous con el nombre en blanco
				"user1", "listAdults", "", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// 7 - (-) Un usuario crea un rendezVous con la descripción a null
				"user1", "listAdults", "firstRendezVous", null, "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// 8 - (-) Un usuario crea un rendezVous con la descripción en blanco
				"user1", "listAdults", "firstRendezVous", "", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// 9 - (-) Un usuario crea un rendezVous con la orgDate a null
				"user1", "listAdults", "firstRendezVous", "completeDescription", null, "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// 10 - (-) Un usuario crea un rendezVous con la orgDate en pasado
				"user1", "listAdults", "firstRendezVous", "completeDescription", "past", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// 11 - (-) Un usuario crea un rendezVous en el que la picture no es una URL
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "picture.png", 37.3546759, -5.9779805, false, false, false, ConstraintViolationException.class
			}, {
				// 12 - (-) Un usuario crea un rendezVous en el que la latitud es null y la longitud no
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", null, -5.9779805, false, false, false, IllegalArgumentException.class
			}, {
				// 13 - (-) Un usuario crea un rendezVous en el que la longitud es null y la latitud no
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, null, false, false, false, IllegalArgumentException.class
			}, {
				// 14 - (-) Un usuario crea un rendezVous en el que isDeleted es true
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, true, false, IllegalArgumentException.class
			}, {
				// 15 - (-) Un usuario menor de 18 crea un rendezVous con isForAdults a true
				"user5", "listUnder18", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", 37.3546759, -5.9779805, false, false, true, IllegalArgumentException.class
			}, {
				// 16 - (+) Un usuario crea un rendezVous sin picture
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", null, 37.3546759, -5.9779805, false, false, true, null
			}, {
				// 17 - (+) Un usuario crea un rendezVous sin GPSCoordinates
				"user1", "listAdults", "firstRendezVous", "completeDescription", "future", "http://www.images.com/picture.png", null, null, false, false, true, null
			}, {
				// 18 - (+) Un usuario mayor de 18 crea un rendezVous con isForAdults a true
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

			this.startTransaction();

			this.templateListCreateAndDisplayRendezVous((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], rendezVousDate, (String) testingData[i][5], (Double) testingData[i][6],
				(Double) testingData[i][7], (boolean) testingData[i][8], (boolean) testingData[i][9], (boolean) testingData[i][10], (Class<?>) testingData[i][11]);

			this.rollbackTransaction();

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
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	// -------------------------------------------------------------------------------
	// [UC-002] Eliminar virtualmente un RendezVous.
	// 
	// Requisitos relacionados:
	//   · REQ 5.3: Update or delete the rendezvouses that he or she's created.
	//              Deletion is virtual, that is: the information is not removed from
	//              the database, but the rendezvous cannot be updated. Deleted
	//              rendezvouses are flagged as such when they are displayed.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverVirtualDeleteRendezVous() {

		// testingData[i][0] -> username del usuario loggeado.
		// testingData[i][1] -> rendezVous que va a marcarse como eliminado.
		// testingData[i][2] -> excepción que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (-) Un usuario no loggeado elimina virtualmente un RendezVous.
				null, "rendezVous1", IllegalArgumentException.class
			}, {
				// 2 - (-) Un usuario loggeado elimina virtualmente un RendezVous que no es suyo.
				"user1", "rendezVous2", IllegalArgumentException.class
			}, {
				// 3 - (+) Un usuario loggeado elimina virtualmente un RendezVous suyo.
				"user1", "rendezVous1", null
			}, {
				// 4 - (-) Un usuario loggeado elimina virtualmente un RendezVous ya marcado como eliminado.
				"user1", "rendezVous1", IllegalArgumentException.class
			}, {
				// 5 - (+) Un usuario loggeado elimina virtualmente un RendezVous con orgDate en pasado.
				"user1", "rendezVous1", null
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			final RendezVous rendezVousToDelete = this.rendezVousService.findOne(super.getEntityId((String) testingData[i][1]));

			if (i < 3 || i == 4)
				rendezVousToDelete.setIsDeleted(false);

			if (i < 4)
				rendezVousToDelete.setOrgDate(new LocalDate().plusDays(1).toDate());
			else if (i == 4)
				rendezVousToDelete.setOrgDate(new LocalDate().minusDays(1).toDate());

			this.startTransaction();

			this.templateVirtualDeleteRendezVous((String) testingData[i][0], rendezVousToDelete, (Class<?>) testingData[i][2]);

			this.rollbackTransaction();

		}

	}

	protected void templateVirtualDeleteRendezVous(final String username, final RendezVous rendezVousToDelete, final Class<?> expected) {

		// 1. Loggearse como Usuario (o como null)
		super.authenticate(username);

		Class<?> caught = null;

		try {

			User user = null;

			if (username != null)
				user = this.userService.findByUserAccount(LoginService.getPrincipal());

			// 2. Listar mis rendezVouses

			if (user != null)
				this.rendezVousService.findAllByUser(user);

			// 3. Marcar un rendezVous como eliminado

			this.rendezVousService.virtualDelete(rendezVousToDelete);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}
}
