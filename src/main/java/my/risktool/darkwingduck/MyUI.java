package my.risktool.darkwingduck;

import java.util.List;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import my.risktool.darkwingduck.frontend.ThreatForm;
import my.risktool.darkwingduck.model.Threat;
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

	private ThreatService threatService = new ThreatService();
	private Grid<Threat> threatGrid = new Grid<>();

	private ThreatCategoryUI threatCategoryUI = new ThreatCategoryUI(this);
	private ThreatUI threatUI = new ThreatUI(this);

	private MenuBar barmenu = new MenuBar();

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.addComponent(threatCategoryUI);

		threatCategoryUI.setVisible(true);

		layout.addComponents(barmenu, threatCategoryUI);

		MenuBar.Command getThreats = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layout.removeComponent(threatCategoryUI);
				layout.addComponent(threatUI);

			}
		};
		
		MenuBar.Command getThreatsCategories = new MenuBar.Command() {

			@Override
			public void menuSelected(MenuItem selectedItem) {
				layout.removeComponent(threatUI);
				layout.addComponent(threatCategoryUI);

			}
		};

		/* menu */
		MenuItem drinks = barmenu.addItem("Threat Categories", null, getThreatsCategories);
		MenuItem hots = barmenu.addItem("Threats", null, getThreats);

		setContent(layout);

	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
