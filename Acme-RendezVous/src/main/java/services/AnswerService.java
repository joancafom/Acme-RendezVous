
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AnswerRepository;
import security.LoginService;
import domain.Answer;
import domain.Question;
import domain.RendezVous;
import domain.User;
import forms.RSVPForm;

@Service
@Transactional
public class AnswerService {

	/* Repositories */

	@Autowired
	private AnswerRepository	answerRepository;

	/* Services */
	@Autowired
	private UserService			userService;

	@Autowired
	private QuestionService		questionService;

	@Autowired
	private RendezVousService	rendezVousService;

	@Autowired
	private Validator			validator;


	/* CRUD METHODS */

	public void delete(final Answer answer) {
		Assert.notNull(answer);

		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);
	}

	public Answer save(final Answer answer) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.notNull(answer.getText());
		Assert.notNull(answer.getQuestion());

		this.answerRepository.save(answer);

		return answer;
	}
	/* Other Methods */

	public Collection<Answer> findAllByRendezVousAndUser(final RendezVous rendezVous, final User user) {

		Assert.notNull(rendezVous);
		Assert.notNull(user);

		return this.answerRepository.findAllByRendezVousAndUserIds(rendezVous.getId(), user.getId());

	}

	public Answer create(final Question question) {
		final Answer answer = new Answer();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		answer.setUser(user);
		answer.setQuestion(question);

		return answer;
	}

	public List<Answer> reconstuct(final RSVPForm form, final BindingResult binding) {
		final List<Answer> res = new ArrayList<Answer>();
		final RendezVous rendezVous = this.rendezVousService.findOne(form.getRendezVous());
		final List<Question> questions = this.questionService.findAllOrderedByRendezVous(rendezVous);

		for (int i = 0; i < questions.size(); i++) {
			final Answer a = this.create(questions.get(i));
			a.setText(form.getAnswers().get(i));
			this.validator.validate(a, binding);
			res.add(a);

		}

		return res;
	}

	public Collection<Answer> findAllByUserAndRendezVous(final User user, final RendezVous rendezVous) {
		return this.answerRepository.findAllByUserAndRendezVous(user.getId(), rendezVous.getId());
	}

}
