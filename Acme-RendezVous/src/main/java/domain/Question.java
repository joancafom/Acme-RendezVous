
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Question extends DomainEntity {

	private String	text;


	@NotBlank
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}


	/* Relationships */

	private RendezVous			rendezVous;
	private Collection<Answer>	answers;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public RendezVous getRendezVous() {
		return this.rendezVous;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "question")
	public Collection<Answer> getAnswers() {
		return this.answers;
	}

	public void setRendezVous(final RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

	public void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;
	}

}
