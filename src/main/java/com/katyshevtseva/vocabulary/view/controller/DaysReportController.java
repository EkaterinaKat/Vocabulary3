package com.katyshevtseva.vocabulary.view.controller;

import com.katyshevtseva.fx.ReportUtils;
import com.katyshevtseva.fx.WindowBuilder.FxController;
import com.katyshevtseva.general.ReportCell;
import com.katyshevtseva.vocabulary.core.service.DaysReportService;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.List;

public class DaysReportController implements FxController {

    @FXML
    private GridPane tablePane;

    @FXML
    private void initialize() {
        List<List<ReportCell>> report = DaysReportService.getReport();
        ReportUtils.showReport(report, tablePane, false);
    }
}
