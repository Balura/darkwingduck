package my.risktool.darkwingduck.model;

import java.io.Serializable;

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
@Table(name="applications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Application.findAll", query = "FROM Application a"),
    @NamedQuery(name = "Application.findById", query = "FROM Application a WHERE a.id = :id"),
    @NamedQuery(name = "Application.findByName", query = "FROM Application a WHERE a.name = :name"),
    @NamedQuery(name = "Application.filterByApplication", query = "FROM Application a WHERE LOWER(a.name) LIKE LOWER(:name)")})
public class Application implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "name")
	private String name;
	
	@ManyToOne
	@JoinColumn(name = "confidentiality_id", referencedColumnName = "id")
	private Confidentiality confidentiality_fk;
	
	@ManyToOne
	@JoinColumn(name = "integrity_id", referencedColumnName = "id")
	private Integrity integrity_fk;
	
	@ManyToOne
	@JoinColumn(name = "availability_id", referencedColumnName = "id")
	private Availability availability_fk;
	
	@ManyToOne
	@JoinColumn(name = "data_classification_id", referencedColumnName = "id")
	private DataClassification data_classification_fk;
	
	public Application() {
	}
	
	public Application(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Application(String name) {
		this.name = name;
	}
	
	public Application(Integer id, String name, Confidentiality confidentiality_fk, Integrity integrity_fk,
			Availability availability_fk, DataClassification data_classification_fk) {
		super();
		this.id = id;
		this.name = name;
		this.confidentiality_fk = confidentiality_fk;
		this.integrity_fk = integrity_fk;
		this.availability_fk = availability_fk;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Confidentiality getConfidentiality_fk() {
		return confidentiality_fk;
	}

	public void setConfidentiality_fk(Confidentiality confidentiality_fk) {
		this.confidentiality_fk = confidentiality_fk;
	}

	public Integrity getIntegrity_fk() {
		return integrity_fk;
	}

	public void setIntegrity_fk(Integrity integrity_fk) {
		this.integrity_fk = integrity_fk;
	}

	public Availability getAvailability_fk() {
		return availability_fk;
	}

	public void setAvailability_fk(Availability availability_fk) {
		this.availability_fk = availability_fk;
	}

	public DataClassification getData_classification_fk() {
		return data_classification_fk;
	}

	public void setData_classification_fk(DataClassification data_classification_fk) {
		this.data_classification_fk = data_classification_fk;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	
}
