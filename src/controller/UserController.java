package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import service.DBConnector;
import service.DialogWindow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Optional;

public class UserController {

    @FXML
    private ComboBox<String> combo_course;

    @FXML
    private TextArea ta_agenda;

    @FXML
    private RadioButton rb_wege;

    @FXML
    private ToggleGroup rb_feed;

    @FXML
    private RadioButton rb_nongluten;

    @FXML
    private RadioButton rb_normal;

    @FXML
    private CheckBox cb_fv_decission;

    @FXML
    private TextArea ta_fv_details;

    @FXML
    private Button btn_send;


    String [] comboSelected;
    @FXML
    void courseAction(ActionEvent event) throws SQLException, ClassNotFoundException {
//        select course_agenda from course where course_name = "Python" and coursece_date = "2018-9-09" and course_place = "Warszawa";
//        System.out.println(combo_course.getValue());
        if(combo_course.getValue() != null) {
            comboSelected = combo_course.getValue().split(" ");
            System.out.println("pierwsza wartossc " + comboSelected[0]);
            System.out.println("druga wartossc " + comboSelected[1]);
            System.out.println("czecia wartossc " + comboSelected[2]);
        }



        db = new DBConnector();
        Connection conn = db.getConn();

        String sql = "select course_agenda from course where course_name = ? and coursece_date = ? and course_place = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, comboSelected[0]);
        ps.setString(2, comboSelected[1]);
        ps.setString(3, comboSelected[2]);
        ResultSet agenda_result = ps.executeQuery();

        if(agenda_result.next()) {
            ta_agenda.setText(agenda_result.getString("course_agenda"));
        }
    }

    @FXML
    void fvAction(MouseEvent event) {

        if (cb_fv_decission.isSelected()){
            ta_fv_details.setDisable(false);
        } else {
            ta_fv_details.clear();
            ta_fv_details.setDisable(true);
        }

    }
    private String feed;
    private int fv_decision;
    private  String  fv_details;

    @FXML
    void submitAction(MouseEvent event) throws SQLException, ClassNotFoundException {
        if(combo_course.getValue() == null) {
            DialogWindow alert = new DialogWindow(
                    Alert.AlertType.ERROR,
                    "Kurs",
                    "Błąd wyboru kursu",
                    "UWAGA \n Nie wybrano Kursu!!");
        } else {
//            wyzywienie
            if(rb_wege.isSelected()) {
                feed = rb_wege.getText();
            } else if(rb_nongluten.isSelected()){
                feed = rb_nongluten.getText();
            } else if(rb_normal.isSelected()){
                feed = rb_normal.getText();
            } else {
                DialogWindow feedAlert = new DialogWindow(
                        Alert.AlertType.ERROR,
                        "Jedzenie",
                        "jaki posiłek?",
                        "Proszę o dokonanie wyboru diety!");
            }
           // System.out.println(feed);


//            faktura
           if (cb_fv_decission.isSelected()){
               fv_decision = 1;
                fv_details = ta_fv_details.getText();

           } else {
                fv_decision = 0;
                fv_details = null;
           }
//                fv_decision = cb_fv_decission.isSelected() ? 1 : 0;
//               fv_details = cb_fv_decission.isSelected() ? ta_fv_details.getText() : ;
//               System.out.println(LoginController.id_u);
//              INSERT INTO submission (users_id_u, course_id_course, feed, fv_deccission, fv_details) VALUES (?, ?, ?, ?, ?);

            db = new DBConnector();
            Connection conn = db.getConn();
            String subSql = "(SELECT id_course FROM course WHERE course_name = ? and coursece_date = ? and course_place = ?)";

            String sql ="INSERT INTO submission (course_id_course, users_id_u, feed, fv_deccission, fv_details) VALUES (" +
                    subSql + ", ?, ?, ?, ?);";

            PreparedStatement psUpdate = conn.prepareStatement(sql);

            psUpdate.setString(1, comboSelected[0]);
            psUpdate.setString(2, comboSelected[1]);
            psUpdate.setString(3, comboSelected[2]);
            psUpdate.setInt(4, LoginController.id_u);
            psUpdate.setString(5, feed);
            psUpdate.setInt(6, fv_decision);
            psUpdate.setString(7, fv_details);

            System.out.println( comboSelected[0]);
            System.out.println( comboSelected[1]);
            System.out.println( comboSelected[2]);



            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Zapis");
            confirmAlert.setHeaderText("Czy na pewno chcesz się  zapisać na kurs?");
            confirmAlert.setContentText("Jeżeli jesteś pewnien, że chcesz się zapisa klikni OK, jeżeli nie wybierz CANCEL.");
            Optional<ButtonType> confirmDecision = confirmAlert.showAndWait();
            String decision = "";
            if (confirmDecision.get() == ButtonType.OK) {
                psUpdate.executeUpdate();
                System.out.println("zapisano w bazie na nowy kurs " + comboSelected[0] + " " + comboSelected[1] + " " + comboSelected[2]);
                decision = "zapisano w bazie na nowy kurs \n" + comboSelected[0] + " " + comboSelected[1] + " " + comboSelected[2];
            } else {
                decision = "nie zapisano na kurs";
            }
            DialogWindow resultDialog = new DialogWindow(
                    Alert.AlertType.INFORMATION,
                    "Zapisano na kurs",
                    "Inofrmacja o zapisanym kursie",
                    decision
            );
            clearAction();
        }

    }

    private void clearAction(){
        combo_course.setValue(null);
        ta_agenda.clear();
        ta_agenda.setText("tu będzie agenda");
        rb_normal.setSelected(false);
        rb_nongluten.setSelected(false);
        rb_wege.setSelected(false);
        cb_fv_decission.setSelected(false);
        ta_fv_details.clear();
        ta_fv_details.setText("dane do faktury");
        ta_fv_details.setDisable(true);
    }




    //obiekty globalne
    DBConnector db;
    PreparedStatement ps;
    ObservableList<String> courseList = FXCollections.observableArrayList();
//    int id_course;


    public void initialize() throws SQLException, ClassNotFoundException {
//        select course_name, coursece_date, course_place from course;
//        pobranie danych kursu
        db = new DBConnector();
        Connection conn = db.getConn();
        String sql = "select course_name, coursece_date, course_place from course";
        ps = conn.prepareStatement(sql);
        ResultSet course_resoult = ps.executeQuery();

        while (course_resoult.next()) {

            String course_name = course_resoult.getString("course_name");
            String course_date = course_resoult.getString("coursece_date");
            String course_place = course_resoult.getString("course_place");
            courseList.add(course_name + " " + course_date + " " + course_place);
        }
        combo_course.setItems(courseList);


    }
}
