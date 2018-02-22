
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Access(AccessType.PROPERTY)
public class GPSCoordinates {

	private Double	latitude;
	private Double	longitude;


	@NotNull
	public Double getLatitude() {
		return this.latitude;
	}

	@NotNull
	public Double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

}
