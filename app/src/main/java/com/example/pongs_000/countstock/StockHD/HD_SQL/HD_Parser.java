package com.example.pongs_000.countstock.StockHD.HD_SQL;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.example.pongs_000.countstock.StockHD.HD_Object.HD_Object;
import com.example.pongs_000.countstock.StockHD.HD_UI.HD_Adapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Created by pongs_000 on 28/4/2560.
 */

public class HD_Parser extends AsyncTask<Void,Void,Boolean> {


    Context c;
    RecyclerView rv;
    SwipeRefreshLayout sw;
    String jsonData;

    ArrayList<HD_Object> hd_objects = new ArrayList<>();

    public HD_Parser (Context c, String jsonData , RecyclerView rv, SwipeRefreshLayout sw) {
        this.c = c;
        this.jsonData = jsonData;
        this.rv = rv;
        this.sw = sw;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        return this.parseData();
    }

    @Override
    protected void onPostExecute(Boolean isParsed) {
        super.onPostExecute(isParsed);

        sw.setRefreshing(false);

        if (isParsed){
            rv.setAdapter(new HD_Adapter(c, hd_objects));

        }else {
            Toast.makeText(c,"ข้อมูลไม่ตรงกัน", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean parseData(){

        try{
            JSONArray ja = new JSONArray(jsonData);
            JSONObject jo;

            hd_objects.clear();
            HD_Object hd_object;

            for (int i=0; i<ja.length(); i++){

                jo=ja.getJSONObject(i);

                String idhd = jo.getString("id_key");
                String docudate  = jo.getString("docudate");
                String docuno = jo.getString("docuno");
                String wl_name = jo.getString("wl_name");


                hd_object = new HD_Object();

                hd_object.setHd_id(idhd);
                hd_object.setDocudate(docudate);
                hd_object.setDocuno(docuno);
                hd_object.setWH(wl_name);

                hd_objects.add(hd_object);

            }
            return true;

        }catch (JSONException e){
            e.printStackTrace();

        }
        return false;
    }
}
