
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RendezVousRepository;
import security.LoginService;
import security.UserAccount;
import domain.Announcement;
import domain.Answer;
import domain.Comment;
import domain.GPSCoordinates;
import domain.Question;
import domain.RendezVous;
import domain.ServiceRequest;
import domain.User;
import forms.RSVPForm;
import forms.SimilarRendezVousForm;

@Service
@Transactional
public class RendezVousService {

	/* Repositories */

	@Autowired
	private RendezVousRepository	rendezVousRepository;

	/* Services */

	@Autowired
	private UserService				userService;

	@Autowired
	private QuestionService			questionService;

	@Autowired
	private AnswerService			answerService;

	@Autowired
	private Validator				validator;


	/* Business Methods */

	public RendezVous create() {

		final RendezVous rendezVous = new RendezVous();
		final UserAccount userAccount = LoginService.getPrincipal();

		final User user = this.userService.findByUserAccount(userAccount);

		final Collection<Comment> comments = new HashSet<Comment>();
		final Collection<User> attendants = new HashSet<User>();
		final Collection<Question> questions = new HashSet<Question>();
		final Collection<Announcement> announcements = new HashSet<Announcement>();
		final Collection<RendezVous> similarRendezVouses = new HashSet<RendezVous>();
		final GPSCoordinates coordinates = new GPSCoordinates();
		final Collection<ServiceRequest> serviceRequests = new HashSet<ServiceRequest>();

		rendezVous.setCreator(user);
		rendezVous.setComments(comments);
		rendezVous.setSimilarRendezVouses(similarRendezVouses);
		rendezVous.setAnnouncements(announcements);
		rendezVous.setQuestions(questions);
		attendants.add(user);
		rendezVous.setAttendants(attendants);
		rendezVous.setCoordinates(coordinates);
		rendezVous.setServiceRequests(serviceRequests);

		if (user.getAge() < 18)
			rendezVous.setIsForAdults(false);

		return rendezVous;
	}

	public RendezVous findOne(final int rendezVousId) {
		return this.rendezVousRepository.findOne(rendezVousId);
	}

	public Collection<RendezVous> findAll() {
		return this.rendezVousRepository.findAll();
	}

