package mk.mlabs.najdicimer.persitence.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.model.Listing;
import mk.mlabs.najdicimer.model.Location;
import mk.mlabs.najdicimer.persitence.DatabaseHelper;

/**
 * Created by Mile on 04/26/2016.
 */
public class DatabaseHelperImpl extends SQLiteOpenHelper implements DatabaseHelper {

    // Database Name
    private static final String DATABASE_NAME = "najdiCimer.db";

    // Table Create Statements
    // LISTINGS table create statement
    private static final String CREATE_TABLE_LISTINGS = String.format("CREATE TABLE %s (%s INT(20) PRIMARY KEY NOT NULL AUTOINCREMENT," +
            " %s TEXT, %s VARCHAR(255), %s VARCHAR(70), %s INT(20))", Constants.Table.LISTINGS, Constants.Table.Listings.KEY_ID, Constants.Table.Listings.CONTENT, Constants.Table.Listings.CREATED_ON, Constants.Table.Listings.TITLE, Constants.Table.Listings.USER_ID);

    // LISTING_LOCATION table create statement
    private static final String CREATE_TABLE_LISTING_LOCATION = String.format("CREATE TABLE %s (%s INT(20) PRIMARY KEY NOT NULL AUTOINCREMENT," +
            " %s VARCHAR(255), %s VARCHAR(255), %s VARCHAR(255), %s INT(20) FOREIGN KEY REFERENCES %s(%s)" +
            "ON DELETE CASCADE ON UPDATE CASCADE)", Constants.Table.LISTING_LOCATION, Constants.Table.ListingLocation.KEY_ID, Constants.Table.ListingLocation.LAT, Constants.Table.ListingLocation.LNG, Constants.Table.ListingLocation.NAME, Constants.Table.ListingLocation.KEY_LISTING_ID, Constants.Table.LISTINGS, Constants.Table.ListingLocation.KEY_ID);

    public DatabaseHelperImpl(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_LISTINGS);
        db.execSQL(CREATE_TABLE_LISTING_LOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Table.LISTINGS);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.Table.LISTING_LOCATION);

        // create new tables
        onCreate(db);
    }

    /*
     * Creating a listing
     */
    @Override
    public long createListing(Listing listing) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.Table.Listings.TITLE, listing.getTitle());
        values.put(Constants.Table.Listings.CONTENT, listing.getContent());
        values.put(Constants.Table.Listings.CREATED_ON, listing.getCreatedOn());
        values.put(Constants.Table.Listings.USER_ID, listing.getUser().getId());

        // insert row
        long listingId = db.insert(Constants.Table.LISTINGS, null, values);
        return listingId;
    }

    /*
     * Get single listing
     */
    public Listing getListing(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Constants.Table.LISTINGS + " WHERE "
                + Constants.Table.Listings.KEY_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Listing listing = new Listing();
        listing.setId(c.getLong(c.getColumnIndex(Constants.Table.Listings.KEY_ID)));
        listing.setContent(c.getString(c.getColumnIndex(Constants.Table.Listings.CONTENT)));
        listing.setTitle(c.getString(c.getColumnIndex(Constants.Table.Listings.TITLE)));
        listing.setCreatedOn(c.getString(c.getColumnIndex(Constants.Table.Listings.CREATED_ON)));
        //listing.setUserId(c.getLong(c.getColumnIndex(Constants.Table.Listings.USER_ID)));

        return listing;
    }

    /*
     * Get all listings
     * */
    public List<Listing> getAllListings() {
        List<Listing> listings = new ArrayList<Listing>();
        String selectQuery = "SELECT * FROM " + Constants.Table.LISTINGS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Listing listing = new Listing();
                listing.setId(c.getLong(c.getColumnIndex(Constants.Table.Listings.KEY_ID)));
                listing.setContent(c.getString(c.getColumnIndex(Constants.Table.Listings.CONTENT)));
                listing.setTitle(c.getString(c.getColumnIndex(Constants.Table.Listings.TITLE)));
                listing.setCreatedOn(c.getString(c.getColumnIndex(Constants.Table.Listings.CREATED_ON)));
                //listing.setUserId(c.getLong(c.getColumnIndex(Constants.Table.Listings.USER_ID)));
                //NEEDS TO PULL THE WHOLE USER, NOT JUST THE ID

                // adding to listings
                listings.add(listing);
            } while (c.moveToNext());
        }

        return listings;
    }

    /*
     * Updating a listing
     */
    public long updateListing(Listing listing) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.Table.Listings.TITLE, listing.getTitle());
        values.put(Constants.Table.Listings.CONTENT, listing.getContent());

        // updating row
        return db.update(Constants.Table.LISTINGS, values, Constants.Table.Listings.KEY_ID + " = ?",
                new String[]{String.valueOf(listing.getId())});
    }

    /*
     * Deleting a listing
     */
    public void deleteListing(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.Table.LISTINGS, Constants.Table.Listings.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*
    * Creating a listing_location
    */
    public long createListingLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.Table.ListingLocation.LAT, location.getLat());
        values.put(Constants.Table.ListingLocation.LNG, location.getLng());
        values.put(Constants.Table.ListingLocation.NAME, location.getName());
        //values.put(Constants.Table.ListingLocation.KEY_LISTING_ID, location.getListingId());

        // insert row
        return db.insert(Constants.Table.LISTING_LOCATION, null, values);
    }

    /*
    * Get single listing_location
    */
    @Override
    public Location getListingLocation(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + Constants.Table.LISTING_LOCATION + " WHERE "
                + Constants.Table.ListingLocation.KEY_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Location location = new Location();
        location.setId(c.getLong(c.getColumnIndex(Constants.Table.ListingLocation.KEY_ID)));
        location.setLat(c.getString(c.getColumnIndex(Constants.Table.ListingLocation.LAT)));
        location.setLng((c.getString(c.getColumnIndex(Constants.Table.ListingLocation.LNG))));
        location.setName(c.getString(c.getColumnIndex(Constants.Table.ListingLocation.NAME)));
        //location.setListingId(c.getLong(c.getColumnIndex(Constants.Table.ListingLocation.KEY_LISTING_ID)));

        return location;
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
