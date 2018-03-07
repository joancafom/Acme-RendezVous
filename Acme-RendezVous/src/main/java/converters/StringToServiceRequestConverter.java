
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ServiceRequestRepository;
import domain.ServiceRequest;

@Component
@Transactional
public class StringToServiceRequestConverter implements Converter<String, ServiceRequest> {

	@Autowired
	ServiceRequestRepository	serviceRequestRepository;


	@Override
	public ServiceRequest convert(final String text) {
		final ServiceRequest result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.serviceRequestRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
