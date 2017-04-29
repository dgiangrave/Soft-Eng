package NewMainMapManagement;

import DBController.DatabaseController;
import adminMenuStart.AutoLogout;
import controllers.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.TextFields;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by AugustoR on 4/27/17.
 */
public class NewMainMapManagementController extends controllers.mapScene {
    @FXML
    private AnchorPane backgroundAnchorPane;

    @FXML
    private Label mainTitle_Label;

    @FXML
    private Button directoryManagement_Button;

    @FXML
    private Button adminManagement_Button;

    @FXML
    private Button signOut_Button;

    @FXML
    private Button emergency_Button;

    @FXML
    private Label LogInPerson_Label;

    @FXML
    private StackPane mapStack;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane admin_FloorPane;

    @FXML
    private ImageView map_viewer;

    @FXML
    private Button zoomOut_button;

    @FXML
    private ChoiceBox<String> floor_ChoiceBox;

    @FXML
    private Button zoomIn_button;

    @FXML
    private Button clear_Button;

    @FXML
    private Button save_Button;

    private int nodeEdgeX1;
    private int nodeEdgeY1;
    private int nodeEdgeX2;
    private int nodeEdgeY2;

    private controllers.MapOverlay graph;

    private static final double labelRadius = 10.5;

    private Node firstNode;
    private Circle dragCircle;
    private Node dragNode;

    private Circle lastColoredStart;

    private Circle lastColoredEnd;

    private int edgesSelected = 0;

    private boolean selectedNode;


    private Circle btK;
    private boolean addSingleEdgeMode;
    private boolean addMultiEdgeMode;
    private boolean dragMode;
    private boolean popoverShown;

    private int permissionLevel;

    private final Circle[] temporaryButton = {null};



    //Set to english by default
    private int c_language = 0;

    private int currentFloor;

    private DatabaseController databaseController = DatabaseController.getInstance();

    private AutoLogout al = new AutoLogout();

    public void initialize() {
        setUserString(LogInPerson_Label.getText());
        addSingleEdgeMode = false;
        addMultiEdgeMode = false;
        popoverShown = false;

        selectedNode = false;

        //set default floor to start
        //we will use floor 1 for now
        currentFloor = 1;
        //set to admin level
        permissionLevel = 2;

        dragMode = false;

        graph = new controllers.MapOverlay(admin_FloorPane,(mapScene) this);
        MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true,
                currentFloor, permissionLevel);

        setFloorChoices();

