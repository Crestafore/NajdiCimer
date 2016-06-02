package mk.mlabs.najdicimer.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.ArrayList;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONArrayParser;
import mk.mlabs.najdicimer.helper.Status;
import mk.mlabs.najdicimer.model.Message;

/**
 * Created by Darko on 5/12/2016.
 */
public class MessagesFragment extends Fragment {
    private ArrayList<Message> messages;

    public static MessagesFragment newInstance(){
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
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
        View view = inflater.inflate(R.layout.messages_fragment, container, false);
        view.findViewById(R.id.mf_received_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveFeedTask().execute("received");
            }
        });

        view.findViewById(R.id.mf_sent_messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RetrieveFeedTask().execute("sent");
            }
        });

        return view;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONArray> {
        private JSONArray jsonArray;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Fetching...");
            progressDialog.show();
        }

        @Override
        protected JSONArray doInBackground(String... params) {
            jsonArray = JSONArrayParser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/user/message/" + params[0] + "/" + mk.mlabs.najdicimer.helper.Status.userId);
            return jsonArray;
        }


        protected void onPostExecute(JSONArray response) {
            messages = JSONArrayParser.parseJSON(response, Message.class);
            progressDialog.dismiss();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MessagesFragmentList messagesFragmentList = MessagesFragmentList.newInstance(messages);
            fragmentTransaction.replace(R.id.ma_fragment_container, messagesFragmentList).commit();
        }
    }
}
