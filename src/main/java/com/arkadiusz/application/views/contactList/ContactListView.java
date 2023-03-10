package com.arkadiusz.application.views.contactList;

import com.arkadiusz.application.data.entity.Contact;
import com.arkadiusz.application.data.service.ContactService;
import com.arkadiusz.application.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;

@org.springframework.stereotype.Component
@Scope("prototype")
@PageTitle("Kontakty")
@Route(value = "contacts", layout = MainLayout.class)
@PermitAll
public class ContactListView extends VerticalLayout {

    public static String descriptionOfView = "Tutaj znajdują się informacje o osobach zaangażowanych w Twój projekt";

    Grid<Contact> grid = new Grid<>(Contact.class);
    TextField filterText = new TextField();
    ContactForm form;
    private final ContactService service;

    public ContactListView(ContactService service) {
        this.setId("contactListView");
        this.addClassName("bg-contrast-5");
        this.service = service;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(
                getToolbar(),
                getContent()
        );

        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setContact(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.addClassNames("appcontent");
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllWorkScopes());
        form.setWidth("25em");

        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveContact(ContactForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(ContactForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Wyszukaj...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Dodaj kontakt");
        addContactButton.addClickListener(e -> addContact());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);

        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void configureGrid() {
        grid.addClassName("contact-grid");
        grid.setSizeFull();
        grid.setColumns("firstName", "lastName", "phone");
        grid.addColumn(contact -> contact.getWorkScope().getName()).setHeader("Zakres prac");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Firma");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));


        grid.getColumnByKey("firstName").setHeader("Imię").setSortable(false);
        grid.getColumnByKey("lastName").setHeader("Nazwisko");
        grid.getColumnByKey("phone").setHeader("Telefon").setSortable(false);

        grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));
    }

    private void editContact(Contact contact) {
        if (contact == null) {
            closeEditor();
        } else {
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

}
