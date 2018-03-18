
package services;

import java.util.Collection;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.ServiceRequestRepository;
import security.LoginService;
import domain.Administrator;
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

	@Autowired
	private AdministratorService		administratorService;


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
		// v2.0 - Changes by Alicia

		final User currentUser = this.userService.findByUserAccount(LoginService.getPrincipal());
		final LocalDate now = new LocalDate();

		Assert.notNull(newServiceRequest);
		Assert.notNull(currentUser);

		// Make sure that the requested service isn't canceled
		Assert.isTrue(!newServiceRequest.getService().getIsCanceled());

		//We cannot edit ServiceRequests
		Assert.isTrue(newServiceRequest.getId() == 0);
		Assert.notNull(newServiceRequest.getRendezVous());
		Assert.isTrue(currentUser.equals(newServiceRequest.getRendezVous().getCreator()));

		//Ensure the creditCard's not expired
		Assert.isTrue((now.getYear() == newServiceRequest.getCreditCard().getYear() && now.getMonthOfYear() == newServiceRequest.getCreditCard().getMonth()) || (now.getYear() < newServiceRequest.getCreditCard().getYear())
			|| (now.getYear() == newServiceRequest.getCreditCard().getYear() && now.getMonthOfYear() < newServiceRequest.getCreditCard().getMonth()));

		//We must make sure that the RendezVous does not have that Service already
		final Boolean res = this.serviceService.checkRendezVousUsingService(newServiceRequest.getRendezVous(), newServiceRequest.getService());
		Assert.isTrue(!res);

		return this.serviceRequestRepository.save(newServiceRequest);

	}

	public void delete(final ServiceRequest serviceRequest) {

		// v1.0 - Implemented by JA
		// v2.0 - Changes by Alicia

		Assert.notNull(serviceRequest);

		if (LoginService.getPrincipal().getId() != serviceRequest.getRendezVous().getCreator().getUserAccount().getId()) {
			final Administrator administrator = this.administratorService.findByUserAccount(LoginService.getPrincipal());
			Assert.notNull(administrator);
		}

		this.serviceRequestRepository.delete(serviceRequest);

	}
	/* v1.0 - josembell */
	public void flush() {
		this.serviceRequestRepository.flush();

	}
}
