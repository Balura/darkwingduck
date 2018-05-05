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
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.MeasureUI;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.Measure;
import my.risktool.darkwingduck.model.Threat;

/**
 * 
 * @author Johnny
 *TODO
 *Textfield Längen prüfen
 */

public class MeasureForm extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Database db = new Database();
	EntityManager em = db.getEm();

	private TextField measureTxtF = new TextField("Maßnahme");
	TypedQuery<Threat> query = em.createNamedQuery("Threat.findAll", Threat.class);
	List<Threat> threats = query.getResultList();
	private ComboBox<Threat> cbThreats = new ComboBox<>("Bedrohung auswählen", threats);
	
	
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");

	private Measure measure;
	private Binder<Measure> binder = new Binder<>(Measure.class);
	private MeasureUI measureUI;

	public MeasureForm(MeasureUI measureUI) {
		this.measureUI = measureUI;
		// threatTxtF.setMaxLength(100);
		
		// visibility only true when new threat category or selected item
		setVisible(false);
		deleteBtn.setVisible(false);

		// Sets the combobox to show a certain property as the item caption
		cbThreats.setPlaceholder("Keine Bedrohung ausgewählt");
		// Disallow null selections
		cbThreats.setEmptySelectionAllowed(false);

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(measureTxtF, cbThreats, buttons);

		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);

		binder.forField(measureTxtF).bind(Measure::getMeasure, Measure::setMeasure);
		binder.forField(cbThreats).bind(Measure::getThreat_fk, Measure::setThreat_fk);

		saveBtn.addClickListener(e -> saveMeasure(em));
		deleteBtn.addClickListener(e -> deleteMeasure(em, measure.getId()));
	}

	// is called when click on existing threat in grid
	public void setMeasure(Measure measure) {
		this.measure = measure;
		binder.setBean(measure);

		deleteBtn.setVisible(true);
		setVisible(true);
		measureTxtF.selectAll();
	}

	private void deleteMeasure(EntityManager em, int id) {
		Measure newMeasure = new Measure();
		TypedQuery<Measure> query = em.createNamedQuery("Measure.findById", Measure.class).setParameter("id", id);
		newMeasure = query.getSingleResult();

		em.getTransaction().begin();
		em.remove(newMeasure);
		em.getTransaction().commit();

		Notification.show("Category deleted");
		measureUI.updateGrid(em);

		setVisible(false);// hide form
	}

	/*
	 * textfield länge muss noch gecheckt werden
	 * 
	 * 
	 * 
	 * 
	 */

	private void saveMeasure(EntityManager em) {

		Measure newMeasure = new Measure();
		newMeasure.setMeasure(measureTxtF.getValue());

		// check if textfields are not empty
		if (measureTxtF.getValue().length() == 0) {
			Notification.show("Maßnahme eingeben");
			measureTxtF.selectAll();
		} else if (cbThreats.isEmpty()) {
			Notification.show("Kategorie auswählen");
			measureTxtF.selectAll();
		} else {

			// clear all persistent entities
			em.clear();

			// check if threat already exists in database
			TypedQuery<Measure> query = em.createNamedQuery("Measure.findAll", Measure.class);
			List<Measure> measures = query.getResultList();
			boolean duplicateEntry = false;

			for (Measure measure : measures) {
				if (measure.getMeasure().equals(measureTxtF.getValue())) {
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

					if (measure.getId() != null) {
						em.merge(measure);
						Notification.show("Bedrohung aktualisiert");

					} else { // if not existing
						em.persist(measure);
						Notification.show("Bedrohung gespeichert");
					}

					tx.commit();

				} catch (Exception e) {
					System.err.println("Exception: " + e.getMessage());
				}
				// hide form
				setVisible(false);// hide form
				measureUI.updateGrid(em);
			}
		}

	}

}
