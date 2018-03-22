
package forms;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

public class SystemConfigurationForm {

	private String	businessName;
	private String	bannerURL;
	private String	welcomeMessageES;
	private String	welcomeMessageEN;


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
	@Pattern(regexp = "^((?![\\=\\|]).)*$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageES() {
		return this.welcomeMessageES;
	}

	@NotBlank
	@Pattern(regexp = "^((?![\\=\\|]).)*$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getWelcomeMessageEN() {
		return this.welcomeMessageEN;
	}

	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

	public void setWelcomeMessageES(final String welcomeMessageES) {
		this.welcomeMessageES = welcomeMessageES;
	}

	public void setWelcomeMessageEN(final String welcomeMessageEN) {
		this.welcomeMessageEN = welcomeMessageEN;
	}

}
