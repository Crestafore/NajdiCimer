package mk.mlabs.najdicimer.fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.LinkedHashMap;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONParser;

/**
 * Created by SimonaS on 05/05/2016.
 */
public class SignupFragment extends Fragment {

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText date;
    private EditText username;
    private EditText password;
    private EditText confirm;
    private Button signupButton;
    private TextView loginText;
    private int day = 0;
    private int month = 0;
    private int year = 0;


    public SignupFragment() {}

    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void init(){
        name = (EditText) getActivity().findViewById(R.id.input_name);
        surname = (EditText) getActivity().findViewById(R.id.input_surname);
        email = (EditText) getActivity().findViewById(R.id.input_email);
        date = (EditText) getActivity().findViewById(R.id.input_date);
        username = (EditText) getActivity().findViewById(R.id.input_username);
        password = (EditText) getActivity().findViewById(R.id.input_password);
        confirm = (EditText) getActivity().findViewById(R.id.input_confirm_password);
        loginText = (TextView) getActivity().findViewById(R.id.link_login);
        signupButton = (Button) getActivity().findViewById(R.id.btn_signup);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.signup, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ma_fragment_container, LoginFragment.newInstance(), "LoginFragment").commit();
            }
        });

        showDateDialog();
    }

    private void signup(){

        if(!validate()){
            onSignupFailed();
        }else{
            onSignupSuccess();
            String data = null;

            if(day != 0 && month != 0 && year != 0)
                data = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

            new RetrieveFeedTask().execute(name.getText().toString(), surname.getText().toString(), data , email.getText().toString(), username.getText().toString(), password.getText().toString());
        }
    }

    public void onSignupSuccess() {
        Toast.makeText(getActivity(), "Sign up successfull", Toast.LENGTH_LONG).show();
    }

    public void onSignupFailed() {
        Toast.makeText(getActivity(), "Sign up failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String n = name.getText().toString();
        String s = surname.getText().toString();
        String e = email.getText().toString();
        String u = username.getText().toString();
        String p = password.getText().toString();
        String c = confirm.getText().toString();

        if(n.compareTo("") == 0){
            name.setError("enter a name");
            valid = false;
        }

        if(s.compareTo("") == 0){
            surname.setError("enter a surname");
            valid = false;
        }

        if(e.compareTo("") == 0){
            email.setError("enter an email");
            valid = false;
        }

        if(u.compareTo("") == 0){
            username.setError("enter a username");
            valid = false;
        }

        if(p.compareTo("") == 0){
            password.setError("enter a password");
            valid = false;
        }

        if(c.compareTo("") == 0){
            confirm.setError("confirm password");
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(e).matches()){
            email.setError("enter a valid email");
            valid = false;
        }

        if(p.compareTo(c) != 0){
            confirm.setError("password doesn't match");
            valid = false;
        }

        return valid;
    }

    private void showDateDialog(){

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate=Calendar.getInstance();
                int mYear=mcurrentDate.get(Calendar.YEAR);
                int mMonth=mcurrentDate.get(Calendar.MONTH);
                int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker=new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        day = selectedday;
                        month = selectedmonth + 1;
                        year = selectedyear;

                        date.setText(String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
                    }
                },mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }

        });

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {

        private ProgressDialog progressDialog;
        private JSONObject jsonObject;


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Creating an account...");
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            LinkedHashMap<String, String> p = new LinkedHashMap<>();
            p.put("name", params[0]);
            p.put("surname", params[1]);
            p.put("birthDate", params[2]);
            p.put("email", params[3]);
            p.put("username", params[4]);
            p.put("password", params[5]);

            JSONParser parser = new JSONParser();
            jsonObject = parser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/user/signup", p, "POST");
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();
            getFragmentManager().beginTransaction().replace(R.id.ma_fragment_container, LoginFragment.newInstance()).commit();
        }
    }
}

