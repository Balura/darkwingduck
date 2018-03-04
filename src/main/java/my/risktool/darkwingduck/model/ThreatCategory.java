package my.risktool.darkwingduck.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "threat_catalogue")
public class ThreatCatalogue implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "abbr")
	private String abbr;
	
	@Column(name = "threat")
	private String threat;
	
	@OneToMany(mappedBy="threat")
	private List<Threat> threats = new ArrayList<>();
		
	public ThreatCatalogue() {
	}
	
	public ThreatCatalogue(int id, String abbr, String threat) {
		this.id = id;
		this.abbr = abbr;
		this.threat = threat;
	}
	
	public ThreatCatalogue(String threat) {
		this.threat = threat;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}
	
	public String getAbbr() {
		return abbr;
	}

	public String getThreat() {
		return threat;
	}

	public void setThreat(String threat) {
		this.threat = threat;
	}

	@Override
	public ThreatCatalogue clone() throws CloneNotSupportedException {
		return (ThreatCatalogue) super.clone();
	}
	
	@Override
	public String toString() {
		return "Threat: " + this.id + ", " + this.abbr + ", " + this.threat;
	}

	
}