
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
import domain.Comment;
import domain.GPSCoordinates;
import domain.Question;
import domain.RendezVous;
import domain.User;
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

		rendezVous.setCreator(user);
		rendezVous.setComments(comments);
		rendezVous.setSimilarRendezVouses(similarRendezVouses);
		rendezVous.setAnnouncements(announcements);
		rendezVous.setQuestions(questions);
		attendants.add(user);
		rendezVous.setAttendants(attendants);
		rendezVous.setCoordinates(coordinates);

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

		if (user != null)
			Assert.isTrue(rendezVous.getCreator().equals(user));

		if (rendezVous.getId() != 0)
			Assert.isTrue(this.rendezVousRepository.findOne(rendezVous.getId()).getIsFinal() == false);

		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));

		final RendezVous result = this.rendezVousRepository.save(rendezVous);

		if (rendezVous.getId() == 0) {
			user.getAttendedRendezVouses().add(result);
			user.getCreatedRendezVouses().add(result);
		}
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
	/* Functional Requirements */

	public void virtualDelete(final RendezVous rendezVous) {
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

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

	public void cancelRSVP(final RendezVous rendezVous) {
		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(rendezVous.getAttendants().contains(user));

		rendezVous.getAttendants().remove(user);
		user.getAttendedRendezVouses().remove(rendezVous);

		this.rendezVousRepository.save(rendezVous);

	}

	public void acceptRSVP(final RendezVous rendezVous) {
		Assert.notNull(rendezVous);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(user);

		Assert.isTrue(!rendezVous.getIsDeleted());
		Assert.isTrue(rendezVous.getOrgDate().after(new Date()));
		Assert.isTrue(!rendezVous.getAttendants().contains(user));

		rendezVous.getAttendants().add(user);
		user.getAttendedRendezVouses().add(rendezVous);

		this.rendezVousRepository.save(rendezVous);

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

}
