package com.example.application.views;

import com.example.application.data.entity.Expense;
import com.example.application.data.entity.Investment;
import com.example.application.data.service.ExpenseService;
import com.example.application.data.service.InvestmentService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Moja Inwestycja")
@PermitAll
public class MyInvestmentView extends VerticalLayout {

    private final ExpenseService service;
    private final InvestmentService investmentService;
    private Dialog investmentForm;
    static String descriptionOfView = "Strona główna Twojej inwestycji - to tutaj znajdują się kluczowe informacje dotyczące projektu.";
    private Investment myInvestment;
    private Component topSectionContent;

    public MyInvestmentView(ExpenseService service, InvestmentService investmentService) {
        this.service = service;
        this.investmentService = investmentService;
        this.myInvestment = investmentService.findAllInvestments().get(0);
        this.setId("myInvestmentView");
        this.addClassName("bg-contrast-5");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        this.topSectionContent = createTopSectionContent();

        configureMyInvestmentForm();

        add(topSectionContent, createBudgetSectionContent(), investmentForm);

        setSizeFull();
    }


    private Component createTopSectionContent() {
        HorizontalLayout layout = new HorizontalLayout();

        Image logoImage = new Image("images/house-logo.png", "Investment logo");
//        logoImage.setHeight("25%");
        logoImage.setWidth("25%");
        logoImage.getStyle().set("padding", "5%");


        VerticalLayout info = new VerticalLayout();
        info.add(createInfoRow("Nazwa inwestycji", myInvestment.getInvestmentName(), VaadinIcon.HOME),
                new Hr(),
                createInfoRow("Data rozpoczęcia inwestycji", String.valueOf(myInvestment.getInvestmentStartDate()), VaadinIcon.DATE_INPUT),
                new Hr(),
                createInfoRow("Budżet (PLN)", String.valueOf(myInvestment.getInvestmentBudget().setScale(0, RoundingMode.FLOOR)), VaadinIcon.MONEY),
                createEditButton());


        info.setSpacing(false);
        info.setPadding(false);
        info.getStyle().set("position", "relative");
        layout.setAlignItems(Alignment.CENTER);
        layout.add(logoImage, info);
        layout.setMaxWidth("70%");
        layout.getStyle().set("box-shadow", "inset 0 -1px var(--lumo-contrast-10pct)");
        return layout;
    }

    private Button createEditButton() {
        Button editButton = new Button(VaadinIcon.EDIT.create());
        editButton.getStyle()
                .set("position", "absolute")
                .set("right", "0")
                .set("top", "0");

//        editButton.addClickListener(e -> editInvestment(myInvestment));
//        editButton.addClickListener(e -> investmentForm.setVisible(true));
        editButton.addClickListener(e -> investmentForm.open());
//        form.addListener(ContactForm.SaveEvent.class, this::saveContact);

        return editButton;
    }

    private void configureMyInvestmentForm() {
        MyInvestmentForm investmentInsideForm = new MyInvestmentForm(myInvestment);
        investmentForm = new Dialog(investmentInsideForm);
        investmentForm.setWidth("25em");
        investmentForm.setCloseOnOutsideClick(false);

        investmentInsideForm.addListener(MyInvestmentForm.SaveEvent.class, this::saveInvestment);
        investmentInsideForm.addListener(MyInvestmentForm.CloseEvent.class, e -> closeForm());


    }

    private void closeForm() {
        investmentForm.close();
//        form.setContact(null);
//        investmentForm.setVisible(false);
    }

//    private void editInvestment(Investment investment) {
//        if (investment == null) {
//            System.out.println("INWESTYCJA PUSTA");
//        } else {
//            investmentForm.setInvestment(investment);
////            investmentForm.setVisible(true);
//            saveInvestment();
//        }
//    }

    private void saveInvestment(MyInvestmentForm.SaveEvent event) {
        investmentService.saveInvestment(event.getInvestment());
        investmentForm.close();
        UI.getCurrent().getPage().reload();
    }


