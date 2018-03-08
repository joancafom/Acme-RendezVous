
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRequestRepository;
import security.LoginService;
import domain.ServiceRequest;
import domain.User;

@Service
@Transactional
public class ServiceRequestService {

	/* Repositories */

	@Autowired
	private ServiceRequestRepository	serviceRequestRepository;

	/* Supporting Services */

	@Autowired
	private UserService					userService;

	@Autowired
	private ServiceService				serviceService;


	/* Business Methods */

	public ServiceRequest create(final domain.Service service) {

		// v1.0 - Implemented by JA

		//In the controller we will be able to select which Service we want to apply for
		//and once decided, we will choose what RendezVous is requesting that Service

		final ServiceRequest res = new ServiceRequest();

		Assert.notNull(service);

		res.setService(service);

		return res;

	}
	public ServiceRequest findOne(final int serviceRequestId) {

		// v1.0 - Implemented by JA

		return this.serviceRequestRepository.findOne(serviceRequestId);
	}

	public Collection<ServiceRequest> findAll() {

		// v1.0 - Implemented by JA

		return this.serviceRequestRepository.findAll();
	}

	public ServiceRequest save(final ServiceRequest newServiceRequest) {

		// v1.0 - Implemented by JA

		final User currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());

		Assert.notNull(newServiceRequest);
		Assert.notNull(currentUser);

		//We cannot edit ServiceRequests
		Assert.isTrue(newServiceRequest.getId() == 0);
		Assert.isTrue(currentUser.equals(newServiceRequest.getRendezVous().getCreator()));

		//We must make sure that the RendezVous does not have that Service already
		Assert.isTrue(!this.serviceService.checkRendezVousUsingService(newServiceRequest.getRendezVous(), newServiceRequest.getService()));

		return this.serviceRequestRepository.save(newServiceRequest);

	}

}
