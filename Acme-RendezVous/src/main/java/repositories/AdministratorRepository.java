
package repositories;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Manager;
import domain.RendezVous;
import domain.Service;

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

	@Query("select avg(rv.questions.size) from RendezVous rv")
	Double avgQuestionsPerRendezVous();

	@Query("select sqrt(sum(rv.questions.size * rv.questions.size) / count(rv.questions.size) - avg(rv.questions.size) * avg(rv.questions.size)) from RendezVous rv")
	Double stdQuestionsPerRendezVous();

	@Query("select avg(c.replies.size) from Comment c")
	Double avgRepliesPerComment();

	@Query("select sqrt(sum(c.replies.size * c.replies.size) / count(c.replies.size) - avg(c.replies.size) * avg(c.replies.size)) from Comment c")
	Double stdRepliesPerComment();

	// v1.0 - Implemented by JA
	@Query("select count(a)*1.0 / (select count(rv)*1.0 from RendezVous rv) from Answer a")
	Double avgAnswerPerRendezVous();

	@Query("select sum(q.answers.size) from Question q group by q.rendezVous")
	Collection<Long> answerPerRendezVous();

	// v2.0 - Implemented by Alicia
	@Query(value = "select s from Service s where s.serviceRequests.size > 0 order by s.serviceRequests.size desc")
	Page<Service> bestSellingServices(Pageable pageable);

	// v1.0 - Implemented by Alicia
	@Query("select m1 from Manager m1 where m1.services.size > (select avg(m2.services.size) from Manager m2)")
	Collection<Manager> managersMoreServicesThanAverage();

	// v2.0 - Implemented by Alicia
	@Query(value = "select s.manager from Service s where s.isCanceled = true group by s.manager order by count(s) desc")
	Page<Manager> managersWithMoreServicesCancelled(Pageable pageable);

	// Acme-RendezVous 2.0 - Level B ------------------------------------------------------------
	// v1.0 - Implemented by Alicia
	@Query("select sum(sr.service.categories.size) * 1.0 / (select count(r) * 1.0 from RendezVous r) from ServiceRequest sr")
	Double avgCategoriesPerRendezVous();

	// v1.0 - Implemented by Alicia
	@Query("select avg(c.services.size * 1.0 / (select count(s) from Service s)) from Category c")
	Double avgRatioServicesPerCategory();

	// v1.0 - Implemented by Alicia
	@Query("select avg(r.serviceRequests.size) from RendezVous r")
	Double avgServicesRequestedPerRendezVous();

	// v1.0 - Implemented by Alicia
	@Query("select min(r.serviceRequests.size) from RendezVous r")
	Double minServicesRequestedPerRendezVous();

	// v1.0 - Implemented by Alicia
	@Query("select max(r.serviceRequests.size) from RendezVous r")
	Double maxServicesRequestedPerRendezVous();

	// v1.0 - Implemented by Alicia
	@Query("select sqrt(sum(r.serviceRequests.size * r.serviceRequests.size) / count(r.serviceRequests.size) - avg(r.serviceRequests.size) * avg(r.serviceRequests.size)) from RendezVous r")
	Double stdServicesRequestedPerRendezVous();
}
