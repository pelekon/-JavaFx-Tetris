package sample;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.util.Random;

// Notes
// Total width: 600
// Total height: 800

class Game
{
    // Static defines
    public static final int tileSize = 40;
    public static final int gridWidth = 15; // X axis
    public static final int gridHeight = 20; // Y axis
    // Game variables
    private Stage game;
    private GraphicsContext gc;
    private GraphicsContext sidePanelGc;
    private double time = 0;
    private double batchTime = 1.0;
    private int thisGameScore = 0;
    private WrappedInt activePlayerScore;
    boolean isFinalized;
    boolean isWindowClosed;
    // Hold reference to each not movable block in grid map
    private TetrisBlock[][] grids = new TetrisBlock[gridWidth][gridHeight];
    private MovableObject mainBlock; //Can be null!

    void InitGameWindow(WrappedInt score)
    {
        isFinalized = false;
        isWindowClosed = false;
        activePlayerScore = score;

        game = new Stage();
        game.setTitle("Tetris");
        game.initModality(Modality.APPLICATION_MODAL);
        GridPane root = new GridPane();
        root.setStyle("-fx-background-image: url(\"sample/TetrisPlaneBg.png\")");
        Scene gameScene = new Scene(root, tileSize * gridWidth + 280, tileSize * gridHeight);
        gameScene.setOnKeyPressed(e -> HandleKeyPressEvent(e.getCode()));
        game.setScene(gameScene);
        // Add canvas
        Canvas gameField = new Canvas(tileSize * gridWidth, tileSize * gridHeight);
        gc = gameField.getGraphicsContext2D();
        Canvas sidePanel = new Canvas(300, tileSize * gridHeight);
        sidePanelGc = sidePanel.getGraphicsContext2D();
        GridPane.setConstraints(gameField, 0, 0);
        GridPane.setConstraints(sidePanel, 1, 0);
        root.getChildren().addAll(gameField, sidePanel);
        // Draw basic words
        Font font = Font.font("Comic Sans", FontWeight.BOLD, 40);
        sidePanelGc.setFill(Color.ROSYBROWN);
        sidePanelGc.setFont(font);
        sidePanelGc.fillText("Tetris", 100, 100);
        font = Font.font("Comic Sans", FontWeight.BOLD, 14);
        sidePanelGc.setFont(font);
        sidePanelGc.fillText("Move via left/right arrows.", 20, 200);
        sidePanelGc.fillText("Press up arrow to rotate block.", 20, 230);
        sidePanelGc.fillText("Press down arrow to speed up falling.", 20, 260);
        sidePanelGc.fillText("Press spacebar to put block down.", 20, 290);
        sidePanelGc.fillText("Press esc to end game.", 20, 320);
        sidePanelGc.fillText("Score: 0", 20, 400);
        game.setOnShown(e -> StartGame());
        game.setResizable(false);
        game.showAndWait();
        isWindowClosed = true;
        if(!isFinalized)
            Finalize();
    }

    private void StartGame()
    {
        timer.start();
        SpawnNewObject();
    }

    private void Finalize()
    {
        isFinalized = true;

        // Clean up
        timer.stop();
        if(mainBlock != null)
        {
            mainBlock.CleanUp();
            mainBlock = null;
        }

        activePlayerScore.setVal(thisGameScore);

        if(!isWindowClosed)
            game.close();
    }

    private AnimationTimer timer = new AnimationTimer()
    {
        public void handle(long now)
        {
            time += 0.016;

            if(time >= batchTime)
            {
                time = 0.0;
                Update();
            }
        }
    };

    private void Update()
    {
        if(mainBlock == null)
            SpawnNewObject();
        else
            MoveActiveObject(MovableObject.MoveDirection.MOVE_DOWN, false);

        CheckGridLines();
    }

    private void ReDrawObjects(int startLine)
    {
        for (int y = startLine; y  >= 0; --y)
        {
            for(int x = 0; x < gridWidth; ++x)
            {
                if(grids[x][y] != null)
                {
                    gc.clearRect(tileSize * x, tileSize * y, tileSize, tileSize);
                    Color clr = grids[x][y].getColor();
                    gc.setFill(clr);
                    gc.setStroke(Color.BLACK);
                    gc.fillRect(tileSize * x, tileSize * (y + 1), tileSize, tileSize);
                    gc.strokeRect(tileSize * x + 2, tileSize * (y + 1) + 2, tileSize - 3, tileSize - 3);
                    grids[x][y].setPosY(y + 1);
                    grids[x][y + 1] = grids[x][y];
                    grids[x][y] = null;
                }
            }
        }
    }

