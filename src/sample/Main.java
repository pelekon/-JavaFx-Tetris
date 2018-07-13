package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Main extends Application
{
    private StringBuilder user = new StringBuilder("Guest");
    private Label userInform;
    private Stage window;

    public static void main(String[] args)
    {
        launch(args);
        UserChoice.SaveProgramData();
    }

    public void start(Stage primaryStage) throws Exception
    {
        window = primaryStage;
        primaryStage.setTitle("Tetris main menu");
        UserChoice.ShowUserChoice(user);

        // Build main menu items
        Label banner = new Label();
        Font bannerFont = Font.font("Comic Sans", FontWeight.BOLD, 25);
        banner.setFont(bannerFont);
        banner.setTextFill(Color.DARKMAGENTA);
        banner.setText("Welcome in Tetris main menu!");

        userInform = new Label();
        StringBuilder sb = new StringBuilder("Current user: ");
        sb.append(user);
        Font userInformFont = Font.font("Comic Sans", FontWeight.BOLD, 20);
        userInform.setFont(userInformFont);
        userInform.setTextFill(Color.DARKMAGENTA);
        userInform.setText(sb.toString());

        DropShadow dsGameButton = new DropShadow();
        dsGameButton.setColor(Color.valueOf("#f013d6"));

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> StartGame());
        startGameButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        startGameButton.setEffect(dsGameButton);
        startGameButton.setMinWidth(84.0);

        Button showRankingButton = new Button("Show Ranking");
        showRankingButton.setOnAction(e -> RankingWindow.InitRankingWindow(UserChoice.GetUsersList()));
        showRankingButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        showRankingButton.setEffect(dsGameButton);

        Button changeUserButton = new Button("Change User");
        changeUserButton.setOnAction(e -> {
            UserChoice.BuildChooseUserWindow(user);
            sb.delete(0, sb.length());
            sb.append("Current user: ");
            sb.append(user);
            userInform.setText(sb.toString());
        });
        changeUserButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        changeUserButton.setEffect(dsGameButton);

        Button exitProgramButton = new Button("Close Program");
        exitProgramButton.setOnAction(e -> primaryStage.close());
        exitProgramButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        exitProgramButton.setEffect(dsGameButton);

        // Set up layout
        GridPane mainLayout = new GridPane();
        mainLayout.setStyle("-fx-background-image: url(\"sample/TetrisMenu.jpg\"); -fx-background-repeat: no-repeat; -fx-background-size: cover;");
        mainLayout.setPadding(new Insets(5,5,5,5));
        mainLayout.setVgap(8);
        mainLayout.setHgap(10);
        mainLayout.getColumnConstraints().add(new ColumnConstraints(250));
        mainLayout.getRowConstraints().add(new RowConstraints(50));
        GridPane.setConstraints(banner, 1, 1);
        GridPane.setConstraints(userInform, 0, 2);
        GridPane.setConstraints(startGameButton, 1, 12);
        GridPane.setConstraints(showRankingButton, 2, 12);
        GridPane.setConstraints(changeUserButton, 1, 17);
        GridPane.setConstraints(exitProgramButton, 2, 17);

        mainLayout.getChildren().addAll(banner, userInform, startGameButton, showRankingButton, changeUserButton, exitProgramButton);

        Scene mainScene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void StartGame()
    {
        window.hide();
        WrappedInt score = new WrappedInt();
        WrappedBool canInitGame = new WrappedBool();
        while (canInitGame.getVal())
        {
            Game game = new Game();
            game.InitGameWindow(score);
            for(UserDataNode udn : UserChoice.GetUsersList())
            {
                if(udn.getName().equals(user.toString()))
                {
                    if(udn.getScore() < score.getVal())
                        udn.setScore(score.getVal());
                    break;
                }
            }
            canInitGame.setVal(false);
            GameResultWindow.BuildResultWindow(canInitGame, score.getVal());
        }
        window.show();
    }
}

class WrappedInt
{
    private int val = 0;
    int getVal() {return val;}
    void setVal(int x) {val = x;}
}

class WrappedBool
{
    private boolean val = true;
    boolean getVal() { return val; }
    void setVal(boolean x) { val = x; }
}
