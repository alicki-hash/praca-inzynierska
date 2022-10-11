package com.example.application.views;

import com.example.application.data.entity.Investment;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class MyInvestmentForm extends FormLayout {

    H3 title = new H3("Edytuj dane inwestycji");
    TextField investmentName = new TextField("Nazwa inwestycji");
    BigDecimalField investmentBudget = new BigDecimalField("Budżet");
    DatePicker investmentStartDate = new DatePicker("Data rozpoczęcia inwestycji");

    Button editButton = new Button("Edytuj");
    Button cancelButton = new Button("Anuluj");

    private Investment investment;
    Binder<Investment> binder = new BeanValidationBinder<>(Investment.class);

    public MyInvestmentForm(Investment investment) {
        binder.bindInstanceFields(this);

        this.investment = investment;
        binder.readBean(investment);
//        binder.forField(investmentBudget)
//                .withConverter(new DoubleToBigDecimalConverter())
//                .bind(investment::getInvestmentBudget, investment::setInvestmentBudget);

        add(
                title,
                investmentName,
                investmentBudget,
                investmentStartDate,
                createButtonLayout());

    }

    private Component createButtonLayout() {
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        editButton.addClickListener(event -> validateAndEdit());
        cancelButton.addClickListener(event -> fireEvent(new MyInvestmentForm.CloseEvent(this)));

        editButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        HorizontalLayout buttonLayout = new HorizontalLayout(editButton, cancelButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        return buttonLayout;
    }

    private void validateAndEdit() {
        try {
            binder.writeBean(investment);
            System.out.println("4 " + investment.getInvestmentStartDate());
            fireEvent(new MyInvestmentForm.SaveEvent(this, investment));

            System.out.println("5 " + investment.getInvestmentStartDate());
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class MyInvestmentFormEvent extends ComponentEvent<MyInvestmentForm> {
        private Investment investment;

        protected MyInvestmentFormEvent(MyInvestmentForm source, Investment investment) {
            super(source, false);
            this.investment = investment;
        }

        public Investment getInvestment() {
            return investment;
        }
    }

    public static class SaveEvent extends MyInvestmentForm.MyInvestmentFormEvent {
        SaveEvent(MyInvestmentForm source, Investment investment) {
            super(source, investment);
        }
    }


    public static class CloseEvent extends MyInvestmentForm.MyInvestmentFormEvent {
        CloseEvent(MyInvestmentForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
