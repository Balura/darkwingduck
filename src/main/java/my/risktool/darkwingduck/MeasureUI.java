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

import my.risktool.darkwingduck.MyUI;
import my.risktool.darkwingduck.frontend.MeasureForm;
import my.risktool.darkwingduck.model.Database;
import my.risktool.darkwingduck.model.Measure;

public class MeasureUI extends FormLayout{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Grid<Measure> grid = new Grid<>();
	private MeasureForm form = new MeasureForm(this);
	
	private TextField filterText = new TextField();
	private TextField editor = new TextField();
	
	Binder<Measure> binder = grid.getEditor().getBinder();
	
	public MeasureUI(MyUI myUI) {
		Database db = new Database();
		EntityManager em = db.getEm();
		
		updateGrid(em);
		
		filterText.setPlaceholder("Filter nach Maßnahme...");
		filterText.addValueChangeListener(e -> updateGrid(em));// event fired
		filterText.setValueChangeMode(ValueChangeMode.LAZY);// short pause of typing

//		grid.setCaption("Doppelklick zum editieren");
		
		grid.addColumn(Measure::getMeasure).setEditorComponent(editor, Measure::setMeasure).setCaption("Maßnahme");
		grid.addColumn(Measure::getThreat_fk).setCaption("Bedrohung");
		
		
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.setVisible(true);
		grid.getEditor().setEnabled(false);

		grid.getEditor().addCancelListener(event -> Notification.show("cancel"));

		grid.getEditor().addSaveListener(event -> {

			if (event.getBean().getMeasure().length() > 0) {
				//check if measure already exists in database
				TypedQuery<Measure> query = em.createNamedQuery("Measures.findAll", Measure.class);
				List<Measure> measures = query.getResultList(); 
				boolean duplicateEntry = false;

				for (Measure measure : measures) {
					if (measure.getMeasure().equals(event.getBean().getMeasure())) {
						duplicateEntry = true;
					}
				}

				//save measure into database if not already in database
				if (duplicateEntry) { 
					Notification.show("Maßnahme existiert bereits");
				} else {

					//save or update selected or inserted measure category
					try {
						EntityTransaction tx = em.getTransaction();
						tx.begin();
						
						if(event.getBean().getId() != null) {
							em.merge(event.getBean());
							Notification.show("Maßnahme aktualisiert");
							
						} else { //if not existing
							em.persist(event.getBean());
							Notification.show("Maßnahme gespeichert");
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
				Notification.show("Maßnahme eingeben");
			}
			
		});

		com.vaadin.ui.Button clearFilterTextBtn = new com.vaadin.ui.Button(VaadinIcons.CLOSE);
		clearFilterTextBtn.setDescription("Clear Filter");
		clearFilterTextBtn.addClickListener(e -> filterText.clear());

		CssLayout filtering = new CssLayout();
		filtering.addComponents(filterText, clearFilterTextBtn);
		filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

		com.vaadin.ui.Button addMeasureCategoryBtn = new com.vaadin.ui.Button("Neue Maßnahme");

		addMeasureCategoryBtn.addClickListener(e -> {
			grid.asSingleSelect().clear();
			form.setMeasure(new Measure());
		});

		grid.asSingleSelect().addValueChangeListener(event -> {
			if (event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setMeasure(event.getValue());
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout(filtering, addMeasureCategoryBtn);
		
		HorizontalLayout main = new HorizontalLayout(grid, form);
		main.setWidth("100%");
		grid.setWidth("100%");
		main.setExpandRatio(grid, 3);
		main.setExpandRatio(form, 1);
		// main.setSizeFull();
		// grid.setSizeFull();
//		main.setExpandRatio(grid, 1);
		
		addComponents(toolbar, main);
	}
	
	public void updateGrid(EntityManager em) {
        TypedQuery<Measure> query = em.createNamedQuery("Measure.filterByMeasure", Measure.class).setParameter("measure", "%"+filterText.getValue()+"%");

        for (Measure measure : query.getResultList()) {
            
            System.out.println("Measure: " + measure);
        }
        
		grid.setItems(query.getResultList());
	}
	
}
