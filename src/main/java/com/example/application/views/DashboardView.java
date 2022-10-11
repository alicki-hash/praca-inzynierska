package com.example.application.views;

import com.example.application.data.service.ExpenseService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Wykres")
@PermitAll
public class DashboardView extends VerticalLayout {

    private final ExpenseService service;

    static String descriptionOfView = "W tej zakładce możesz przeanalizować swoje wydatki na podstawie wykresów";


    public DashboardView(ExpenseService service) {
        this.addClassName("bg-contrast-5");
        this.service = service;
        this.setSizeFull();
        addClassName("dashboard-view");
        add(getExpensesPieChart(), getExpensesColumnChart());
    }

//    private Component getExpensesStats() {
//        Span stats = new Span("Suma wydatków: " + service.sumAllExpenses() + " zł");
//        stats.addClassNames("text-xl", "mt-m");
//
//        return stats;
//    }

    private Component getExpensesPieChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.addClassNames("appcontent");
        DataSeries dataSeries = new DataSeries();
        service.findAllWorkScopes().forEach(workScope -> {
            dataSeries.add(new DataSeriesItem(workScope.getName(),
                    service.sumExpensesByWorkscope(workScope.getName())));
        });

        chart.getConfiguration().setSeries(dataSeries);
        chart.getConfiguration().setTitle("Suma wydatków do tej pory: " + service.sumAllExpenses() + " zł");

        Tooltip tooltip = new Tooltip();
        tooltip.setValueDecimals(1);
        tooltip.setPointFormat("<b> {point.y} PLN</b>");
        chart.getConfiguration().setTooltip(tooltip);

        return chart;
    }

    private Component getExpensesColumnChart() {
        Chart chart = new Chart(ChartType.COLUMN);
        chart.addClassNames("appcontent");
        Configuration configuration = chart.getConfiguration();

//        DataSeries dataSeries = new DataSeries();

//        configuration.getLegend().setEnabled(true);

        List allExpensesYears = service.getAllExpensesYears();

        for(Object year : allExpensesYears){
            DataSeries dataSeries = new DataSeries();
            dataSeries.add(new DataSeriesItem("Sty", service.sumExpensesByDate((int)year, 1)));
            dataSeries.add(new DataSeriesItem("Lut", service.sumExpensesByDate((int)year, 2)));
            dataSeries.add(new DataSeriesItem("Mar", service.sumExpensesByDate((int)year, 3)));
            dataSeries.add(new DataSeriesItem("Kwi", service.sumExpensesByDate((int)year, 4)));
            dataSeries.add(new DataSeriesItem("Maj", service.sumExpensesByDate((int)year, 5)));
            dataSeries.add(new DataSeriesItem("Cze", service.sumExpensesByDate((int)year, 6)));
            dataSeries.add(new DataSeriesItem("Lip", service.sumExpensesByDate((int)year, 7)));
            dataSeries.add(new DataSeriesItem("Sie", service.sumExpensesByDate((int)year, 8)));
            dataSeries.add(new DataSeriesItem("Wrz", service.sumExpensesByDate((int)year, 9)));
            dataSeries.add(new DataSeriesItem("Paź", service.sumExpensesByDate((int)year, 10)));
            dataSeries.add(new DataSeriesItem("Lis", service.sumExpensesByDate((int)year, 11)));
            dataSeries.add(new DataSeriesItem("Gru", service.sumExpensesByDate((int)year, 12)));

            dataSeries.setName(String.valueOf(year));
            configuration.addSeries(dataSeries);
        }

        Legend legend = new Legend();
        configuration.setLegend(legend);


        configuration.setTitle("Wydatki na przełomie miesięcy: ");


        XAxis x = new XAxis();
        configuration.getxAxis().setType(AxisType.CATEGORY);
        x.setCategories("Sty", "Lut", "Mar", "Kwi", "Maj", "Cze", "Lip",
                "Sie", "Wrz", "Paź", "Lis", "Gru");
        configuration.addxAxis(x);

        YAxis y = new YAxis();
        y.setMin(0);
        y.setTitle("Kwota");
        configuration.addyAxis(y);


        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));
        column.getDataLabels().setFormatter("function() { return this.y + ' PLN';}");
        configuration.setPlotOptions(column);


        return chart;
    }
}
