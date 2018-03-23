
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.LoginService;
import security.UserAccount;
import domain.Administrator;
import domain.Manager;
import domain.RendezVous;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository -------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	@Autowired
	private RendezVousService		rendezVousService;


	// Other Business Process ---------------------------

	public Double getAvgCreatedRendezVousesPerUser() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgCreatedRendezVousesPerUser();
	}

	public Double getStdDeviationCreatedRendezVousesPerUser() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.stdDeviationCreatedRendezVousesPerUser();
	}

	public Double getUsersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses();
	}

	public Double getAvgUsersPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgUsersPerRendezVous();
	}

	public Double getStdDeviationUsersPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.stdDeviationUsersPerRendezVous();
	}

	public Double getAvgRSVPPerUser() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgRSVPPerUser();
	}

	public Double getStdDeviationRSVPPerUser() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.stdDeviationRSVPPerUser();
	}

	public Collection<RendezVous> getTopTenMoreRSVP() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		final Page<RendezVous> rendezVousPage = this.administratorRepository.topMoreRSVP(new PageRequest(0, 10));
		return rendezVousPage.getContent();
	}

	public Double getAvgAnnouncementsPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgAnnouncementsPerRendezVous();
	}

	public Double getStdAnnouncementsPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.stdAnnouncementsPerRendezVous();
	}

	public Collection<RendezVous> getRendezVousAbove75AvgAnnouncements() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.rendezVousAbove75AvgAnnouncements();
	}

	public Collection<RendezVous> getRendezVousAboveAvgPlus10SimilarRendezVouses() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.rendezVousAboveAvgPlus10SimilarRendezVouses();
	}

	public Double getAvgQuestionsPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgQuestionsPerRendezVous();
	}

	public Double getStdQuestionsPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.stdQuestionsPerRendezVous();
	}

	public Double getAvgRepliesPerComment() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgRepliesPerComment();
	}

	public Double getStdRepliesPerComment() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);
		return this.administratorRepository.stdRepliesPerComment();
	}

	public Double getAvgAnswersPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		return this.administratorRepository.avgAnswerPerRendezVous();
	}
	public Double getStdAnswersPerRendezVous() {

		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(admin);

		final Collection<Long> answerPerRendezVous = this.administratorRepository.answerPerRendezVous();

		Long sumSq = 0L;

		for (final Long a : answerPerRendezVous)
			sumSq += a * a;

		Double res = 0.0;
		final Double count = new Double(this.rendezVousService.count());

		if (count != 0.0)
			res = Math.sqrt((sumSq / count) - Math.pow(this.getAvgAnswersPerRendezVous(), 2));

		return res;
	}

	public Administrator findByUserAccount(final UserAccount userAccount) {
		return this.administratorRepository.findByUserAccount(userAccount.getId());
	}

	// v3.0 - Implemented by Alicia
	public Collection<domain.Service> bestSellingServices() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<domain.Service> servicePage = this.administratorRepository.bestSellingServices(new PageRequest(0, 5));
		return servicePage.getContent();
	}

	// v2.0 - Implemented by Alicia
	public Collection<Manager> managersMoreServicesThanAverage() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.managersMoreServicesThanAverage();
	}

	// v3.0 - Implemented by Alicia
	public Collection<Manager> managersWithMoreServicesCancelled() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<Manager> managerPage = this.administratorRepository.managersWithMoreServicesCancelled(new PageRequest(0, 5));
		return managerPage.getContent();
	}

	// Acme-RendezVous 2.0 - Level B ------------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	public Double getAvgCategoriesPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgCategoriesPerRendezVous();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgRatioServicesPerCategory() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgRatioServicesPerCategory();
	}

	// v1.0 - Implemented by Alicia
	public Double getAvgServicesRequestedPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.avgServicesRequestedPerRendezVous();
	}

	// v1.0 - Implemented by Alicia
	public Double getMinServicesRequestedPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.minServicesRequestedPerRendezVous();
	}

	// v1.0 - Implemented by Alicia
	public Double getMaxServicesRequestedPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.maxServicesRequestedPerRendezVous();
	}

	// v1.0 - Implemented by Alicia
	public Double getStdServicesRequestedPerRendezVous() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		return this.administratorRepository.stdServicesRequestedPerRendezVous();
	}

	// v3.0 - Implemented by Alicia
	public Collection<domain.Service> topSellingServices() {
		final Administrator admin = this.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(admin);

		final Page<domain.Service> servicePage = this.administratorRepository.topSellingServices(new PageRequest(0, 5));
		return servicePage.getContent();
	}

}
