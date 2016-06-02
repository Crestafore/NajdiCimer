package mk.mlabs.najdicimer.fragment;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.MessagesAdapter;
import mk.mlabs.najdicimer.model.Message;

/**
 * Created by Darko on 5/12/2016.
 */
public class MessagesFragmentList extends ListFragment {
    private ArrayList<Message> messages;
    private ListView listView;

    public MessagesFragmentList(){
        messages = new ArrayList<Message>();
        listView = null;
    }

    public static MessagesFragmentList newInstance(ArrayList<Message> messages){
        MessagesFragmentList fragment = new MessagesFragmentList();
        Bundle args = new Bundle();
        args.putParcelableArrayList("messages", messages);
        fragment.setArguments(args);
        //return new MessagesFragmentList();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            messages = getArguments().getParcelableArrayList("messages");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.view_messages_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = getListView();
        MessagesAdapter listingsAdapter = new MessagesAdapter(getActivity(), messages);
        listView.setAdapter(listingsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
    }
}
