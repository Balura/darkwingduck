package my.risktool.darkwingduck.frontend;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.ThreatUI;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.Threat;
import my.risktool.darkwingduck.model.ThreatCategory;

/**
 * 
 * @author Johnny
 *TODO
 *Textfield Längen prüfen
 */

public class ThreatForm extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Database db = new Database();
	EntityManager em = db.getEm();

	private TextField threatTxtF = new TextField("Bedrohung");
	TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findAll", ThreatCategory.class);
	List<ThreatCategory> threatCategories = query.getResultList();
	private ComboBox<ThreatCategory> cbThreatCategories = new ComboBox<>("Kategorie auswählen", threatCategories);
	private NativeSelect<String> confidentialitySelect = new NativeSelect<>("Vertraulichkeit");
	private NativeSelect<String> integritySelect = new NativeSelect<>("Integrität");
	private NativeSelect<String> availabilitySelect = new NativeSelect<>("Verfügbarkeit");
	
	
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");

	private Threat threat;
	private Binder<Threat> binder = new Binder<>(Threat.class);
	private ThreatUI threatUI;

	public ThreatForm(ThreatUI threatUI) {
		this.threatUI = threatUI;
		// threatTxtF.setMaxLength(100);
		
		/*removes emtpy value from dropdowns*/
		confidentialitySelect.setEmptySelectionAllowed(false);
		integritySelect.setEmptySelectionAllowed(false);
		availabilitySelect.setEmptySelectionAllowed(false);
		
		/*set values for dropdowns*/
		confidentialitySelect.setItems("Ja", "Nein");
		integritySelect.setItems("Ja", "Nein");
		availabilitySelect.setItems("Ja", "Nein");

		// visibility only true when new threat category or selected item
		setVisible(false);
		deleteBtn.setVisible(false);

		// Sets the combobox to show a certain property as the item caption
		cbThreatCategories.setPlaceholder("Keine Bedrohung ausgewählt");
		// Disallow null selections
		cbThreatCategories.setEmptySelectionAllowed(false);

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(threatTxtF, cbThreatCategories, confidentialitySelect, integritySelect, availabilitySelect, buttons);

		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);

		binder.forField(threatTxtF).bind(Threat::getThreat, Threat::setThreat);
		binder.forField(cbThreatCategories).bind(Threat::getThreatCategory_fk, Threat::setThreatCategory_fk);
		binder.forField(confidentialitySelect).bind(Threat::getConfidentialityAsString, Threat::setConfidentialityAsString);
		binder.forField(integritySelect).bind(Threat::getIntegrityAsString, Threat::setIntegrityAsString);
		binder.forField(availabilitySelect).bind(Threat::getAvailabilityAsString, Threat::setAvailabilityAsString);

		saveBtn.addClickListener(e -> saveThreat(em));
		deleteBtn.addClickListener(e -> deleteThreat(em, threat.getId()));
	}

	// is called when click on existing threat in grid
	public void setThreat(Threat threat) {
		System.out.println(threat);
		System.out.println("binder " + binder);
		this.threat = threat;
		binder.setBean(threat);

		deleteBtn.setVisible(true);
		setVisible(true);
		threatTxtF.selectAll();
	}

	private void deleteThreat(EntityManager em, int id) {
		Threat newThreat = new Threat();
		TypedQuery<Threat> query = em.createNamedQuery("Threat.findById", Threat.class).setParameter("id", id);
		newThreat = query.getSingleResult();

		em.getTransaction().begin();
		em.remove(newThreat);
		em.getTransaction().commit();

		Notification.show("Category deleted");
		threatUI.updateGrid(em);

		setVisible(false);// hide form
	}

	/*
	 * textfield länge muss noch gecheckt werden
	 * 
	 * 
	 * 
	 * 
	 */

	private void saveThreat(EntityManager em) {

		Threat newThreat = new Threat();
		newThreat.setThreat(threatTxtF.getValue());

		// check if textfields are not empty
		if (threatTxtF.getValue().length() == 0) {
			Notification.show("Bedrohung eingeben");
			threatTxtF.selectAll();
		} else if (cbThreatCategories.isEmpty()) {
			Notification.show("Kategorie auswählen");
			threatTxtF.selectAll();
		} else {

			// clear all persistent entities
			em.clear();

			// check if threat already exists in database
			TypedQuery<Threat> query = em.createNamedQuery("Threat.findAll", Threat.class);
			List<Threat> threats = query.getResultList();
			boolean duplicateEntry = false;

			for (Threat threat : threats) {
				if (threat.getThreat().equals(threatTxtF.getValue())) {
					duplicateEntry = true;
				}
			}

			// save threat into database if not already in database
			if (duplicateEntry) {
				Notification.show("Bedrohung existiert bereits");
			} else {

				// save or update selected or inserted threat category
				try {
					EntityTransaction tx = em.getTransaction();
					tx.begin();

					if (threat.getId() != null) {
						em.merge(threat);
						Notification.show("Bedrohung aktualisiert");

					} else { // if not existing
						em.persist(threat);
						Notification.show("Bedrohung gespeichert");
					}

					tx.commit();

				} catch (Exception e) {
					System.err.println("Exception: " + e.getMessage());
				}
				// hide form
				setVisible(false);// hide form
				threatUI.updateGrid(em);
			}
		}

	}

}
