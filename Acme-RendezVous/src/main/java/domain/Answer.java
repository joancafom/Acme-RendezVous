
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Answer extends DomainEntity {

	private String	answer;


	@NotBlank
	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(final String answer) {
		this.answer = answer;
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
