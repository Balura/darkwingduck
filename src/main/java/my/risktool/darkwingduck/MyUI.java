package my.risktool.darkwingduck;

import java.awt.Label;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("ciri")
@SuppressWarnings("serial")
public class MyUI extends UI {

	/**
	 * 
	 */
	private ThreatCategoryUI threatCategoryUI = new ThreatCategoryUI(this);
	private ThreatUI threatUI = new ThreatUI(this);
	private MeasureUI measureUI = new MeasureUI(this);
//	private ApplicationUI applicationUI = new ApplicationUI(this);

	private MenuBar navigation = new MenuBar();

	@Override
	protected void init(VaadinRequest request) {
//		final HorizontalLayout mainLayout = new HorizontalLayout();
		final VerticalLayout headerLayout = new VerticalLayout();
		final VerticalLayout layoutMain = new VerticalLayout();
		
		threatCategoryUI.setVisible(true);
		
		headerLayout.setStyleName("header");
		final com.vaadin.ui.Label label = new com.vaadin.ui.Label("Label can be dangerous");
		
		headerLayout.addComponent(label);
		
		/* menu */
		MenuBar.Command getThreats = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layoutMain.removeComponent(threatCategoryUI);
				layoutMain.removeComponent(measureUI);
//				layoutMain.removeComponent(applicationUI);
				layoutMain.addComponent(threatUI);

			}
		};

		MenuBar.Command getThreatsCategories = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layoutMain.removeComponent(threatUI);
				layoutMain.removeComponent(measureUI);
//				layoutMain.removeComponent(applicationUI);
				layoutMain.addComponent(threatCategoryUI);

			}
		};
		
		MenuBar.Command getMeasures = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layoutMain.removeComponent(threatCategoryUI);
				layoutMain.removeComponent(threatUI);
//				layoutMain.removeComponent(applicationUI);
				layoutMain.addComponent(measureUI);

			}
		};
		
		MenuBar.Command getApplications = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layoutMain.removeComponent(threatCategoryUI);
				layoutMain.removeComponent(threatUI);
				layoutMain.removeComponent(measureUI);
//				layoutMain.addComponent(applicationUI);

			}
		};
		
		MenuItem menuAdmin = navigation.addItem("Admin", null, null);
		MenuItem menuApplications = navigation.addItem("Applikationen", null, getApplications);
		
		MenuItem menuItemThreatCategory = menuAdmin.addItem("Bedrohungskategorien", null, getThreatsCategories);
		MenuItem menuItemThreat = menuAdmin.addItem("Bedrohungen", null, getThreats);
		MenuItem menuItemMeasure = menuAdmin.addItem("Maßnahmen", null, getMeasures);
		
		
		layoutMain.addComponents(headerLayout, navigation, threatCategoryUI);
		
//		mainLayout.addComponents(navigation, layout);
		setContent(layoutMain);
		
//        connection();

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
	
//	public void connection() {
//		System.out.println("test");
//        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("my.risktool.darkwingduck_PU");
//        EntityManager em = emf.createEntityManager();
//        System.out.println("Connected");
//        TypedQuery<ThreatCategory> query = em.createNamedQuery("ThreatCategories.findAll", ThreatCategory.class);
//
//        for (ThreatCategory threatCategory : query.getResultList()) {
//            
//            System.out.println("Threat: " + threatCategory);
//        }
//
//    }
}
