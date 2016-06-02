package mk.mlabs.najdicimer.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InvalidObjectException;
import java.net.URI;
import java.net.URISyntaxException;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONParser;
import mk.mlabs.najdicimer.model.Listing;

/**
 * Created by Darko on 4/14/2016.
 */
public class ListingDetailsFragment extends Fragment {
    private int active;
    private Listing listing;

    public ListingDetailsFragment() {
        active = -1;
    }

    public static ListingDetailsFragment newInstance(int active) {
        ListingDetailsFragment fragment = new ListingDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("active", active);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            active = getArguments().getInt("active");
        }
        new RetrieveFeedTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.listing_details, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        //display();
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void display() {
        TextView textViewTitle = (TextView) getActivity().findViewById(R.id.ld_title);
        textViewTitle.setText(listing.getTitle());
        TextView textViewContent = (TextView) getActivity().findViewById(R.id.ld_content);
        textViewContent.setText(Html.fromHtml(listing.getContent()));
        TextView textViewAuthor = (TextView) getActivity().findViewById(R.id.ld_author);
        textViewAuthor.setText(listing.getUser().getUsername());
        TextView textViewTimestamp = (TextView) getActivity().findViewById(R.id.ld_timestamp);
        textViewTimestamp.setText(listing.getCreatedOn());
        //Slider
        SliderLayout slider = (SliderLayout) getActivity().findViewById(R.id.vl_listings_slider_1);
        slider.setPresetTransformer(SliderLayout.Transformer.Stack);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);

        for(String img : listing.getImageURLs()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            try{

                textSliderView
                        .image(String.valueOf(new URI(Constants.SERVER_IP_ADDRESS + img)))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

            }catch(URISyntaxException e){
                e.printStackTrace();
            }
            slider.addSlider(textSliderView);
        }
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, JSONObject> {
        private JSONObject jsonObject;
        private Exception exception;
        private ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        @Override
        protected JSONObject doInBackground(Void... urls) {
            jsonObject = new JSONParser().downloadContent(Constants.SERVER_API_IP_ADDRESS + "/listing/" + active, null, "GET");
            return jsonObject;
        }

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected void onPostExecute(JSONObject response) {
            if (response == null) {
                Log.i("INFO", "THERE WAS AN ERROR");
            }
            progressBar.setVisibility(View.GONE);
            listing = null;
            try {
                listing = new JSONParser().parseJSON(response, Listing.class);
                display();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidObjectException e) {
                e.printStackTrace();
            }
        }
    }
}
