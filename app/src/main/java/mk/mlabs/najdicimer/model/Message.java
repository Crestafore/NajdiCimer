package mk.mlabs.najdicimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Darko on 4/14/2016.
 */
public class Message extends BaseEntity implements Parcelable {
    private String content;
    private String sentOn;
    private User userFrom;
    private User userTo;
    private Boolean seen;

    public Message() {
    }

    public Message(String content, String sentOn, User userFrom, User userTo) {
        this.content = content;
        this.sentOn = sentOn;
        this.userFrom = userFrom;
        this.userTo = userTo;
        seen = false;
    }

    protected Message(Parcel in) {
        content = in.readString();
        sentOn = in.readString();
        userFrom = in.readParcelable(User.class.getClassLoader());
        userTo = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getContent() {
        return content;
    }

    public String getSentOn() {
        return sentOn;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public User getUserTo() {
        return userTo;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSentOn(String sentOn) {
        this.sentOn = sentOn;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public static Message parse(JSONObject object) throws JSONException {
        Message message = new Message();

        Long id = object.getLong("id");
        String content = object.getString("content");
        String sentOn = object.getString("sentOn");
        User userFrom = User.parse(object.getJSONObject("userFrom"));
        User userTo = User.parse(object.getJSONObject("userTo"));

        message.setId(id);
        message.setContent(content);
        message.setSentOn(sentOn);
        message.setUserFrom(userFrom);
        message.setUserTo(userTo);

        return message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeString(sentOn);
        parcel.writeParcelable(userFrom, i);
        parcel.writeParcelable(userTo, i);
    }
}
