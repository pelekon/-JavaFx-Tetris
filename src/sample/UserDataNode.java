package sample;

public class UserDataNode
{
    private String name;
    private int score;

    public void setName(String str) {name = str;}
    public void setScore(int val) {score = val;}
    public String getName() {return name;}
    public int getScore() {return score;}

    UserDataNode(String str, int val)
    {
        name = str;
        score = val;
    }
    UserDataNode()
    {
        name = "Null";
        score = 0;
    }
}
