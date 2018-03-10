
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

import repositories.UserRepository;
import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import domain.Answer;
import domain.Comment;
import domain.RendezVous;
import domain.User;
import forms.UserRegisterForm;

@Service
@Transactional
public class UserService {

	/* Repository */
	@Autowired
	private UserRepository		userRepository;

	/* Services */
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private Validator			validator;


	public User create() {
		final User user = new User();
		final Collection<Answer> answers = new HashSet<Answer>();
		final Collection<RendezVous> createdRendezVouses = new HashSet<RendezVous>();
		final Collection<RendezVous> attendedRendezVouses = new HashSet<RendezVous>();
		final Collection<Comment> comments = new HashSet<Comment>();
		final UserAccount userAccount = this.userAccountService.create();

		if (userAccount.getAuthorities().isEmpty()) {
			final Authority auth = new Authority();
			auth.setAuthority(Authority.USER);
			userAccount.getAuthorities().add(auth);
		}

		user.setAnswers(answers);
		user.setCreatedRendezVouses(createdRendezVouses);
		user.setAttendedRendezVouses(attendedRendezVouses);
		user.setComments(comments);
		user.setUserAccount(userAccount);

		return user;
	}

	public User findOne(final int userId) {
		return this.userRepository.findOne(userId);
	}

	public Collection<User> findAll() {
		return this.userRepository.findAll();
	}

	public User save(final User user) {

		Assert.notNull(user);
		Assert.isTrue(user.getId() == 0);

		/* Hash the password */
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(user.getUserAccount().getPassword(), null);
		user.getUserAccount().setPassword(hashedPassword);

		return this.userRepository.save(user);
	}

	public void flush() {

		//v1.0 - Implemented by JA

		this.userRepository.flush();
	}

	//Other Business Methods

	public User findByUserAccount(final UserAccount userAccount) {
		return this.userRepository.findByUserAccount(userAccount.getId());
	}

	public User reconstruct(final UserRegisterForm userRegisterForm, final BindingResult binding) {

		final User user = this.create();

		user.setName(userRegisterForm.getName());
		user.setSurnames(userRegisterForm.getSurnames());
		user.setPostalAddress(userRegisterForm.getPostalAddress());
		user.setPhoneNumber(userRegisterForm.getPhoneNumber());
		user.setEmail(userRegisterForm.getEmail());
		user.setDateOfBirth(userRegisterForm.getDateOfBirth());

		this.validator.validate(user, binding);

		user.getUserAccount().setUsername(userRegisterForm.getUsername());
		user.getUserAccount().setPassword(userRegisterForm.getPassword());

		final Errors userAccountErrors = new BeanPropertyBindingResult(user.getUserAccount(), binding.getObjectName());

		this.validator.validate(user.getUserAccount(), userAccountErrors);

		binding.addAllErrors(userAccountErrors);

		return user;
	}
}
