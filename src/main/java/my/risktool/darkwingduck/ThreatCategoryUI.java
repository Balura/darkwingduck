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

import my.risktool.darkwingduck.frontend.ThreatCategoryForm;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.ThreatCategory;

public class ThreatCategoryUI extends FormLayout {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private ThreatCategoryService service = new ThreatCategoryService();
	private Grid<ThreatCategory> grid = new Grid<>();
	private ThreatCategoryForm form = new ThreatCategoryForm(this);

	private TextField filterText = new TextField();
	private TextField editor = new TextField();
	
	Binder<ThreatCategory> binder = grid.getEditor().getBinder();

	public ThreatCategoryUI(MyUI myUI) {
		Database db = new Database();
		EntityManager em = db.getEm();
		
		updateGrid(em);

		filterText.setPlaceholder("Filter nach Kategorie...");
		filterText.addValueChangeListener(e -> updateGrid(em));// event fired
		filterText.setValueChangeMode(ValueChangeMode.LAZY);// short pause of typing

//		grid.setCaption("Doppelklick zum editieren");

		grid.addColumn(ThreatCategory::getAbbr).setEditorComponent(editor, ThreatCategory::setAbbr).setCaption("Kürzel");
		grid.addColumn(ThreatCategory::getCategory).setEditorComponent(editor, ThreatCategory::setAbbr).setCaption("Bedrohungskategorie");
		
		
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setVisible(true);
		grid.getEditor().setEnabled(false);

		grid.getEditor().addCancelListener(event -> Notification.show("Abgebrochen"));

		grid.getEditor().addSaveListener(event -> {

			if (event.getBean().getCategory().length() > 0) {
				//check if threat already exists in database
				TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findAll", ThreatCategory.class);
				List<ThreatCategory> threatCategories = query.getResultList(); 
				boolean duplicateEntry = false;

				for (ThreatCategory category : threatCategories) {
					if (category.getCategory().equals(event.getBean().getCategory())) {
						duplicateEntry = true;
					}
				}

				//save threat into database if not already in database
				if (duplicateEntry) { 
					Notification.show("Kategorie existiert bereits");
				} else {

					//save or update selected or inserted threat category
					try {
						EntityTransaction tx = em.getTransaction();
						tx.begin();
						
						if(event.getBean().getId() != null) {
							em.merge(event.getBean());
							Notification.show("Kategorie aktualisiert");
							
						} else { //if not existing
							em.persist(event.getBean());
							Notification.show("Kategorie gespeichert");
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
				Notification.show("Kategorie eingeben");
			}
			
		});

		com.vaadin.ui.Button clearFilterTextBtn = new com.vaadin.ui.Button(VaadinIcons.CLOSE);
		clearFilterTextBtn.setDescription("Clear Filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		com.vaadin.ui.Button addThreatCategoryBtn = new com.vaadin.ui.Button("Neue Kategorie");

		addThreatCategoryBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			form.setThreatCategory(new ThreatCategory());
		});

		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setThreatCategory(event.getValue());
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout(filtering, addThreatCategoryBtn);
		
		HorizontalLayout main = new HorizontalLayout(grid, form);
		// main.setSizeFull();
		// grid.setSizeFull();
//		main.setExpandRatio(grid, 1);
		
		addComponents(toolbar, main);
	}

	public void updateGrid(EntityManager em) {
        TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.filterByCategory", ThreatCategory.class).setParameter("category", "%"+filterText.getValue()+"%");

        for (ThreatCategory threatCategory : query.getResultList()) {
            
            System.out.println("Threat: " + threatCategory);
        }
        
		grid.setItems(query.getResultList());
	}

}
