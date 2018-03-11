
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {

	@Query("select a from User u inner join u.attendedRendezVouses r inner join r.announcements a where u.id = ?1 order by a.creationMoment desc")
	List<Announcement> findByUserIDChronological(int userId);

}
