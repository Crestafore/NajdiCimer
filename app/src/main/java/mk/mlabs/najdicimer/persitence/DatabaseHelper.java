package mk.mlabs.najdicimer.persitence;

import android.graphics.Bitmap;

import java.util.List;

import mk.mlabs.najdicimer.model.Listing;
import mk.mlabs.najdicimer.model.Location;

/**
 * Created by Mile on 04/26/2016.
 */
public interface DatabaseHelper {

    // LISTINGS
    long createListing(Listing listing);
    Listing getListing(long id);
    List<Listing> getAllListings();
    long updateListing(Listing listing);
    void deleteListing(long id);

    // LISTING_LOCATION
    long createListingLocation(Location location);
    Location getListingLocation(long id);

    // close the DB
    void closeDB();
}