    private void CheckGridLines()
    {
        boolean isFullLine;

        for (int y = gridHeight - 1; y  >= 0; --y)
        {
            isFullLine = true;

            for (int x = 0; x < gridWidth; ++x)
            {
                if(grids[x][y] == null)
                {
                    isFullLine = false;
                    break;
                }
            }

            if (isFullLine)
            {
                DespawnLine(y);
                ReDrawObjects(y - 1);
                y += 1;
            }
        }
    }

    private void DespawnLine(int line)
    {
        thisGameScore += 10;
        UpdateScore();

        for(int x = 0; x < gridWidth; ++x)
        {
            TetrisBlock obj = grids[x][line];
            gc.clearRect(tileSize * obj.getPosX(), tileSize * obj.getPosY(), tileSize, tileSize);
            obj = null;
            grids[x][line] = null;
        }
    }

    private void MoveActiveObject(MovableObject.MoveDirection direction, boolean isInstantMove)
    {
        if(mainBlock == null)
            return;

        int x = 0;
        int y = 0;
        boolean canDetachBlocks = false;

        switch(direction)
        {
            case MOVE_DOWN:
                y = 1;
                canDetachBlocks = true;
                break;
            case MOVE_LEFT:
                x = -1;
                break;
            case MOVE_RIGHT:
                x = 1;
                break;
        }

        if(canDetachBlocks && isInstantMove)
        {
            while(true)
            {
                if (ValidatePoints(x, y))
                    mainBlock.Move(gc, x, y);
                else
                {
                    for(TetrisBlock obj : mainBlock.GetPieces())
                        grids[obj.getPosX()][obj.getPosY()] = obj;

                    mainBlock.CleanUp();
                    mainBlock = null;
                    break;
                }
            }
        }
        else if (ValidatePoints(x, y))
            mainBlock.Move(gc, x, y);
        else
        {
            if (canDetachBlocks)
            {
                for(TetrisBlock obj : mainBlock.GetPieces())
                    grids[obj.getPosX()][obj.getPosY()] = obj;

                mainBlock.CleanUp();
                mainBlock = null;
            }
        }
    }

    private void TransformObject()
    {
        if(mainBlock == null)
            return;

        if(mainBlock.getBlockType() == MovableObject.BlockType.BLOCK_O_TYPE)
            return;

        mainBlock.HideBlock(gc);

        int originX = mainBlock.GetPieces().get(0).getPosX();
        int originY = mainBlock.GetPieces().get(0).getPosY();
        int[][] points = new int[4][2];
        int index = 0;

        for (TetrisBlock obj : mainBlock.GetPieces())
        {
            int oldX = obj.getPosX();
            int oldY = obj.getPosY();

            // Convert points to small matrix with point (0, 0) as center
            int newX = oldX - originX;
            int newY = oldY - originY;
            newY *= -1; // Reverse Y axis to matrix indexation

            // Matrix rotation
            double tempX = (newX * Math.cos(Math.PI/2)) - (newY * Math.sin(Math.PI/2));
            double tempY = (newX * Math.sin(Math.PI/2)) + (newY * Math.cos(Math.PI/2));
            newX = (int)Math.round(tempX);
            newY = (int)Math.round(tempY);
            newY *= -1; // Reverse Y axis to match grid indexation
            newX += originX;
            newY += originY;

            points[index][0] = newX;
            points[index][1] = newY;
            ++index;
        }

        index = 0;
        boolean canOverridePoints = true;

        // Check if transformation wont cause exception due to points outside of game grid
        for(int i = 0; i < 4; ++i)
        {
            if (points[i][0] < 0 || points[i][0] > 14 || points[i][1] < 0 || points[i][1] > 19)
            {
                canOverridePoints = false;
                break;
            }

            if(grids[points[i][0]][points[i][1]] != null)
                canOverridePoints = false;
        }

        if(canOverridePoints)
        {
            for (TetrisBlock obj : mainBlock.GetPieces())
            {
                obj.setPosX(points[index][0]);
                obj.setPosY(points[index][1]);
                ++index;
            }
        }

        mainBlock.Render(gc);
    }

    private void SpawnNewObject()
    {
        MovableObject.BlockType blockType = RollBlockType();
        Color color = GetColorForObject(blockType);
        CreateMovableObject(blockType, color);

        if (ValidateSpawnPoints())
            mainBlock.Render(gc);
        else
            Finalize();
    }

    private void HandleKeyPressEvent(KeyCode code)
    {
        if (code == KeyCode.UP)
            TransformObject();
        else if (code == KeyCode.DOWN)
            MoveActiveObject(MovableObject.MoveDirection.MOVE_DOWN, false);
        else if (code == KeyCode.LEFT)
            MoveActiveObject(MovableObject.MoveDirection.MOVE_LEFT, false);
        else if (code == KeyCode.RIGHT)
            MoveActiveObject(MovableObject.MoveDirection.MOVE_RIGHT, false);
        else if (code == KeyCode.ESCAPE)
            Finalize();
        else if(code == KeyCode.SPACE)
            MoveActiveObject(MovableObject.MoveDirection.MOVE_DOWN, true);
    }

