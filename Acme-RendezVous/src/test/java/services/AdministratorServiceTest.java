
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Comment;
import domain.Question;
import domain.RendezVous;
import domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class AdministratorServiceTest extends AbstractTest {

	//Service Under Test
	@Autowired
	private AdministratorService	administratorService;

	//Helping Services
	@Autowired
	private UserService				userService;

	@Autowired
	private RendezVousService		rendezVousService;

	@Autowired
	private CommentService			commentService;


	//Drivers

	//--- Level C ---

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 16.3.1
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation of rendezVous per User
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardRendezVousCreatedPerUser() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardRendezVousCreatedPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 16.3.2
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the ratio of users that have created RendezVouses vs. have never created
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDahsboardRatioCreatedRendezVouses() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDahsboardRatioCreatedRendezVouses((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 16.3.3
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation of users per rendezVous
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardUsersPerRendezVous() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardUsersPerRendezVous((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 16.3.4
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation of the RSVPd RendezVouses Per User
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardRSVPRendezVousPerUser() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardRSVPRendezVousPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 16.3.5
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the top 10 rendezVouses which have more RSVPs
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardTopTenRSVPd() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardTopTenRSVPd((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	//--- Level B ---

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 17.2.1
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and std of Announcements per RendezVous
	 * 
	 * - 2) An unauthenticated actor tries to get the same results
	 * 
	 * - 3) A manager actor tries to get the same results
	 */

	@Test
	public void driverDashboardAnnouncementsPerRendezVous() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardAnnouncementsPerRendezVous((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 17.2.2
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the RendezVouses whose number of announcements
	 * is above 75% the average
	 * 
	 * - 2) An unauthenticated actor tries to get the same results
	 * 
	 * - 3) A manager actor tries to get the same results
	 */

	@Test
	public void driverDashboardRendezVousesAbove75AvgAnnouncements() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardRendezVousesAbove75AvgAnnouncements((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 17.2.3
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the RendezVouses that are linked to a number of RendezVouses greater than Avg + 10%
	 * 
	 * - 2) An unauthenticated actor tries to get the same results
	 * 
	 * - 3) A manager actor tries to get the same results
	 */

	@Test
	public void driverDashboardRendezVousAboveAvgPlus10SimilarRendezVouses() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardRendezVousAboveAvgPlus10SimilarRendezVouses((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	//--- Level A ---

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 22.1.1
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation of the number of questions per rendezVous
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardQuestionsPerRendezVous() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardQuestionsPerRendezVous((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 22.1.2
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation of the number of answers per rendezVous
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardAnswersPerRendezVous() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardAnswersPerRendezVous((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-010: Display a Dashboard
	 * 1. Log in as an Admin
	 * 2. Display the Dashboard
	 * 
	 * Involved REQs: 22.1.3
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and std of replies per Comment
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardRepliesPerComment() {

		final Object testingData[][] = {
			{
				"admin", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"manager1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.templateDashboardRepliesPerComment((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	//Templates

	//--- Level C ---

	protected void templateDashboardRendezVousCreatedPerUser(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgCreatedRendezVousPerUser = this.administratorService.getAvgCreatedRendezVousesPerUser();
			final Double stdCreatedRendezVousPerUser = this.administratorService.getStdDeviationCreatedRendezVousesPerUser();
			final List<Integer> numberRendezVousesPerUser = new ArrayList<Integer>();

			for (final User u : this.userService.findAll())
				numberRendezVousesPerUser.add(u.getCreatedRendezVouses().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgCreatedRendezVousPerUser, 3);
			final Double retrievedStd = this.roundNumber(stdCreatedRendezVousPerUser, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberRendezVousesPerUser), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberRendezVousesPerUser), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDahsboardRatioCreatedRendezVouses(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			Double retrievedRatio = this.administratorService.getUsersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses();
			Integer usersCreatedRendezVous = 0;
			Integer usersNoCreatedRendezVous = 0;

			for (final User u : this.userService.findAll())
				if (u.getCreatedRendezVouses().isEmpty())
					usersNoCreatedRendezVous++;
				else
					usersCreatedRendezVous++;

			//We need to round statistics up two 3 decimals to compare...
			retrievedRatio = this.roundNumber(retrievedRatio, 3);
			final Double computedRatio = this.roundNumber(usersCreatedRendezVous / usersNoCreatedRendezVous, 3);

			Assert.isTrue(retrievedRatio.equals(computedRatio));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardUsersPerRendezVous(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgUsersPerRendezVous = this.administratorService.getAvgUsersPerRendezVous();
			final Double stdUsersPerRendezVous = this.administratorService.getStdDeviationUsersPerRendezVous();
			final List<Integer> numberUsersPerRendezVous = new ArrayList<Integer>();

			for (final RendezVous rv : this.rendezVousService.findAll())
				numberUsersPerRendezVous.add(rv.getAttendants().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgUsersPerRendezVous, 3);
			final Double retrievedStd = this.roundNumber(stdUsersPerRendezVous, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberUsersPerRendezVous), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberUsersPerRendezVous), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardRSVPRendezVousPerUser(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgRSVPRendezVousPerUser = this.administratorService.getAvgRSVPPerUser();
			final Double stdRSVPRendezVousPerUser = this.administratorService.getStdDeviationRSVPPerUser();
			final List<Integer> numberRSVPRendezVousesPerUser = new ArrayList<Integer>();

			for (final User u : this.userService.findAll())
				numberRSVPRendezVousesPerUser.add(u.getAttendedRendezVouses().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgRSVPRendezVousPerUser, 3);
			final Double retrievedStd = this.roundNumber(stdRSVPRendezVousPerUser, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberRSVPRendezVousesPerUser), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberRSVPRendezVousesPerUser), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardTopTenRSVPd(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final List<RendezVous> retrievedTopTen = new ArrayList<RendezVous>(this.administratorService.getTopTenMoreRSVP());
			List<RendezVous> computedTopTen = new ArrayList<RendezVous>(this.rendezVousService.findAll());

			final Comparator<RendezVous> cmpRSVP = new Comparator<RendezVous>() {

				@Override
				public int compare(final RendezVous o1, final RendezVous o2) {

					int res;

					if (o1.getAttendants().size() == o2.getAttendants().size())
						res = 0;
					else if (o1.getAttendants().size() > o2.getAttendants().size())
						res = -1;
					else
						res = +1;

					return res;
				}
			};

			Collections.sort(computedTopTen, cmpRSVP);
			computedTopTen = computedTopTen.subList(0, computedTopTen.size() < 10 ? computedTopTen.size() : 10);

			Assert.isTrue(computedTopTen.equals(retrievedTopTen));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	//--- Level B ---

	protected void templateDashboardAnnouncementsPerRendezVous(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgAnnouncementsPerRV = this.administratorService.getAvgAnnouncementsPerRendezVous();
			final Double stdAnnouncementsPerRV = this.administratorService.getStdAnnouncementsPerRendezVous();
			final List<Integer> numberAnnouncementsPerRV = new ArrayList<Integer>();

			for (final RendezVous rv : this.rendezVousService.findAll())
				numberAnnouncementsPerRV.add(rv.getAnnouncements().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgAnnouncementsPerRV, 3);
			final Double retrievedStd = this.roundNumber(stdAnnouncementsPerRV, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberAnnouncementsPerRV), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberAnnouncementsPerRV), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardRendezVousesAbove75AvgAnnouncements(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Set<RendezVous> retrievedRendezVouses = new HashSet<RendezVous>(this.administratorService.getRendezVousAbove75AvgAnnouncements());
			final Double limit = this.administratorService.getAvgAnnouncementsPerRendezVous() * 0.75;

			final Collection<RendezVous> computedRendezVouses = new HashSet<RendezVous>();
			for (final RendezVous rv : this.rendezVousService.findAll())
				if (rv.getAnnouncements().size() > limit)
					computedRendezVouses.add(rv);

			Assert.isTrue(retrievedRendezVouses.equals(computedRendezVouses));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardRendezVousAboveAvgPlus10SimilarRendezVouses(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Set<RendezVous> retrievedRendezVouses = new HashSet<RendezVous>(this.administratorService.getRendezVousAboveAvgPlus10SimilarRendezVouses());

			//Compute the avg
			final List<Integer> numberLikedRendezVousesPerRV = new ArrayList<Integer>();

			for (final RendezVous rv : this.rendezVousService.findAll())
				numberLikedRendezVousesPerRV.add(rv.getSimilarRendezVouses().size());

			final Double limit = this.roundNumber(this.computeAverage(numberLikedRendezVousesPerRV), 3) * 1.1;

			//Compute the RendezVouses
			final Collection<RendezVous> computedRendezVouses = new HashSet<RendezVous>();
			for (final RendezVous rv : this.rendezVousService.findAll())
				if (rv.getSimilarRendezVouses().size() > limit)
					computedRendezVouses.add(rv);

			Assert.isTrue(retrievedRendezVouses.equals(computedRendezVouses));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	//--- Level A ---

	protected void templateDashboardQuestionsPerRendezVous(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgQuestionsPerRendezVous = this.administratorService.getAvgQuestionsPerRendezVous();
			final Double stdQuestionsPerRendezVous = this.administratorService.getStdQuestionsPerRendezVous();
			final List<Integer> numberQuestionsPerRendezVous = new ArrayList<Integer>();

			for (final RendezVous rv : this.rendezVousService.findAll())
				numberQuestionsPerRendezVous.add(rv.getQuestions().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgQuestionsPerRendezVous, 3);
			final Double retrievedStd = this.roundNumber(stdQuestionsPerRendezVous, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberQuestionsPerRendezVous), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberQuestionsPerRendezVous), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardAnswersPerRendezVous(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgAnswersPerRendezVous = this.administratorService.getAvgAnswersPerRendezVous();
			final Double stdAnswersPerRendezVous = this.administratorService.getStdAnswersPerRendezVous();
			final List<Integer> numberAnswersPerRendezVous = new ArrayList<Integer>();

			int tmp = 0;
			for (final RendezVous rv : this.rendezVousService.findAll()) {

				for (final Question q : rv.getQuestions())
					tmp += q.getAnswers().size();

				numberAnswersPerRendezVous.add(tmp);

				tmp = 0;
			}

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgAnswersPerRendezVous, 3);
			final Double retrievedStd = this.roundNumber(stdAnswersPerRendezVous, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberAnswersPerRendezVous), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberAnswersPerRendezVous), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	protected void templateDashboardRepliesPerComment(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgRepliesPerComment = this.administratorService.getAvgRepliesPerComment();
			final Double stdRepliesPerComment = this.administratorService.getStdRepliesPerComment();
			final List<Integer> numberRepliesPerComment = new ArrayList<Integer>();

			for (final Comment c : this.commentService.findAll())
				numberRepliesPerComment.add(c.getReplies().size());

			//We need to round statistics up two 3 decimals to compare...
			final Double retrievedAvg = this.roundNumber(avgRepliesPerComment, 3);
			final Double retrievedStd = this.roundNumber(stdRepliesPerComment, 3);
			final Double computedAvg = this.roundNumber(this.computeAverage(numberRepliesPerComment), 3);
			final Double computedStd = this.roundNumber(this.computeStd(numberRepliesPerComment), 3);

			Assert.isTrue(retrievedAvg.equals(computedAvg));
			Assert.isTrue(retrievedStd.equals(computedStd));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

	//Statistics Calculation methods

	private Double computeAverage(final Collection<? extends Number> inputCollection) {

		//v1.0 - Implemented by JA

		Double res = 0.0;

		if (!inputCollection.isEmpty()) {

			Double accSum = 0.0;

			for (final Number n : inputCollection)
				accSum += n.doubleValue();

			res = accSum / inputCollection.size();
		}

		return res;
	}

	private Double computeStd(final Collection<? extends Number> inputCollection) {

		//v1.0 - Implemented by JA

		Double res = 0.0;

		if (!inputCollection.isEmpty()) {

			Double sumSq = 0.0;
			for (final Number n : inputCollection)
				sumSq += n.doubleValue() * n.doubleValue();
			res = Math.sqrt((sumSq / inputCollection.size()) - Math.pow(this.computeAverage(inputCollection), 2));

		}

		return res;
	}

	private Double roundNumber(final Number number, final int decimalPlaces) {

		//v1.0 - Implemented by JA

		final Double dcPlacesB10 = Math.pow(10, decimalPlaces);

		final Double res = Math.round(number.doubleValue() * dcPlacesB10) / dcPlacesB10;

		return res;
	}
}
