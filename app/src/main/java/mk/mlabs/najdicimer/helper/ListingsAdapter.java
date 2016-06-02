package mk.mlabs.najdicimer.helper;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import mk.mlabs.najdicimer.MainActivity;
import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.fragment.ListingDetailsFragment;
import mk.mlabs.najdicimer.model.Listing;

/**
 * Created by Darko on 4/21/2016.
 */
public class ListingsAdapter extends BaseAdapter implements BaseSliderView.OnSliderClickListener {
    private List<Listing> listings;
    private Activity ctx;
    private LayoutInflater inflater;

    public ListingsAdapter(Activity ctx, List<Listing> listings){
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
        if (listings != null)
            this.listings = listings;
        else this.listings = new ArrayList<Listing>();
    }

    public void add(Listing item){
        listings.add(item);
        notifyDataSetChanged();
    }

    public void remove(int position){
        listings.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listings.size();
    }

    @Override
    public Object getItem(int position) {
        return listings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if (convertView == null){
            holder = new Holder();
            convertView = holder.layout = (FrameLayout) inflater.inflate(R.layout.view_listings_item, null);
            //add the carousel here
            holder.sliderLayout = (SliderLayout)holder.layout.findViewById(R.id.vl_listings_slider);
            holder.sliderLayout.setPresetTransformer(SliderLayout.Transformer.Stack);
            holder.sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
            //holder.title = (TextView) holder.layout.findViewById(R.id.vl_listing_title);
            convertView.setTag(holder);
        }

        holder = (Holder) convertView.getTag();

        Listing listing = (Listing) getItem(position);
        //update the image carousel here
        holder.sliderLayout.removeAllSliders();
        for(String image : listing.getImageURLs()) {
            TextSliderView textSliderView = new TextSliderView(ctx);
            try {
                textSliderView
                        .description(listing.getTitle())
                        .image(String.valueOf(new URI(Constants.SERVER_IP_ADDRESS + image)))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this)
                        .bundle(new Bundle())
                        .getBundle().putString("id", String.valueOf(listing.getId()));
                //Log.e("h", listing.getTitle() + "-" + String.valueOf(new URI(Constants.SERVER_IP_ADDRESS + image)));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            holder.sliderLayout.addSlider(textSliderView);
        }
        //holder.title.setText(listing.getTitle());

        return convertView;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        FragmentManager fragmentManager = ctx.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ma_fragment_container, ListingDetailsFragment.newInstance(Integer.parseInt((String) slider.getBundle().get("id"))), "ListingDetailsFragment").addToBackStack(null).commit();
    }

    static class Holder{
        FrameLayout layout;
        SliderLayout sliderLayout;
    }
}