    private void UpdateScore()
    {
        sidePanelGc.clearRect(15, 380, 200, 200);
        sidePanelGc.fillText("Score: " + thisGameScore, 20, 400);

        if(thisGameScore % 100 == 0)
        {
            if(batchTime > 0.2)
                batchTime -= 0.2;

            if(batchTime < 0.2)
                batchTime = 0.2;
        }
    }

    // #################################################################
    //              Inner Block management functions
    // #################################################################

    private MovableObject.BlockType RollBlockType()
    {
        MovableObject.BlockType type = MovableObject.BlockType.BLOCK_I_TYPE;
        Random generator = new Random();
        int index = generator.nextInt(7);

        switch(index)
        {
            case 1:
                type = MovableObject.BlockType.BLOCK_O_TYPE;
                break;
            case 2:
                type = MovableObject.BlockType.BLOCK_T_TYPE;
                break;
            case 3:
                type = MovableObject.BlockType.BLOCK_J_TYPE;
                break;
            case 4:
                type = MovableObject.BlockType.BLOCK_L_TYPE;
                break;
            case 5:
                type = MovableObject.BlockType.BLOCK_S_TYPE;
                break;
            case 6:
                type = MovableObject.BlockType.BLOCK_Z_TYPE;
                break;
        }

        return type;
    }

    private Color GetColorForObject(MovableObject.BlockType type)
    {
        Color color = Color.CRIMSON;

        switch(type)
        {
            case BLOCK_O_TYPE:
                color = Color.GREEN;
                break;
            case BLOCK_T_TYPE:
                color = Color.BLUE;
                break;
            case BLOCK_J_TYPE:
                color = Color.YELLOW;
                break;
            case BLOCK_L_TYPE:
                color = Color.PURPLE;
                break;
            case BLOCK_S_TYPE:
                color = Color.ORANGE;
                break;
            case BLOCK_Z_TYPE:
                color = Color.LIME;
                break;
        }

        return color;
    }

    private void CreateMovableObject(MovableObject.BlockType blockType, Color color)
    {
        switch(blockType)
        {
            case BLOCK_I_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 0, color), // Main block
                        new TetrisBlock(6, 0, color),
                        new TetrisBlock(8, 0, color),
                        new TetrisBlock(9, 0, color));
                break;
            case BLOCK_O_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 0, color), // Main block
                        new TetrisBlock(7, 1, color),
                        new TetrisBlock(8, 1, color),
                        new TetrisBlock(8, 0, color));
                break;
            case BLOCK_T_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 1, color), // Main block
                        new TetrisBlock(6, 1, color),
                        new TetrisBlock(8, 1, color),
                        new TetrisBlock(7, 0, color));
                break;
            case BLOCK_J_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 1, color), // Main block
                        new TetrisBlock(8, 1, color),
                        new TetrisBlock(9, 1, color),
                        new TetrisBlock(7, 0, color));
                break;
            case BLOCK_L_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 1, color), // Main block
                        new TetrisBlock(5, 1, color),
                        new TetrisBlock(6, 1, color),
                        new TetrisBlock(7, 0, color));
                break;
            case BLOCK_S_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 0, color), // Main block
                        new TetrisBlock(7, 1, color),
                        new TetrisBlock(6, 1, color),
                        new TetrisBlock(8, 0, color));
                break;
            case BLOCK_Z_TYPE:
                mainBlock = new MovableObject(color, blockType,
                        new TetrisBlock(7, 0, color), // Main block
                        new TetrisBlock(7, 1, color),
                        new TetrisBlock(6, 0, color),
                        new TetrisBlock(8, 1, color));
                break;
        }
    }

    private boolean IsOutOfGridMap(int modX, int modY)
    {
        for (TetrisBlock obj : mainBlock.GetPieces())
            if (obj.getPosX() + modX < 0 || obj.getPosX() + modX > 14 || obj.getPosY() + modY < 0 || obj.getPosY() + modY > 19)
                return true;

        return false;
    }

    private boolean ValidateSpawnPoints()
    {
        for (TetrisBlock obj : mainBlock.GetPieces())
        {
            if (grids[obj.getPosX()][obj.getPosY()] != null)
                return false;
        }

        return true;
    }

    private boolean ValidatePoints(int modX, int modY)
    {
        if(IsOutOfGridMap(modX, modY))
            return false;

        for (TetrisBlock obj : mainBlock.GetPieces())
             if(grids[obj.getPosX() + modX][obj.getPosY() + modY] != null)
                 return false;

        return true;
    }
}
