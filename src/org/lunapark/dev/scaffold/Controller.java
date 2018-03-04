package org.lunapark.dev.scaffold;

import javafx.scene.control.Alert;
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
        if (srcDir != null) {
            btnGenerate.setDisable(true);
            btnSrc.setDisable(true);
            tfName.setDisable(true);
            tfPackage.setDisable(true);

            String sProjectName = tfName.getText();
            String sPackage = tfPackage.getText();
            creator.startGenerate(sProjectName, sPackage, srcDir);
        }
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSetSrcClick() {
        try {
            showDialog(stage);
        } catch (Exception e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
            showWarning(e.getMessage());
        }
    }

    private void showDialog(Stage primaryStage) throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select source directory");
//        TODO Mock data
//        String defaultDir = System.getProperty("user.dir");
        String defaultDir = "e:\\LibGDX_template\\template\\";
        directoryChooser.setInitialDirectory(new File(defaultDir));
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

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lunar Scaffolder");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
