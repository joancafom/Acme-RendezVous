
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ServiceRepository;
import security.LoginService;
import domain.Manager;
import domain.RendezVous;
import domain.ServiceRequest;
import domain.User;

@Service
@Transactional
public class ServiceService {

	/* Repositories */

	@Autowired
	private ServiceRepository		serviceRepository;

	/* Supporting Services */

	@Autowired
	private UserService				userService;

	@Autowired
	private RendezVousService		rendezVousService;

	@Autowired
	private ServiceRequestService	serviceRequestService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private Validator				validator;


	/* Business Methods */

	public domain.Service create() {

		/* v1.0 - josembell */

		final domain.Service res = new domain.Service();

		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(manager);

		final Collection<ServiceRequest> serviceRequests = new HashSet<ServiceRequest>();

		res.setManager(manager);
		res.setServiceRequests(serviceRequests);
		res.setIsCanceled(false);

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
		/* v1.0 - josembell */
		// v2.0 - Modified by JA

		Assert.notNull(newService);

		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(manager);

		if (newService.getId() != 0)
			Assert.isTrue(manager.getServices().contains(newService));
		else
			Assert.isTrue(!newService.getIsCanceled());

		Assert.isTrue(newService.getManager().equals(manager));

		final domain.Service savedService = this.serviceRepository.save(newService);
		Assert.notNull(savedService);

		manager.getServices().add(savedService);

		return savedService;
	}

	public void delete(final domain.Service service) {
		/* v1.0 - josembell */
		final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
		Assert.notNull(manager);
		Assert.isTrue(manager.getServices().contains(service));
		Assert.isTrue(service.getManager().equals(manager));
		Assert.isTrue(service.getServiceRequests().isEmpty());

		this.serviceRepository.delete(service);
	}

	public void flush() {
		// v1.0 - Implemented by JA
		this.serviceRepository.flush();
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

	public boolean checkRendezVousUsingService(final RendezVous rendezVous, final domain.Service service) {

		// v1.0 - Implemented by JA

		boolean res = true;

		Assert.notNull(rendezVous);
		Assert.notNull(service);

		//If we obtain a rendezVous is because we are using that service!
		res = this.serviceRepository.checkRendezVousIdUsingServiceId(rendezVous.getId(), service.getId()) != null;

		return res;

	}

	public void cancel(final domain.Service service) {

		// v2.0 - Implemented by Alicia

		Assert.isTrue(!service.getIsCanceled());

		for (final ServiceRequest sr : service.getServiceRequests())
			this.serviceRequestService.delete(sr);

		service.setIsCanceled(true);

		this.save(service);

	}

	/* V1.0 - josembell */
	public domain.Service reconstruct(final domain.Service prunedService, final BindingResult binding) {
		final domain.Service result;
		if (prunedService.getId() == 0) {
			result = prunedService;
			final Manager manager = this.managerService.findByUserAccount(LoginService.getPrincipal());
			result.setManager(manager);
			result.setServiceRequests(new HashSet<ServiceRequest>());
			this.validator.validate(result, binding);
		}

		else {
			final domain.Service savedService = this.findOne(prunedService.getId());
			Assert.notNull(savedService);
			result = prunedService;

			result.setIsCanceled(savedService.getIsCanceled());
			result.setServiceRequests(savedService.getServiceRequests());
			result.setManager(savedService.getManager());

			this.validator.validate(result, binding);
		}

		return result;

	}

}
