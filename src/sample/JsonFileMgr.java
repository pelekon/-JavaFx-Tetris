package sample;
import java.io.FileWriter;
import java.util.*;
import java.io.FileReader;
// Imports for Json file operations
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class JsonFileMgr
{
    static boolean GetDataFromJson(ArrayList<UserDataNode> users)
    {
        JSONParser parser = new JSONParser();
        JSONArray a;

        try(FileReader file = new FileReader("UsersData.json"))
        {
            a = (JSONArray) parser.parse(file);
        }
        catch(Exception e)
        {
            return false;
        }

        for (Object jo : a)
        {
            JSONObject user = (JSONObject) jo;
            UserDataNode udn = new UserDataNode();
            udn.setName(user.get("name").toString());
            try
            {
                udn.setScore(Integer.parseInt(user.get("score").toString()));
            }
            catch(Exception e)
            {
                udn.setScore(0);
            }
            users.add(udn);
        }

        return true;
    }

    static void SaveDataToJson(ArrayList<UserDataNode> users)
    {
        try(FileWriter writer = new FileWriter("UsersData.json"))
        {
            boolean canWriteComma = false;
            writer.write('[');
            for(UserDataNode udn : users)
            {
                if(canWriteComma)
                    writer.write(',');
                writer.write(System.lineSeparator());
                JSONObject obj = new JSONObject();
                obj.put("name", udn.getName());
                Integer val = new Integer(udn.getScore());
                obj.put("score", val.toString());
                writer.write(obj.toJSONString());
                canWriteComma = true;
            }

            writer.write(System.lineSeparator());
            writer.write(']');
        }
        catch(Exception e)
        {
            System.out.println("Error during saving UserData!");
        }
    }
}
