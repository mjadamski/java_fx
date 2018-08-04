package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class UserController {

    @FXML
    private Label lbl_email;

    //metoda, która zawszę się uruchamia jako pierwsza w kontrolerze
    public void initialize(){
        lbl_email.setText(lbl_email.getText() + LoginController.email);
    }

}
