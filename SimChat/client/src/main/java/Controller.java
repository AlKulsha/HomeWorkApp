import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField;

    @FXML
    Button sendBtn;

    public void sendBtnAction(ActionEvent actionEvent) {
        textArea.appendText(msgField.getText() + "\n");
        msgField.clear();
        msgField.requestFocus();
    }

}