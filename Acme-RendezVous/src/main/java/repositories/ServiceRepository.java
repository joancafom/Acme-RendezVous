
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.RendezVous;
import domain.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

	//v2.0 - Implemented by JA
	@Query("select sr.rendezVous from ServiceRequest sr where sr.rendezVous.creator.id = ?1 and sr.service.id = ?2")
	Collection<RendezVous> rendezVousesCreatorIdUsingServiceId(final int creatorId, final int serviceId);

	//v1.0 - Implemented by JA
	@Query("select sr.rendezVous from ServiceRequest sr where sr.rendezVous.id = ?1 and sr.service.id = ?2")
	RendezVous checkRendezVousIdUsingServiceId(final int rendezVousId, final int serviceId);

	//v1.0 - Implemented by JA
	@Query("select sr.service from ServiceRequest sr where sr.rendezVous.id = ?1")
	Collection<domain.Service> servicesByRendezVousId(final int rendezVousId);

}
