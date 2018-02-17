
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	private Date	dateOfBirth;


	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}


	/* Relationships */

	private Collection<Answer>			answers;
	private Collection<Question>		questions;
	private Collection<RendezVous>		createdRendezVouses;
	private Collection<RendezVous>		attendedRendezVouses;
	private Collection<Comment>			comments;
	private Collection<Announcement>	announcements;


	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<Answer> getAnswers() {
		return this.answers;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<Question> getQuestions() {
		return this.questions;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<RendezVous> getCreatedRendezVouses() {
		return this.createdRendezVouses;
	}

	@NotNull
	@Valid
	@ManyToMany()
	public Collection<RendezVous> getAttendedRendezVouses() {
		return this.attendedRendezVouses;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "user")
	public Collection<Announcement> getAnnouncements() {
		return this.announcements;
	}

	public void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;
	}

	public void setQuestions(final Collection<Question> questions) {
		this.questions = questions;
	}

	public void setCreatedRendezVouses(final Collection<RendezVous> createdRendezVouses) {
		this.createdRendezVouses = createdRendezVouses;
	}

	public void setAttendedRendezVouses(final Collection<RendezVous> attendedRendezVouses) {
		this.attendedRendezVouses = attendedRendezVouses;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	public void setAnnouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;
	}

}
