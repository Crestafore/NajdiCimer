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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InvalidObjectException;
import java.util.HashMap;

import mk.mlabs.najdicimer.R;
import mk.mlabs.najdicimer.helper.Constants;
import mk.mlabs.najdicimer.helper.JSONParser;
import mk.mlabs.najdicimer.helper.Status;
import mk.mlabs.najdicimer.model.User;

/**
 * Created by SimonaS on 05/05/2016.
 */
public class LoginFragment extends Fragment {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private TextView signupLink;
    private User loggedUser;

    public LoginFragment(){}

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void init(){
        username = (EditText) getActivity().findViewById(R.id.input_username);
        password = (EditText) getActivity().findViewById(R.id.input_password);
        loginButton = (Button) getActivity().findViewById(R.id.btn_login);
        signupLink = (TextView) getActivity().findViewById(R.id.link_signup);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.login, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ma_fragment_container, SignupFragment.newInstance(), "SignupFragment").commit();
            }
        });
    }

    private void login(){

        if(!validate()){
            onLoginFailed();
        }else{
            onLoginSuccess();
            new RetrieveFeedTask().execute(username.getText().toString(), password.getText().toString());
        }
    }

    public void onLoginSuccess() {
        Toast.makeText(getActivity(), "Login successfull", Toast.LENGTH_LONG).show();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.compareTo("") == 0){
            username.setError("enter a username");
            valid = false;
        }

        if(pass.compareTo("") == 0) {
            password.setError("enter a password");
            valid = false;
        }

        return valid;
    }

    class RetrieveFeedTask extends AsyncTask<String, Void, JSONObject> {
        private JSONObject jsonObject;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

        }

        @Override
        protected JSONObject doInBackground(String... params) {
            HashMap<String, String> p = new HashMap<String, String>();
            p.put("username", params[0]);
            p.put("password", params[1]);

            JSONParser parser = new JSONParser();
            jsonObject = parser.downloadContent(Constants.SERVER_API_IP_ADDRESS + "/user/login", p, "POST");
            return jsonObject;
        }


        @Override
        protected void onPostExecute(JSONObject response) {
            if (response == null) {
                Log.i("INFO", "THERE WAS AN ERROR");
            }
            progressDialog.dismiss();
            loggedUser = null;
            try {
                TextView res = (TextView) getActivity().findViewById(R.id.username_response);
                if(response != null) {
                    loggedUser = new JSONParser().parseJSON(response, User.class);
                    res.setText(loggedUser.getUsername());
                    mk.mlabs.najdicimer.helper.Status.logIn(getActivity(), loggedUser.getId());
                    getFragmentManager().beginTransaction().replace(R.id.ma_fragment_container, ViewListingsFragment.newInstance()).commit();
                }
                else {
                    Toast.makeText(getActivity(), "Response null", Toast.LENGTH_LONG).show();
                    res.setText("User doesn't exist");
                }
                // display();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidObjectException e) {
                e.printStackTrace();
            }
        }
    }

}
