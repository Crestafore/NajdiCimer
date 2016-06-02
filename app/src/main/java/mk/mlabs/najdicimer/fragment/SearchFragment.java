package mk.mlabs.najdicimer.fragment;


import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InvalidObjectException;
import java.util.ArrayList;
import java.util.HashMap;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONArrayParser;
import mk.mlabs.najdicimer.helper.JSONParser;
import mk.mlabs.najdicimer.helper.ListingsAdapter;
import mk.mlabs.najdicimer.model.Listing;
import mk.mlabs.najdicimer.model.User;

/**
 * Created by SimonaS on 11/05/2016.
 */
public class SearchFragment extends Fragment {

    private ArrayList<Listing> listings;
    private ListView list;

    public SearchFragment() {}

    public static SearchFragment newInstance(){
        SearchFragment searchFragment = new SearchFragment();
        return searchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.search_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn = (Button) getActivity().findViewById(R.id.btnSearch);
        final TextView txt = (TextView) getActivity().findViewById(R.id.txtSearch);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = txt.getText().toString();
                new RetrieveFeedTask().execute(query);
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONArray>{
        private JSONArray jsonArray;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Searching...");
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            jsonArray = JSONArrayParser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/listing/search/" + params[0]);
            return jsonArray;
        }


        protected void onPostExecute(JSONArray response) {
            FrameLayout frameLayout = (FrameLayout) getActivity().findViewById(R.id.ma_search_fragment_container);
            frameLayout.setClickable(false);
            listings = JSONArrayParser.parseJSON(response, Listing.class);
            progressDialog.dismiss();
            getFragmentManager().beginTransaction().replace(R.id.ma_fragment_container, SearchResultsFragment.newInstance(listings), null).commit();
        }
    }
}
