
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import repositories.ManagerRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import security.UserAccountService;
import domain.Manager;
import forms.ManagerRegisterForm;

@Service
@Transactional
public class ManagerService {

	/* Repository */
	@Autowired
	private ManagerRepository	managerRepository;

	/* Services */
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	/* Version 1.1 - josembell */
	public Manager create() {
		final Manager manager = new Manager();
		final Collection<domain.Service> services = new HashSet<domain.Service>();
		final UserAccount userAccount = this.userAccountService.create();

		if (userAccount.getAuthorities().isEmpty()) {
			final Authority auth = new Authority();
			auth.setAuthority(Authority.MANAGER);
			userAccount.getAuthorities().add(auth);
		}

		manager.setServices(services);
		manager.setUserAccount(userAccount);

		return manager;
	}
	/* Version 1.0 - josembell */
	public Manager findOne(final int managerId) {
		return this.managerRepository.findOne(managerId);
	}
	/* Version 1.0 - josembell */
	public Collection<Manager> findAll() {
		return this.managerRepository.findAll();
	}
	/* Version 1.0 - josembell */
	// v2.0 - Changes by Alicia
	public Manager save(final Manager manager) {
		Assert.notNull(manager);
		Assert.isTrue(manager.getId() == 0);

		try {
			LoginService.getPrincipal();
			throw new RuntimeException("An authenticated Actor cannot register to the system");
		} catch (final IllegalArgumentException okFlow) {

		}

		/* Hash the password */
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(manager.getUserAccount().getPassword(), null);
		manager.getUserAccount().setPassword(hashedPassword);

		return this.managerRepository.save(manager);
	}

	public Manager findByUserAccount(final UserAccount userAccount) {
		return this.managerRepository.findByUserAccount(userAccount.getId());
	}
	/* Version 1.0 - josembell */
	public Manager reconstruct(final ManagerRegisterForm managerRegisterForm, final BindingResult binding) {

		final Manager manager = this.create();

		manager.setName(managerRegisterForm.getName());
		manager.setSurnames(managerRegisterForm.getSurnames());
		manager.setPostalAddress(managerRegisterForm.getPostalAddress());
		manager.setPhoneNumber(managerRegisterForm.getPhoneNumber());
		manager.setEmail(managerRegisterForm.getEmail());
		manager.setVat(managerRegisterForm.getVat());

		this.validator.validate(manager, binding);

		manager.getUserAccount().setUsername(managerRegisterForm.getUsername());
		manager.getUserAccount().setPassword(managerRegisterForm.getPassword());

		final Errors userAccountErrors = new BeanPropertyBindingResult(manager.getUserAccount(), binding.getObjectName());

		this.validator.validate(manager.getUserAccount(), userAccountErrors);

		binding.addAllErrors(userAccountErrors);

		return manager;
	}

	// Other business methods

	// v1.0 - Implemented by Alicia
	public void flush() {
		this.managerRepository.flush();
	}
}
