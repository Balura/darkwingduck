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

import my.risktool.darkwingduck.model.Application;
import my.risktool.darkwingduck.model.Availability;
import my.risktool.darkwingduck.model.Confidentiality;
import my.risktool.darkwingduck.model.DataClassification;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.Integrity;

/**
 * 
 * @author Johnny
 *TODO
 *Textfield Längen prüfen
 */

public class ApplicationForm extends FormLayout {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Database db = new Database();
	EntityManager em = db.getEm();

	private TextField applicationTxtF = new TextField("Bedrohung");
	TypedQuery<Availability> queryAvail = em.createNamedQuery("Availability.findAll", Availability.class);
	List<Availability> availability = queryAvail.getResultList();
	TypedQuery<Integrity> queryInt = em.createNamedQuery("Integrity.findAll", Integrity.class);
	List<Integrity> integrity = queryInt.getResultList();
	TypedQuery<Confidentiality> queryConfi = em.createNamedQuery("Confidentiality.findAll", Confidentiality.class);
	List<Confidentiality> confidentiality = queryConfi.getResultList();
	TypedQuery<DataClassification> queryDataClas = em.createNamedQuery("DataClassification.findAll", DataClassification.class);
	List<DataClassification> dataClassification = queryDataClas.getResultList();
	
	private NativeSelect<String> confidentialitySelect = new NativeSelect<>("Vertraulichkeit");
	private NativeSelect<String> integritySelect = new NativeSelect<>("Integrität");
	private NativeSelect<String> availabilitySelect = new NativeSelect<>("Verfügbarkeit");
	private NativeSelect<String> dataClassificationSelect = new NativeSelect<>("Datenklassifizierung");
	
	
	private Button saveBtn = new Button("Save");
	private Button deleteBtn = new Button("Delete");

	private Application application;
	private Binder<Application> binder = new Binder<>(Application.class);
	private ApplicationUI applicationUI;

	public ApplicationForm(ApplicationUI applicationUI) {
		this.applicationUI = applicationUI;
		// applicationTxtF.setMaxLength(100);
		
		/*removes emtpy value from dropdowns*/
		confidentialitySelect.setEmptySelectionAllowed(false);
		integritySelect.setEmptySelectionAllowed(false);
		availabilitySelect.setEmptySelectionAllowed(false);
		dataClassificationSelect.setEmptySelectionAllowed(false);
		
		/*set values for dropdowns*/
//		confidentialitySelect.setItems("Ja", "Nein");
//		integritySelect.setItems("Ja", "Nein");
//		availabilitySelect.setItems("Ja", "Nein");

		// visibility only true when new application category or selected item
		setVisible(false);
		deleteBtn.setVisible(false);

		// Sets the combobox to show a certain property as the item caption
//		cbAvailability.setPlaceholder("Keine Verfügbarkeit ausgewählt");
//		cbIntegrity.setPlaceholder("Keine Integrität ausgewählt");
//		cbConfidentiality.setPlaceholder("Keine Vertraulichkeit ausgewählt");
//		cbDataClassification.setPlaceholder("Keine Datenklasse ausgewählt");
//		// Disallow null selections
//		cbAvailability.setEmptySelectionAllowed(false);
//		cbIntegrity.setEmptySelectionAllowed(false);
//		cbConfidentiality.setEmptySelectionAllowed(false);
//		cbDataClassification.setEmptySelectionAllowed(false);

		setSizeUndefined();
		HorizontalLayout buttons = new HorizontalLayout(saveBtn, deleteBtn);
		addComponents(applicationTxtF, confidentialitySelect, integritySelect, availabilitySelect, dataClassificationSelect, buttons);

		saveBtn.setStyleName(ValoTheme.BUTTON_PRIMARY);
		saveBtn.setClickShortcut(KeyCode.ENTER);

		binder.forField(applicationTxtF).bind(Application::getName, Application::setName);
		binder.forField(confidentialitySelect).bind(Application::getConfidentiality_fk, Application::setConfidentiality_fk);
		binder.forField(integritySelect).bind(Application::getIntegrity_fk, Application::setIntegrity_fk);
		binder.forField(availabilitySelect).bind(Application::getAvailability_fk, Application::setAvailability_fk);
		binder.forField(dataClassificationSelect).bind(Application::getData_classification_fk, Application::setData_classification_fk);

		saveBtn.addClickListener(e -> saveApplication(em));
		deleteBtn.addClickListener(e -> deleteApplication(em, application.getId()));
	}

	// is called when click on existing application in grid
	public void setApplication(Application application) {
		System.out.println(application);
		System.out.println("binder " + binder);
		this.application = application;
		binder.setBean(application);

		deleteBtn.setVisible(true);
		setVisible(true);
		applicationTxtF.selectAll();
	}

	private void deleteApplication(EntityManager em, int id) {
		Application newApplication = new Application();
		TypedQuery<Application> query = em.createNamedQuery("Application.findById", Application.class).setParameter("id", id);
		newApplication = query.getSingleResult();

		em.getTransaction().begin();
		em.remove(newApplication);
		em.getTransaction().commit();

		Notification.show("Applikation gelöscht");
		applicationUI.updateGrid(em);

		setVisible(false);// hide form
	}

	/*
	 * textfield länge muss noch gecheckt werden
	 * 
	 * 
	 * 
	 * 
	 */

	private void saveApplication(EntityManager em) {

		Application newApplication = new Application();
		newApplication.setName(applicationTxtF.getValue());

		// check if textfields are not empty
		if (applicationTxtF.getValue().length() == 0) {
			Notification.show("Name eingeben");
			applicationTxtF.selectAll();
		} else {

			// clear all persistent entities
			em.clear();

			// check if application already exists in database
			TypedQuery<Application> query = em.createNamedQuery("Application.findAll", Application.class);
			List<Application> applications = query.getResultList();
			boolean duplicateEntry = false;

			for (Application application : applications) {
				if (application.getName().equals(applicationTxtF.getValue())) {
					duplicateEntry = true;
				}
			}

			// save application into database if not already in database
			if (duplicateEntry) {
				Notification.show("Applikation existiert bereits");
			} else {

				// save or update selected or inserted application category
				try {
					EntityTransaction tx = em.getTransaction();
					tx.begin();

					if (application.getId() != null) {
						em.merge(application);
						Notification.show("Applikation aktualisiert");

					} else { // if not existing
						em.persist(application);
						Notification.show("Applikation gespeichert");
					}

					tx.commit();

				} catch (Exception e) {
					System.err.println("Exception: " + e.getMessage());
				}
				// hide form
				setVisible(false);// hide form
				applicationUI.updateGrid(em);
			}
		}

	}

}
