
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class Comment extends DomainEntity {

	private String	text;
	private Date	writeMoment;
	private String	picture;


	@NotBlank
	public String getText() {
		return this.text;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getWriteMoment() {
		return this.writeMoment;
	}

	@URL
	public String getPicture() {
		return this.picture;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public void setWriteMoment(final Date writeMoment) {
		this.writeMoment = writeMoment;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}


	/* Relationships */

	private User				user;
	private RendezVous			rendezVous;
	private Collection<Comment>	replies;
	private Comment				parentComment;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public RendezVous getRendezVous() {
		return this.rendezVous;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "parentComment")
	public Collection<Comment> getReplies() {
		return this.replies;
	}

	@Valid
	@ManyToOne(optional = true)
	public Comment getParentComment() {
		return this.parentComment;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setRendezVous(final RendezVous rendezVous) {
		this.rendezVous = rendezVous;
	}

	public void setReplies(final Collection<Comment> replies) {
		this.replies = replies;
	}

	public void setParentComment(final Comment parentComment) {
		this.parentComment = parentComment;
	}

}
