package org.lunapark.dev.scaffold;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.lunapark.dev.scaffold.layout.EventListener;

import java.io.File;

public class Controller implements EventListener {
    public TextField tfName;
    public TextField tfPackage;
    public Button btnGenerate;
    public Button btnQuit;
    public Circle circle;
    public Label lblStatus;
    public Button btnSrc;

    private Stage stage;
    private Creator creator;
    private String srcDir;

    public Controller() {
        creator = new Creator(this);
    }

    public void onGenerateClick() {
        btnGenerate.setDisable(true);
        btnSrc.setDisable(true);
        tfName.setDisable(true);
        tfPackage.setDisable(true);
        String sProjectName = tfName.getText();
        String sPackage = tfPackage.getText();
//        creator.setsProjectName(sProjectName);
//        creator.setsPackageName(sPackage);
        creator.startGenerate(sProjectName, sPackage, srcDir);
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSetSrcClick() {
        try {
            showDialog(stage);
        } catch (Exception e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
        }
    }

    private void showDialog(Stage primaryStage) throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select source directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            btnGenerate.setDisable(false);
            setSource(dir);
        }
    }

    public void closeApp() {
        System.exit(0);
    }

    @Override
    public void complete() {
        circle.setVisible(true);
    }

    @Override
    public void onEvent(String message) {
        lblStatus.setText(message);
        System.out.println(message);
    }

    @Override
    public void setSource(File file) {
        String sSource = file.getAbsolutePath();
        System.out.println(String.format("set source: %s", sSource));
        btnSrc.setText(sSource);
        srcDir = sSource;
//        creator.setSourceDir(sSource);
    }
}
