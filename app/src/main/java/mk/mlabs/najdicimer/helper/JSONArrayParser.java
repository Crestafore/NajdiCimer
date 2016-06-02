package mk.mlabs.najdicimer.helper;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mk.mlabs.najdicimer.model.Listing;
import mk.mlabs.najdicimer.model.Location;
import mk.mlabs.najdicimer.model.Message;
import mk.mlabs.najdicimer.model.Report;
import mk.mlabs.najdicimer.model.User;

/**
 * Created by Darko on 4/14/2016.
 */
public class JSONArrayParser {

    public static JSONArray downloadContent(String uri){
        try {
            URL url = new URL(uri);
            Log.d("custom", url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return new JSONArray(stringBuilder.toString());
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }

    public static <T> ArrayList<T> parseJSON(JSONArray json, Class<T> tClass){
        ArrayList<T> list = new ArrayList<T>();

        try {
            JSONArray jsonArray = json;

            for (int i=0;i<jsonArray.length();i++){
                //ASSEMBLE THE JSON OBJECT
                JSONObject object = jsonArray.getJSONObject(i);

                if (tClass.equals(Listing.class)){
                    list.add(tClass.cast(Listing.parse(object)));
                }else if (tClass.equals(Location.class)){
                    list.add(tClass.cast(Location.parse(object)));
                }else if (tClass.equals(Message.class)){
                    list.add(tClass.cast(Message.parse(object)));
                }else if (tClass.equals(Report.class)){
                    list.add(tClass.cast(Report.parse(object)));
                }else if (tClass.equals(User.class)){
                    list.add(tClass.cast(User.parse(object)));
                }else
                    throw new InvalidObjectException("This object is not supported by najdicimeri.com");
                //Log.d("obj", object.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }

}
