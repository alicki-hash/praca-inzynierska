package com.arkadiusz.application.views;


import com.arkadiusz.application.data.entity.Expense;
import com.arkadiusz.application.data.entity.Status;
import com.arkadiusz.application.data.entity.WorkScope;
import com.arkadiusz.application.data.service.ExpenseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.crudui.crud.CrudOperation;
import org.vaadin.crudui.crud.impl.GridCrud;
import org.vaadin.crudui.form.impl.field.provider.ComboBoxProvider;
import org.vaadin.crudui.form.impl.form.factory.DefaultCrudFormFactory;
import org.vaadin.crudui.layout.impl.WindowBasedCrudLayout;

import javax.annotation.security.PermitAll;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@PageTitle("Wydatki")
@Route(value = "expenses", layout = MainLayout.class)
@PermitAll
public class ExpenseView extends VerticalLayout {

    static String descriptionOfView = "Tabela przedstawia wydatki poniesione na poczet projektu. Wydatki można sortować wg. kryteriów 'data' oraz 'kwota'.";
    Locale userLocale = UI.getCurrent().getLocale();

    public ExpenseView(ExpenseService service) {

        this.addClassName("bg-contrast-5");

        DefaultCrudFormFactory<Expense> formFactory = new DefaultCrudFormFactory<Expense>(Expense.class) {
            @Override
            public void configureForm(FormLayout formLayout, List<HasValueAndElement> fields) {
                Component titleField = (Component) fields.get(0);
                formLayout.setColspan(titleField, 2);

                Component descriptionField = (Component) fields.get(5);
                formLayout.setColspan(descriptionField, 2);

                binder.forMemberField(fields.get(0)).asRequired("Uzupełnij pole!");
                binder.forMemberField(fields.get(1)).asRequired("Uzupełnij pole!");
                binder.forMemberField(fields.get(2)).asRequired("Uzupełnij pole!");
            }
        };

        GridCrud<Expense> crud = new GridCrud<>(Expense.class, formFactory);
        Grid<Expense> grid = crud.getGrid();


        // na szybko toolbar - do poprawy
        crud.getContent().getComponentAt(0).getElement()
                .getChild(0).getChild(0).getStyle().set("margin", "0 0 var(--lumo-space-m) 0");


        ((WindowBasedCrudLayout) crud.getCrudLayout()).setWindowCaption(
                CrudOperation.DELETE, "Czy na pewno chcesz usunąć ten wydatek?");

        ((WindowBasedCrudLayout) crud.getCrudLayout()).setWindowCaption(
                CrudOperation.ADD, "Dodaj nowy wydatek");

        ((WindowBasedCrudLayout) crud.getCrudLayout()).setWindowCaption(
                CrudOperation.UPDATE, "Edytuj wydatek");


        formFactory.setButtonCaption(CrudOperation.ADD, "Dodaj");
        formFactory.setButtonCaption(CrudOperation.DELETE, "Usuń");
        formFactory.setButtonCaption(CrudOperation.UPDATE, "Edytuj");
        formFactory.setCancelButtonCaption("Anuluj");

        setSizeFull();

        TextField filterText = new TextField();
        filterText.setPlaceholder("Wyszukaj");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> crud.refreshGrid());
        crud.getCrudLayout().addFilterComponent(filterText);


        formFactory.setVisibleProperties("title", "dateOfExpense", "amount", "status", "workScope",
                "descriptionOfExpense");

//        formFactory.setVisibleProperties(CrudOperation.DELETE, "title",
//                "dateOfExpense");

        formFactory.setFieldCaptions("Tytuł", "Data", "Kwota (PLN)", "Status", "Zakres prac", "Opis");

        formFactory.setFieldProvider(CrudOperation.DELETE, "title", title -> {
            var titleField = new TextArea();
            return titleField;
        });


        formFactory.setFieldProvider("workScope",
                new ComboBoxProvider<>("Work Scopes", service.findAllWorkScopes(),
                        new TextRenderer<>(WorkScope::getName), WorkScope::getName));

        formFactory.setFieldProvider("status",
                new ComboBoxProvider<>("Status", service.findAllStatuses(),
                        new TextRenderer<>(Status::getName), Status::getName));


        // do zrobienia bo nie dziala AutoOpen oraz CustomValue

//        ComboBox statusField = new ComboBox<>("Status", service.findAllStatuses());
//        formFactory.setFieldProvider("status", status -> statusField);


        crud.setFindAllOperation(() -> service.findAllExpenses(filterText.getValue()));
        crud.setAddOperation(service::saveExpense);
        crud.setUpdateOperation(service::saveExpense);
        crud.setDeleteOperation(service::deleteExpense);


        grid.setColumns("title", "dateOfExpense");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        grid.setItemDetailsRenderer(createExpenseDetails());

        grid.getColumnByKey("title").setSortable(false).setHeader("Tytuł");
        grid.getColumnByKey("dateOfExpense").setHeader("Data");


//        grid.addColumn(new NumberRenderer<>(expense -> expense.getAmount(),
//                NumberFormat.getInstance(userLocale))).setHeader("Kwota (PLN)");

        grid.addColumn(expense -> {
            return currencyFormat(expense.getAmount());
        }).setHeader("Kwota");

        grid.addColumn(expense -> {
            WorkScope workScope = expense.getWorkScope();
            return workScope.getName();
        }).setHeader("Zakres Prac");

        grid.addComponentColumn(expense ->
                        createStatusBadge(expense.getStatus().getName()))
                .setHeader("Status");

        crud.setCrudFormFactory(formFactory);

        add(
                crud
        );
    }


    private ComponentRenderer<ExpenseDetailsForm, Expense> createExpenseDetails() {
        return new ComponentRenderer<>(ExpenseDetailsForm::new, ExpenseDetailsForm::setExpenseDescription);
    }

    private static class ExpenseDetailsForm extends FormLayout {
        private final TextArea expenseDescription = new TextArea("Opis");


        public ExpenseDetailsForm() {
            Stream.of(expenseDescription).forEach(field -> {
                field.setReadOnly(true);
                add(field);
            });

            setResponsiveSteps(new ResponsiveStep("0", 3));
            setColspan(expenseDescription, 3);

        }


        public void setExpenseDescription(Expense expense) {
            expenseDescription.setValue(expense.getDescriptionOfExpense());
        }
    }


    private Span createStatusBadge(String status) {
        String theme;
        switch (status) {
            case "Zapłacone":
                theme = "badge success primary";
                break;
            case "Niezapłacone":
                theme = "badge contrast primary";
                break;
            default:
                theme = "badge primary";
                break;
        }
        Span badge = new Span(status);
        badge.getElement().getThemeList().add(theme);
        return badge;
    }

    public static String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }

}
