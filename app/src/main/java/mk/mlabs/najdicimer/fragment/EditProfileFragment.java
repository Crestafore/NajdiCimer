package mk.mlabs.najdicimer.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InvalidObjectException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONParser;
import mk.mlabs.najdicimer.model.User;

/**
 * Created by SimonaS on 07/05/2016.
 */
public class EditProfileFragment extends Fragment {

    private long id;
    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText date;
    private EditText username;
    private EditText password;
    private TextView usernameText;
    private TextView nameText;
    private int day;
    private int month;
    private int year;
    private Button editButton;
    private User user;

    public EditProfileFragment(){}

    public static EditProfileFragment newInstance(long id) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.id = getArguments().getLong("id");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.edit_profile_fragment, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        new RetrieveFeedTask().execute(id);
    }

    private void init(){
        name = (EditText) getActivity().findViewById(R.id.editName);
        surname = (EditText) getActivity().findViewById(R.id.editSurname);
        username = (EditText) getActivity().findViewById(R.id.editUsername);
        email = (EditText) getActivity().findViewById(R.id.editEmail);
        date = (EditText) getActivity().findViewById(R.id.editDateOfBirth);
        password = (EditText) getActivity().findViewById(R.id.editPassword);
        usernameText = (TextView) getActivity().findViewById(R.id.user_profile_username);
        nameText = (TextView) getActivity().findViewById(R.id.user_profile_name);
        editButton = (Button) getActivity().findViewById(R.id.btn_edit);
    }

    private void showData(){
        usernameText.setText(user.getUsername());
        nameText.setText(user.getName() + " " + user.getSurname());
        username.setText(user.getUsername());
        name.setText(user.getName());
        surname.setText(user.getSurname());
        email.setText(user.getEmail());
        date.setText(user.getBirthDate());
        password.setText(user.getPassword());
    }

    private void showDateDialog(){

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        day = selectedday;
                        month = selectedmonth + 1;
                        year = selectedyear;

                        date.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Избери дата");
                mDatePicker.show();
            }

        });

    }

    private void sendInfo(){
        String i = user.getId().toString();
        String n = name.getText().toString();
        String s = surname.getText().toString();
        String u = username.getText().toString();
        String e  = email.getText().toString();
        String d = date.getText().toString();
        String p = password.getText().toString();

        new RetrieveFeedTaskEdit().execute(i,n,s,e,u,p,d);
    }

    class RetrieveFeedTask extends AsyncTask<Long, Void, JSONObject> {
        private JSONObject jsonObject;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Getting data...");
            progressDialog.show();

        }

        @Override
        protected JSONObject doInBackground(Long... params) {

            JSONParser parser = new JSONParser();
            jsonObject = parser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/user/" + params[0], null, "GET");
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject response) {
            if (response == null) {
                Log.i("INFO", "THERE WAS AN ERROR");
            }
            progressDialog.dismiss();
            try {
                if(response != null) {
                    user = new JSONParser().parseJSON(response, User.class);
                    showData();
                    showDateDialog();
                    editButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendInfo();
                        }
                    });

                }
                else {
                    Toast.makeText(getActivity(), "Response null", Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidObjectException e) {
                e.printStackTrace();
            }
        }
    }

    class RetrieveFeedTaskEdit extends AsyncTask<String, Void, JSONObject> {
        private JSONObject jsonObject;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Зачувување...");
            progressDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {

            LinkedHashMap<String, String> p = new LinkedHashMap<>();
            p.put("id", params[0]);
            p.put("name", params[1]);
            p.put("surname", params[2]);
            p.put("email", params[3]);
            p.put("username", params[4]);
            p.put("password", params[5]);
            p.put("birthDate", params[6]);

            JSONParser parser = new JSONParser();
            jsonObject = parser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/user/update/" + params[0], p, "POST");
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject response) {
            if (response == null) {
                Log.i("INFO", "THERE WAS AN ERROR");
            }
            progressDialog.dismiss();
        }
    }
}