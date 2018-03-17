
package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.transaction.Transactional;

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


	//Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 16.5
	 * An actor who is authenticated as a user must be able to
	 * Display a stream of announcements that have been posted to
	 * the rendezvouses that he or she's RSVPd.
	 * The announcements must be listed chronologically in
	 * descending order.
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) A user correctly retrieves all the announcements related to the rendezVouses
	 * he/she has RSVPd, ordered chronologically in descending order
	 * 
	 * - 2) An unauthenticated actor tries to get the stream of announcements
	 * 
	 * - 3) A manager actor tries to get the stream of announcements
	 */
	@Test
	public void driverAnnouncementStream() {

		final Object testingData[][] = {
			{
				"user1", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateAnnouncementStream((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}
	//Templates

	protected void templateAnnouncementStream(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final List<Announcement> retrievedAnnouncements = this.announcementService.findByCurrentChronological();
			Assert.notNull(retrievedAnnouncements);

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

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}
}
