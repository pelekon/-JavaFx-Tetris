package sample;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GameResultWindow
{
    static void BuildResultWindow(WrappedBool playAgain, int score)
    {
        Stage resultWindow = new Stage();
        resultWindow.setTitle("Game Result");
        resultWindow.initModality(Modality.APPLICATION_MODAL);
        // Build window items
        Font userInformFont = Font.font("Comic Sans", FontWeight.BOLD, 14);
        Label resultLabel = new Label();
        StringBuilder sb = new StringBuilder("Your score:   ");
        sb.append(score);
        resultLabel.setFont(userInformFont);
        resultLabel.setTextFill(Color.valueOf("#FFF400"));
        resultLabel.setText(sb.toString());

        Label questionLabel = new Label();
        questionLabel.setFont(userInformFont);
        questionLabel.setTextFill(Color.valueOf("#FFF400"));
        questionLabel.setText("Do you want to play again?");

        DropShadow dsQuestionButton = new DropShadow();
        dsQuestionButton.setColor(Color.valueOf("#f013d6"));

        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            playAgain.setVal(true);
            resultWindow.close();
        });
        yesButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        yesButton.setEffect(dsQuestionButton);

        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            playAgain.setVal(false);
            resultWindow.close();
        });
        noButton.setStyle("-fx-background-color: #9e32c9; -fx-background-color: linear-gradient(to bottom, #9e32c9 5%, #be13f7 100%); -fx-background-color: linear-gradient(to top, #9e32c9 5%, #be13f7 100%); -fx-border-insets: 0,-3,7,0; -fx-text-fill: #ffffff;");
        noButton.setEffect(dsQuestionButton);

        GridPane resultLayout = new GridPane();
        resultLayout.setStyle("-fx-background-image: url(\"sample/TetrisResult.png\"); -fx-background-repeat: no-repeat; -fx-background-size: cover;");
        resultLayout.setPadding(new Insets(5,5,5,5));
        resultLayout.setVgap(8);
        resultLayout.setHgap(10);
        GridPane.setConstraints(resultLabel, 1, 1);
        GridPane.setConstraints(questionLabel, 1, 5);
        GridPane.setConstraints(yesButton, 1, 10);
        GridPane.setConstraints(noButton, 2, 10);

        resultLayout.getChildren().addAll(resultLabel, questionLabel, yesButton, noButton);

        Scene gameScene = new Scene(resultLayout, 300, 175);
        resultWindow.setScene(gameScene);
        resultWindow.showAndWait();
    }
}
