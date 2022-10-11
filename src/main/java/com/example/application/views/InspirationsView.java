package com.example.application.views;

import com.example.application.data.entity.InspirationsPhotoModel;
import com.example.application.data.service.InspirationsService;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.List;

@PageTitle("Inspiracje")
@Route(value = "inspirations", layout = MainLayout.class)
@PermitAll
public class InspirationsView extends VerticalLayout {

    @Autowired
    private InspirationsService inspirationsService;

    private int currentPage = 1;
    static String descriptionOfView = "Tutaj znajdziesz galerię z inspiracjami dotyczącymi wystroju wnętrz.";

    public InspirationsView(InspirationsService inspirationsService) {

        this.setAlignItems(Alignment.CENTER);
        this.addClassName("bg-contrast-5");


        createPhotoGallery(inspirationsService, currentPage);

        Button nextPageButton = createNextPageButton();
        add(nextPageButton);

    }

    private Button createNextPageButton() {
        Button nextPageButton = new Button("Załaduj więcej zdjęć");
        nextPageButton.setId("nextPageButton");

        ComponentEventListener nextPageListener = e -> {
            currentPage += 1;
            createPhotoGallery(inspirationsService, currentPage);
        };

        nextPageButton.addClickListener(nextPageListener);

        nextPageButton.getStyle().set("position", "fixed");
        nextPageButton.getStyle().set("bottom", "0");
        nextPageButton.getStyle().set("left", "0");
        nextPageButton.getStyle().set("margin", "30px");
        return nextPageButton;
    }


    private void createPhotoGallery(InspirationsService inspirationsService, int currentPage) {

        List<InspirationsPhotoModel> photoList = inspirationsService.collectPhotos(currentPage);

        for (InspirationsPhotoModel photo : photoList) {


            Image img = new Image(photo.getLargeUrl(), String.valueOf(photo.getId()));

            img.setWidth("45%");
            img.getStyle().set("box-shadow", "var(--lumo-box-shadow-m)");

            HorizontalLayout layout = new HorizontalLayout();
            Span imageDescription = new Span();
            H2 imageAuthor = new H2("Autor: " + photo.getPhotographer());
            Anchor imageUrl2 = new Anchor(photo.getUrl(), new Button("Przejdź do oryginalnego zdjęcia na stronie Pexels"));
            imageUrl2.setWidthFull();


            imageDescription.add(imageAuthor, imageUrl2);
            layout.add(imageDescription, img);
            layout.setWidthFull();
            layout.setJustifyContentMode(JustifyContentMode.AROUND);

            add(layout);
        }

    }

//    void loadNextPage() {
//        currentPage = currentPage + 1;
//        System.out.println(currentPage + "przed");
//        createPhotoGallery(inspirationsService, 2);
////        UI.getCurrent().getPage().reload();
//        System.out.println(currentPage);
//    }


}
