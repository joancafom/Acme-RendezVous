
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import repositories.ServiceRepository;

@Service
@Transactional
public class ServiceService {

	/* Repositories */

	@Autowired
	private ServiceRepository	serviceRepository;


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

}
