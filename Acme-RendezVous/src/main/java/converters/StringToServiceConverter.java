
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.ServiceRepository;
import domain.Service;

@Component
@Transactional
public class StringToServiceConverter implements Converter<String, Service> {

	@Autowired
	ServiceRepository	serviceRepository;


	@Override
	public Service convert(final String text) {
		final Service result;
		final int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.serviceRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}
		return result;
	}
}
