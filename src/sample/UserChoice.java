package sample;

import java.util.*;

// Imports for JavaFx
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.geometry.*;

class UserChoice
{
    static private ArrayList<UserDataNode> users = new ArrayList<UserDataNode>();

    static void ShowUserChoice(StringBuilder user)
    {
        // Force add new user if json file is missing
        if(!JsonFileMgr.GetDataFromJson(users))
        {
            BuildAddUserWindow(user);
            return;
        }

        // Choose user if list is not empty otherwise force add user
        if(users.size() > 0)
            BuildChooseUserWindow(user);
        else
            BuildAddUserWindow(user);
    }

    static void BuildAddUserWindow(StringBuilder user)
    {
        // In case when json file is empty
        Stage addUserWindow = new Stage();
        addUserWindow.initModality(Modality.APPLICATION_MODAL);
        addUserWindow.setTitle("Add User");
        TextField userName = new TextField();
        userName.setText("DefaultUser");
        Label fieldUserName = new Label();
        fieldUserName.setText("User Name:");
        Button addUserButton = new Button("Add user");
        addUserButton.setOnAction(e -> {
            boolean canAddNewUser = true;
            for(UserDataNode usr : users)
                if (usr.getName().equals(userName.getText()))
                    canAddNewUser = false;

            if(canAddNewUser)
            {
                user.delete(0, user.length());
                user.append(userName.getText());
                UserDataNode udn = new UserDataNode(userName.getText(), 0);
                users.add(udn);
                addUserWindow.close();
            }
            else
                userName.setText("Name already taken!");
        });

        // Set up layout
        GridPane addUserLayout = new GridPane();
        addUserLayout.setPadding(new Insets(5,5,5,5));
        addUserLayout.setVgap(8);
        addUserLayout.setHgap(10);
        GridPane.setConstraints(fieldUserName, 0, 1);
        GridPane.setConstraints(userName, 1, 1);
        GridPane.setConstraints(addUserButton, 1, 2);
        addUserLayout.getChildren().addAll(fieldUserName, userName, addUserButton);

        Scene addUserScene = new Scene(addUserLayout, 300, 150);
        addUserWindow.setScene(addUserScene);
        addUserWindow.showAndWait();
    }

    static void BuildChooseUserWindow(StringBuilder user)
    {
        Stage chooseUserWindow = new Stage();
        chooseUserWindow.initModality(Modality.APPLICATION_MODAL);
        chooseUserWindow.setTitle("Choose user");
        // Define scene elements
        Label label = new Label();
        label.setText("Choose user:");

        ComboBox<String> userChooseList = new ComboBox<>();
        for(UserDataNode node : users)
            userChooseList.getItems().add(node.getName());
        userChooseList.setPromptText("Select user");

        Button addAdditionalUserButton = new Button("Add user");
        addAdditionalUserButton.setOnAction(e -> {
            BuildAddUserWindow(user);
            userChooseList.getItems().clear();
            for(UserDataNode node : users)
                userChooseList.getItems().add(node.getName());
        });

        Button closeWindowButton = new Button("Done");
        closeWindowButton.setOnAction(e -> {
            user.delete(0, user.length());
            if(userChooseList.getValue() == null)
                user.append("Guest");
            else
                user.append(userChooseList.getValue());
            chooseUserWindow.close();
        });

        // Set up layout
        GridPane chooseUserLayout = new GridPane();
        chooseUserLayout.setPadding(new Insets(5,5,5,5));
        chooseUserLayout.setVgap(8);
        chooseUserLayout.setHgap(10);
        GridPane.setConstraints(label, 0, 1);
        GridPane.setConstraints(userChooseList, 1, 1);
        GridPane.setConstraints(addAdditionalUserButton, 0, 2);
        GridPane.setConstraints(closeWindowButton, 1, 2);
        chooseUserLayout.getChildren().addAll(label, userChooseList, addAdditionalUserButton, closeWindowButton);

        Scene chooseUserScene = new Scene(chooseUserLayout, 300, 200);
        chooseUserWindow.setScene(chooseUserScene);
        chooseUserWindow.showAndWait();
    }

    static void SaveProgramData()
    {
        JsonFileMgr.SaveDataToJson(users);
    }

    static ArrayList<UserDataNode> GetUsersList() { return users; }
}
