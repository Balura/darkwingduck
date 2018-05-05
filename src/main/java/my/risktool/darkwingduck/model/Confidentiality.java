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
@Table(name="confidentiality")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Confidentiality.findAll", query = "FROM Confidentiality t"),
    @NamedQuery(name = "Confidentiality.findById", query = "FROM Confidentiality t WHERE t.id = :id"),
    @NamedQuery(name = "Confidentiality.findByValue", query = "FROM Confidentiality t WHERE t.confidentiality = :confidentiality")
})
public class Confidentiality implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "confidentiality")
	private String confidentiality;
	
	@OneToMany(mappedBy = "confidentiality_fk", cascade = CascadeType.MERGE)
	private Collection<Application> applicationCollection;
	
	public Confidentiality() {
		
	}
	
	public Confidentiality(Integer id, String confidentiality) {
		this.id = id;
		this.confidentiality = confidentiality;
	}
	
	public Confidentiality(String confidentiality) {
		this.confidentiality = confidentiality;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConfidentiality() {
		return confidentiality;
	}

	public void setConfidentiality(String confidentiality) {
		this.confidentiality = confidentiality;
	}
	
	public Collection<Application> getApplicationCollection() {
		return applicationCollection;
	}

	public void setApplicationCollection(Collection<Application> applicationCollection) {
		this.applicationCollection = applicationCollection;
	}
	
	@Override
	public String toString() {
		return this.confidentiality;
	}
	
}
