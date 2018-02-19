
package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import repositories.RendezVousRepository;
import domain.RendezVous;

@Component
@Transactional
public class StringToRendezVousConverter implements Converter<String, RendezVous> {

	@Autowired
	RendezVousRepository	rendezVousRepository;


	@Override
	public RendezVous convert(final String text) {
		RendezVous result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.rendezVousRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}
}
