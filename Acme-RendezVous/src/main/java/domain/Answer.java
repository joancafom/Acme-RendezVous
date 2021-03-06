
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Answer extends DomainEntity {

	private String	text;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getText() {
		return this.text;
	}

	public void setText(final String text) {
		this.text = text;
	}


	/* Relationships */

	private User		user;
	private Question	question;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public User getUser() {
		return this.user;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Question getQuestion() {
		return this.question;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public void setQuestion(final Question question) {
		this.question = question;
	}

}
