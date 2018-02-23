
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.RendezVous;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select avg(u.createdRendezVouses.size) from User u")
	Double avgCreatedRendezVousesPerUser();

	@Query("select sqrt(sum(u.createdRendezVouses.size * u.createdRendezVouses.size) / count(u.createdRendezVouses.size) - avg(u.createdRendezVouses.size) * avg(u.createdRendezVouses.size)) from User u")
	Double stdDeviationCreatedRendezVousesPerUser();

	@Query("select (select count(u1) * 1.0 from User u1 where u1.createdRendezVouses.size > 0) / count(u2) * 1.0 from User u2 where u2.createdRendezVouses.size = 0")
	Double usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses();

	@Query("select avg(rv.attendants.size) from RendezVous rv")
	Double avgUsersPerRendezVous();

	@Query("select sqrt(sum(rv.attendants.size * rv.attendants.size) / count(rv.attendants.size) - avg(rv.attendants.size) * avg(rv.attendants.size)) from RendezVous rv")
	Double stdDeviationUsersPerRendezVous();

	@Query("select avg(u.attendedRendezVouses.size) from User u")
	Double avgRSVPPerUser();

	@Query("select sqrt(sum(u.attendedRendezVouses.size * u.attendedRendezVouses.size) / count(u.attendedRendezVouses.size) - avg(u.attendedRendezVouses.size) * avg(u.attendedRendezVouses.size)) from User u")
	Double stdDeviationRSVPPerUser();

	@Query(value = "select rv from RendezVous rv order by rv.attendants.size desc")
	Page<RendezVous> topMoreRSVP(Pageable pageable);

	@Query("select avg(rv.announcements.size) from RendezVous rv")
	Double avgAnnouncementsPerRendezVous();

	@Query("select sqrt(sum(rv.announcements.size * rv.announcements.size) / count(rv.announcements.size) - avg(rv.announcements.size) * avg(rv.announcements.size)) from RendezVous rv")
	Double stdAnnouncementsPerRendezVous();

	@Query("select rv1 from RendezVous rv1 where rv1.announcements.size > (select avg(rv2.announcements.size) from RendezVous rv2) * 0.75")
	Collection<RendezVous> rendezVousAbove75AvgAnnouncements();

	@Query("select rv1 from RendezVous rv1 where rv1.similarRendezVouses.size > (select avg(rv2.similarRendezVouses.size + (select avg(rv3.similarRendezVouses.size) * 0.1 from RendezVous rv3)) from RendezVous rv2)")
	Collection<RendezVous> rendezVousAboveAvgPlus10SimilarRendezVouses();

	@Query("select a from Administrator a where a.userAccount.id=?1")
	Administrator findByUserAccount(int userAccountId);

}
