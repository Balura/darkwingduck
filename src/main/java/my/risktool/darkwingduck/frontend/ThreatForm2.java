//package my.risktool.darkwingduck.frontend;
//
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.TypedQuery;
//
//import com.vaadin.data.Binder;
//import com.vaadin.event.ShortcutAction.KeyCode;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.FormLayout;
//import com.vaadin.ui.Grid;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.TextField;
//import com.vaadin.ui.themes.ValoTheme;
//
//import my.risktool.darkwingduck.ThreatUI;
//import my.risktool.darkwingduck.dao.ThreatDao;
//import my.risktool.darkwingduck.model.Database;
//import my.risktool.darkwingduck.model.Threat;
//import my.risktool.darkwingduck.model.ThreatCategory;
//import my.risktool.darkwingduck.service.ThreatCategoryService;
//import my.risktool.darkwingduck.service.ThreatService;
//
//public class ThreatForm2 extends FormLayout {
//	Database db = new Database();
//	EntityManager em = db.getEm();
//	
//	private TextField threatTxtF = new TextField("Bedrohung");
//	TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findAll", ThreatCategory.class);
//	List<ThreatCategory> threatCategories = query.getResultList(); 
//	private ComboBox<ThreatCategory> sample = new ComboBox<>("Bedrohung auswählen", threatCategories);
//	
//	private Threat threat;
//	private Binder<Threat> binder = new Binder<>(Threat.class);
//	private ThreatUI threatUI;
//
//	private Binder<ThreatCategory> binderCatgory = new Binder<>(ThreatCategory.class);
//
//	private Grid<ThreatCategory> threatGrid = new Grid(Threat.class);
//	private Button saveBtn = new Button("Save");
//	private Button deleteBtn = new Button("Delete");
//	private Label errorMsg = new Label();
//	
//
//	
//
//	public ThreatForm2(ThreatUI threatUI) {
////		this.threatUI = threatUI;
//		errorMsg.setVisible(false);
////		threatTxtF.setMaxLength(100);
//
//		// visibility only true when new threat category or selected item
//		setVisible(false);
//
//		// Sets the combobox to show a certain property as the item caption
//		sample.setPlaceholder("Keine Bedrohung ausgewählt");
//		// Disallow null selections
//		sample.setEmptySelectionAllowed(false);
//
//		setSizeUndefined();
//		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
//		addComponents(errorMsg, threatTxtF, sample, buttons);
//
//		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
//		saveBtn.setClickShortcut(KeyCode.ENTER);
//
////		binder.bindInstanceFields(sample);
//		binder.forField(threatTxtF).bind(Threat::getThreat, Threat::setThreat);
////		binderCatgory.forField(sample).bind(ThreatCategory::getCategory, ThreatCategory::setCategory);
////		binder.setBean(threat);
////		binder.bindInstanceFields(threatTxtF);
////		binder.forMemberField(threatTxtF);
////		binder.bindInstanceFields(sample);
////		binder.bindInstanceFields(threatUI);
//		// binder.forField(threatCat).bind(Threat::getThreatCatalogueThreat,
//		// Threat::setThreat);
//		// binder.bind(sample, "Bedrohung");
//		// binder.forField(sample).bind(ThreatCategory::getCategory,
//		// ThreatCategory::setCategory);
//		// binder.bindInstanceFields(threatTxtF);
//
////		saveBtn.addClickListener(e -> saveThreat());
////		deleteBtn.addClickListener(e -> deleteThreat(threat.getId()));
//	}
//
//	public void setThreat(Threat threat) {
//		System.out.println(threat);
//		System.out.println("binder " + binder);
//		this.threat = threat;
//		binder.setBean(threat);
//
//		// deleteBtn.setVisible();
//		setVisible(true);
//		threatTxtF.selectAll();
//	}
//
////	private void deleteThreat(int id) {
////		threatService.delete(id);
////		threatUI.updateGrid();
////		setVisible(false);
////	}
//
//	/*
//	 * 
//	 * 
//	 * textfield länge muss noch gecheckt werden
//	 * 
//	 * 
//	 * 
//	 */
//
////	private void saveThreat() {
////		System.out.println(threat);
////		Threat newThreat = new Threat();
////		// check if textfield is not empty
////		if (threatTxtF.getValue().length() > 0) {
////
////			// check if threat already exists in database
////			List<Threat> threats = threatService.findAll();
////			boolean duplicateEntry = false;
////
////			for (Threat threat : threats) {
////				if (threat.getThreat().equals(threatTxtF.getValue())) {
////					duplicateEntry = true;
////				}
////			}
////
////			// save threat into database if not already in database
////			if (duplicateEntry) {
////				errorMsg.setValue("Threat already exists");
////				errorMsg.setVisible(true);
////			} else {
////				// threatCategoryService.
////				// threat.setThreatCategory(threatCategoryService.);
////				// ThreatCategory threatCategory = ThreatCategoryService.findById(1);
////				System.out.println("samplevalue " + sample.getValue());
////				System.out.println("threatcategory " + ThreatCategoryService.findById(1));
////				// threatCategory = sample.getValue();
////				// System.out.println("threatcategory " + threatCategory);
////				System.out.println("threatfinal :" + threat);
////
////				// int categoryID = sample.getValue();
////
////				// newThreat.setThreatCategory(ThreatCategoryService.findById(categoryID));
////				newThreat.setThreatCategory_fk(sample.getValue());
////				newThreat.setThreat(threatTxtF.getValue());
////				System.out.println("threatfinal :" + newThreat);
////				// setThreatCategory(service.findById(1)
////
////				threatService.save(newThreat);
////				setVisible(false);
////			}
////
////			// update grid
//////			threatUI.updateGrid();
////
////		} else { // if textfield is empty
////			errorMsg.setValue("Cannot save empty threat");
////			errorMsg.setVisible(true);
////		}
////
////	}
//
//}
