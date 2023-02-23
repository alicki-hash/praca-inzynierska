package com.arkadiusz.application.views;

import com.arkadiusz.application.security.SecurityService;
import com.arkadiusz.application.views.contactList.ContactListView;
import com.arkadiusz.application.views.documents.DocumentsView;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

@JsModule("@vaadin/vaadin-lumo-styles/utility.js")
@CssImport(value = "./themes/flowcrmtutorial/styles.css", include = "lumo-utility")
public class MainLayout extends AppLayout {


    private SecurityService securityService;


    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        createHeader();
        createDrawer();

    }

    private void createHeader() {
        Span logo = new Span("Praca Inżynierska");
        logo.addClassNames("text-xl");
        logo.getStyle().set("font-weight", "bold");

        Button logoutButton = new Button("Wyloguj", e -> securityService.logout());
        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo, logoutButton);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.getThemeList().add("dark");
        addToNavbar(header);
    }

    private void createDrawer() {
//        RouterLink listView = new RouterLink("Kontakty", ContactListView.class);
//        listView.setHighlightCondition(HighlightConditions.sameLocation());
//
//        RouterLink expenseView = new RouterLink("Wydatki", ExpenseView.class);
//        expenseView.setHighlightCondition(HighlightConditions.sameLocation());
//
//        VerticalLayout drawer = new VerticalLayout(
//                listView,
//                expenseView,
//                new RouterLink("Wykres", DashboardView.class)
//        );
//
//        drawer.getElement().getStyle().set("text-transform", "uppercase");

        Tabs tabs = new Tabs();
        tabs.add(
                createTab(VaadinIcon.HOME, "Moja Inwestycja", MyInvestmentView.class, MyInvestmentView.descriptionOfView),
                createTab(VaadinIcon.PHONE, "Kontakty", ContactListView.class, ContactListView.descriptionOfView),
                createTab(VaadinIcon.CART, "Wydatki", ExpenseView.class, ExpenseView.descriptionOfView),
                createTab(VaadinIcon.PIE_CHART, "Wykresy", DashboardView.class, DashboardView.descriptionOfView),
                createTab(VaadinIcon.FOLDER, "Dokumenty", DocumentsView.class, DocumentsView.descriptionOfView),
                createTab(VaadinIcon.CAMERA, "Inspiracje", InspirationsView.class, InspirationsView.descriptionOfView));


        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addSelectedChangeListener(tab -> {
            // checking if infoButton is visible in previous tab and hiding
            if (tab.getPreviousTab().getElement().getChild(0).getChild(2).isVisible()) {
                tab.getPreviousTab().getElement().getChild(0).getChild(2).setVisible(false);
            }
            // showing info button on active tab
            tab.getSelectedTab().getElement().getChild(0).getChild(2).setVisible(true);
        });
        addToDrawer(tabs);
    }


    private Tab createTab(VaadinIcon viewIcon, String viewName, Class navigateTo, String descriptionOfView) {

        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        if (descriptionOfView == "") descriptionOfView = "Brak opisu dla tej zakładki";

        Button infoButton = createInfoButton(descriptionOfView);

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName), infoButton);
        link.setRoute(navigateTo);
        link.setTabIndex(-1);


        return new Tab(link);
    }

    private Button createInfoButton(String descriptionOfView) {

        Button infoButton = new Button();
        infoButton.setIcon(new Icon(VaadinIcon.INFO_CIRCLE));
        infoButton.setVisible(false);
        infoButton.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin", "0 0 0 auto")
                .set("--lumo-button-size", "var(--lumo-size-xs)")
                .set("background", "none");

        infoButton.addClassName("pulseIcon");
        infoButton.addClickListener(e-> infoButton.getClassNames().remove("pulseIcon"));

        Div sampleNotification = new Div(new Text(descriptionOfView));
        sampleNotification.getStyle().set("padding", "var(--lumo-space-m)");

        ContextMenu infoMenu = new ContextMenu();
        infoMenu.setOpenOnClick(true);
        infoMenu.setTarget(infoButton);
        infoMenu.add(sampleNotification);

        return infoButton;
    }
}
