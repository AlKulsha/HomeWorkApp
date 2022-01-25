package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegController {

@FXML public TextField loginField;
@FXML public PasswordField passwordField;
@FXML public TextField nickField;
@FXML public TextArea textArea;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void clickBtnReg(ActionEvent actionEvent) {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String nickname = nickField.getText().trim();
        controller.tryToReg(login, password, nickname);

    }

    public void regStatus(String result){
        if(result.equals("/reg_ok")){
            textArea.appendText("Регистрация прошла успешно");
        } else {
            textArea.appendText("Регистрация не завершена. Выберите другой логин или пароль\n");
        }
    }
}
