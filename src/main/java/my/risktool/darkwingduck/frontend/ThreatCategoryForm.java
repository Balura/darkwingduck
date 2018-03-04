package my.risktool.darkwingduck.frontend;

import java.util.List;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.ThreatCategoryUI;
import my.risktool.darkwingduck.model.ThreatCategory;
import my.risktool.darkwingduck.service.ThreatCategoryService;

public class ThreatCategoryForm extends FormLayout {

	private TextField abbrTxtF = new TextField("Abbreviation");
	private TextField threatTxtF = new TextField("Threat");
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");
	private Label errorMsg = new Label();
	
	private ThreatCategoryService threatCategoryService = new ThreatCategoryService();
	private ThreatCategory threatCategory;
	private Binder<ThreatCategory> binder = new Binder<>(ThreatCategory.class);
	private ThreatCategoryUI threatCategoryUI;

	public ThreatCategoryForm(ThreatCategoryUI threatCategoryUI) {
		this.threatCategoryUI = threatCategoryUI;
		errorMsg.setVisible(false);
//		abbrTxtF.setMaxLength(5);
//		threatTxtF.setMaxLength(45);
		
		//visibility only true when new threat category or selected item
		setVisible(false);
		deleteBtn.setVisible(false);
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(errorMsg, abbrTxtF, threatTxtF, buttons);
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		
//		binder.bindInstanceFields(this);
		binder.forField(abbrTxtF).bind(ThreatCategory::getAbbr, ThreatCategory::setCategory);
		binder.forField(threatTxtF).bind(ThreatCategory::getCategory, ThreatCategory::setCategory);
		
		saveBtn.addClickListener(e -> saveThreatCategory());
		deleteBtn.addClickListener(e -> deleteThreat(threatCategory.getId()));
	}
	
	public void setThreatCategory(ThreatCategory threatCategory) {
		deleteBtn.setVisible(true);
		this.threatCategory = threatCategory;
		binder.setBean(threatCategory);
	
		setVisible(true);
		threatTxtF.selectAll();
	}
	
	//deletes threat category by id and reloads grid
	private void deleteThreat(int id) {
		threatCategoryService.delete(id);
		threatCategoryUI.updateGrid();
		setVisible(false);
	}
	
	/*textfield länge muss noch gecheckt werden
	 * 
	 * 
	 * 
	 * 
	 */
	
	//saves or updates threat category and reloads grid
	private void saveThreatCategory() {
		
		// check if textfield is not empty
		if(threatTxtF.getValue().length() > 0) { 
			
			//check if threat already exists in database
			List<ThreatCategory> threatCategories = threatCategoryService.findAll(); 
			boolean duplicateEntry = false;
			
			for(ThreatCategory category : threatCategories) {
				if(category.getCategory().equals(threatTxtF.getValue())) {
					duplicateEntry = true;
				}
			}
			
			//save threat into database if not already in database
			if (duplicateEntry) { 
				errorMsg.setValue("Category already exists");
				errorMsg.setVisible(true);
			} else {
				//save or update selected or inserted threat category
				threatCategoryService.save(threatCategory);
				//hide form
				setVisible(false);
			}
			
			//update grid
			threatCategoryUI.updateGrid();
			
		} else { //if textfield is empty
			errorMsg.setValue("Cannot save empty category");
			errorMsg.setVisible(true);
		}
	
	}

}
