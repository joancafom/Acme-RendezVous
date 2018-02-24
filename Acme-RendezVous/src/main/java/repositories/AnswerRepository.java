
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

	@Query("select a from Answer a where a.user.id = ?2 and a.question.rendezVous.id = ?1")
	Collection<Answer> findAllByRendezVousAndUserIds(int rendezVousId, int userId);

}
