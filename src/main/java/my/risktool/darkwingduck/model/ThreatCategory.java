package my.risktool.darkwingduck.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "threat_categories")
public class ThreatCategory implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "abbr")
	private String abbr;
	
	@Column(name = "category")
	private String category;
	
	@OneToMany(targetEntity=Threat.class, mappedBy="threatCategory", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Threat> threats = new ArrayList<>();
		
	public ThreatCategory() {
	}
	
	public ThreatCategory(int id, String abbr, String threat) {
		this.id = id;
		this.abbr = abbr;
		this.category = category;
	}
	
	public ThreatCategory(String category) {
		this.category = category;
	}
	
	public ThreatCategory(String abbr, String category) {
		this.category = category;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public ThreatCategory clone() throws CloneNotSupportedException {
		return (ThreatCategory) super.clone();
	}
	
//	@Override
//	public String toString() {
//		return "Category: " + this.id + ", " + this.abbr + ", " + this.category;
//	}
	@Override
	public String toString() {
		return this.category;
	}
}