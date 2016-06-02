package mk.mlabs.najdicimer.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.ListingsAdapter;
import mk.mlabs.najdicimer.model.Listing;

/**
 * Created by Darko on 5/11/2016.
 */
public class SearchResultsFragment extends ListFragment {
    private ArrayList<Listing> listings;
    private ListView listView;

    public SearchResultsFragment(){
        listings = new ArrayList<Listing>();
        listView = null;
    }

    public static SearchResultsFragment newInstance(ArrayList<Listing> listings){
        SearchResultsFragment fragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("listings", listings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listings = getArguments().getParcelableArrayList("listings");
        }
        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentByTag("SearchFragment")).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.view_listings_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = getListView();
        ListingsAdapter listingsAdapter = new ListingsAdapter(getActivity(), listings);
        listView.setAdapter(listingsAdapter);
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
    }
}
