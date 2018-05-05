package my.risktool.darkwingduck.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "measures")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Measure.findAll", query = "FROM Measure m"),
		@NamedQuery(name = "Measure.findById", query = "FROM Measure m WHERE m.id = :id"),
		@NamedQuery(name = "Measure.findByMeasure", query = "FROM Measure m WHERE m.measure = :measure"),
		@NamedQuery(name = "Measure.filterByMeasure", query = "FROM Measure m WHERE LOWER(m.measure) LIKE LOWER(:measure)")})
public class Measure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;

	@NotNull
	@Column(name = "measure")
	private String measure;

	@ManyToOne
	@JoinColumn(name = "threat_id", referencedColumnName = "id")
	private Threat threat_fk;

	public Measure() {

	}

	public Measure(Integer id, String measure) {
		this.id = id;
		this.measure = measure;
	}

	public Measure(String measure) {
		this.measure = measure;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public Threat getThreat_fk() {
		return threat_fk;
	}

	public void setThreat_fk(Threat threat_fk) {
		this.threat_fk = threat_fk;
	}
	
	@Override
	public String toString() {
		return "Measure: " + this.id + ", " + this.measure;
	}

}
