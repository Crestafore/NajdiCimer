package mk.mlabs.najdicimer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import mk.mlabs.najdicimer.LocationGeotaggingActivity;
import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.MultipartUtility;

/**
 * Created by Mile on 07/05/2016.
 */
public class CreateListingFragment extends Fragment {
    private EditText title;
    private EditText content;
    private String locationName;
    private Button uploadImages;
    private Button enterLocation;
    private Button createListing;
    private byte[] bitmapa;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private LatLng listingLocation = new LatLng(0, 0);

    public CreateListingFragment() {
    }

    public static CreateListingFragment newInstance() {
        CreateListingFragment fragment = new CreateListingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void init() {
        title = (EditText) getActivity().findViewById(R.id.cl_title);
        content = (EditText) getActivity().findViewById(R.id.cl_content);
        createListing = (Button) getActivity().findViewById(R.id.cl_create_listing);
        enterLocation = (Button) getActivity().findViewById(R.id.cl_search_location);
        uploadImages = (Button) getActivity().findViewById(R.id.cl_upload_images);
        locationName = "";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.create_listing, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        createListing.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createListing();
            }
        });

        uploadImages.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        enterLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 10/05/2016
                // open map picker intent
                Intent intent = new Intent(getActivity(), LocationGeotaggingActivity.class);
                startActivityForResult(intent, 100);
            }
        });
    }

    private void createListing() {
        if (!validateTitleAndContent()) {
            Toast.makeText(getActivity(), "Настана грешка. Огласот не е испратен.", Toast.LENGTH_LONG).show();
        } else {
            new RetrieveFeedTask().execute(title.getText().toString(), content.getText().toString(), "", "1", locationName, String.valueOf(listingLocation.latitude), String.valueOf(listingLocation.longitude));
        }
    }

    public boolean validateTitleAndContent() {
        String listingTitle = title.getText().toString();
        String listingContent = content.getText().toString();

        if (listingTitle.trim().equals("")) {
            title.setError("внеси наслов");
            return false;
        }
        if (listingContent.trim().equals("")) {
            content.setError("внеси содржина");
            return false;
        }
        return true;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Огласот се испраќа...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String charset = "UTF-8";
            String requestURL = Constants.SERVER_API_IP_ADDRESS + "/listing/new";
            MultipartUtility multipart = null;
            try {
                multipart = new MultipartUtility(requestURL, charset);
            } catch (IOException e) {
                e.printStackTrace();
            }
            multipart.addFormField("title", params[0]);
            multipart.addFormField("content", params[1]);
            multipart.addFormField("userId", params[3]);
            multipart.addFormField("locationName", params[4]);
            multipart.addFormField("lat", params[5]);
            multipart.addFormField("lng", params[6]);
            try {
                multipart.addFilePart("file[]", bitmapa);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                multipart.finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Огласот е успешно креиран!", Toast.LENGTH_LONG).show();
            getFragmentManager().beginTransaction().replace(R.id.ma_fragment_container, ViewListingsFragment.newInstance()).commit();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Сликај со камера", "Избери постоечка слика",
                "Откажи"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Прикачи слики");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Сликај со камера")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[item].equals("Избери постоечка слика")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Одбери слика"),
                            SELECT_FILE);
                } else if (items[item].equals("Откажи")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 100) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                bitmapa = stream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode != Activity.RESULT_CANCELED) {
            Bundle bundle = data.getParcelableExtra("bundle");
            listingLocation = bundle.getParcelable("latLng");
            locationName = data.getStringExtra("locationName");
        }
    }

}