        // creates a node when clicking the map
        map_viewer.setOnMouseClicked((MouseEvent e) -> {
            //clear on any selection stuff for the rest of the map
            addSingleEdgeMode = false;
            if (addMultiEdgeMode) {
                //dont try to add node if just trying to click out of multi-edge selection
                addMultiEdgeMode = false;
            } else if(dragMode) {
                dragMode = false;
                dragModeUpdate();
            } else if (selectedNode) {
                selectedNode = false;
                graph.wipeEdgeLines();
                //color the node as well
                if (lastColoredStart != null) {
                    lastColoredStart.setStroke(lastColoredStart.getFill());
                    lastColoredStart.setStrokeWidth(1);
                    lastColoredStart = null;
                }
            } else if(popoverShown) {
                popoverShown = false;
                if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)){
                    admin_FloorPane.getChildren().remove(temporaryButton[0]);
                }
            } else {
                graph.wipeEdgeLines();

                if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)){
                    admin_FloorPane.getChildren().remove(temporaryButton[0]);
                }
                btK = new Circle(labelRadius);//new Button();
                btK.setLayoutX(e.getX());
                btK.setLayoutY(e.getY());
                admin_FloorPane.getChildren().add(btK);
                temporaryButton[0] = btK;

                //set the popovershown var
                popoverShown = true;

                PopOver pop = new PopOver();
                createPop(pop, btK, "Create");
                pop.show(btK);
            }
        });

        try {
            al.wait(20);
            fuckABitch();
        } catch (InterruptedException e) {
            System.out.println("FUCK");
        }


    }

    public void fuckABitch() {
        al.checkActivity();
    }

    public PopOver createMultiFloorPop(PopOver pop, Circle btK, ArrayList<Integer> floors, Node selectedNode) {

        ArrayList<Edge> deleteThese = new ArrayList<>();
        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 5, 10));

        VBox vb = new VBox();

        HBox hbCancelSave = new HBox();

        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);

        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(60);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);

        // give it a list of multifloor edges for this node
        // so it can parse through and get the floors it is connected to
        for (int f : floors) {
            // fields.add(new TextField(/*floor number parsed*/));
            TextField thisField = new TextField(Integer.toString(f));
            thisField.setAlignment(Pos.CENTER);
            thisField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.equals("")) {
                    // get node with these button coordinates
                    ArrayList<Edge> edges = selectedNode.getEdgeList();
                    for (Edge e : edges) {
                        if (e.getEndNode().getFloor() == Integer.parseInt(oldValue)) {
                            deleteThese.add(e);
                        }
                    }

                }
            });
            vb.getChildren().add(thisField);
        }

        vb.getChildren().addAll(hbCancelSave);
        anchorpane.getChildren().addAll(grid, vb);
        AnchorPane.setBottomAnchor(vb, 8.0);
        AnchorPane.setRightAnchor(vb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        pop.setDetachable(true);
        pop.setDetached(false);
        pop.setCornerRadius(4);
        pop.setContentNode(anchorpane);

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pop.hide();
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                for (Edge ed : deleteThese) {
                    databaseController.deleteEdge(ed.getStartNode().getPosX(), ed.getStartNode().getPosY(),
                            ed.getStartNode().getFloor(), ed.getEndNode().getPosX(), ed.getEndNode().getPosY(),
                            ed.getEndNode().getFloor());
                }
                pop.hide();
                resetScreen();
            }
        });
        return pop;

    }

    private void dragModeUpdate() {
        graph.wipeEdgeLines();
        if (dragNode != null) {
            System.out.println("drag done");
            System.out.println("old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
            System.out.println("new: x= " + dragCircle.getLayoutX() + ", y= " + dragCircle.getLayoutY());
            System.out.println("---");
            ArrayList<Node> neighborlist = new ArrayList<>();

            //need to see if actually moved it though.
            if (dragNode.getPosX() != dragCircle.getLayoutX() || dragNode.getPosY() != dragCircle.getLayoutY() ||
                    dragNode.getFloor() != currentFloor) {

                for (controllers.Edge thisEdge : dragNode.getEdgeList()) {
                    Node temp = thisEdge.getNeighbor(dragNode);
                    neighborlist.add(temp);
                    temp.getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }

                databaseController.newNode((int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor, dragNode.getIsHidden(),
                        dragNode.getEnabled(), dragNode.getType(), dragNode.getName(),
                        "SOFTENGWPIsjijflkjjfjjfklaljjjfalkjooejallajjjflijjfflRyanIsAwesome",
                        dragNode.getPermissionLevel());

                databaseController.transferNodeLoc(dragNode.getPosX(), dragNode.getPosY(), dragNode.getFloor(),
                        (int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor);

                databaseController.deleteNode(dragNode.getPosX(), dragNode.getPosY(), currentFloor);

                databaseController.updateNode((int) dragCircle.getLayoutX(), (int) dragCircle.getLayoutY(), currentFloor, dragNode.getIsHidden(),
                        dragNode.getEnabled(), dragNode.getType(), dragNode.getName(),
                        dragNode.getRoomNum(), dragNode.getPermissionLevel());

                //add the edges to the new node
                for (Node n : neighborlist) {
                    DatabaseController.getInstance().newEdge((int) dragCircle.getLayoutX(),
                            (int) dragCircle.getLayoutY(), currentFloor,
                            n.getPosX(), n.getPosY(), n.getFloor());
                }
            }
            resetScreen();

            //if successful, highlight node and show edges
            //TODO >??
        }
    }

    public PopOver createPop(PopOver pop, Circle btK, String mode){

        AnchorPane anchorpane = new AnchorPane();
        Button buttonSave = new Button("Save");
        Button buttonCancel = new Button("Cancel");
        Label nameLabel = new Label("Name");
        Label typeLabel = new Label("Type");
        Label roomLabel = new Label("Room Number");
        Label permissionLabel = new Label("Permission");
        ChoiceBox permissionBox = new ChoiceBox(FXCollections.observableArrayList(
                "Visitor", "Employee", "Admin"));
        TextField nodeName = new TextField();
        TextField nodeType = new TextField();
        ArrayList<String> types;
        types = databaseController.getNodeTypes();
        TextFields.bindAutoCompletion(nodeType, types);
        TextField nodeRoom = new TextField();
        CheckBox isHidden = new CheckBox("Hidden");
        CheckBox isEnabled = new CheckBox("Enabled");
        isHidden.setSelected(false);
        isEnabled.setSelected(true);
        nodeName.setPromptText("Name");
        nodeType.setPromptText("Type");
        nodeRoom.setPromptText("Room Number");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 5, 10));

        VBox vb = new VBox();
        HBox hbCancelSave = new HBox();
        HBox hbCheckBox = new HBox();
        HBox hbPermission = new HBox();
        vb.setPadding(new Insets(10, 10, 5, 10));
        vb.setSpacing(10);
        hbCancelSave.setPadding(new Insets(0, 0, 0, 0));
        hbCancelSave.setSpacing(60);
        hbCancelSave.getChildren().addAll(buttonCancel, buttonSave);
        hbCheckBox.getChildren().addAll(isHidden, isEnabled);
        hbCheckBox.setSpacing(25);
        hbPermission.setPadding(new Insets(0, 0, 0, 0));
        hbPermission.getChildren().addAll(permissionLabel, permissionBox);
        hbPermission.setSpacing(10);

        vb.getChildren().addAll(nameLabel, nodeName,
                typeLabel, nodeType, roomLabel, nodeRoom, hbCheckBox,
                hbPermission, hbCancelSave);
        anchorpane.getChildren().addAll(grid,vb);   // Add grid from Example 1-5
        AnchorPane.setBottomAnchor(vb, 8.0);
        AnchorPane.setRightAnchor(vb, 5.0);
        AnchorPane.setTopAnchor(grid, 10.0);

        if (mode.equals("Edit")){
            ResultSet rset = databaseController.getNode((int) btK.getLayoutX(), (int) btK.getLayoutY(), currentFloor);
            try {
                while (rset.next()){
                    nodeName.setText(rset.getString("NAME"));
                    nodeType.setText(rset.getString("TYPE"));
                    nodeRoom.setText(rset.getString("ROOMNUM"));
                    if (rset.getInt("PERMISSIONS") == 0){
                        permissionBox.getSelectionModel().select(0);
                    } else if (rset.getInt("PERMISSIONS") == 1){
                        permissionBox.getSelectionModel().select(1);
                    } else {
                        permissionBox.getSelectionModel().select(2);
                    }
                    if(rset.getBoolean("ISHIDDEN")){
                        isHidden.setSelected(true);
                    }
                    if (rset.getBoolean("ENABLED")){
                        isEnabled.setSelected(true);
                    }
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }


        pop.setDetachable(true);
        pop.setDetached(false);
        pop.setCornerRadius(4);
        pop.setContentNode(anchorpane);

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String thisNodeName = nodeName.getText();
                String thisNodeType = nodeType.getText();
                String thisNodeRoom = nodeRoom.getText();
                if (!thisNodeName.equals("") && !thisNodeType.equals("") && !thisNodeRoom.equals("")) {
                    int permission;
                    if (permissionBox.getValue().toString().equals("Admin")){
                        permission = 2;
                    } else if (permissionBox.getValue().toString().equals("Employee")){
                        permission = 1;
                    } else {
                        permission = 0;
                    }
                    if (mode.equals("Edit")) {
                        DBController.DatabaseController.getInstance().updateNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, permission);
                        pop.hide();
                        admin_FloorPane.getChildren().remove(btK);
                        resetScreen();
                    } else if (mode.equals("Create")){
                        DBController.DatabaseController.getInstance().newNode((int) btK.getLayoutX(), (int) btK.getLayoutY(),
                                currentFloor, isHidden.isSelected(), isEnabled.isSelected(), thisNodeType, thisNodeName, thisNodeRoom, permission);
                        pop.hide();
                        admin_FloorPane.getChildren().remove(btK);
                        resetScreen();
                    }
                }
            }
        });

        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pop.hide();
                if (mode.equals("Create")) {
                    admin_FloorPane.getChildren().remove(btK);
                }
            }
        });

        return pop;
    }

    //requeries database and resets screen
    public void resetScreen() {
        controllers.MapController.getInstance().requestMapCopy();
        graph.setMapAndNodes(controllers.MapController.getInstance().getCollectionOfNodes().getMap(currentFloor), true,
                currentFloor, permissionLevel);
        graph.wipeEdgeLines();
        edgesSelected = 0;

    }

    public void rightClickEvent(int x, int y, Circle c, int mode) {
        //CODE TO HANDLE RIGHT CLICK MENU STUFF GOES HERE:
        switch (mode) {
            case 1: // remove node
                Node selectedNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (selectedNode == null) {
                    break;
                }
                for (controllers.Edge thisEdge : selectedNode.getEdgeList()) {
                    thisEdge.getNeighbor(selectedNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                databaseController.deleteNode(x, y, currentFloor);
                System.out.println("deleting node...");

                resetScreen();
                break;
            case 2:  // edit node
                PopOver pop = new PopOver();
                createPop(pop, c, "Edit");
                pop.show(c);
                break;
            case 3:
                MapController.getInstance().attachSurroundingNodes(x, y, currentFloor);
                resetScreen();
                //show some edge lines as visual feedback:
                Node temp = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                graph.createEdgeLines(temp.getEdgeList(), true, true);
                break;
            case 4:
                addSingleEdgeMode = true;

                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(x, y, currentFloor);
                if (firstNode == null) {
                    System.out.println("RIP trying to get node");
                    resetScreen();
                    clearButton_Clicked();
                    break;
                }
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);

                break;
            case 5:
                addMultiEdgeMode = true;
                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(x, y, currentFloor);
                if (firstNode == null) {
                    System.out.println("RIP trying to get node");
                    resetScreen();
                    clearButton_Clicked();
                    break;
                }
                graph.createEdgeLines(firstNode.getEdgeList(), true, false);
                break;
            case 6:
                Node thisNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (thisNode == null) {
                    break;
                }
                for (controllers.Edge thisEdge : thisNode.getEdgeList()) {
                    thisEdge.getNeighbor(thisNode).getEdgeList().remove(thisEdge);
                    DBController.DatabaseController.getInstance().deleteEdge(thisEdge.getStartNode().getPosX(),
                            thisEdge.getStartNode().getPosY(), thisEdge.getStartNode().getFloor(), thisEdge.getEndNode().getPosX(),
                            thisEdge.getEndNode().getPosY(), thisEdge.getEndNode().getFloor());
                }
                resetScreen();
                break;
            case 7:
                //draggable code:
                System.out.println("draggable");
                dragMode = true;
                dragMode = true;
                final Bounds paneBounds = admin_FloorPane.localToScene(admin_FloorPane.getBoundsInLocal());
                dragCircle = c;
                dragNode = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                if (dragNode == null) {
                    System.out.println("ERROR GETTING DRAG NODE");
                    break;
                }
                System.out.println("test old: x= " + dragNode.getPosX() + ", y= " + dragNode.getPosY());
                //wipe edge lines from screen to not clutter stuff up
                graph.wipeEdgeLines();
                //This code is for placing nodes
                c.setOnMouseDragged(e -> {
                    if (e.getSceneX() > paneBounds.getMinX() && e.getSceneX() < paneBounds.getMaxX()
                            && e.getSceneY() > paneBounds.getMinY() && e.getSceneY() < paneBounds.getMaxY()) {
                        c.setLayoutX((e.getSceneX() - paneBounds.getMinX()));
                        c.setLayoutY((e.getSceneY() - paneBounds.getMinY()));
                    }
                });
                break;
            case 8: //  click on stair/elevator node
                Node selectedNode2 = MapController.getInstance().getCollectionOfNodes().getNode(x, y, currentFloor);
                //handle errors
                if (selectedNode2 == null) {
                    break;
                }
                ArrayList<Edge> edges = selectedNode2.getEdgeList();
                ArrayList<Integer> floors = new ArrayList<>();
                for (Edge e : edges){
                    if (!floors.contains(e.getEndNode().getFloor())){
                        floors.add(e.getEndNode().getFloor());
                    }
                }
                PopOver pop2 = new PopOver();
                createMultiFloorPop(pop2, c, floors, selectedNode2);
                pop2.show(c);
                break;
            default:
                System.out.println("default. This probably should not have been possible...");
                break;
        }
    }

    //handle a click on an edge.
    public void edgeClickRemove(int x1, int y1, int x2, int y2){
        DBController.DatabaseController.getInstance().deleteEdge(x1,
                y1, currentFloor, x2, y2, currentFloor);
        System.out.println("removed edge on click");
        resetScreen();
        if (firstNode != null) {
            Node temp = MapController.getInstance().getCollectionOfNodes().getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
            graph.createEdgeLines(temp.getEdgeList(), true, true);
        }
    }

    public void sceneEvent(int x, int y, Circle c) {

        //add edge from menu (both multi and single)
        if (addSingleEdgeMode || addMultiEdgeMode) {
            System.out.println("adding edge...");
            nodeEdgeX2 = (int) x;
            nodeEdgeY2 = (int) y;
            DBController.DatabaseController.getInstance().newEdge(firstNode.getPosX(),
                    firstNode.getPosY(), firstNode.getFloor(), nodeEdgeX2, nodeEdgeY2, currentFloor);
            resetScreen();
            addSingleEdgeMode = false;
            if (firstNode != null) {
                firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                        .getNode(firstNode.getPosX(), firstNode.getPosY(), firstNode.getFloor());
                //don't know if above method is successful
                //must check again if firstNode is not null
                if (firstNode != null) {
                    graph.createEdgeLines(firstNode.getEdgeList(), true, true);
                    c.toFront();
                }
            }

            return;
        }

        //don't highlight if in drag mode
        if (dragMode) {
            return;
        }
        //set the selected node switch
        selectedNode = true;

        //remove any temporary nodes
        if (popoverShown){
            if (temporaryButton[0] != null && !databaseController.isActualLocation((int) temporaryButton[0].getLayoutX(), (int) temporaryButton[0].getLayoutY(), currentFloor)){
                admin_FloorPane.getChildren().remove(temporaryButton[0]);
            }

        }
        //highlight the node
        nodeEdgeX1 = (int) x;
        nodeEdgeY1 = (int) y;
        System.out.println(nodeEdgeX1 + "     " + nodeEdgeY1);
        firstNode = controllers.MapController.getInstance().getCollectionOfNodes()
                .getNode(nodeEdgeX1, nodeEdgeY1, currentFloor);
        graph.createEdgeLines(firstNode.getEdgeList(), true, true);

        //color the node as well
        if (lastColoredStart !=  null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
            lastColoredStart = null;
        }
        lastColoredStart = c;
        c.setStrokeWidth(6.5);
        c.setStroke(Color.ROYALBLUE);
        c.toFront();

    }

    //Change to main Menu
    public void mainMenuButton_Clicked() {

        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        adminMenuStart.adminMenuStartController controller = loader.getController();
        //Set the correct username for the next scene
        controller.setUsername(LogInPerson_Label.getText());
        System.out.println(LogInPerson_Label.getText());

        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();

            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
        controller.setLanguageChoices();
    }

    public void setUserString(String user) {
        LogInPerson_Label.setText(user);
    }


    //Sets the map of the desired floor
    public void setFloorChoices(){
        floor_ChoiceBox.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "Outside",
                "Belkin 1", "Belkin 2", "Belkin 3", "Belkin 4", "Belkin Basement");

        //reset ui interaction
        dragMode = false;
        popoverShown = false;
        selectedNode = false;


        floor_ChoiceBox.getSelectionModel().select(0);
        map_viewer.setImage(new Image("/images/cleaned1.png"));
        floor_ChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                boolean outside = false;
                String currentF = "";
                //Print the floors accordingly
                //CODE HERE!!!!!!!

                if (newValue.intValue() == 7) {
                    //outside
                    currentFloor = 0;
                } else if(newValue.intValue() > 7) {
                    currentFloor = newValue.intValue();
                } else {
                    currentFloor = newValue.intValue() + 1;
                }

                mapImage newMapImage = new proxyMap(currentFloor);
                newMapImage.display(map_viewer);

                if(!outside) {
                    //c_Floor_Label.setText(Integer.toString(currentFloor));
                }else{
                    //c_Floor_Label.setText("");
                    //floor_Label.setText(currentF);
                }
                //true ot see nodes false otherwise
                graph.setMapAndNodes(MapController.getInstance().getCollectionOfNodes().getMap(currentFloor),true,
                        currentFloor, permissionLevel);
            }
        });

    }

    //sets the current language given information form other screens
    public void setC_language(int i){
        c_language = i;
    }

    //Signs the user out
    public void signOutButton_Clicked(){
        /*FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/NewIntroUIView.fxml");
        //patientMenuStart.patientMenuStartController controller = loader.getController();
        NewIntroUI.NewIntroUIController controller = loader.getController();
        //sets the current language
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
        controller.englishButtons_Labels();
        //set up spanish labels
    }else if(c_language == 1){
        controller.spanishButtons_Labels();
    }
        //set permissions back
        controller.setPermissionLevel(0);
        //set label to empty
        controller.setWelcome("");*/
    }

    //Manage when the directory button is clicked
    public void DirectoryManButton_Clicked(){

    }

    //Manages when the admin management button is clicked
    public void AdminManButton_Clicked(){

    }

    //Manages when the emergency button is clicked
    public void emergencyButton_Clicked(){
        FXMLLoader loader = switch_screen(backgroundAnchorPane, "/views/emergencyView.fxml");
        emergency.emergencyController controller = loader.getController();
        //sends the current language to the next screen
        controller.setCurrentLanguage(c_language);
        //set up english labels
        if(c_language == 0){
            controller.englishButtons_Labels();
            //set up spanish labels
        }else if(c_language == 1){
            controller.spanishButtons_Labels();
        }
    }

    //Manages when the user clicks the save button
    public void saveButton_Clicked(){

    }

    //Manages when the user clicks the clear button
    public void clearButton_Clicked(){

        graph.wipeEdgeLines();
        edgesSelected = 0;

        addSingleEdgeMode = false;
        addMultiEdgeMode = false;

        //reset last colored stroke to default
        if (lastColoredStart != null) {
            lastColoredStart.setStroke(lastColoredStart.getFill());
            lastColoredStart.setStrokeWidth(1);
        }

        if (lastColoredEnd != null) {
            lastColoredEnd.setStroke(lastColoredEnd.getFill());
            lastColoredEnd.setStrokeWidth(1);
        }
    }


    //Zooms the map in
    public void zoomInButton_Clicked() {

    }

    //Zooms out the map
    public void zoomOutButton_Clicked() {

    }

    //Labels for english
    public void englishButtons_Labels(){

    }
    //Labels for spanish
    public void spanishButtons_Labels(){

    }

    public void setUser(String user){
        LogInPerson_Label.setText(user);

    }

    public void autoLogout() {
        System.out.println("The system has automatically logged off due to inactivity.");
        //backgroundAnchorPane = new AnchorPane();
        //Change to patient menu
        //if(backgroundAnchorPane == null) {
        //    System.out.println("backgroundAnchorPane is null");
        //} else {
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NewIntroUIView.fxml"));
        if(loader.getController() == null) {
            System.out.println("fuck");
        }else {
            NewIntroUI.NewIntroUIController controller = loader.getController();
            //sends the current language to the next screen

            controller.setCurrentLanguage(c_language);
            //set up english labels
            if (c_language == 0) {
                controller.englishButtons_Labels();
                //set up spanish labels
            } else if (c_language == 1) {
                controller.spanishButtons_Labels();
            }
        }
        */
        emergencyButton_Clicked();
    }
}