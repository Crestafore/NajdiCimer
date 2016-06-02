package mk.mlabs.najdicimer.helper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;

import mk.mlabs.najdicimer.model.Listing;
import mk.mlabs.najdicimer.model.Location;
import mk.mlabs.najdicimer.model.Message;
import mk.mlabs.najdicimer.model.Report;
import mk.mlabs.najdicimer.model.User;


/**
 * Created by Darko on 4/7/2016.
 */
public class JSONParser {
    public JSONObject downloadContent(String uri, HashMap<String, String> params, String requestMethod){
        try {
            URL url = new URL(uri);
            byte[] postDataBytes = null;

            if(requestMethod.compareTo("POST") == 0){
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String,String> param : params.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                    postData.append('=');
                    postData.append(URLEncoder.encode(param.getValue(), "UTF-8"));

                }

                postDataBytes = postData.toString().getBytes("UTF-8");

            }

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(requestMethod);

            if(requestMethod.compareTo("POST") == 0){
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                os.write(postDataBytes);
                os.close();
            }

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                if(stringBuilder.toString().compareTo("") != 0)
                    return new JSONObject(stringBuilder.toString());
                else
                    return null;
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

    public <T> T parseJSON(JSONObject json, Class<T> tClass) throws JSONException, InvalidObjectException {
        T object;

        if (tClass.equals(Listing.class)){
            object =  tClass.cast(Listing.parse(json));
        }else if (tClass.equals(Location.class)){
            object =  tClass.cast(Location.parse(json));
        }else if (tClass.equals(Message.class)){
            object =  tClass.cast(Message.parse(json));
        }else if (tClass.equals(Report.class)){
            object =  tClass.cast(Report.parse(json));
        }else if (tClass.equals(User.class)){
            object =  tClass.cast(User.parse(json));
        }else
            throw new InvalidObjectException("This object is not supported by najdicimeri.com");

        return object;
    }
}