    private Component createInfoRow(String description, String content, VaadinIcon icon) {
        Span primary = new Span(content);
        primary.addClassNames("text-xl");

        Span secondary = new Span(description);
        secondary.addClassNames("text-s");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(Alignment.CENTER);

        FlexLayout flexLayout = new FlexLayout(secondary, primary);
        flexLayout.setFlexDirection(FlexLayout.FlexDirection.COLUMN);

        horizontalLayout.add(icon.create(), flexLayout);
//        horizontalLayout.getStyle().set("background-color", "gray");
        return horizontalLayout;
    }

    private Component createBudgetSectionContent() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
//        Div chart = new Div(configureBudgetChart());
        VerticalLayout chart = new VerticalLayout(createHeader(VaadinIcon.BAR_CHART, "Zestawienie wydatków"), configureBudgetChart());
        VerticalLayout lastExpenses = new VerticalLayout(createHeader(VaadinIcon.CART, "Wydatki z ostatnich 6 miesięcy"), configureLastExpenses());
//        Div lastExpenses = new Div(configureLastExpenses());

        lastExpenses.getStyle().set("padding", "var(--lumo-space-s)");
        chart.getStyle().set("padding", "var(--lumo-space-s)");

        horizontalLayout.add(chart, lastExpenses);
        horizontalLayout.setWidthFull();
        horizontalLayout.setJustifyContentMode(JustifyContentMode.EVENLY);
        return horizontalLayout;
    }


    private Component configureBudgetChart() {
        Chart chart = new Chart(ChartType.COLUMN);

        Configuration conf = chart.getConfiguration();
//        conf.setTitle(new Title("Zestawienie wydatków"));

        XAxis xAxis = new XAxis();
        xAxis.setCategories("Budżet", "Koszt");
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Kwota");
        conf.addyAxis(yAxis);

        Tooltip tooltip = new Tooltip();
        tooltip.setFormatter(
                "function() { return ''+ this.series.name +': '+ this.y +''+' PLN';}");
        conf.setTooltip(tooltip);

        conf.addSeries(new ListSeries("Budżet", (myInvestment.getInvestmentBudget().subtract(service.sumAllExpenses()))));
        conf.addSeries(new ListSeries("Koszty", service.sumAllExpenses()));

        chart.addClassName("appcontent");

        return chart;
    }

    private Component configureLastExpenses() {

        List<Expense> expenses = service.findAllExpenses("");

        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus6Months = currentDate.minusMonths(6);

        List<Expense> lastSixMonthsExpenses = expenses.stream().filter(e -> e.getDateOfExpense()
                .isAfter(currentDateMinus6Months)).collect(Collectors.toList());

        ComponentRenderer<Component, Expense> expenseInfoRenderer = new ComponentRenderer<>(expense -> {

            Span date = new Span("Data: " + expense.getDateOfExpense().toString());
            Span amount = new Span("Kwota: " + expense.getAmount().toString());

            VerticalLayout expenseInformation = new VerticalLayout(date, amount);
            expenseInformation.setSpacing(false);
            expenseInformation.setPadding(false);

            Details details = new Details(expense.getTitle(), expenseInformation);
            details.setOpened(false);
            details.getStyle().set("border-bottom", "solid 1px var(--lumo-contrast-10pct)");

            return details;
        });

        VirtualList list = new VirtualList();
        list.setItems(lastSixMonthsExpenses);
        list.setRenderer(expenseInfoRenderer);
        list.addClassNames("appcontent");
        list.getStyle().set("background", "white");

        return list;

//        border-bottom: solid 1px var(--lumo-contrast-10pct);
    }

    private FlexLayout createHeader(VaadinIcon icon, String title) {
        FlexLayout header = new FlexLayout(
                icon.create(),
                new Label(title));

        header.setAlignItems(Alignment.CENTER);
        addClassName("text-l");
        header.getComponentAt(0).getElement().getStyle().set("margin-right", "var(--lumo-space-xs)");
        return header;
    }


}
