
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Announcement;

@Component
@Transactional
public class AnnouncementToStringConverter implements Converter<Announcement, String> {

	@Override
	public String convert(final Announcement announcement) {
		final String result;

		if (announcement == null)
			result = null;
		else
			result = String.valueOf(announcement.getId());

		return result;
	}
}
