
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import repositories.AdministratorRepository;
import domain.RendezVous;

@Service
@Transactional
public class AdministratorService {

	// Managed Repository -------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;


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
}