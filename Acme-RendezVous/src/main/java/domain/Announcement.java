
package domain;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Announcement extends DomainEntity {

	private Date	creationMoment;
	private String	title;
	private String	description;


	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getCreationMoment() {
		return this.creationMoment;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setCreationMoment(final Date creationMoment) {
		this.creationMoment = creationMoment;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setDescription(final String description) {
		this.description = description;
	}


	/* Relationships */

	private RendezVous	rendezVous;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public RendezVous getRendezVous() {
		return this.rendezVous;
	}

	public void setRendezVous(final RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

}
