package mk.mlabs.najdicimer.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InvalidObjectException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONArrayParser;
import mk.mlabs.najdicimer.helper.ListingsAdapter;
import mk.mlabs.najdicimer.model.Listing;

/**
 * Created by Darko on 4/22/2016.
 */
public class ViewListingsFragment extends ListFragment {
    private ArrayList<Listing> listings;
    private boolean userScrolled;
    private int offset;
    private int end;
    private ListView listView;

    public ViewListingsFragment(){
        listings = new ArrayList<Listing>();
        userScrolled = false;
        offset = 0;
        end = 5;
    }

    public static ViewListingsFragment newInstance(){
        ViewListingsFragment fragment = new ViewListingsFragment();
        Bundle args = new Bundle();
        //args.putInt("listings", listings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //active = getArguments().getInt("active");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.view_listings_fragment, container, false);
        new RetrieveFeedTask().execute(offset,end);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = getListView();
        ListingsAdapter listingsAdapter = new ListingsAdapter(getActivity(), listings);
        listView.setAdapter(listingsAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                // If scroll state is touch scroll then set userScrolled
                // true
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // Now check if userScrolled is true and also check if
                // the item is end then update list view and set
                // userScrolled to false
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {

                    userScrolled = false;
                    new RetrieveFeedTask().execute(offset, end);
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
    }

    class RetrieveFeedTask extends AsyncTask<Integer, Void, JSONArray>{
        private JSONArray jsonArray;
        private ProgressBar progressBar;

        @Override
        protected JSONArray doInBackground(Integer... params) {
            jsonArray = JSONArrayParser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/listing/piece/" + params[0] + "/" + params[1]);
            return jsonArray;
        }

        protected void onPreExecute() {
            progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(JSONArray response) {

            //listings = JSONArrayParser.parseJSON(response, Listing.class);
            listings.addAll(JSONArrayParser.parseJSON(response, Listing.class));
            Log.e("lsts", String.valueOf(listings.size()));

            //focus some component here

            progressBar.setVisibility(View.GONE);
           // ListView listView = getListView();
            //ListingsAdapter listingsAdapter = new ListingsAdapter(getActivity(), listings);
            //listView.setAdapter(listingsAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("hwa", String.valueOf(position));
                }
            });

            offset = end;
            end += 5;

        }
    }
}
