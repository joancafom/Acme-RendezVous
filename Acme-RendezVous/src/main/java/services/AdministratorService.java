
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import repositories.AdministratorRepository;
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
		return this.administratorRepository.avgCreatedRendezVousesPerUser();
	}

	public Double getStdDeviationCreatedRendezVousesPerUser() {
		return this.administratorRepository.stdDeviationCreatedRendezVousesPerUser();
	}

	public Double getUsersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses() {
		return this.administratorRepository.usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses();
	}

	public Double getAvgUsersPerRendezVous() {
		return this.administratorRepository.avgUsersPerRendezVous();
	}

	public Double getStdDeviationUsersPerRendezVous() {
		return this.administratorRepository.stdDeviationUsersPerRendezVous();
	}

	public Double getAvgRSVPPerUser() {
		return this.administratorRepository.avgRSVPPerUser();
	}

	public Double getStdDeviationRSVPPerUser() {
		return this.administratorRepository.stdDeviationRSVPPerUser();
	}

	public Collection<RendezVous> getTopTenMoreRSVP() {
		final Page<RendezVous> rendezVousPage = this.administratorRepository.topMoreRSVP(new PageRequest(0, 10));
		return rendezVousPage.getContent();
	}

	public Double getAvgAnnouncementsPerRendezVous() {
		return this.administratorRepository.avgAnnouncementsPerRendezVous();
	}

	public Double getStdAnnouncementsPerRendezVous() {
		return this.administratorRepository.stdAnnouncementsPerRendezVous();
	}

	public Collection<RendezVous> getRendezVousAbove75AvgAnnouncements() {
		return this.administratorRepository.rendezVousAbove75AvgAnnouncements();
	}

	public Collection<RendezVous> getRendezVousAboveAvgPlus10SimilarRendezVouses() {
		return this.administratorRepository.rendezVousAboveAvgPlus10SimilarRendezVouses();
	}

	public Double getAvgQuestionsPerRendezVous() {
		return this.administratorRepository.avgQuestionsPerRendezVous();
	}

	public Double getStdQuestionsPerRendezVous() {
		return this.administratorRepository.stdQuestionsPerRendezVous();
	}

	public Double getAvgRepliesPerComment() {
		return this.administratorRepository.avgRepliesPerComment();
	}

	public Double getStdRepliesPerComment() {
		return this.administratorRepository.stdRepliesPerComment();
	}

	public Double getAvgAnswersPerRendezVous() {

		final Collection<Long> answerPerRendezVous = this.administratorRepository.answerPerRendezVous();

		Long sum = 0L;

		for (final Long a : answerPerRendezVous)
			sum += a;

		Double res = 0.0;
		final Double count = new Double(this.rendezVousService.findAll().size());

		if (count != 0.0)
			res = sum / count;

		return res;
	}
	public Double getStdAnswersPerRendezVous() {

		final Collection<Long> answerPerRendezVous = this.administratorRepository.answerPerRendezVous();

		Long sumSq = 0L;

		for (final Long a : answerPerRendezVous)
			sumSq += a * a;

		Double res = 0.0;
		final Double count = new Double(this.rendezVousService.findAll().size());

		if (count != 0.0)
			res = Math.sqrt((sumSq / count) - Math.pow(this.getAvgAnswersPerRendezVous(), 2));

		return res;
	}

	public Administrator findByUserAccount(final UserAccount userAccount) {
		return this.administratorRepository.findByUserAccount(userAccount.getId());
	}

	// v1.0 - Implemented by Alicia
	public Collection<domain.Service> bestSellingServices() {
		return this.administratorRepository.bestSellingServices();
	}

	// v1.0 - Implemented by Alicia
	public Collection<Manager> managersMoreServicesThanAverage() {
		return this.administratorRepository.managersMoreServicesThanAverage();
	}

	// v1.0 - Implemented by Alicia
	public Collection<Manager> managersWithMoreServicesCancelled() {
		return this.administratorRepository.managersWithMoreServicesCancelled();
	}

}
