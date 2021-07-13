package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.entity.FrequentWord.Status;
import com.katyshevtseva.vocabulary.core.service.FrequentWordService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;

public class FrequentSectionController implements FxController {
    private FrequentWordService service = Core.getInstance().frequentWordService();
    @FXML
    private PieChart chart;
    @FXML
    private Button sortButton;

    @FXML
    private void initialize() {
        fillChart();
    }

    private void fillChart() {
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        int total = 0;

        for (Status status : Status.values()) {
            int count = service.getStatusCount(status);
            total += count;
            PieChart.Data data = new PieChart.Data(status.toString() + " - " + count, count);
            chartData.add(data);
        }

        chart.setData(chartData);
        chart.setTitle("Total: " + total);
    }
}
