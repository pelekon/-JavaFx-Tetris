package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;
import java.util.Comparator;

class RankingWindow
{
    static void InitRankingWindow(ArrayList<UserDataNode> users)
    {
        Stage rankingWindow = new Stage();
        rankingWindow.initModality(Modality.APPLICATION_MODAL);
        rankingWindow.setTitle("Ranking");

        ObservableList<UserDataNode> usersList = FXCollections.observableArrayList(users);
        Comparator<UserDataNode> comparator = Comparator.comparingInt(UserDataNode::getScore);
        comparator = comparator.reversed();
        FXCollections.sort(usersList, comparator);

        TableView<UserDataNode> rankingTable = new TableView<UserDataNode>();
        TableColumn<UserDataNode, String> nameCollumn = new TableColumn<UserDataNode, String>("Name");
        nameCollumn.setMinWidth(200);
        nameCollumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<UserDataNode, String> scoreCollumn = new TableColumn<UserDataNode, String>("Score");
        scoreCollumn.setMinWidth(150);
        scoreCollumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        rankingTable.setItems(usersList);
        rankingTable.getColumns().addAll(nameCollumn, scoreCollumn);

        GridPane rankingLayout = new GridPane();
        rankingLayout.getChildren().add(rankingTable);
        Scene rankingScene = new Scene(rankingLayout, 350, 380);
        rankingWindow.setScene(rankingScene);
        rankingWindow.setResizable(false);
        rankingWindow.showAndWait();
    }
}
