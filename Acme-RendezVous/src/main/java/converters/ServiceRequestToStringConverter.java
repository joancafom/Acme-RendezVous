
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.ServiceRequest;

@Component
@Transactional
public class ServiceRequestToStringConverter implements Converter<ServiceRequest, String> {

	@Override
	public String convert(final ServiceRequest serviceRequest) {
		String result;

		if (serviceRequest == null)
			result = null;
		else
			result = String.valueOf(serviceRequest.getId());
		return result;
	}

}
