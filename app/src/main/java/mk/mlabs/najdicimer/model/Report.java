package mk.mlabs.najdicimer.model;

import org.json.JSONObject;

/**
 * Created by Darko on 4/14/2016.
 */
public class Report extends BaseEntity {
    private String content;
    private String submittedOn;
    private User userFrom;
    private Listing listing;
    private Boolean seen;

    public Report() {
    }

    public Report(String content, String submittedOn, User userFrom, Listing listing) {
        this.content = content;
        this.submittedOn = submittedOn;
        this.userFrom = userFrom;
        this.listing = listing;
        seen = false;
    }

    public String getContent() {
        return content;
    }

    public String getSubmittedOn() {
        return submittedOn;
    }

    public User getUserFrom() {
        return userFrom;
    }

    public Listing getListing(){
        return listing;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSubmittedOn(String submittedOn) {
        this.submittedOn = submittedOn;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setListing(Listing listing){
        this.listing = listing;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public static Report parse(JSONObject object) {
        return null;
    }
}
