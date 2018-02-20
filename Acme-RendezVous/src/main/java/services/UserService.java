
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.UserRepository;
import security.UserAccount;
import domain.User;

@Service
@Transactional
public class UserService {

	/* Repository */
	@Autowired
	private UserRepository	userRepository;


	public User findByUserAccount(final UserAccount userAccount) {
		return this.userRepository.findByUserAccount(userAccount.getId());
	}

}
