package sample;

import javafx.scene.paint.Color;

class TetrisBlock
{
    private int posX;
    private int posY;
    private Color color;

    int getPosX() { return posX; }
    int getPosY() { return posY; }
    Color getColor() { return color; }

    void setPosX(int x) { posX = x; }
    void setPosY(int y) { posY = y; }

    TetrisBlock(int x, int y, Color c)
    {
        posX = x;
        posY = y;
        color = c;
    }

    TetrisBlock()
    {
        posX = 0;
        posY = 0;
        color = Color.GRAY;
    }
}
