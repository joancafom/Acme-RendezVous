
package forms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

public class UserRegisterForm {

	private String	username;
	private String	password;
	private String	repeatedPassword;
	private String	name;
	private String	surnames;
	private String	postalAddress;
	private String	phoneNumber;
	private String	email;
	private Date	dateOfBirth;
	private Boolean	termsAndConditions;


	public Boolean getTermsAndConditions() {
		return this.termsAndConditions;
	}

	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return this.username;
	}

	@Size(min = 5, max = 32)
	public String getPassword() {
		return this.password;
	}

	public String getRepeatedPassword() {
		return this.repeatedPassword;
	}

	@NotBlank
	public String getName() {
		return this.name;
	}

	@NotBlank
	public String getSurnames() {
		return this.surnames;
	}

	public String getPostalAddress() {
		return this.postalAddress;
	}

	@Pattern(regexp = "(^\\+?[0-9]{9,}$)")
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	@NotNull
	@Email
	public String getEmail() {
		return this.email;
	}

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getDateOfBirth() {
		return this.dateOfBirth;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setRepeatedPassword(final String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setSurnames(final String surnames) {
		this.surnames = surnames;
	}

	public void setPostalAddress(final String postalAddress) {
		this.postalAddress = postalAddress;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setTermsAndConditions(final Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

}
