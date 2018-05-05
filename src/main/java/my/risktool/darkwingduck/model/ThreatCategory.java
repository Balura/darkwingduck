package my.risktool.darkwingduck.model;

import java.io.Serializable;
import java.util.Collection;

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
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "threat_categories")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ThreatCategories.findAll", query = "FROM ThreatCategory t"),
    @NamedQuery(name = "ThreatCategories.findById", query = "FROM ThreatCategory t WHERE t.id = :id"),
    @NamedQuery(name = "ThreatCategories.findByAbbr", query = "FROM ThreatCategory t WHERE t.abbr = :abbr"),
    @NamedQuery(name = "ThreatCategories.findByCategory", query = "FROM ThreatCategory t WHERE t.category = :category"),
	@NamedQuery(name = "ThreatCategories.filterByCategory", query = "FROM ThreatCategory t WHERE LOWER(t.category) LIKE LOWER(:category)"),
//	@NamedQuery(name = "ThreatCategories.saveCategory", query = "INSERT INTO ThreatCategory (abbr, category) VALUES (:abbr, :category)")
   	})
public class ThreatCategory implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@NotNull
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "abbr")
	private String abbr;
	
	@Column(name = "category")
	private String category;
	
//	@OneToMany(targetEntity=Threat.class, mappedBy="threatCategory", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
//	private List<Threat> threats = new ArrayList<>();
	
	@OneToMany(mappedBy = "threatCategory_fk", cascade = CascadeType.MERGE)
	private Collection<Threat> threatCollection;
		
	public ThreatCategory() {
	}
	
	public ThreatCategory(Integer id, String abbr, String category) {
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
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
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

	public Collection<Threat> getThreatCollection() {
		return threatCollection;
	}

	public void setThreatCollection(Collection<Threat> threatCollection) {
		this.threatCollection = threatCollection;
	}

	@Override
	public ThreatCategory clone() throws CloneNotSupportedException {
		return (ThreatCategory) super.clone();
	}
	
	 @Override
	    public boolean equals(Object object) {
	        // TODO: Warning - this method won't work in the case the id fields are not set
	        if (!(object instanceof ThreatCategory)) {
	            return false;
	        }
	        ThreatCategory other = (ThreatCategory) object;
	        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	            return false;
	        }
	        return true;
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