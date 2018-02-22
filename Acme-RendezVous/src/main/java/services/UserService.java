
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return this.userRepository.save(user);
	}

	public User findByUserAccount(final UserAccount userAccount) {
		return this.userRepository.findByUserAccount(userAccount.getId());
	}

	public void save(final UserRegisterForm userRegisterForm) {

		/* 1. Creamos el User */
		final User user = this.create();

		/* 2. Al user le añadimos los parametros del form */
		user.getUserAccount().setUsername(userRegisterForm.getUsername());
		user.getUserAccount().setPassword(userRegisterForm.getPassword());
		user.setName(userRegisterForm.getName());
		user.setSurnames(userRegisterForm.getSurnames());
		user.setPostalAddress(userRegisterForm.getPostalAddress());
		user.setPhoneNumber(userRegisterForm.getPhoneNumber());
		user.setEmail(userRegisterForm.getEmail());
		user.setDateOfBirth(userRegisterForm.getDateOfBirth());

		/* 3. Le añadimos los parametros que eran hidden en el form */
		user.setAnswers(new HashSet<Answer>());
		user.setAttendedRendezVouses(new HashSet<RendezVous>());
		user.setComments(new HashSet<Comment>());
		user.setCreatedRendezVouses(new HashSet<RendezVous>());

		/* 4. Hasheamos la contraseña */
		final Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		final String hashedPassword = encoder.encodePassword(user.getUserAccount().getPassword(), null);
		user.getUserAccount().setPassword(hashedPassword);

		/* 5. Guardamos el usuario en la BD */
		this.userRepository.save(user);

	}

}
