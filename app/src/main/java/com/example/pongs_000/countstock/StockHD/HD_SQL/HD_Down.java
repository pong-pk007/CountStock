package com.example.pongs_000.countstock.StockHD.HD_SQL;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by pongs_000 on 28/4/2560.
 */

public class HD_Down extends AsyncTask<Void, Void, String>{

    Context c;
    String url;
    RecyclerView rv;
    SwipeRefreshLayout sw;


    public HD_Down(Context c, String url, RecyclerView rv, SwipeRefreshLayout sw){
        this.c = c;
        this.url = url;
        this.rv = rv;
        this.sw = sw;
    }

    @Override
    protected void onPreExecute() {
        if(!sw.isRefreshing()){
            sw.setRefreshing(true);
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return this.dowloadData();
    }

    @Override
    protected void onPostExecute(String jsonData) {
        if (jsonData == null){
            sw.setRefreshing(false);
            Toast.makeText(c, "เครือข่ายล้มเหลว", Toast.LENGTH_SHORT).show();
        }else {
            new HD_Parser(c,jsonData, rv,sw).execute();
        }

        super.onPostExecute(jsonData);
    }

    private String dowloadData(){

        HttpURLConnection con = HD_Con.connect(url);
        if (con==null){
            return null;
        }
        try{
            InputStream is = new BufferedInputStream(con.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer jsonData = new StringBuffer();

            while ((line = br.readLine()) != null){

                jsonData.append(line+"\n");
            }

            br.close();
            is.close();
            return jsonData.toString();

        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }
}
