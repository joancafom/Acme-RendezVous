
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class AnswerService {

	/* Repositories */

	@Autowired
	private AnswerRepository	answerRepository;


	/* CRUD METHODS */

	public void delete(final Answer answer) {
		Assert.notNull(answer);

		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);
	}

	/* Other Methods */

	public Collection<Answer> findAllByRendezVousAndUser(final RendezVous rendezVous, final User user) {

		Assert.notNull(rendezVous);
		Assert.notNull(user);

		return this.answerRepository.findAllByRendezVousAndUserIds(rendezVous.getId(), user.getId());

	}

}
