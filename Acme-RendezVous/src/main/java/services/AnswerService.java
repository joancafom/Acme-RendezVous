
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnswerRepository;
import domain.Answer;

@Service
@Transactional
public class AnswerService {

	/* Repositories */

	@Autowired
	private AnswerRepository	answerRepository;


	/* Business Methods */

	public void delete(final Answer answer) {
		Assert.notNull(answer);

		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);
	}

}
