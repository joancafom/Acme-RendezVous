
package forms;

public class ManagerRegisterForm {

	/* V1.0 - josembell */

	private String	username;
	private String	password;
	private String	repeatedPassword;
	private String	name;
	private String	surnames;
	private String	postalAddress;
	private String	phoneNumber;
	private String	email;
	private String	vat;
	private Boolean	termsAndConditions;


	public Boolean getTermsAndConditions() {
		return this.termsAndConditions;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getRepeatedPassword() {
		return this.repeatedPassword;
	}

	public String getName() {
		return this.name;
	}

	public String getSurnames() {
		return this.surnames;
	}

	public String getPostalAddress() {
		return this.postalAddress;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public String getVat() {
		return this.vat;
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

	public void setVat(final String vat) {
		this.vat = vat;
	}

	public void setTermsAndConditions(final Boolean termsAndConditions) {
		this.termsAndConditions = termsAndConditions;
	}

}
