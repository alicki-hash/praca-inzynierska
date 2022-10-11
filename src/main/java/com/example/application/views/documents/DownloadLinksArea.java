package com.example.application.views.documents;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.*;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadLinksArea extends VerticalLayout {

    private final File uploadFolder;

    public DownloadLinksArea(File uploadFolder) {
        addClassNames("appcontent");
        this.getStyle().set("background-color", "white");

        this.uploadFolder = uploadFolder;
        refreshFileLinks();
//        setMargin(true);
        setSpacing(false);
    }

    public void refreshFileLinks() {
        removeAll();
        add(new H4("Pobierz pliki:"));

        for (File file : uploadFolder.listFiles()) {
                addLinkToFile(file);
        }
    }

    private void addLinkToFile(File file){
        StreamResource streamResource = new StreamResource(file.getName(), () -> getStream(file));
        Button deleteButton = createDeleteButton();

        Anchor link = new Anchor(streamResource, String.format("%s (%d KB)", file.getName(),
                (int) file.length() / 1024));
        link.getElement().setAttribute("download", true);

        deleteButton.addClickListener(e -> {
            file.delete();
            refreshFileLinks();
        });
        HorizontalLayout row = new HorizontalLayout(link, dateOfFile(file.lastModified()), deleteButton);

        add(row, new Hr());
    }

    private Button createDeleteButton() {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));

        deleteButton.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin", "0 0 0 auto")
                .set("--lumo-button-size", "var(--lumo-size-xs)");
        return deleteButton;
    }

    private InputStream getStream(File file) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return stream;
    }

    private Component dateOfFile(long millis){
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(millis);
        Span dateOfModification = new Span(date);


        return dateOfModification;
    }
}
