package com.example.pongs_000.countstock;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user, pass;
    private TextInputLayout inputLayoutUsername, inputLayoutPassword;
    private Button btnLogin;

    private static final String MY_PREFS = "my_prefs";
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    private static String TAG = "Myservice";
    private static String Server = new Server().name();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
         editor = shared.edit();

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("  King Check Stock");
        getSupportActionBar().setSubtitle("  ระบบเช็คสต็อกสินค้า");

        if( getIntent().getBooleanExtra("Exit me", false)){
            return;
        }

            inputLayoutUsername = (TextInputLayout) findViewById(R.id.input_layout_username);
            inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
            user = (EditText) findViewById(R.id.username);
            pass = (EditText) findViewById(R.id.password);

            user.addTextChangedListener(new MyTextWatcher(user));
            pass.addTextChangedListener(new MyTextWatcher(pass));



            btnLogin = (Button) findViewById(R.id.login_button);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
                new onLoad().execute(Server+"login.php?user="+user.getText().toString()+"&pass="+pass.getText().toString());
            }
        });
    }

    private void submitForm() {
        if (!validateUsername()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }

    private boolean validateUsername() {
        if (user.getText().toString().trim().isEmpty()) {
            inputLayoutUsername.setError(getString(R.string.err_msg_name));
            requestFocus(user);
            return false;
        } else {
            inputLayoutUsername.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePassword() {
        if (pass.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(pass);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.username:
                    validateUsername();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

    private class onLoad extends AsyncTask<String,Void,Void>
    {
        private ProgressDialog pd;
        boolean con = false;
        String id = null;
        String username,fullname;
        private String message;

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("กำลังทำงาน");
            pd.setMessage("โหลดข้อมูล...");
            pd.show();
        }

        @Override
        protected Void doInBackground(String... params)
    {
        String jString;
            try {
                jString = getJsonFromUrl(params[0]);

                if (jString != null) {

                    JSONArray jArray = new JSONArray(jString);
                    for (int i=0; i < jArray.length() ; i++ ) {
                        JSONObject jObj = jArray.getJSONObject(i);

                        id = jObj.getString("userid");
                        fullname = jObj.getString("fullname");
                        username = jObj.getString("username");

                    }// for
                    editor.putString("userid",id);
                    editor.putString("fullname",fullname);
                    editor.commit();
                    con = true;
                }// if

            } catch (IOException e) {
                //MessageBox("การเชื่อมต่อผิดพลาด");
                message = e.toString();
            } catch (JSONException e) {
                //MessageBox("การรับส่งผิดพลาด");
                message = e.toString();
            }catch (Exception e) {
                //Log.d(TAG, "Problem reading " +  ex.getLocalizedMessage());
                //MessageBox("การเชื่อมต่อผิดพลาด");
                message = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void v)
        {
            pd.dismiss();
            if(con!=false && id!=null)
            {
                Toast.makeText(MainActivity.this, "ล็อกอินสำเร็จ" ,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this, Home.class);
                i.putExtra("id",id);
                i.putExtra("fullname",fullname);
                i.putExtra("username",username);
                startActivity(i);
            }
            else if (con == false){
                MessageBox("การเชื่อมต่อผิดพลาด");
                //MessageBox(message);
            }
            else {
                MessageBox("รหัสผ่านผิด");
            }
        }
    }

    private String getJsonFromUrl(String strUrl) throws IOException {

        URL url = new URL(strUrl);
        try {
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(6*1000);
            httpCon.connect();

            int responseCode = httpCon.getResponseCode();
            //Log.d(TAG, "The response is: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK){
                //Log.d(TAG, " size: " + httpCon.getContentLength());

                InputStream ins = httpCon.getInputStream();
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(ins,"UTF-8"));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                    //Log.d(TAG, line);
                }
                rd.close();
                return response.toString();
            }

        } catch (Exception ex) {
            //Log.d(TAG, "Problem reading " +  ex.getLocalizedMessage());
            //ex.printStackTrace();
        }
        return null;
    }

    public void MessageBox(String str) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(str);
        alert.setIcon(R.drawable.king_logo50x50);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    protected void onActivityResult ( int requestCode, int resultCode, Intent intent )
    {
        super.onActivityResult ( requestCode, resultCode, intent );
        if ( requestCode == 1)
        {
            finish();
        }
    }


}
