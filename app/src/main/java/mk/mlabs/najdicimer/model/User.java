package mk.mlabs.najdicimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darko on 4/7/2016.
 */
public class User extends BaseEntity implements Parcelable {
    private String name;
    private String surname;
    private String birthDate;
    private String email;
    private String username;
    private String password;
    private String imageURL;
    private List<Message> sentMessages;
    private List<Message> receivedMessages;
    private String uploadPath;
    private Boolean isAdmin;

    public User() {
    }

    public User(String name, String surname, String birthDate, String email, String username, String password, String imageURL, Boolean isAdmin, String uploadPath) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.email = email;
        this.username = username;
        this.password = password;
        this.imageURL = imageURL;
        this.isAdmin = isAdmin;
        this.uploadPath = uploadPath;
    }

    protected User(Parcel in) {
        name = in.readString();
        surname = in.readString();
        birthDate = in.readString();
        email = in.readString();
        username = in.readString();
        password = in.readString();
        imageURL = in.readString();
        uploadPath = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    //getters
    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Boolean getIsAdmin() {

        return isAdmin;
    }

    public List<Message> getReceivedMessages() {
        return receivedMessages;
    }

    public List<Message> getSentMessages() {
        return sentMessages;
    }

    public String getUploadPath() {
        return uploadPath;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setReceivedMessages(List<Message> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public void setSentMessages(List<Message> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public void setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    public static User parse(JSONObject object) throws JSONException {
        User user = new User();

        Long id = object.getLong("id");
        String name = object.getString("name");
        String surname = object.getString("surname");
        String birthDate = object.getString("birthDate");
        String email = object.getString("email");
        String username = object.getString("username");
        String password = object.getString("password");
        String imageURL = object.getString("imageURL");
        List<Message> sentMessages = new ArrayList<Message>();
        String uploadPath = object.getString("uploadPath");
        Boolean isAdmin = object.getBoolean("isAdmin");

        user.setId(id);
        user.setName(name);
        user.setSurname(surname);
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setImageURL(imageURL);
        user.setUploadPath(uploadPath);
        user.setIsAdmin(isAdmin);
        return user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(surname);
        parcel.writeString(birthDate);
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(imageURL);
        parcel.writeString(uploadPath);
    }
}
