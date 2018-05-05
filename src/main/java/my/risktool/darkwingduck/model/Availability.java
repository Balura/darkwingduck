package my.risktool.darkwingduck.model;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author Johnny
 *
 *TODO 
 *Database optimization for standard value; only yes or no are allowed, so not really necessary
 */

@SuppressWarnings("serial")
@Entity
@Table(name="availability")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Availability.findAll", query = "FROM Availability t"),
    @NamedQuery(name = "Availability.findById", query = "FROM Availability t WHERE t.id = :id"),
    @NamedQuery(name = "Availability.findByValue", query = "FROM Availability t WHERE t.availability = :availability")
})
public class Availability implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "availability")
	private String availability;
	
	@OneToMany(mappedBy = "availability_fk", cascade = CascadeType.MERGE)
	private Collection<Application> applicationCollection;
	
	public Availability() {
		
	}
	
	public Availability(Integer id, String availability) {
		this.id = id;
		this.availability = availability;
	}
	
	public Availability(String availability) {
		this.availability = availability;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}
	
	public Collection<Application> getApplicationCollection() {
		return applicationCollection;
	}

	public void setApplicationCollection(Collection<Application> applicationCollection) {
		this.applicationCollection = applicationCollection;
	}
	
	@Override
	public String toString() {
		return this.availability;
	}
	
}
