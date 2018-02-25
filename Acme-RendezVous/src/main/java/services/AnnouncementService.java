
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.AnnouncementRepository;
import security.LoginService;
import domain.Announcement;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class AnnouncementService {

	/* Repositories */

	@Autowired
	private AnnouncementRepository	announcementRepository;

	/* Services */

	@Autowired
	private UserService				userService;


	/* CRUD Methods */

	public Announcement create(final RendezVous rendezVous) {

		final Announcement announcement = new Announcement();
		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(rendezVous);
		Assert.isTrue(rendezVous.getCreator().equals(user));

		announcement.setCreationMoment(new Date());
		announcement.setRendezVous(rendezVous);

		return announcement;
	}

	public Announcement findOne(final int announcementId) {
		return this.announcementRepository.findOne(announcementId);
	}

	public Announcement save(final Announcement announcement) {

		Assert.notNull(announcement);

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.isTrue(announcement.getId() == 0);
		Assert.isTrue(announcement.getRendezVous().getCreator().equals(user));

		announcement.setCreationMoment(new Date());

		return this.announcementRepository.save(announcement);
	}

	public void delete(final Announcement announcement) {

		Assert.notNull(announcement);

		this.announcementRepository.delete(announcement);
	}

	//Other Business Methods

	public Collection<Announcement> findByCurrentChronological() {

		final User user = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(user);

		return this.announcementRepository.findByUserIDChronological(user.getId());
	}

}