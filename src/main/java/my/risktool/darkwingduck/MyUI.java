package my.risktool.darkwingduck;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import my.risktool.darkwingduck.frontend.ThreatCatalogueForm;
import my.risktool.darkwingduck.frontend.ThreatForm;
import my.risktool.darkwingduck.model.Threat;
import my.risktool.darkwingduck.model.ThreatCatalogue;
import my.risktool.darkwingduck.service.ThreatCatalogueService;
import my.risktool.darkwingduck.service.ThreatService;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
	
	private ThreatCatalogueService service = new ThreatCatalogueService();
	private Grid<ThreatCatalogue> grid = new Grid<>();
	
	private ThreatService threatService = new ThreatService();
	private Grid<Threat> threatGrid = new Grid<>();
	
	private TextField filterText = new TextField();
	private TextField threatEditor = new TextField();
	private ThreatCatalogueForm form = new ThreatCatalogueForm(this);
	private ThreatForm threatform = new ThreatForm(this);
	
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		
		filterText.setPlaceholder("filter by name...");
        filterText.addValueChangeListener(e -> updateList());//event fired
        filterText.setValueChangeMode(ValueChangeMode.LAZY);//short pause of typing
		
        grid.setCaption("Double click to edit");
        grid.addColumn(ThreatCatalogue::getAbbr).setCaption("Abbreviation");
		grid.addColumn(ThreatCatalogue::getThreat).setEditorComponent(threatEditor, ThreatCatalogue::setThreat).setCaption("Threat");
		grid.getEditor().setEnabled(true);
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.getEditor().addCancelListener(event -> Notification.show("cancel"));
		grid.getEditor().addSaveListener( event -> {
			
			if(event.getBean().getThreat().length() > 0) {
				//check if threat already exists in database
				List<ThreatCatalogue> threats = service.findAll(); 
				boolean duplicateEntry = false;
				
				for(ThreatCatalogue threat : threats) {
					if(threat.getThreat().equals(event.getBean().getThreat())) {
						duplicateEntry = true;
					}
				}
				
				//save threat into database if not already in database
				if (duplicateEntry) { 
					Notification.show("Threat already exists");
					updateList();
				} else {
					service.update(event.getBean());
					Notification.show("Updated");
				}
				
			} else { //if threat is empty
				Notification.show("Cannot update empty threat");
			
			}

		});
		
		threatGrid.addColumn(Threat::getThreat).setCaption("Threat");
		threatGrid.addColumn(Threat::getThreatCatalogueThreat).setCaption("Threat Catalogue");
		updateThreats();
		
		com.vaadin.ui.Button clearFilterTextBtn = new com.vaadin.ui.Button(VaadinIcons.CLOSE);
        clearFilterTextBtn.setDescription("Clear Filter");
        clearFilterTextBtn.addClickListener(e -> filterText.clear());
        
        CssLayout filtering = new CssLayout();
        filtering.addComponents(filterText, clearFilterTextBtn);
        filtering.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        
        com.vaadin.ui.Button addThreatBtn = new com.vaadin.ui.Button("Add new threat");
        addThreatBtn.addClickListener(e -> {
        	grid.asSingleSelect().clear();
        	form.setThreat(new ThreatCatalogue());
        });
		
        HorizontalLayout toolbar = new HorizontalLayout(filtering, addThreatBtn);
        
		HorizontalLayout main = new HorizontalLayout(grid, form );
		main.setSizeFull();
        grid.setSizeFull();
        main.setExpandRatio(grid, 1);
		
		layout.addComponents(toolbar, main, threatGrid, threatform);
		
		updateList();
		
		setContent(layout);
		
		form.setVisible(false);

		
		grid.asSingleSelect().addValueChangeListener(event -> {
			if(event.getValue() == null) {
				form.setVisible(false);
			} else {
				form.setThreat(event.getValue());
			}
		});
		
		threatGrid.asSingleSelect().addValueChangeListener(event -> {
			System.out.println("event " + event.getValue());
			if(event.getValue() == null) {
				form.setVisible(false);
			} else {
				threatform.setThreat(event.getValue());
			}
		});
	}
	
	public void updateThreats() {
		System.out.println(filterText.getValue());
//		List<ThreatCatalogue> threats = service.findAll(filterText.getValue());
		List<Threat> threats = threatService.findAll();
		threatGrid.setItems(threats);
	}
	
	public void updateList() {
		System.out.println(filterText.getValue());
		List<ThreatCatalogue> threats = service.findAll(filterText.getValue());
//		List<ThreatCatalogue> threats = service.findAll();
		grid.setItems(threats);
	}
	
	
	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
