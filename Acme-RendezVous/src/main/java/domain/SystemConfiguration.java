
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	//Properties

	private String	businessName;
	private String	bannerURL;
	private String	welcomeMessages;


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBusinessName() {
		return this.businessName;
	}

	@NotBlank
	@URL
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBannerURL() {
		return this.bannerURL;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessages() {
		return this.welcomeMessages;
	}

	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	public void setWelcomeMessages(final String welcomeMessages) {
		this.welcomeMessages = welcomeMessages;
	}

}
