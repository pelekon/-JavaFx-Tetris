package sample;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public class MovableObject
{
    public enum MoveDirection
    {
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
    }

    public enum BlockType
    {
        BLOCK_I_TYPE,
        BLOCK_O_TYPE,
        BLOCK_T_TYPE,
        BLOCK_J_TYPE,
        BLOCK_L_TYPE,
        BLOCK_S_TYPE,
        BLOCK_Z_TYPE,
    }

    private ArrayList<TetrisBlock> blocks;
    private Color color;
    private BlockType type;

    MovableObject(Color cl, BlockType blockType, TetrisBlock... pieces)
    {
        color = cl;
        type = blockType;
        blocks = new ArrayList<TetrisBlock>();

        for(TetrisBlock obj : pieces)
            blocks.add(obj);
    }

    public ArrayList<TetrisBlock> GetPieces() { return blocks; }
    public BlockType getBlockType() { return type; }

    public void Render(GraphicsContext gc)
    {
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        //for(TetrisBlock obj : blocks)
            //
        for(TetrisBlock obj : blocks)
        {
            gc.fillRect(Game.tileSize * obj.getPosX(), Game.tileSize * obj.getPosY(), Game.tileSize, Game.tileSize);
            gc.strokeRect(Game.tileSize * obj.getPosX() + 2, Game.tileSize * obj.getPosY() + 2, Game.tileSize - 3, Game.tileSize - 3);
        }
    }

    public void HideBlock(GraphicsContext gc)
    {
        for(TetrisBlock obj : blocks)
            gc.clearRect(Game.tileSize * obj.getPosX(), Game.tileSize * obj.getPosY(), Game.tileSize, Game.tileSize);
    }

    public void Move(GraphicsContext gc, int modX, int modY)
    {
        for(TetrisBlock piece : blocks)
            gc.clearRect(Game.tileSize * piece.getPosX(), Game.tileSize * piece.getPosY(), Game.tileSize, Game.tileSize);

        for (TetrisBlock piece : blocks)
        {
            piece.setPosX(piece.getPosX() + modX);
            piece.setPosY(piece.getPosY() + modY);
            gc.setFill(color);
            gc.setStroke(Color.BLACK);
            gc.fillRect(Game.tileSize * piece.getPosX(), Game.tileSize * piece.getPosY(), Game.tileSize, Game.tileSize);
            gc.strokeRect(Game.tileSize * piece.getPosX() + 2, Game.tileSize * piece.getPosY() + 2, Game.tileSize - 3, Game.tileSize - 3);
        }
    }

    public void CleanUp()
    {
        blocks.clear();
    }
}
