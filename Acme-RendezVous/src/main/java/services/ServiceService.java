
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRepository;
import security.LoginService;
import domain.RendezVous;
import domain.User;

@Service
@Transactional
public class ServiceService {

	/* Repositories */

	@Autowired
	private ServiceRepository	serviceRepository;

	/* Supporting Services */

	@Autowired
	private UserService			userService;

	@Autowired
	private RendezVousService	rendezVousService;


	/* Business Methods */

	public domain.Service create() {

		//TODO: Implement this method correctly
		//Responsible: Bellido (Req 5.2)

		final domain.Service res = null;

		return res;

	}

	public domain.Service findOne(final int serviceId) {

		// v1.0 - Implemented by JA

		return this.serviceRepository.findOne(serviceId);
	}

	public Collection<domain.Service> findAll() {

		// v1.0 - Implemented by JA

		return this.serviceRepository.findAll();
	}

	public domain.Service save(final domain.Service newService) {

		//TODO: Implement this method correctly

		return this.serviceRepository.save(newService);

	}

	public void delete(final domain.Service service) {

		//TODO: Implement this method correctly
		//Responsible: Bellido (req 5.2)

		this.serviceRepository.delete(service);
	}

	/* Other Business methods */

	public Collection<RendezVous> getRendezVousesCreatedNotUsingService(final domain.Service service) {

		// v2.0 - Implemented by JA

		final Collection<RendezVous> res;

		final User creator = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(creator);
		Assert.notNull(service);

		res = new HashSet<RendezVous>(this.rendezVousService.findAllByUser(creator));

		res.removeAll(this.serviceRepository.rendezVousesCreatorIdUsingServiceId(creator.getId(), service.getId()));

		Assert.notNull(res);

		return res;

	}

	public Collection<domain.Service> getServicesUsedByRendezVous(final RendezVous rendezVous) {

		// v1.0 - Implemented by JA

		final Collection<domain.Service> res;

		Assert.notNull(rendezVous);

		res = this.serviceRepository.servicesByRendezVousId(rendezVous.getId());

		Assert.notNull(res);

		return res;

	}
	public boolean checkRendezVousUsingService(final RendezVous rendezVous, final domain.Service service) {

		// v1.0 - Implemented by JA

		boolean res = true;

		Assert.notNull(rendezVous);
		Assert.notNull(service);

		//If we obtain a rendezVous is because we are using that service!
		res = this.serviceRepository.checkRendezVousIdUsingServiceId(rendezVous.getId(), service.getId()) != null;

		return res;

	}
}