	public RendezVous save(final RendezVous rendezVous) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);
		Assert.notNull(rendezVous);
		final RendezVous oldRendezVous = this.rendezVousRepository.findOne(rendezVous.getId());

		if (user != null)
			Assert.isTrue(rendezVous.getCreator().equals(user));

		if (rendezVous.getId() != 0) {
			Assert.isTrue(oldRendezVous.getIsDeleted() == false);
			Assert.isTrue(oldRendezVous.getIsFinal() == false);
		}

		if (user.getAge() < 18)
			Assert.isTrue(rendezVous.getIsForAdults() == false);

		Assert.notNull(rendezVous.getOrgDate());
		final Date now = new Date();
		Assert.isTrue(rendezVous.getOrgDate().after(now));
		final RendezVous result = this.rendezVousRepository.save(rendezVous);

		if (rendezVous.getId() == 0) {
			user.getAttendedRendezVouses().add(result);
			user.getCreatedRendezVouses().add(result);
			Assert.isTrue(!rendezVous.getIsDeleted());
		}

		Assert.isTrue(rendezVous.getCoordinates() == null || (rendezVous.getCoordinates().getLatitude() != null && rendezVous.getCoordinates().getLongitude() != null)
			|| (rendezVous.getCoordinates().getLatitude() == null && rendezVous.getCoordinates().getLongitude() == null));

		return result;
	}

	public void delete(final RendezVous rendezVous) {
		Assert.notNull(rendezVous);

		Assert.isTrue(this.rendezVousRepository.exists(rendezVous.getId()));

		for (final User u : rendezVous.getAttendants())
			u.getAttendedRendezVouses().remove(rendezVous);

		for (final RendezVous rv : this.rendezVousRepository.findAll())
			if (rv.getSimilarRendezVouses().contains(rendezVous))
				rv.getSimilarRendezVouses().remove(rendezVous);

		for (final Question q : rendezVous.getQuestions())
			this.questionService.delete(q);

		this.rendezVousRepository.delete(rendezVous);
	}

	public void flush() {

		//v1.0 - Implemented by JA

		this.rendezVousRepository.flush();
	}

	/* Other Business Methods */

	public void virtualDelete(final RendezVous rendezVous) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.isTrue(!rendezVous.getIsDeleted());

		if (user != null)
			Assert.isTrue(rendezVous.getCreator().equals(user));

		rendezVous.setIsDeleted(true);

		this.rendezVousRepository.save(rendezVous);

	}

	public Collection<RendezVous> findAllNotAdult() {

		return this.rendezVousRepository.findAllNotAdult();
	}

	public Collection<RendezVous> findAllNotAdultByUser(final User user) {
		final Collection<RendezVous> notAdult = new HashSet<RendezVous>();
		for (final RendezVous rv : user.getAttendedRendezVouses())
			if (rv.getIsForAdults() == false)
				notAdult.add(rv);
		return notAdult;
	}

	public RendezVous cancelRSVP(final RendezVous rendezVous) {
		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(rendezVous.getAttendants().contains(user));

		//We delete the answers to the questions
		final Collection<Answer> userAnswers = this.answerService.findAllByRendezVousAndUser(rendezVous, user);

		for (final Answer a : userAnswers)
			this.answerService.delete(a);

		rendezVous.getAttendants().remove(user);
		user.getAttendedRendezVouses().remove(rendezVous);

		return this.rendezVousRepository.save(rendezVous);

	}

	public RendezVous acceptRSVP(final RendezVous rendezVous) {

		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(!rendezVous.getAttendants().contains(user));
		if (rendezVous.getIsForAdults())
			Assert.isTrue(user.getAge() >= 18);

		//Check that the User has answered all the questions
		final Collection<Answer> userAnswers = this.answerService.findAllByRendezVousAndUser(rendezVous, user);

		Assert.notNull(userAnswers);
		Assert.isTrue(rendezVous.getQuestions().size() == userAnswers.size());

		rendezVous.getAttendants().add(user);
		user.getAttendedRendezVouses().add(rendezVous);

		return this.rendezVousRepository.save(rendezVous);

	}

	public RendezVous reconstruct(final RendezVous rendezVous, final BindingResult binding) {
		final RendezVous result;

		if (rendezVous.getId() == 0)
			result = rendezVous;
		else {
			result = this.rendezVousRepository.findOne(rendezVous.getId());
			this.validator.validate(result, binding);
		}

		return result;
	}

	public RendezVous reconstruct(final RSVPForm rendezVous, final BindingResult binding) {
		final RendezVous result;

		if (rendezVous.getRendezVous() == 0)
			result = this.rendezVousRepository.findOne(rendezVous.getRendezVous());
		else {
			result = this.rendezVousRepository.findOne(rendezVous.getRendezVous());
			this.validator.validate(result, binding);
		}

		return result;
	}

	public Collection<RendezVous> findAllExceptCreatedByUser(final User user) {
		return this.rendezVousRepository.findAllExceptCreatedByUser(user.getId());
	}

	public Collection<RendezVous> findAllNotAdultExceptCreatedByUser(final User user) {
		return this.rendezVousRepository.findAllNotAdultExceptCreatedByUser(user.getId());
	}

	public RendezVous getSimilarRendezVousByForm(final SimilarRendezVousForm rendezVous) {
		return this.rendezVousRepository.findOne(rendezVous.getRendezVous());
	}

	public RendezVous getParentRendezVousByForm(final SimilarRendezVousForm rendezVous) {
		return this.rendezVousRepository.findOne(rendezVous.getId());
	}

	public void addSimilarRendezVous(final RendezVous pRV, final RendezVous sRV) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(pRV.getCreator().equals(user));
		Assert.isTrue(user.getCreatedRendezVouses().contains(pRV));
		Assert.isTrue(!pRV.getSimilarRendezVouses().contains(sRV));
		Assert.isTrue(pRV.getIsDeleted() == false);
		pRV.getSimilarRendezVouses().add(sRV);
	}

	public void deleteSimilarRendezVous(final RendezVous pRV, final RendezVous sRV) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.isTrue(pRV.getCreator().equals(user));
		Assert.isTrue(user.getCreatedRendezVouses().contains(pRV));
		Assert.isTrue(pRV.getSimilarRendezVouses().contains(sRV));
		Assert.isTrue(pRV.getIsDeleted() == false);
		pRV.getSimilarRendezVouses().remove(sRV);
	}

	public RendezVous reconstructRendezVous(final RendezVous prunedRendezVous, final BindingResult binding) {
		final RendezVous result;

		if (prunedRendezVous.getId() == 0) {
			result = prunedRendezVous;
			final UserAccount userAccount = LoginService.getPrincipal();

			final User user = this.userService.findByUserAccount(userAccount);

			final Collection<Comment> comments = new HashSet<Comment>();
			final Collection<User> attendants = new HashSet<User>();
			final Collection<Question> questions = new HashSet<Question>();
			final Collection<Announcement> announcements = new HashSet<Announcement>();
			final Collection<RendezVous> similarRendezVouses = new HashSet<RendezVous>();
			final Collection<ServiceRequest> serviceRequests = new HashSet<ServiceRequest>();

			result.setCreator(user);
			result.setComments(comments);
			result.setSimilarRendezVouses(similarRendezVouses);
			result.setAnnouncements(announcements);
			result.setQuestions(questions);
			result.setServiceRequests(serviceRequests);
			attendants.add(user);
			result.setAttendants(attendants);
			result.setServiceRequests(serviceRequests);

			if (user.getAge() < 18)
				result.setIsForAdults(false);

			this.validator.validate(result, binding);
		} else {
			final RendezVous savedRendezVous = this.rendezVousRepository.findOne(prunedRendezVous.getId());
			result = prunedRendezVous;

			result.setCreator(savedRendezVous.getCreator());
			result.setComments(savedRendezVous.getComments());
			result.setSimilarRendezVouses(savedRendezVous.getSimilarRendezVouses());
			result.setAnnouncements(savedRendezVous.getAnnouncements());
			result.setQuestions(savedRendezVous.getQuestions());
			result.setAttendants(savedRendezVous.getAttendants());
			result.setServiceRequests(savedRendezVous.getServiceRequests());

			this.validator.validate(result, binding);
		}

		return result;
	}

	public Collection<RendezVous> findAllByUser(final User user) {

		//v1.0 - Implemented by JA

		Collection<RendezVous> res;

		Assert.notNull(user);

		res = this.rendezVousRepository.findAllByUserId(user.getId());

		Assert.notNull(res);

		return res;

	}

}
