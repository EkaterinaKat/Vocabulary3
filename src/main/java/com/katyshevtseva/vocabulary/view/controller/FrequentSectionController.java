package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.PieChartData;
import com.katyshevtseva.vocabulary.core.Core;
import com.katyshevtseva.vocabulary.core.service.FrequentWordService;
import com.katyshevtseva.vocabulary.view.utils.VocabularyWindowBuilder;
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
        sortButton.setOnAction(event -> VocabularyWindowBuilder.getInstance()
                .openSortingWindow(new SortingController(this::fillChart), this::fillChart));
    }

    private void fillChart() {
        ObservableList<PieChart.Data> observableList = FXCollections.observableArrayList();
        PieChartData pieChartData = service.getStatusCountPieChartData();

        for (PieChartData.Segment segment : pieChartData.getGetSegmentList()) {
            PieChart.Data data = new PieChart.Data(segment.getTitleAmountAndPercentInfo(), segment.getAmount());
            observableList.add(data);
        }

        chart.setData(observableList);
        chart.setTitle(pieChartData.getTitle());
    }
}
