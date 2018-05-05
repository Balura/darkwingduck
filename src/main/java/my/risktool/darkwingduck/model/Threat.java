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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

@Entity
@Table(name="threats")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Threat.findAll", query = "FROM Threat t"),
    @NamedQuery(name = "Threat.findById", query = "FROM Threat t WHERE t.id = :id"),
    @NamedQuery(name = "Threat.findByName", query = "FROM Threat t WHERE t.threat = :threat"),
    @NamedQuery(name = "Threat.filterByThreat", query = "FROM Threat t WHERE LOWER(t.threat) LIKE LOWER(:threat)")})
public class Threat implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "threat")
	private String threat;
	
	@Column(name = "confidentiality")
	private Boolean confidentiality;
	
	@Column(name = "integrity")
	private Boolean integrity;
	
	@NotNull
	@Column(name = "availability")
	private Boolean availability;
	
	@OneToMany(mappedBy = "threat_fk", cascade = CascadeType.MERGE)
	private Collection<Measure> measureCollection;
	
	@ManyToOne
	@JoinColumn(name = "threat_category_id", referencedColumnName = "id")
	private ThreatCategory threatCategory_fk;
	
	public Threat() {
		this.confidentiality = true;
		this.integrity = true;
		this.availability = true;
	}
	
	public Threat(Integer id, String threat) {
		this.id = id;
		this.threat = threat;
		this.confidentiality = true;
		this.integrity = true;
		this.availability = true;
	}
	
	public Threat(String threat) {
		this.threat = threat;
		this.confidentiality = true;
		this.integrity = true;
		this.availability = true;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getThreat() {
		return threat;
	}

	public void setThreat(String threat) {
		this.threat = threat;
		
	}
	
	public Boolean getConfidentiality() {
		return confidentiality;
	}
	
	public String getConfidentialityAsString() {
		if(getConfidentiality()) {
			return "Ja";
		} else {
			return "Nein";
		} 
	}
	
	public String getIntegrityAsString() {
		if(getIntegrity()) {
			return "Ja";
		} else {
			return "Nein";
		} 
	}
	
	public String getAvailabilityAsString() {
		if(getAvailability()) {
			return "Ja";
		} else {
			return "Nein";
		} 
	}
	
	public Collection<Measure> getMeasureCollection() {
		return measureCollection;
	}
	
	public void setMeasureCollection(Collection<Measure> measureCollection) {
		this.measureCollection = measureCollection;
	}

	public void setConfidentiality(Boolean confidentiality) {
		this.confidentiality = confidentiality;
	}
	
	public void setConfidentialityAsString(String confidentialityAsString) {
		if (confidentialityAsString.equals("Ja")) {
			setConfidentiality(true);
		} else /*if (confidentialityAsString.equals("Nein") || confidentialityAsString.equals(""))*/ {
			setConfidentiality(false);
		} 
	}
	
	public void setIntegrityAsString(String integrityAsString) {
		if (integrityAsString.equals("Ja")) {
			setIntegrity(true);
		} else /*if (confidentialityAsString.equals("Nein") || confidentialityAsString.equals(""))*/ {
			setIntegrity(false);
		} 
	}
	
	public void setAvailabilityAsString(String availabilyAsString) {
		if (availabilyAsString.equals("Ja")) {
			setAvailability(true);
		} else /*if (confidentialityAsString.equals("Nein") || confidentialityAsString.equals(""))*/ {
			setAvailability(false);
		} 
	}

	public Boolean getIntegrity() {
		return integrity;
	}

	public void setIntegrity(Boolean integrity) {
		this.integrity = integrity;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public ThreatCategory getThreatCategory_fk() {
		return threatCategory_fk;
	}

	public void setThreatCategory_fk(ThreatCategory threatCategory_fk) {
		this.threatCategory_fk = threatCategory_fk;
	}

	@Override
	public Threat clone() throws CloneNotSupportedException {
		return (Threat) super.clone();
	}
	
	@Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Threat)) {
            return false;
        }
        Threat other = (Threat) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
	
//	@Override
//	public String toString() {
//		return "Threat: " + this.id + ", " + this.threat;
//	}
	
	@Override
	public String toString() {
		return this.threat;
	}
	
}
