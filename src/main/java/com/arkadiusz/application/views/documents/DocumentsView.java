package com.arkadiusz.application.views.documents;


import com.arkadiusz.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.io.File;

@Route(value = "documents", layout = MainLayout.class)
@PageTitle("Dokumenty")
@PermitAll
public class DocumentsView extends VerticalLayout {
    public static String descriptionOfView = "W tym miejscu możesz przechowywać ważne dokumenty oraz pliki";

    public DocumentsView() {
        this.addClassName("bg-contrast-5");

        File uploadFolder = getUploadFolder();
        UploadArea uploadArea = new UploadArea(uploadFolder);
        DownloadLinksArea linksArea = new DownloadLinksArea(uploadFolder);

        uploadArea.getUploadField().addSucceededListener(e -> {
            uploadArea.hideErrorField();
            linksArea.refreshFileLinks();
        });

        add(uploadArea, linksArea);

        setSizeFull();
    }

    private static File getUploadFolder() {
        File folder = new File("uploaded-files");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
}


//    static String descriptionOfView = "";
//
//    public DocumentsView() {
//
//
//////        StreamResource streamResource = new StreamResource(
//////                "report.pdf", () -> getClass().getResourceAsStream("/report.pdf")); // file in src/main/resources/
//////
//////        PdfBrowserViewer viewer = new PdfBrowserViewer(streamResource);
//////        viewer.setHeight("100%");
//////        add(viewer);
////
////        StreamResource streamResource = new StreamResource(
////                "report.pdf", () -> getClass().getResourceAsStream("/report.pdf")); // file in src/main/resources/
////
////        PdfBrowserViewer viewer = new PdfBrowserViewer(streamResource);
////        viewer.setHeight("100%");
//    }

