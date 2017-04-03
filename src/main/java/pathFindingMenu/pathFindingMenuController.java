package pathFindingMenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

/**
 * Created by AugustoR on 3/30/17.
 */
public class pathFindingMenuController extends controllers.AbsController{
    @FXML
    private AnchorPane backgroundAnchorPane;
    @FXML
    private  ImageView view;
    @FXML
    private Circle blackCircle_Circle;

    @FXML
    private Circle redCircle_Circle;

    @FXML
    private Button emergency_Button;

    @FXML
    private Button mainMenu_Button;

    @FXML
    private Button cancel_Button;

    @FXML
    private Button submit_Button;

    @FXML
    private Label currentFloor_Label;

    @FXML
    private Label mode_Label;

    @FXML
    private Label username_Label;


    public void emergencyButton_Clicked(){
        System.out.println("The user has clicked the emergency Button");
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
    }

    public void cancelButton_Clicked(){

        System.out.println("The user has clicked the cancel Button");
    }

    public void submitButton_Clicked(){

        System.out.println("The user has clicked the submit Button");
    }

    public void mainMenuButton_Clicked(){
        if(username_Label.getText().equals("")) {
            switch_screen(backgroundAnchorPane, "/views/patientMenuStartView.fxml");
        }else{
            //Get the scene loader
            FXMLLoader loader = switch_screen(backgroundAnchorPane,"/views/adminMenuStartView.fxml");
            //Get the controller of the scene
            adminMenuStart.adminMenuStartController controller = loader.getController();
            controller.setUsername(username_Label.getText());

        }

    }

    public void setMode(String mode){
        mode_Label.setText(mode);

    }

    public void setUserString(String user){
        username_Label.setText(user);

    }




}
