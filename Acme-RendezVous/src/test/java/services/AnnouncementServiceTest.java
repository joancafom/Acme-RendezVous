
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import domain.Announcement;
import domain.RendezVous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AnnouncementServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private AnnouncementService	announcementService;

	//Helping Services
	@Autowired
	private UserService			userService;

	@Autowired
	private RendezVousService	rendezVousService;

	@PersistenceContext
	private EntityManager		entityManager;


	//Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Stream of Announcements
	 * 1. Log in as a User
	 * 2. Get the Stream of Announcements of their RSVP rendezVouses
	 * 
	 * Involved REQs: 16.5
	 * 
	 * Test Cases (5; 3+ 2-):
	 * 
	 * + 1) A user logs in and correctly retrieves all the announcements related to the rendezVouses
	 * he/she has RSVPd, ordered chronologically in descending order
	 * 
	 * - 2) An unauthenticated actor tries to get the stream of announcements
	 * 
	 * - 3) A manager actor tries to get the stream of announcements
	 * 
	 * + 4) A user logs in, RSVPs a rendezVous, and correctly retrieves all the announcements related to the rendezVouses
	 * he/she has RSVPd, ordered chronologically in descending order (With the freshly RSVPd included)
	 * 
	 * + 5) A user logs in, cancels a rendezVous, and correctly retrieves all the announcements related to the rendezVouses
	 * he/she has RSVPd, ordered chronologically in descending order (Without the canceled one)
	 */
	@Test
	public void driverAnnouncementStream() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> a rendezVous to RSVP and cancel.
		// testingData[i][2] -> if we want to cancel the RendezVous after having RSVPd.
		// testingData[i][3] -> the expected exception.

		final Date futureDate = new LocalDate().plusDays(1).toDate();

		final Object testingData[][] = {
			{
				"user1", null, false, null
			}, {
				null, null, false, IllegalArgumentException.class
			}, {
				"manager1", null, false, IllegalArgumentException.class
			}, {
				"user2", "rendezVous7", false, null
			}, {
				"user3", "rendezVous7", true, null
			}
		};

		RendezVous rendezVousToAttend = null;

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			if (testingData[i][1] != null) {

				rendezVousToAttend = this.rendezVousService.findOne(this.getEntityId((String) testingData[i][1]));

				this.authenticate(rendezVousToAttend.getCreator().getUserAccount().getUsername());

				rendezVousToAttend.setOrgDate(futureDate);
				rendezVousToAttend = this.rendezVousService.save(rendezVousToAttend);

				final Announcement testAnnouncement = this.announcementService.create(rendezVousToAttend);

				testAnnouncement.setTitle("Testing Announcement");
				testAnnouncement.setDescription("Testing Description");

				this.announcementService.save(testAnnouncement);
				this.announcementService.flush();

				this.unauthenticate();
			} else
				rendezVousToAttend = null;

			this.templateAnnouncementStream((String) testingData[i][0], rendezVousToAttend, (Boolean) testingData[i][2], (Class<?>) testingData[i][3]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	//Templates

	protected void templateAnnouncementStream(final String username, final RendezVous rendezVous, final Boolean cancelRendezVous, final Class<?> expected) {
		//v2.0 Implemented by JA

		Class<?> caught = null;

		//1. Login as User
		this.authenticate(username);

		try {

			//2. Get the Stream of Announcements of their RSVP rendezVouses
			final List<Announcement> retrievedAnnouncements = this.announcementService.findByCurrentChronological();

			final Comparator<Announcement> chronologicalCmp = new Comparator<Announcement>() {

				@Override
				public int compare(final Announcement o1, final Announcement o2) {
					return o2.getCreationMoment().compareTo(o1.getCreationMoment());
				}
			};

			final User currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());
			final List<Announcement> annoucementStream = new ArrayList<Announcement>();

			for (final RendezVous rv : currentUser.getAttendedRendezVouses())
				annoucementStream.addAll(rv.getAnnouncements());

			Collections.sort(annoucementStream, chronologicalCmp);

			Assert.isTrue(retrievedAnnouncements.equals(annoucementStream));

			//Let's check now what happens if we RSVP/cancel a rendezVous

			if (rendezVous != null) {

				this.rendezVousService.acceptRSVP(rendezVous);
				this.rendezVousService.flush();

				//Now, we should be able to retrieve an updated list
				List<Announcement> retrievedAnnouncementsAfter = this.announcementService.findByCurrentChronological();

				Assert.isTrue(!annoucementStream.equals(retrievedAnnouncementsAfter));

				annoucementStream.addAll(rendezVous.getAnnouncements());

				Assert.isTrue(annoucementStream.containsAll(retrievedAnnouncementsAfter));
				Assert.isTrue(retrievedAnnouncementsAfter.containsAll(annoucementStream));

				if (!cancelRendezVous) {
					this.rendezVousService.cancelRSVP(rendezVous);
					this.rendezVousService.flush();

					//Now, we should be able to retrieve an updated list
					retrievedAnnouncementsAfter = this.announcementService.findByCurrentChronological();

					Assert.isTrue(retrievedAnnouncements.equals(retrievedAnnouncementsAfter));
				}

			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	// -------------------------------------------------------------------------------
	// [UC-011] Listar Announcements y crear un nuevo Announcement.
	// 
	// Requisitos relacionados:
	//   · REQ 12: Rendezvouses may have announcements. The system must record the
	//             moment when an announcement is made, plus a title and a
	//             description.
	//   · REQ 15.1: An actor who is not authenticated must be able to list the
	//               announcements that are associated with each rendezvous.
	//   · REQ 16.3: An actor who is authenticated as a user must be able to create an
	//               announcement regarding one of the rendezvouses that he or she's
	//               created previously.
	// -------------------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	// -------------------------------------------------------------------------------

	@Test
	public void driverListAndCreateAnnouncement() {

		// testingData[i][0] -> username del usuario loggeado.
		// testingData[i][1] -> creationMoment del announcement a crear.
		// testingData[i][2] -> title del announcement a crear.
		// testingData[i][3] -> description del rendezVous a crear.
		// testingData[i][4] -> rendezVous del announcement a crear.
		// testingData[i][5] -> excepción que debe saltar.

		final Object testingData[][] = {
			{
				// 1 - (+) Un usuario no loggeado lista announcements de un rendezVous
				null, null, null, null, "rendezVous1", null
			}, {
				// 2 - (-) Un usuario no loggeado crea un announcement
				null, null, "announcementTitle", "announcementDescription", "rendezVous1", IllegalArgumentException.class
			}, {
				// 3 - (+) Un usuario lista announcements de un rendezVous suyo y crea un announcement más
				"user1", null, "announcementTitle", "announcementDescription", "rendezVous1", null
			}, {
				// 4 - (-) Un usuario crea un announcement con el título a null
				"user1", null, null, "announcementDescription", "rendezVous1", ConstraintViolationException.class
			}, {
				// 5 - (-) Un usuario crea un announcement con el título en blanco
				"user1", null, "", "announcementDescription", "rendezVous1", ConstraintViolationException.class
			}, {
				// 6 - (-) Un usuario crea un announcement con la descripción a null
				"user1", null, "announcementTitle", null, "rendezVous1", ConstraintViolationException.class
			}, {
				// 7 - (-) Un usuario crea un announcement con la descripción en blanco
				"user1", null, "announcementTitle", "", "rendezVous1", ConstraintViolationException.class
			}, {
				// 8 - (-) Un usuario crea un announcement para un rendezVous que no es suyo
				"user1", null, "announcementTitle", "announcementDescription", "rendezVous2", IllegalArgumentException.class
			}, {
				// 9 - (-) Un administrador crea un announcement
				"admin", null, "announcementTitle", "announcementDescription", "rendezVous1", IllegalArgumentException.class
			}, {
				// 10 - (-) Un manager crea un announcement
				"manager1", null, "announcementTitle", "announcementDescription", "rendezVous1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {

			RendezVous rendezVous = null;

			if (testingData[i][4] != null)
				rendezVous = this.rendezVousService.findOne(super.getEntityId((String) testingData[i][4]));

			this.startTransaction();

			this.templateListAndCreateAnnouncement((String) testingData[i][0], (Date) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], rendezVous, (Class<?>) testingData[i][5]);

			this.rollbackTransaction();
			this.entityManager.clear();

		}

	}
	protected void templateListAndCreateAnnouncement(final String username, final Date creationMoment, final String title, final String description, final String rendezVousBean, final RendezVous rendezVous, final Class<?> expected) {

		// 1. Loggearse como Usuario (o como null)
		super.authenticate(username);

		Class<?> caught = null;

		try {

			// 2. Listar todos los announcements asociados con un rendezVous

			final Collection<Announcement> allByRendezVous = this.announcementService.findByRendezVousId(super.getEntityId(rendezVousBean));

			if (!(username == null && expected == null)) {
				// 3. Crear un nuevo announcement para el rendezVous

				final Announcement createdAnnouncement = this.announcementService.create(rendezVous);

				createdAnnouncement.setCreationMoment(creationMoment);
				createdAnnouncement.setTitle(title);
				createdAnnouncement.setDescription(description);

				final Announcement savedAnnouncement = this.announcementService.save(createdAnnouncement);

				// Flush
				this.announcementService.flush();

				// 4. Listar los announcements y comprobar que contiene al nuevo

				final Collection<Announcement> newAllByRendezVous = this.announcementService.findByRendezVousId(super.getEntityId(rendezVousBean));

				Assert.isTrue(!allByRendezVous.containsAll(newAllByRendezVous));
				Assert.isTrue(this.announcementService.findAll().contains(savedAnnouncement));
			}

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		super.unauthenticate();
		super.checkExceptions(expected, caught);

	}

	/*
	 * v1.1 - josembell
	 * 
	 * [UC-014] - Eliminar un anuncio
	 * 
	 * REQ: 17.1
	 */

	@Test
	public void driverDeleteAnnouncement() {
		final Object testingData[][] = {
			{
				/* + 1) Un administrador elimina un anuncio de un RendezVous creado por él */
				"admin", "announcement1", null
			}, {
				/* - 2) Un usuario no identificado elimina un anuncio */
				null, "announcement1", IllegalArgumentException.class
			}, {
				/* - 3) Un administrador identificado elimina un announcement null */
				"admin", null, IllegalArgumentException.class
			}, {
				/* - 4) Un user intenta eliminar un announcement */
				"user1", "announcement1", IllegalArgumentException.class
			}, {
				/* - 5) Un manager intenta eliminar un announcement */
				"user1", "announcement1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++) {
			Announcement announcement;
			if (testingData[i][1] != null)
				announcement = this.announcementService.findOne(this.getEntityId((String) testingData[i][1]));
			else
				announcement = null;

			//System.out.println("Test " + (i + 1));
			this.templateDeleteAnnouncement((String) testingData[i][0], announcement, (Class<?>) testingData[i][2]);
			//System.out.println("Test " + (i + 1) + " - OK");
		}

	}

	/* v1.1 - josembell */
	protected void templateDeleteAnnouncement(final String username, final Announcement announcement, final Class<?> expected) {
		Class<?> caught = null;

		/* 1. Loggearte como administrador */
		this.authenticate(username);

		try {
			/* 2. Listar todos los RendezVouses */
			final Collection<Announcement> allAnnouncements = this.announcementService.findAll();

			/* 3. Seleccionar un RendezVous -> announcement.getRendezVous() */

			/* 4. Seleccionar un anuncio -> el que entra por parámetros */
			this.announcementService.delete(announcement);
			Assert.isTrue(this.announcementService.findAll().size() + 1 == allAnnouncements.size());

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
