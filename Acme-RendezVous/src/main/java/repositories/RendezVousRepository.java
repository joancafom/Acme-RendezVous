
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.RendezVous;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Integer> {

}
