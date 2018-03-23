
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = @Index(columnList = "isCanceled"))
public class Service extends DomainEntity {

	//Properties

	private String	name;
	private String	description;
	private String	picture;
	private boolean	isCanceled;


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

	@URL
	public String getPicture() {
		return this.picture;
	}

	public boolean getIsCanceled() {
		return this.isCanceled;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	public void setIsCanceled(final boolean isCanceled) {
		this.isCanceled = isCanceled;
	}


	//Relationships

	private Manager						manager;
	private Collection<ServiceRequest>	serviceRequests;
	private Collection<Category>		categories;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	@NotNull
	@Valid
	@OneToMany(mappedBy = "service")
	public Collection<ServiceRequest> getServiceRequests() {
		return this.serviceRequests;
	}

	@NotNull
	@Valid
	@ManyToMany
	public Collection<Category> getCategories() {
		return this.categories;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	public void setServiceRequests(final Collection<ServiceRequest> serviceRequests) {
		this.serviceRequests = serviceRequests;
	}

	public void setCategories(final Collection<Category> categories) {
		this.categories = categories;
	}
}
