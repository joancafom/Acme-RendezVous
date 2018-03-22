
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


	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getBusinessName() {
		return this.businessName;
	}

	@NotBlank
	@URL
	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

}
