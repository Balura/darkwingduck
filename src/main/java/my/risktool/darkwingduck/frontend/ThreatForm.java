package my.risktool.darkwingduck.frontend;

import java.util.Collection;
import java.util.List;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.MyUI;
import my.risktool.darkwingduck.dao.ThreatDao;
import my.risktool.darkwingduck.model.Threat;
import my.risktool.darkwingduck.model.ThreatCatalogue;
import my.risktool.darkwingduck.service.ThreatCatalogueService;
import my.risktool.darkwingduck.service.ThreatService;

public class ThreatForm extends FormLayout {
	
	private ThreatService threatService = new ThreatService();
	private ThreatCatalogue threatCatalogue = new ThreatCatalogue();
	private ThreatCatalogueService threatCatalogueService = new ThreatCatalogueService();
	private Threat threat;
	private static ThreatDao threatDao;
	private Binder<Threat> binder = new Binder<>(Threat.class);
	private MyUI myUI;
	
	private Grid<ThreatCatalogue> threatGrid = new Grid(Threat.class);
	private TextField threatTxtF = new TextField("Threat");
	private TextField threatCat = new TextField("Category");
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");
	private Label errorMsg = new Label();
	private List<ThreatCatalogue> threatsCollection = threatCatalogueService.findAllThreats();
	
	private ComboBox<ThreatCatalogue> sample = new ComboBox<>("Bedrohung auswählen", threatsCollection);

	public ThreatForm(MyUI myUI) {
		System.out.println("Collection " + threatCatalogue);
		this.myUI = myUI;
		errorMsg.setVisible(false);
		
		// Sets the combobox to show a certain property as the item caption
		sample.setPlaceholder("Keine Bedrohung ausgewählt");
		// Disallow null selections
        sample.setEmptySelectionAllowed(false);
		
		
		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(errorMsg, threatTxtF, sample, buttons);
		
		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);
		
//		binder.bindInstanceFields(this);
		binder.forField(threatTxtF).bind(Threat::getThreat, Threat::setThreat);
//		binder.forField(threatCat).bind(Threat::getThreatCatalogueThreat, Threat::setThreat);
//		binder.bind(sample, "Bedrohung");
		
		saveBtn.addClickListener(e -> saveThreat());
		deleteBtn.addClickListener(e -> deleteThreat(threat.getId()));
	}
	
	public void setThreat(Threat threat) {
		System.out.println(threat);
		this.threat = threat;
		binder.setBean(threat);
		
//		deleteBtn.setVisible();
		setVisible(true);
		threatTxtF.selectAll();
	}

	private void deleteThreat(int id) {
		threatService.delete(id);
		myUI.updateThreats();
//		setVisible(false);
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
		System.out.println(threat);
		// check if textfield is not empty
		if(threatTxtF.getValue().length() > 0) { 
			
			//check if threat already exists in database
			List<Threat> threats = threatService.findAll(); 
			boolean duplicateEntry = false;
			
			for(Threat threat : threats) {
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
//				setVisible(false);
			}
			
			//update grid
			myUI.updateThreats();;
			
		} else { //if textfield is empty
			errorMsg.setValue("Cannot save empty threat");
			errorMsg.setVisible(true);
		}
	
	}

}
