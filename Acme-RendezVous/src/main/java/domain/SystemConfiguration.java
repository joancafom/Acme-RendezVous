
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfiguration extends DomainEntity {

	//Properties

	private String	businessName;
	private String	bannerURL;


	@NotBlank
	public String getBusinessName() {
		return this.businessName;
	}

	@URL
	public void setBusinessName(final String businessName) {
		this.businessName = businessName;
	}

	public String getBannerURL() {
		return this.bannerURL;
	}

	public void setBannerURL(final String bannerURL) {
		this.bannerURL = bannerURL;
	}

}
