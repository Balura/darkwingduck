package my.risktool.darkwingduck.frontend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.ThreatCategoryUI;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.ThreatCategory;

public class ThreatCategoryForm extends FormLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextField abbrTxtF = new TextField("Abbreviation");
	private TextField threatTxtF = new TextField("Threat");
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");
	
	private ThreatCategory threatCategory;
	private Binder<ThreatCategory> binder = new Binder<>(ThreatCategory.class);
	private ThreatCategoryUI threatCategoryUI;

	public ThreatCategoryForm(ThreatCategoryUI threatCategoryUI) {
		this.threatCategoryUI = threatCategoryUI;
		Database db = new Database();
		EntityManager em = db.getEm();
//		abbrTxtF.setMaxLength(5);
//		threatTxtF.setMaxLength(45);
		
		//visibility only true when new threat category or selected item
		setVisible(false);
		deleteBtn.setVisible(false);
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(abbrTxtF, threatTxtF, buttons);
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		
//		binder.bindInstanceFields(this);
		binder.forField(abbrTxtF).bind(ThreatCategory::getAbbr, ThreatCategory::setAbbr);
		binder.forField(threatTxtF).bind(ThreatCategory::getCategory, ThreatCategory::setCategory);
		
		saveBtn.addClickListener(e -> saveThreatCategory(em));
		deleteBtn.addClickListener(e -> deleteThreat(em, threatCategory.getId()));
	}
	
	//is called when click on existing threat in grid
	public void setThreatCategory(ThreatCategory threatCategory) {
		deleteBtn.setVisible(true);
		this.threatCategory = threatCategory;
		binder.setBean(threatCategory);
	
		setVisible(true); //show form
		abbrTxtF.selectAll();
	}
	
	//deletes threat category by id and reloads grid
	private void deleteThreat(EntityManager em, int id) {
		ThreatCategory newThreatCategory = new ThreatCategory();
		TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findById", ThreatCategory.class).setParameter("id", id);
		newThreatCategory = query.getSingleResult(); 
		
		em.getTransaction().begin();
		em.remove(newThreatCategory);
		em.getTransaction().commit();
		
		Notification.show("Category deleted");
		threatCategoryUI.updateGrid(em);
		
		setVisible(false);//hide form
	}
	
	/*textfield länge muss noch gecheckt werden
	 * 
	 * 
	 * 
	 * 
	 */
	
	private void saveThreatCategory(EntityManager em) {

		ThreatCategory newThreatCategory = new ThreatCategory();
		newThreatCategory.setAbbr(abbrTxtF.getValue());
		newThreatCategory.setCategory(threatTxtF.getValue());
		
		// check if textfields are not empty
		if(abbrTxtF.getValue().length() == 0) {
			Notification.show("Kürzel eingeben");
			abbrTxtF.selectAll();
		} else if(threatTxtF.getValue().length()  == 0){
			Notification.show("Kategorie eingeben");
			threatTxtF.selectAll();
		} else {
			// clear all persistent entities
			em.clear();
			
			//check if threat already exists in database
			TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findAll", ThreatCategory.class);
			List<ThreatCategory> threatCategories = query.getResultList(); 
			boolean duplicateEntry = false;
			
			for(ThreatCategory category : threatCategories) {
				if(category.getCategory().equals(threatTxtF.getValue())) {
					duplicateEntry = true;
				}
			}
			
			//save threat into database if not already in database
			if (duplicateEntry) { 
				Notification.show("Category already exists");
			} else {

				//save or update selected or inserted threat category
				try {
					EntityTransaction tx = em.getTransaction();
					tx.begin();
					
					if(threatCategory.getId() != null) {
						em.merge(threatCategory);
						Notification.show("Category updated");
						
					} else { //if not existing
						em.persist(threatCategory);
						Notification.show("Category saved");
					}
					
		        	tx.commit();
		        	
				} catch (Exception e) {
					System.err.println("Exception: " + e.getMessage());
				}
				//hide form
				setVisible(false);//hide form
				 threatCategoryUI.updateGrid(em);
			}
		}
        
	}

}
