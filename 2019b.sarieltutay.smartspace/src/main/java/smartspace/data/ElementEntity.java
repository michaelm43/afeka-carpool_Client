package smartspace.data;

import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import smartspace.dao.rdb.MapToJsonConverter;

@Entity
@Table(name="ELEMENTS")
public class ElementEntity implements SmartspaceEntity<String> {

	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name;
	private String type;
	private Date creationTimestamp;
	private boolean expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Map<String, Object> moreAttributes;

	public ElementEntity() {
		//to delete
		this.creationTimestamp = new Date();
		//to delete
		this.location = new Location();
		
	}
	
	//to delete
	public ElementEntity(String name) {
		this();
		this.name = name;	
	}
	
	public ElementEntity( Location location, String name, String type,
			Date creationTimestamp, boolean expired, String creatorSmartspace, String creatorEmail,
			Map<String, Object> moreAttributes) {
		super();
		this.location = location;
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationTimestamp;
		this.expired = expired;
		this.creatorSmartspace = creatorSmartspace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
	}

	@Transient
	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	@Transient
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	@Embedded
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	@Lob
	@Convert(converter=MapToJsonConverter.class)
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	@Id
	@Column(name="ID")
	public String getKey() {
		return this.elementSmartspace + "=" +  this.elementId;
	}

	@Override
	public void setKey(String key) {
		String[] split = key.split("=");
		if(split!=null & split.length==2) {
			this.elementSmartspace = split[0];
			this.elementId = split[1];	
		}
	}

	@Override
	public String toString() {
		return "ElementEntity [elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", creationTimestamp=" + creationTimestamp
				+ ", expired=" + expired + ", creatorSmartspace=" + creatorSmartspace + ", creatorEmail=" + creatorEmail
				+ ", moreAttributes=" + moreAttributes + "]";
	}
	
	

}
