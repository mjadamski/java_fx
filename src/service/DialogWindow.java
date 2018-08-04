package service;

import javafx.scene.control.Alert;

public class DialogWindow {
    public DialogWindow(Alert.AlertType alertType, String title, String titleHeader, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(titleHeader);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
