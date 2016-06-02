package mk.mlabs.najdicimer;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import mk.mlabs.najdicimer.fragment.CreateListingFragment;
import mk.mlabs.najdicimer.fragment.EditProfileFragment;
import mk.mlabs.najdicimer.fragment.LoginFragment;
import mk.mlabs.najdicimer.fragment.MapFragment;
import mk.mlabs.najdicimer.fragment.MessagesFragment;
import mk.mlabs.najdicimer.fragment.SearchFragment;
import mk.mlabs.najdicimer.fragment.SignupFragment;
import mk.mlabs.najdicimer.fragment.ViewListingsFragment;
import mk.mlabs.najdicimer.helper.Status;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String FIRST_RUN = "FIRST_RUN";
    ProgressBar progressBar;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    private void init(){
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void sendNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("najdicimer.mk")
                        .setContentText("Добродојдовте во фамилијата цимери!!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, LocationGeotaggingActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(LocationGeotaggingActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        //stackBuilder.addNextIntent(resultIntent);
        /*PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);*/
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        int mId = 10;
        mNotificationManager.notify(mId, mBuilder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean firstRun = sharedPreferences.getBoolean(FIRST_RUN, false);
        if (!firstRun){ //showSpash
            sendNotification();
            //note that this has runned before..
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(FIRST_RUN, true);
            editor.commit(); //flush
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SearchFragment sf = (SearchFragment) fragmentManager.findFragmentByTag("SearchFragment");
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.ma_search_fragment_container);
                if(sf != null){
                    frameLayout.setClickable(false);
                    fragmentTransaction.remove(sf).commit();
                }else{
                    frameLayout.setClickable(true);
                    fragmentTransaction.add(R.id.ma_search_fragment_container, SearchFragment.newInstance(), "SearchFragment").commit();
                }
            }
        });

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view

        setupDrawerContent(nvDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

        init();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putLong("id", 1); //user id
        fragment.setArguments(args);

        //fragmentTransaction.add(R.id.ma_fragment_container, LoginFragment.newInstance(), "MapFragment").addToBackStack(null).commit();
        fragmentTransaction.add(R.id.ma_fragment_container, ViewListingsFragment.newInstance(), "ViewListingsFragment").addToBackStack(null).commit();
    }

    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.nav_login:
                fragmentClass = LoginFragment.class;
                break;
            case R.id.nav_signup:
                fragmentClass = SignupFragment.class;
                break;
            case R.id.nav_profile:
                fragmentClass = EditProfileFragment.class;
                break;
            case R.id.nav_messages:
                fragmentClass = MessagesFragment.class;
                break;
            case R.id.nav_logout:
                Status.logOut(this);
                fragmentClass = LoginFragment.class;
                break;
            case R.id.nav_view_listings:
                fragmentClass = ViewListingsFragment.class;
                break;
            case R.id.nav_view_nearby_listings:
                fragmentClass = MapFragment.class;
                break;
            case R.id.nav_create_listing:
                fragmentClass = CreateListingFragment.class;
                break;
            default:
                fragmentClass = ViewListingsFragment.class;
        }

        try {
            if (fragmentClass == EditProfileFragment.class)
                fragment = EditProfileFragment.newInstance(Status.userId);
            else
                fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.ma_fragment_container, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
            //getFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            getFragmentManager().beginTransaction().commit();
        } else {
            //handle finish
            finish(); // Closes app
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
