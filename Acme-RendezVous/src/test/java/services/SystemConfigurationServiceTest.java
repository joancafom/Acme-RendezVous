
package services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.SystemConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// System Under Test

	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	// Fixtures

	@PersistenceContext
	private EntityManager				entityManager;


	// Drivers

	/*
	 * v1.0 - Implemented by JA
	 * 
	 * UC-2-0013: Display and Edit SystemConfiguration
	 * 1. Log in to the system as an Administrator
	 * 2. Display SystemConfiguration
	 * 3. Edit SystemConfiguration
	 * 4. Display SystemConfiguration
	 * 
	 * Involved REQs: 12
	 * 
	 * Test Cases (11; 1+ 10-):
	 * 
	 * + 1) An Administrator logs in, displays the SystemConfiguration and updates it using correct data
	 * 
	 * - 2) A User tries to edit the SystemConfiguration providing correct data.
	 * 
	 * - 3) An unauthenticated actor tries to edit the SystemConfiguration providing correct data.
	 * 
	 * - 4) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (businessName = " ")
	 * 
	 * - 5) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (businessName = null)
	 * 
	 * - 6) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (businessName with XSS)
	 * 
	 * - 7) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (bannerURL = " ")
	 * 
	 * - 8) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (bannerURL = null)
	 * 
	 * - 9) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (welcomeMessages = null)
	 * 
	 * - 10) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (welcomeMessages = " ")
	 * 
	 * - 11) An Administrator logs in, displays the SystemConfiguration and tries to update it with incorrect data (welcomeMessages with XSS)
	 */

	@Test
	public void driverListEditService() {

		// testingData[i][0] -> username of the Actor to log in.
		// testingData[i][1] -> the SystemConfiguration to edit --> blank if current
		// testingData[i][2] -> the businessName of the SystemConfiguration.
		// testingData[i][3] -> the bannerURL of the SystemConfiguration.
		// testingData[i][4] -> the welcomeMessages of the SystemConfiguration.
		// testingData[i][5] -> String with the names of the parameters we want to update, separated by blank spaces
		// testingData[i][6] -> the expected exception.

		final Object testingData[][] = {

			{
				"admin", "", "Blank Space Company", "https://goo.gl/pQRHGA", "es=Hola|fr=Bonjour", "businessName bannerURL welcomeMessages", null
			}, {
				"user1", "", "Blank Space Company", "https://goo.gl/pQRHGA", "es=Hola|fr=Bonjour", "businessName bannerURL welcomeMessages", IllegalArgumentException.class
			}, {
				null, "", "Blank Space Company", "https://goo.gl/pQRHGA", "es=Hola|fr=Bonjour", "businessName bannerURL welcomeMessages", IllegalArgumentException.class
			}, {
				"admin", "", " ", null, null, "businessName", ConstraintViolationException.class
			}, {
				"admin", "", null, null, null, "businessName", ConstraintViolationException.class
			}, {
				"admin", "", "<script>alert('Hacked!');</script>", null, null, "businessName", ConstraintViolationException.class
			}, {
				"admin", "", null, " ", null, "bannerURL", ConstraintViolationException.class
			}, {
				"admin", "", null, null, null, "bannerURL", ConstraintViolationException.class
			}, {
				"admin", "", null, null, null, "welcomeMessages", ConstraintViolationException.class
			}, {
				"admin", "", null, null, " ", "welcomeMessages", ConstraintViolationException.class
			}, {
				"admin", "", null, null, "<script>alert('Hacked!');</script>", "welcomeMessages", ConstraintViolationException.class
			}
		};

		SystemConfiguration sysConfig = null;
		String stateBefore = null;

		for (int i = 0; i < testingData.length; i++) {

			this.startTransaction();

			if (testingData[i][1] == "") {
				sysConfig = this.systemConfigurationService.getCurrentSystemConfiguration();
				Assert.notNull(sysConfig);
				stateBefore = sysConfig.toString();
			} else {
				sysConfig = null;
				stateBefore = null;
			}

			final String changes = (String) testingData[i][5];

			if (changes.contains("businessName"))
				sysConfig.setBusinessName((String) testingData[i][2]);

			if (changes.contains("bannerURL"))
				sysConfig.setBannerURL((String) testingData[i][3]);

			if (changes.contains("welcomeMessages"))
				sysConfig.setWelcomeMessages((String) testingData[i][4]);

			this.templateDisplayEditService((String) testingData[i][0], sysConfig, changes, stateBefore, (Class<?>) testingData[i][6]);

			this.rollbackTransaction();
			this.entityManager.clear();
		}

	}
	// Test Templates	

	protected void templateDisplayEditService(final String username, final SystemConfiguration sysConfig, final String changesPerformed, final String stateBefore, final Class<?> expected) {

		//v1.0 Implemented by JA

		Class<?> caught = null;

		//1. Log in to the system
		this.authenticate(username);

		try {

			//2. Display SystemConfiguration
			final SystemConfiguration sysConfigBefore = this.systemConfigurationService.getCurrentSystemConfiguration();
			Assert.notNull(sysConfigBefore);

			//3. Edit SystemConfiguration (given with the editions)

			final SystemConfiguration savedSC = this.systemConfigurationService.save(sysConfig);
			this.systemConfigurationService.flush();

			//Check the changes we have made

			if (changesPerformed.contains("businessName"))
				Assert.isTrue(savedSC.getBusinessName().equals(sysConfig.getBusinessName()));

			if (changesPerformed.contains("bannerURL"))
				Assert.isTrue(savedSC.getBannerURL().equals(sysConfig.getBannerURL()));

			if (changesPerformed.contains("welcomeMessages"))
				Assert.isTrue(savedSC.getWelcomeMessages().equals(sysConfig.getWelcomeMessages()));

			//4. Display SystemConfiguration
			final SystemConfiguration sysConfigAfter = this.systemConfigurationService.getCurrentSystemConfiguration();
			final String stateAfter = sysConfigAfter.toString();

			Assert.isTrue(!stateAfter.equals(stateBefore));
			Assert.isTrue(this.systemConfigurationService.findAll().size() == 1);

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.unauthenticate();
		this.checkExceptions(expected, caught);

	}
}
