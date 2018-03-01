package my.risktool.darkwingduck.frontend;

import java.util.List;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.GeneralFunctions;
import my.risktool.darkwingduck.MyUI;
import my.risktool.darkwingduck.dao.ThreatCatalogueDao;
import my.risktool.darkwingduck.model.ThreatCatalogue;
import my.risktool.darkwingduck.service.ThreatCatalogueService;

public class ThreatCatalogueForm extends FormLayout {

	private Grid<ThreatCatalogue> threatGrid = new Grid(ThreatCatalogue.class);
	private TextField abbrTxtF = new TextField("Abbreviation");
	private TextField threatTxtF = new TextField("Threat");
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");
	private Label errorMsg = new Label();
	
	private ThreatCatalogueService threatService = new ThreatCatalogueService();
	private ThreatCatalogue threat;
	private static ThreatCatalogueDao threatDao;
	private Binder<ThreatCatalogue> binder = new Binder<>(ThreatCatalogue.class);
	private MyUI myUI;

	public ThreatCatalogueForm(MyUI myUI) {
		this.myUI = myUI;
		errorMsg.setVisible(false);
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(errorMsg, abbrTxtF, threatTxtF, buttons);
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		
//		binder.bindInstanceFields(this);
		binder.forField(abbrTxtF).bind(ThreatCatalogue::getAbbr, ThreatCatalogue::setAbbr);
		binder.forField(threatTxtF).bind(ThreatCatalogue::getThreat, ThreatCatalogue::setThreat);
		
		saveBtn.addClickListener(e -> saveThreat());
		deleteBtn.addClickListener(e -> deleteThreat(threat.getId()));
	}
	
	public void setThreat(ThreatCatalogue threat) {
		this.threat = threat;
		binder.setBean(threat);
		
//		deleteBtn.setVisible();
		setVisible(true);
		threatTxtF.selectAll();
	}

	private void deleteThreat(int id) {
		threatService.delete(id);
		myUI.updateList();
		setVisible(false);
	}
	
	/*
	 * 
	 * 
	 * bedrohung muss noch gecheckt werden ob schon in datenbank
	 * 
	 * 
	 * 
	 * */
	
	
	private void saveThreat() {
		
		// check if textfield is not empty
		if(threatTxtF.getValue().length() > 0) { 
			
			//check if threat already exists in database
			List<ThreatCatalogue> threats = threatService.findAll(); 
			boolean duplicateEntry = false;
			
			for(ThreatCatalogue threat : threats) {
				if(threat.getThreat().equals(threatTxtF.getValue())) {
					duplicateEntry = true;
				}
			}
			
			//save threat into database if not already in database
			if (duplicateEntry) { 
				errorMsg.setValue("Threat already exists");
				errorMsg.setVisible(true);
			} else {
				threatService.save(threat);
				setVisible(false);
			}
			
			//update grid
			myUI.updateList();
			
		} else { //if textfield is empty
			errorMsg.setValue("Cannot save empty threat");
			errorMsg.setVisible(true);
		}
	
	}

}
