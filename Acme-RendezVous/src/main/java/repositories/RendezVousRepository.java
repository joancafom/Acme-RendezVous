
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RendezVous;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {

	@Query("select r from RendezVous r where r.isForAdults = false")
	Collection<RendezVous> findAllNotAdult();

	@Query("select r from RendezVous r where r.creator.id !=?1")
	Collection<RendezVous> findAllExceptCreatedByUser(int id);

	@Query("select r from RendezVous r where r.isForAdults=false and r.creator.id!=?1")
	Collection<RendezVous> findAllNotAdultExceptCreatedByUser(int id);

	@Query("select r from RendezVous r where r.creator.id = ?1")
	Collection<RendezVous> findAllByUserId(int userId);

	/* v1.1 - josembell */
	@Query("select sr.rendezVous from Category c join c.services s join s.serviceRequests sr where c.id=?1")
	Collection<RendezVous> findAllByCategory(int categoryId);

	/* v1.0 - josembell */
	@Query("select sr.rendezVous from Category c join c.services s join s.serviceRequests sr where c.id=?1 and sr.rendezVous.isForAdults = false")
	Collection<RendezVous> findAllNotAdultByCategory(int categoryId);

}
