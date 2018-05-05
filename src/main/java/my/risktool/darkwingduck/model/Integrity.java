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
@Table(name="integrity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Integrity.findAll", query = "FROM Integrity t"),
    @NamedQuery(name = "Integrity.findById", query = "FROM Integrity t WHERE t.id = :id"),
    @NamedQuery(name = "Integrity.findByValue", query = "FROM Integrity t WHERE t.integrity = :integrity")
})
public class Integrity implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "integrity")
	private String integrity;
	
	@OneToMany(mappedBy = "integrity_fk", cascade = CascadeType.MERGE)
	private Collection<Application> applicationCollection;
	
	public Integrity() {
		
	}
	
	public Integrity(Integer id, String integrity) {
		this.id = id;
		this.integrity = integrity;
	}
	
	public Integrity(String integrity) {
		this.integrity = integrity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIntegrity() {
		return integrity;
	}

	public void setIntegrity(String integrity) {
		this.integrity = integrity;
	}

	public Collection<Application> getApplicationCollection() {
		return applicationCollection;
	}

	public void setApplicationCollection(Collection<Application> applicationCollection) {
		this.applicationCollection = applicationCollection;
	}
	
	@Override
	public String toString() {
		return this.integrity;
	}
	
	
}
