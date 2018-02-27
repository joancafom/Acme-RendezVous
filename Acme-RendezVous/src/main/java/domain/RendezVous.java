
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = @Index(columnList = "isFinal"))
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
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
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


	/* Relationships */

	private User						creator;
	private Collection<User>			attendants;
	private Collection<Comment>			comments;
	private Collection<RendezVous>		similarRendezVouses;
	private Collection<Announcement>	announcements;
	private Collection<Question>		questions;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getCreator() {
		return this.creator;
	}

	@NotNull
	@Valid
	@ManyToMany(mappedBy = "attendedRendezVouses")
	public Collection<User> getAttendants() {
		return this.attendants;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rendezVous")
	public Collection<Comment> getComments() {
		return this.comments;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<RendezVous> getSimilarRendezVouses() {
		return this.similarRendezVouses;
	}

	@NotNull
	@Valid
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "rendezVous")
	public Collection<Announcement> getAnnouncements() {
		return this.announcements;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "rendezVous")
	public Collection<Question> getQuestions() {
		return this.questions;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	public void setAttendants(final Collection<User> attendants) {
		this.attendants = attendants;
	}

	public void setComments(final Collection<Comment> comments) {
		this.comments = comments;
	}

	public void setSimilarRendezVouses(final Collection<RendezVous> similarRendezVouses) {
		this.similarRendezVouses = similarRendezVouses;
	}

	public void setAnnouncements(final Collection<Announcement> announcements) {
		this.announcements = announcements;
	}

	public void setQuestions(final Collection<Question> questions) {
		this.questions = questions;
	}

}
