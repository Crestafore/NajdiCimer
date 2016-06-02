package mk.mlabs.najdicimer.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Darko on 4/14/2016.
 */
public class Location extends BaseEntity implements Parcelable {
    private String name;
    private String lat;
    private String lng;
    private Listing listing;

    public Location() {}

    public Location(String name, String lat, String lng, Listing listing){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.listing = listing;
    }

    protected Location(Parcel in) {
        name = in.readString();
        lat = in.readString();
        lng = in.readString();
        listing = in.readParcelable(Listing.class.getClassLoader());
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public Listing getListing() {
        return listing;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public static Location parse(JSONObject object) throws JSONException {
        Location location = new Location();

        Long id = object.getLong("id");
        String name = object.getString("name");
        String lat = object.getString("lat");
        String lng = object.getString("lng");

        location.setId(id);
        location.setName(name);
        location.setLat(lat);
        location.setLng(lng);

        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(lat);
        parcel.writeString(lng);
        parcel.writeParcelable(listing, i);
    }
}
