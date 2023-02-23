package com.arkadiusz.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterListener;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Logowanie")
public class LoginView extends VerticalLayout implements BeforeEnterListener {

    private LoginOverlay login = new LoginOverlay();
    Image logoImage = new Image("images/house-logo.png", "Investment logo");

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        login.setAction("login");
        login.setI18n(createLoginI18n());
        login.setOpened(true);
        login.setTitle("Praca Inżynierska");
        login.setDescription("Aplikacja wspierająca proces budowy nieruchomości.");


        add(
                login
        );
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    private LoginI18n createLoginI18n() {
        LoginI18n i18n = LoginI18n.createDefault();

        i18n.getForm().setUsername("Nazwa użytkownika");
        i18n.getForm().setTitle("Zaloguj się");
        i18n.getForm().setSubmit("Zaloguj się");
        i18n.getForm().setPassword("Hasło");
        i18n.getForm().setForgotPassword("Nie pamiętam hasła");
        i18n.getErrorMessage().setTitle("Błąd!");
        i18n.getErrorMessage()
                .setMessage("Nieprawidłowe dane.");
        return i18n;
    }

}
