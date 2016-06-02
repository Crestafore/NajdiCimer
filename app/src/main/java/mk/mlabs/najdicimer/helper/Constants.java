package mk.mlabs.najdicimer.helper;

/**
 * Created by Darko on 4/7/2016.
 */
public class Constants {
    public static final String SERVER_API_IP_ADDRESS = "http://192.168.97.5:8080/servlet-showcase/api";
    public static final String SERVER_IP_ADDRESS = "http://192.168.97.5:8080/servlet-showcase";

    public class Table {
        public static final String LISTINGS = "listings";
        public static final String LISTING_LOCATION = "listing_location";

        public class Listings {
            public static final String KEY_ID = "id";
            public static final String CONTENT = "content";
            public static final String TITLE = "title";
            public static final String CREATED_ON = "createdOn";
            public static final String USER_ID = "userId";
        }

        public class ListingLocation {
            public static final String KEY_ID = "id";
            public static final String LAT = "lat";
            public static final String LNG = "lng";
            public static final String NAME = "name";
            public static final String KEY_LISTING_ID = "listingId";
        }
    }
}
