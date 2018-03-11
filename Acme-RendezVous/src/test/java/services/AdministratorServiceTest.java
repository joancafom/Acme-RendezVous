
package services;

import java.util.ArrayList;
import java.util.Collection;
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


	//Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 16.3.1
	 * An actor who is authenticated as an administrator must be able to
	 * Display a dashboard with the avg,std of rendezVous created per user
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation
	 * 
	 * - 2) An unauthenticated actor tries to get the statistics
	 * 
	 * - 3) A manager actor tries to get the statistics
	 */
	@Test
	public void driverDashboardRendezVousPerUser() {

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
			this.templateDashboardRendezVousPerUser((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 16.3.4
	 * An actor who is authenticated as an administrator must be able to
	 * Display a dashboard with the avg,std of rendezVous that are RSVPd per user
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation
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
	 * Req to Test: 17.2.2
	 * An actor who is authenticated as an administrator must be able to
	 * Display a dashboard with the RendezVouses whose number of announcements
	 * is above 75% the average number of announcements per rendezVous
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the RendezVouses
	 * 
	 * - 2) An unauthenticated actor tries to get the RendezVouses
	 * 
	 * - 3) A manager actor tries to get the RendezVouses
	 */
	@Test
	public void driverDashboardRendezVousAbove75AvgAnnouncements() {

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
			this.templateDashboardRendezVousAbove75AvgAnnouncements((String) testingData[i][0], (Class<?>) testingData[i][1]);

	}

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * Req to Test: 22.1.1
	 * An actor who is authenticated as an administrator must be able to
	 * Display a dashboard with the avg,std of questions per rendezVous
	 * 
	 * Test Cases (3; 1+ 2-):
	 * 
	 * + 1) An administrator correctly retrieves the avg and the standard deviation
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
	//Templates

	protected void templateDashboardRendezVousPerUser(final String username, final Class<?> expected) {
		//v1.0 Implemented by JA

		Class<?> caught = null;

		this.authenticate(username);

		try {

			final Double avgCreatedRendezVousPerUser = this.administratorService.getAvgCreatedRendezVousesPerUser();
			final Double stdCreatedRendezVousPerUser = this.administratorService.getStdDeviationCreatedRendezVousesPerUser();
			final List<Integer> numberRendezVousesPerUser = new ArrayList<Integer>();

			for (final User u : this.userService.findAll())
				numberRendezVousesPerUser.add(u.getCreatedRendezVouses().size());

			Assert.notNull(avgCreatedRendezVousPerUser);
			Assert.notNull(stdCreatedRendezVousPerUser);

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

			Assert.notNull(avgRSVPRendezVousPerUser);
			Assert.notNull(stdRSVPRendezVousPerUser);

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

	protected void templateDashboardRendezVousAbove75AvgAnnouncements(final String username, final Class<?> expected) {
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

			Assert.notNull(retrievedRendezVouses);
			Assert.isTrue(retrievedRendezVouses.equals(computedRendezVouses));

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();

		this.checkExceptions(expected, caught);
	}

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

			Assert.notNull(avgQuestionsPerRendezVous);
			Assert.notNull(stdQuestionsPerRendezVous);

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
