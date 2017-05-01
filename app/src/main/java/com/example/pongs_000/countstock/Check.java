package com.example.pongs_000.countstock;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

public class Check extends AppCompatActivity {

    private String hd_id,docdate,docno,wh,userid,userfname;

    //from json string
    private String sku_code, goods_code, sku_name, utq_name, utq_qty, sku_key, goods_key, utq_key;

    public static final int REQUEST_QR_SCAN = 4;
    public static ImageButton readbarcode,search;
    private Button btnSave;
    TextView productkey, productname, unit, multi;


    private static final String MY_PREFS = "my_prefs";
    SharedPreferences shared;
    SharedPreferences.Editor editor;

    public static EditText good_id, input_qty;

    private static String Server = new Server().name();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        hd_id = i.getExtras().getString("id");
        docdate = i.getExtras().getString("docudate");
        docno = i.getExtras().getString("docuno");
        wh = i.getExtras().getString("wh");

        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        editor = shared.edit();
        userid = shared.getString("userid","ไม่พบไอดีผู้ใช้");
        userfname = shared.getString("fullname","ไม่พบชื่อผู้ใช้");

//        initail
        productkey = (TextView) findViewById(R.id.productkey);
        productname = (TextView) findViewById(R.id.productname);
        unit = (TextView) findViewById(R.id.unit);
        multi = (TextView) findViewById(R.id.multi);
        good_id = (EditText) findViewById(R.id.good_id);
        readbarcode = (ImageButton) findViewById(R.id.readbarcode);
        search = (ImageButton) findViewById(R.id.search);
        btnSave = (Button) findViewById(R.id.save);
        input_qty = (EditText) findViewById(R.id.qty);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (good_id.length() > 0 ){

                    new findItem().execute(Server+"findItem.php?itemid="+good_id.getText().toString());

                }else {
                    MessageBox("โปรดกรอกข้อมูล!");
                }
            }
        });



        readbarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                startActivityForResult(Intent.createChooser(intent, "Scan with"), REQUEST_QR_SCAN);
            Intent i = new Intent(Check.this, ScanBarcode.class);
                startActivity(i);
            }
        });

        TextView status = (TextView) findViewById(R.id.status);
        status.setText("ผู้ตรวจนับ : "+userfname+" ,วันที่ตรวจนับ : \n"+docdate+"เลขที่เอกสาร : "+docno+" \nสถานที่เก็บ : "+wh);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_QR_SCAN && resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");
            productkey.setText(contents);
        }
    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("ออกจากการนับสต็อก");
        dialog.setIcon(R.drawable.king_logo50x50);
        dialog.setCancelable(true);
        dialog.setMessage("คุณต้องการนับสต็อกสินค้า หรือไม่?");
        dialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        dialog.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.show();
        //super.onBackPressed();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(mScannerView != null) {
//            mScannerView.stopCamera();
//        }
//    }
//
//    @Override
//    public void handleResult(Result result) {
//        setContentView(R.layout.activity_check);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        mScannerView.stopCameraPreview();
//
//        shared = getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
//        editor = shared.edit();
//
//        good_id = (EditText) findViewById(R.id.good_id);
////        Toast.makeText(Check.this,result.getText(),Toast.LENGTH_SHORT).show();
//        good_id.setText(result.getText());
//
//        Intent i = getIntent();
//        hd_id = i.getExtras().getString("id");
//        docdate = i.getExtras().getString("docudate");
//        docno = i.getExtras().getString("docuno");
//        wh = i.getExtras().getString("wh");
//
//        userid = shared.getString("userid","ไม่พบไอดีผู้ใช้");
//        userfname = shared.getString("fullname","ไม่พบชื่อผู้ใช้");
//
//        TextView test = (TextView) findViewById(R.id.status);
//        test.setText("ผู้ตรวจนับ : "+userfname+" ,วันที่ตรวจนับ : "+docdate+" \nสถานที่เก็บ : "+wh);
//
//                    new findItem().execute(Server+"findItem.php?itemid="+good_id.getText().toString());
//
//
//        readbarcode = (ImageButton) findViewById(R.id.readbarcode);
//        readbarcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mScannerView = new ZXingScannerView(Check.this);
//                setContentView(mScannerView);
//                mScannerView.setResultHandler(Check.this);
//                mScannerView.startCamera();
//
//            }
//        });
//
//
//    }
    private void saveData() {

        HashMap<String, String> postData = new HashMap<String, String>();

        float resultQty;
        resultQty = Float.parseFloat(input_qty.getText().toString());
        float result = resultQty * Float.parseFloat(utq_qty);


//        float qtyvat = Float.parseFloat(input_qty.getText().toString() * utq_qty);

        try {
            postData.put("sku_key", sku_key);
            postData.put("goods_key", goods_key);
            postData.put("sku_code", sku_code);
            postData.put("goods_code", goods_code);
            postData.put("goods_utq", utq_key);
            postData.put("qty", input_qty.getText().toString());
            postData.put("qtyvat", String.valueOf(result));
            postData.put("docno", docno);
            postData.put("userid", userid);


            //post data

            PostResponseAsyncTask task = new PostResponseAsyncTask(Check.this, postData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (s.contains("Record add successfully.")) {
                        MessageBox("บันทึกรายการสำเร็จ");
                    } else {
                        Snackbar.make(findViewById(R.id.coorlayout), "เกิดข้อผิดพลาดระหว่างบันทึกข้อมูล", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        }).show();
                    }

                }
            });

            task.execute(Server + "add_uty.php");
            task.setEachExceptionsHandler(new EachExceptionsHandler() {
                @Override
                public void handleIOException(IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Cannot Connect to Server.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleMalformedURLException(MalformedURLException e) {
                    Toast.makeText(getApplicationContext(),
                            "URL Error.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleProtocolException(ProtocolException e) {
                    Toast.makeText(getApplicationContext(),
                            "Protocal Error.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                    Toast.makeText(getApplicationContext(),
                            "Encoding Error.", Toast.LENGTH_SHORT).show();
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public class findItem extends AsyncTask<String,Void,Void>
    {
        private ProgressDialog pd;
        boolean con = false;
        private String message;

        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(Check.this);
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

                        sku_code = jObj.getString("sku_code");
                        goods_code = jObj.getString("goods_code");
                        sku_name = jObj.getString("sku_name");
                        utq_key = jObj.getString("utq_key");
                        utq_name = jObj.getString("utq_name");
                        utq_qty = jObj.getString("utq_qty");
                        sku_key = jObj.getString("sku_key");
                        goods_key = jObj.getString("goods_key");

                    }// for
                    con = true;

                }else if(jString.equals("0")) {// if

                    MessageBox("ไม่พบข้อมูล");

                }

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
            Toast.makeText(Check.this,"สำเร็จ",Toast.LENGTH_SHORT).show();
            if(con!=false && sku_code != null)
            {
                    productkey.setText("รหัสสินค้า : "+sku_code);
                    productname.setText("ชื่อสินค้า : "+sku_name);
                    unit.setText("หน่วยนับ : "+utq_name);
                    multi.setText("ตัวคูณ : "+utq_qty);

            }else if(con!=false && sku_code == null){
                MessageBox("ไม่พบข้อมูลสินค้า");
                good_id.setText("");
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
        alert.setIcon(R.drawable.king_logo50x50);
        alert.setTitle(str);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }


}
