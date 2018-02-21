
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.RendezVousRepository;
import security.LoginService;
import security.UserAccount;
import domain.Announcement;
import domain.Comment;
import domain.GPSCoordinates;
import domain.Question;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class RendezVousService {

	/* Repositories */

	@Autowired
	private RendezVousRepository	rendezVousRepository;

	/* Services */

	@Autowired
	private UserService				userService;


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

}
