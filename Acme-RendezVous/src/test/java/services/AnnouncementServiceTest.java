
package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
}
