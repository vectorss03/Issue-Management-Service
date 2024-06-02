package com.se14.view.panel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.se14.controller.panel.AnalysisController;
import com.se14.domain.IssuePriority;
import com.se14.domain.IssueStatus;
import com.se14.dto.StatisticsDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;

public class AnalysisPanel extends JPanel {
    private final AnalysisController controller;

    public AnalysisPanel(AnalysisController controller) {
        this.controller = controller;
        setLayout(new GridLayout(2,2));
    }

    public void setCharts(StatisticsDTO statistics) {
        removeAll();

        DefaultCategoryDataset dailyDataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : statistics.getDailyIssueCount().entrySet()) {
            dailyDataset.addValue(entry.getValue(), "# Issue Reported", entry.getKey());
        }
        JFreeChart dailyChart = ChartFactory.createBarChart("Daily Issue Reported", "Date", "# Issue Reported", dailyDataset);
        ChartPanel dailyChartPanel = new ChartPanel(dailyChart);

        CategoryItemRenderer dailyRenderer = dailyChart.getCategoryPlot().getRenderer();
        dailyRenderer.setSeriesPaint(0, new Color(255, 99, 132));
        add(dailyChartPanel);


        DefaultCategoryDataset monthlyDataset = new DefaultCategoryDataset();
        for (Map.Entry<String, Integer> entry : statistics.getMonthlyIssueCount().entrySet()) {
            monthlyDataset.addValue(entry.getValue(), "# Issue Reported", entry.getKey());
        }
        JFreeChart monthlyChart = ChartFactory.createBarChart("Monthly Reported Issue", "Date", "# Issue Reported", monthlyDataset);
        ChartPanel monthlyChartPanel = new ChartPanel(monthlyChart);

        CategoryItemRenderer monthlyRenderer = monthlyChart.getCategoryPlot().getRenderer();
        monthlyRenderer.setSeriesPaint(0, new Color(54, 162, 235));
        add(monthlyChartPanel);


        DefaultPieDataset<String> statusDataset = new DefaultPieDataset<>();
        JFreeChart statusChart = ChartFactory.createPieChart("Current Issue Status", statusDataset, true, false, false);
        PiePlot<String> statusPlot = (PiePlot<String>) statusChart.getPlot();
        statusPlot.setBackgroundPaint(Color.white);
        statusPlot.setSimpleLabels(true);
        statusPlot.setLabelBackgroundPaint(Color.white);

        Color[] statusColors = new Color[]{new Color(255, 99, 132), new Color(255, 159, 64), new Color(54, 162, 235), new Color(75, 192, 192), new Color(201, 203, 207), new Color(255, 205, 86)};
        for (IssueStatus status : IssueStatus.values()) {
            if (statistics.getStatusCount().get(status) == null) continue;

            statusDataset.setValue(status.toString(), statistics.getStatusCount().get(status));
            statusPlot.setSectionPaint(status.toString(), statusColors[status.ordinal()]);
            statusPlot.setExplodePercent(status.toString(), 0.05);
        }

        ChartPanel statusChartPanel = new ChartPanel(statusChart);
        add(statusChartPanel);


        DefaultPieDataset<String> priorityDataset = new DefaultPieDataset<>();
        JFreeChart priorityChart = ChartFactory.createPieChart("Current Issue Priority", priorityDataset, true, false, false);

        PiePlot<String> priorityPlot = (PiePlot<String>) priorityChart.getPlot();
        priorityPlot.setBackgroundPaint(Color.white);
        priorityPlot.setSimpleLabels(true);
        priorityPlot.setLabelBackgroundPaint(Color.white);

        Color[] priorityColors = new Color[]{new Color(255, 99, 132), new Color(255, 159, 64), new Color(255, 205, 86), new Color(75, 192, 192), new Color(54, 162, 235)};
        for (IssuePriority priority : IssuePriority.values()) {
            if (statistics.getPriorityCount().get(priority) == null) continue;

            priorityDataset.setValue(priority.toString(), statistics.getPriorityCount().get(priority));
            priorityPlot.setSectionPaint(priority.toString(), priorityColors[priority.ordinal()]);
            priorityPlot.setExplodePercent(priority.toString(), 0.05);
        }
        ChartPanel priorityChartPanel = new ChartPanel(priorityChart);
        add(priorityChartPanel);

        revalidate();
    }

}
