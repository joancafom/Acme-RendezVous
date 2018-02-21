
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select avg(u.createdRendezVouses.size) from User u")
	Double avgCreatedRendezVousesPerUser();

	@Query("select sqrt(sum(u.createdRendezVouses.size * u.createdRendezVouses.size) / count(u.createdRendezVouses.size) - avg(u.createdRendezVouses.size) * avg(u.createdRendezVouses.size)) from User u")
	Double stdDeviationCreatedRendezVousesPerUser();

	@Query("select (select count(u1) * 1.0 from User u1 where u1.createdRendezVouses.size > 0) / count(u2) * 1.0 from User u2 where u2.createdRendezVouses.size = 0")
	Double usersWithCreatedRendezVousesVSUsersWithoutCreatedRendezVouses();

}
