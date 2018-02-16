
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class RendezVous extends DomainEntity {

	private String			name;
	private String			description;
	private Date			orgDate;
	private String			picture;
	private GPSCoordinates	coordinates;
	private boolean			isFinal;
	private boolean			isDeleted;
	private boolean			isForAdults;


	@NotBlank
	public String getName() {
		return this.name;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getOrgDate() {
		return this.orgDate;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	@Valid
	public GPSCoordinates getCoordinates() {
		return this.coordinates;
	}

	@NotNull
	public boolean getIsFinal() {
		return this.isFinal;
	}

	@NotNull
	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	@NotNull
	public boolean getIsForAdults() {
		return this.isForAdults;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setOrgDate(final Date orgDate) {
		this.orgDate = orgDate;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setCoordinates(final GPSCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	public void setIsFinal(final boolean isFinal) {
		this.isFinal = isFinal;
	}

	public void setIsDeleted(final boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setIsForAdults(final boolean isForAdults) {
		this.isForAdults = isForAdults;
	}

}
