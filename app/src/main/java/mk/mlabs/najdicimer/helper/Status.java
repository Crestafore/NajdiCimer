package mk.mlabs.najdicimer.helper;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;

import mk.mlabs.najdicimer.R;

/**
 * Created by Darko on 5/11/2016.
 */
public class Status {
    public static Boolean loggedIn = false;
    public static long userId = 0;

    public static void logIn(Activity context, long userId){
        NavigationView nvDrawer = (NavigationView) context.findViewById(R.id.nvView);

        //loggedOut
        nvDrawer.getMenu().findItem(R.id.nav_login).setVisible(false);
        nvDrawer.getMenu().findItem(R.id.nav_signup).setVisible(false);
        //context.findViewById(R.id.nav_login).setVisibility(View.GONE);
        //context.findViewById(R.id.nav_signup).setVisibility(View.GONE);

        //loggedIn
        nvDrawer.getMenu().findItem(R.id.nav_profile).setVisible(true);
        nvDrawer.getMenu().findItem(R.id.nav_messages).setVisible(true);
        nvDrawer.getMenu().findItem(R.id.nav_logout).setVisible(true);
        nvDrawer.getMenu().findItem(R.id.nav_create_listing).setVisible(true);
        //context.findViewById(R.id.nav_profile).setVisibility(View.VISIBLE);
        //context.findViewById(R.id.nav_messages).setVisibility(View.VISIBLE);
        //context.findViewById(R.id.nav_logout).setVisibility(View.VISIBLE);
        //context.findViewById(R.id.nav_create_listing).setVisibility(View.VISIBLE);

        loggedIn = true;
        Status.userId = userId;
    }

    public static void logOut(Activity context){
        NavigationView nvDrawer = (NavigationView) context.findViewById(R.id.nvView);

        //loggedOut
        nvDrawer.getMenu().findItem(R.id.nav_login).setVisible(true);
        nvDrawer.getMenu().findItem(R.id.nav_signup).setVisible(true);
        //context.findViewById(R.id.nav_login).setVisibility(View.VISIBLE);
        //context.findViewById(R.id.nav_signup).setVisibility(View.VISIBLE);

        //loggedIn
        nvDrawer.getMenu().findItem(R.id.nav_profile).setVisible(false);
        nvDrawer.getMenu().findItem(R.id.nav_messages).setVisible(false);
        nvDrawer.getMenu().findItem(R.id.nav_logout).setVisible(false);
        nvDrawer.getMenu().findItem(R.id.nav_create_listing).setVisible(false);
        //context.findViewById(R.id.nav_profile).setVisibility(View.GONE);
        //context.findViewById(R.id.nav_messages).setVisibility(View.GONE);
        //context.findViewById(R.id.nav_logout).setVisibility(View.GONE);
        //context.findViewById(R.id.nav_create_listing).setVisibility(View.GONE);

        loggedIn = false;
        userId = 0;
    }
}
