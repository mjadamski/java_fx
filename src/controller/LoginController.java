/**
 * Sample Skeleton for 'loginView.fxml' Controller Class
 */

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.DBConnector;
import service.DialogWindow;
import service.LoginCounter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML // fx:id="tf_login"
    private TextField tf_login; // Value injected by FXMLLoader

    @FXML // fx:id="pf_password"
    private PasswordField pf_password; // Value injected by FXMLLoader

    @FXML // fx:id="tf_password"
    private TextField tf_password; // Value injected by FXMLLoader

    @FXML // fx:id="cb_show"
    private CheckBox cb_show; // Value injected by FXMLLoader

    @FXML // fx:id="btn_login"
    private Button btn_login; // Value injected by FXMLLoader

    @FXML
    void keyLoginAction(KeyEvent event) throws SQLException, ClassNotFoundException, IOException {
        if (event.getCode().equals(KeyCode.ENTER)) {
            System.out.println("via Enter");
            myLoginAction();
        }

    }

    @FXML
    void loginAtion(MouseEvent event) throws SQLException, ClassNotFoundException, IOException {
        myLoginAction();
    }

    public static String email;
    public static int id_u;
    private void myLoginAction() throws SQLException, ClassNotFoundException, IOException {
        DBConnector db = new DBConnector();
        Connection conn = db.getConn();
        db.getConn();
        email = new String(tf_login.getText());

        String pswd = new String("");

        if(cb_show.isSelected()){
            pswd = tf_password.getText();
        } else {
            pswd = pf_password.getText();
        }

        PreparedStatement ps;
        ps = conn.prepareStatement("select permission, email, password, id_u from users where email = ? and password = ?");

        ps.setString(1, email);
        ps.setString(2, pswd);

        ResultSet result = ps.executeQuery();

        if (result.next()) {
            LoginCounter.count = 3;

            String permission = result.getString("permission");
            System.out.println("poprawy Login");
            id_u = result.getInt("id_u");
            System.out.println(id_u);
            if(permission.equals("ROLE_ADMIN")){
                System.out.println("witaj szanowy Adminie");
            } else {
                Stage userStage = new Stage();
                Parent view = FXMLLoader.load(getClass().getResource("/view/userView.fxml"));
                userStage.setTitle("EventManager");
                userStage.setScene(new Scene(view));
                userStage.show();
                //zamkniecie okna logowania po poprawnym zalogowaniu użytkownika
                Stage loginStage = (Stage) btn_login.getScene().getWindow();
                loginStage.close();
            }
        } else {
            DialogWindow dialogWindow = new DialogWindow(
                    Alert.AlertType.ERROR,
                    "Login error",
                    "Error",
                    "błędny login i / lub hasło\n pozostało prób: " + (LoginCounter.count - 1));

            System.out.println("błędny login i / lub hasło\n pozostało prób: " + (LoginCounter.count - 1));

            LoginCounter.logCount();
        }



//        if(tf_login.getText().equals("admin") && (tf_password.getText().equals("admin")) || pf_password.getText().equals("admin")) {
//            System.out.println("Witaj! o Panie wszechmocny!");
//        } else if (tf_login.getText().equals("user") && (tf_password.getText().equals("user") || pf_password.getText().equals("user"))) {
//            System.out.println("Cześć pracowniku");
//
//        } else {
//            DialogWindow dialogWindow = new DialogWindow(
//                    Alert.AlertType.ERROR,
//                    "Login error",
//                    "Error",
//                    "wpisz poprawne dane logowania");
//
//            System.out.println("błędny login i / lub hasło");
//        }
    }

    @FXML
    void showPasswordAction(MouseEvent event) {
        if(cb_show.isSelected()){
            System.out.println("checked");
            tf_password.setVisible(true);
            pf_password.setVisible(false);
//            pobranie hasła z password field i dodanie do text field
            String pswd = pf_password.getText();
            tf_password.setText(pswd);
        } else {
            tf_password.setVisible(false);
            pf_password.setVisible(true);
            System.out.println("unchecked");
            pf_password.setText(tf_password.getText());
        }

    }

}
