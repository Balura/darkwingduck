package my.risktool.darkwingduck.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="threats")
public class Threat implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "threat")
	private String threat;


	@ManyToOne
	@JoinColumn(name = "threat_category_id", nullable=false)
	private ThreatCategory threatCategory;
	
	public Threat() {
	}
	
	public Threat(int id, String threat) {
		this.id = id;
		this.threat = threat;
	}
	
	public Threat(String threat) {
		this.threat = threat;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getThreat() {
		return threat;
	}

	public void setThreat(String threat) {
		this.threat = threat;
	}
	
	public ThreatCategory getThreatCategory() {
		return threatCategory;
	}

	public void setThreatCategory(ThreatCategory threatCategory) {
		this.threatCategory = threatCategory;
	}
	
//	public String getThreatCategoryAbbr() {
//		return threatCategory.getAbbr();
//	}
	
//	public String getThreatCategory() {
//		return threatCategory.getThreat();
//	}
	
//	public int getThreatCategoryID() {
//		return threatCategory.getId();
//	}

	@Override
	public Threat clone() throws CloneNotSupportedException {
		return (Threat) super.clone();
	}
	
	@Override
	public String toString() {
		return "Threat: " + this.id + ", " + this.threat;
	}
	
}
