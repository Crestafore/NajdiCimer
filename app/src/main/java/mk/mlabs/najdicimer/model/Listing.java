package mk.mlabs.najdicimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 4/14/2016.
 */
public class Listing extends BaseEntity implements Parcelable {
    private String title;
    private String content;
    private String createdOn;
    private List<String> imageURLs;
    private User user;
    private Location location;

    public Listing() {

    }

    public Listing(String title, String content, String createdOn, ArrayList<String> imageURLs, User user) {
        this.title = title;
        this.content = content;
        this.createdOn = createdOn;
        this.imageURLs = imageURLs;
        this.user = user;
    }

    protected Listing(Parcel in) {
        title = in.readString();
        content = in.readString();
        createdOn = in.readString();
        imageURLs = in.createStringArrayList();
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public List<String> getImageURLs() {
        return imageURLs;
    }

    public Location getLocation(){
        return location;
    }

    public User getUser() {
        return user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setImageURLs(List<String> imageURLs) {
        this.imageURLs = imageURLs;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public static Listing parse(JSONObject object) throws JSONException {
        Listing listing = new Listing();

        Long id = object.getLong("id");
        String title = object.getString("title");
        String content = object.getString("content");
        String createdOn = object.getString("createdOn");
        JSONArray jsonArray = object.getJSONArray("imageURLs");
        List<String> imageURLs = new ArrayList<String>();
        for (int i=0;i<jsonArray.length();i++) {
            imageURLs.add(jsonArray.getString(i));
        }
        JSONObject userJSONObject = object.getJSONObject("user");
        User user = User.parse(userJSONObject); //doesn't get the messages
        JSONObject locationJSONObject = object.getJSONObject("location");
        Location location = Location.parse(locationJSONObject);

        listing.setId(id);
        listing.setTitle(title);
        listing.setContent(content);
        listing.setCreatedOn(createdOn);
        listing.setImageURLs(imageURLs);
        listing.setUser(user);
        listing.setLocation(location);

        return listing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(content);
        parcel.writeString(createdOn);
        parcel.writeArray(imageURLs.toArray());
        parcel.writeParcelable(user, i);
        parcel.writeParcelable(location, i);
    }
}
