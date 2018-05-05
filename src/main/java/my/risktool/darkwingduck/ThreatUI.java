package my.risktool.darkwingduck;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.frontend.ThreatForm;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.Threat;

public class ThreatUI extends FormLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Threat> grid = new Grid<>();
	private ThreatForm form = new ThreatForm(this);
	
	private TextField filterText = new TextField();
	private TextField editor = new TextField();
	
	Binder<Threat> binder = grid.getEditor().getBinder();
	
	public ThreatUI(MyUI myUI) {
		Database db = new Database();
		EntityManager em = db.getEm();
		
		updateGrid(em);
		
		filterText.setPlaceholder("Filter nach Bedrohung...");
		filterText.addValueChangeListener(e -> updateGrid(em));// event fired
		filterText.setValueChangeMode(ValueChangeMode.LAZY);// short pause of typing

//		grid.setCaption("Doppelklick zum editieren");
		
		grid.addColumn(Threat::getThreat).setEditorComponent(editor, Threat::setThreat).setCaption("Bedrohung");
		grid.addColumn(Threat::getConfidentialityAsString).setCaption("Vertraulichkeit");
		grid.addColumn(Threat::getIntegrityAsString).setCaption("Integrität");
		grid.addColumn(Threat::getAvailabilityAsString).setCaption("Verfügbarkeit");
		grid.addColumn(Threat::getThreatCategory_fk).setCaption("Kategorie");
		
		
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setVisible(true);
		grid.getEditor().setEnabled(false);

		grid.getEditor().addCancelListener(event -> Notification.show("cancel"));

		grid.getEditor().addSaveListener(event -> {

			if (event.getBean().getThreat().length() > 0) {
				//check if threat already exists in database
				TypedQuery<Threat> query = em.createNamedQuery("Threats.findAll", Threat.class);
				List<Threat> threats = query.getResultList(); 
				boolean duplicateEntry = false;

				for (Threat threat : threats) {
					if (threat.getThreat().equals(event.getBean().getThreat())) {
						duplicateEntry = true;
					}
				}

				//save threat into database if not already in database
				if (duplicateEntry) { 
					Notification.show("Bedrohung existiert bereits");
				} else {

					//save or update selected or inserted threat category
					try {
						EntityTransaction tx = em.getTransaction();
						tx.begin();
						
						if(event.getBean().getId() != null) {
							em.merge(event.getBean());
							Notification.show("Bedrohung aktualisiert");
							
						} else { //if not existing
							em.persist(event.getBean());
							Notification.show("Bedrohung gespeichert");
						}
						
			        	tx.commit();
			        	
					} catch (Exception e) {
						System.err.println("Exception: " + e.getMessage());
					}
					//hide form
					setVisible(false);//hide form
					updateGrid(em);
				}
			} else {
				Notification.show("Bedrohung eingeben");
			}
			
		});

		com.vaadin.ui.Button clearFilterTextBtn = new com.vaadin.ui.Button(VaadinIcons.CLOSE);
		clearFilterTextBtn.setDescription("Clear Filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		com.vaadin.ui.Button addThreatCategoryBtn = new com.vaadin.ui.Button("Neue Bedrohung");

		addThreatCategoryBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			form.setThreat(new Threat());
		});

		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setThreat(event.getValue());
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout(filtering, addThreatCategoryBtn);
		
		HorizontalLayout main = new HorizontalLayout(grid, form);
//		main.setWidth("100%");
//		grid.setWidth("100%");
//		grid.setHeight("100%");
		main.setSizeFull();
//		 grid.setSizeFull();
//		main.setExpandRatio(grid, 1);
//		main.setExpandRatio(form, 1);
//		 main.setSizeFull();
//		 grid.setSizeFull();
//		main.setExpandRatio(grid, 1);
		
		addComponents(toolbar, main);
	}
	
	public void updateGrid(EntityManager em) {
        TypedQuery<Threat> query = em.createNamedQuery("Threat.filterByThreat", Threat.class).setParameter("threat", "%"+filterText.getValue()+"%");

        for (Threat threat : query.getResultList()) {
            
            System.out.println("Threat: " + threat);
        }
        
		grid.setItems(query.getResultList());
	}
	
}
