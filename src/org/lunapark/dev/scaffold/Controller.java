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
    public Button btnTarget;

    private Stage stage;
    private Creator creator;
    private String srcDir, targetDir;

    public Controller() {
        creator = new Creator(this);
    }

    public void onGenerateClick() {
        disableAll(true);
        String sProjectName = tfName.getText();
        String sPackage = tfPackage.getText();
        creator.startGenerate(sProjectName, sPackage, srcDir, targetDir);
    }

    private void disableAll(boolean isDisable) {
        btnGenerate.setDisable(isDisable);
        btnSrc.setDisable(isDisable);
        tfName.setDisable(isDisable);
        tfPackage.setDisable(isDisable);
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    public void onSetSrcClick() {
        try {
            setSource(showDialog(stage));
        } catch (Exception e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
//            showWarning(e.getMessage());
        }
    }

    public void onSetTargetClick() {
        try {
            setTargetDir(showDialog(stage));
        } catch (Exception e) {
            System.out.println(String.format("Error: %s", e.getMessage()));
//            showWarning(e.getMessage());
        }
    }

    private String showDialog(Stage primaryStage) throws Exception {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select directory");

        String defaultDir = System.getProperty("user.dir");
        //        String defaultDir = "e:\\LibGDX_template\\template\\";
        directoryChooser.setInitialDirectory(new File(defaultDir));
        File dir = directoryChooser.showDialog(primaryStage);
        return dir.getAbsolutePath();
    }

    private void checkFolders() {
        if (srcDir != null && targetDir != null) btnGenerate.setDisable(false);
    }

    public void closeApp() {
        System.exit(0);
    }

    @Override
    public void complete() {
        circle.setVisible(true);
        disableAll(false);
    }

    @Override
    public void onEvent(String message) {
        lblStatus.setText(message);
        System.out.println(message);
    }


    private void setSource(String sourcePath) {
        System.out.println(String.format("set source: %s", sourcePath));
        btnSrc.setText(sourcePath);
        srcDir = sourcePath;
        checkFolders();
    }

    private void setTargetDir(String targetPath) {
        System.out.println(String.format("set target: %s", targetPath));
        btnTarget.setText(targetPath);
        targetDir = targetPath;
        checkFolders();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Lunar Scaffolder");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
