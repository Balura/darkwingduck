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
@Table(name="data_classification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DataClassification.findAll", query = "FROM DataClassification t"),
    @NamedQuery(name = "DataClassification.findById", query = "FROM DataClassification t WHERE t.id = :id"),
    @NamedQuery(name = "DataClassification.findByValue", query = "FROM DataClassification t WHERE t.dataClassification = :dataClassification")
})
public class DataClassification implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional = false)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@NotNull
	@Size(max=100)
	@Column(name = "dataClassification")
	private String dataClassification;
	
	@OneToMany(mappedBy = "data_classification_fk", cascade = CascadeType.MERGE)
	private Collection<Application> applicationCollection;
	
	public DataClassification() {
		
	}
	
	public DataClassification(Integer id, String dataClassification) {
		this.id = id;
		this.dataClassification = dataClassification;
	}
	
	public DataClassification(String dataClassification) {
		this.dataClassification = dataClassification;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDataClassification() {
		return dataClassification;
	}

	public void setDataClassification(String dataClassification) {
		this.dataClassification = dataClassification;
	}
	
	public Collection<Application> getApplicationCollection() {
		return applicationCollection;
	}

	public void setApplicationCollection(Collection<Application> applicationCollection) {
		this.applicationCollection = applicationCollection;
	}
	
	@Override
	public String toString() {
		return this.dataClassification;
	}
	
}
