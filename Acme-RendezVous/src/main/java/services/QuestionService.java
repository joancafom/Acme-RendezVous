
package services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.QuestionRepository;
import security.LoginService;
import domain.Answer;
import domain.Question;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class QuestionService {

	// Repository and services --------------

	@Autowired
	private QuestionRepository	questionRepository;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private UserService			userService;


	// CRUD methods -------------------------

	public Question create(final RendezVous rendezVous) {

		final Question question = new Question();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(rendezVous);
		Assert.isTrue(rendezVous.getCreator().equals(user));

		final Collection<Answer> answers = new HashSet<Answer>();

		question.setRendezVous(rendezVous);
		question.setAnswers(answers);

		return question;
	}

	public Question save(final Question question) {

		Assert.notNull(question);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.isTrue(question.getId() == 0);
		Assert.isTrue(question.getRendezVous().getCreator().equals(user));

		return this.questionRepository.save(question);
	}

	public Question findOne(final int questionId) {
		return this.questionRepository.findOne(questionId);
	}

	// v1.0 - Implemented by Alicia
	public Collection<Question> findAll() {
		return this.questionRepository.findAll();
	}

	public void delete(final Question question) {
		Assert.notNull(question);
		Assert.isTrue(this.questionRepository.exists(question.getId()));

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.isTrue(user.getCreatedRendezVouses().contains(question.getRendezVous()));

		for (final Answer a : question.getAnswers())
			this.answerService.delete(a);

		this.questionRepository.delete(question);
	}

	public List<Question> findAllOrderedByRendezVous(final RendezVous rendezVous) {
		return this.questionRepository.findAllOrderedByRendezVous(rendezVous.getId());
	}

	// Other business methods

	// v1.9 - Implemented by Alicia
	public void flush() {
		this.questionRepository.flush();
	}

}
