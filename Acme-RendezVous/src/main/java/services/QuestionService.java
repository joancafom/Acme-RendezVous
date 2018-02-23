
package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import domain.Answer;
import domain.Question;

@Service
@Transactional
public class QuestionService {

	/* Repositories */

	@Autowired
	private QuestionRepository	questionRepository;

	@Autowired
	private AnswerService		answerService;


	/* Business Methods */

	public void delete(final Question question) {
		Assert.notNull(question);

		Assert.isTrue(this.questionRepository.exists(question.getId()));

		for (final Answer a : question.getAnswers())
			this.answerService.delete(a);

		this.questionRepository.delete(question);
	}

}
