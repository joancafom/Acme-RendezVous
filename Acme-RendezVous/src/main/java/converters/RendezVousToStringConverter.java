
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.RendezVous;

@Component
@Transactional
public class RendezVousToStringConverter implements Converter<RendezVous, String> {

	@Override
	public String convert(final RendezVous rendezVous) {
		String result;

		if (rendezVous == null)
			result = null;
		else
			result = String.valueOf(rendezVous.getId());

		return result;
	}

}
