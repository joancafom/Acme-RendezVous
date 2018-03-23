
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SystemConfigurationRepository;
import security.LoginService;
import domain.Administrator;
import domain.SystemConfiguration;
import forms.SystemConfigurationForm;

@Service
@Transactional
public class SystemConfigurationService {

	/* Repositories */

	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	@Autowired
	private AdministratorService			adminService;


	/* CRUD Methods */

	public SystemConfiguration create() {

		//v1.0 - Implemented by JA

		final SystemConfiguration sysConfig = new SystemConfiguration();

		return sysConfig;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		//v1.0 - Implemented by JA

		return this.systemConfigurationRepository.findOne(systemConfigurationId);
	}

	public Collection<SystemConfiguration> findAll() {
		//v1.0 - Implemented by JA

		return this.systemConfigurationRepository.findAll();
	}

	public SystemConfiguration save(final SystemConfiguration sC) {

		//v1.0 - Implemented by JA

		Assert.notNull(sC);

		final Administrator admin = this.adminService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Collection<SystemConfiguration> allSCs = this.findAll();

		//Guarantee the uniqueness
		if (allSCs != null)
			for (final SystemConfiguration sysConfig : allSCs)
				if (!sysConfig.equals(sC))
					this.delete(sysConfig);

		return this.systemConfigurationRepository.save(sC);
	}

	public void delete(final SystemConfiguration sC) {

		//v1.0 - Implemented by JA

		Assert.notNull(sC);

		this.systemConfigurationRepository.delete(sC);
	}

	public void flush() {

		//v1.0 - Implemented by JA

		this.systemConfigurationRepository.flush();
	}

	//Other Business Methods

	public SystemConfiguration getCurrentSystemConfiguration() {

		//v1.0 - Implemented by JA

		//Theoretically there is only one SystemConfiguration in our system, so a findAll operation
		//is not an overhead

		final Collection<SystemConfiguration> allSysConfig = this.findAll();
		SystemConfiguration res;

		res = allSysConfig == null ? null : allSysConfig.iterator().next();

		return res;
	}

	public Map<String, String> getWelcomeMessagesMap() {

		//v1.0 - Implemented by JA

		//Returns a Map with keys the languages and values the message for that language

		final Map<String, String> res = new HashMap<String, String>();
		final SystemConfiguration currentSC = this.getCurrentSystemConfiguration();

		//welcomeMessage is always of the form --> lang_iso1=message_for_lang1|lang_iso2=message_for_lang2...

		final String[] welcomeMessagesSplited = currentSC.getWelcomeMessages().split("\\|");

		if (currentSC != null)

			for (final String m : welcomeMessagesSplited) {

				final String[] langAndVal = m.split("=");
				Assert.isTrue(langAndVal.length == 2);

				res.put(langAndVal[0].trim(), langAndVal[1]);
			}

		return res;
	}

	public SystemConfiguration reconstruct(final SystemConfigurationForm systemConfigurationForm) {

		//v1.0 - Implemented by JA

		final SystemConfiguration sC = this.create();

		sC.setBannerURL(systemConfigurationForm.getBannerURL());
		sC.setBusinessName(systemConfigurationForm.getBusinessName());
		sC.setWelcomeMessages("en=" + systemConfigurationForm.getWelcomeMessageEN() + "|es=" + systemConfigurationForm.getWelcomeMessageES());

		return sC;
	}
}
