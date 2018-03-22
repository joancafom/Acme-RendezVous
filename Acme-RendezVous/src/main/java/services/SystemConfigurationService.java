
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.SystemConfigurationRepository;
import security.LoginService;
import domain.Administrator;
import domain.SystemConfiguration;

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

	//Other Business Methods

	public SystemConfiguration getCurrentSystemConfiguration() {

		//v1.0 - Implemented by JA

		//Theoretically there is only one SystemConfiguration in our system.

		final Collection<SystemConfiguration> allSysConfig = this.findAll();
		SystemConfiguration res;

		res = allSysConfig == null ? null : allSysConfig.iterator().next();

		return res;
	}
}
