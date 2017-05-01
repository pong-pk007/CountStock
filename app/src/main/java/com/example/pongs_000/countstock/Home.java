package com.example.pongs_000.countstock;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.pongs_000.countstock.StockHD.HD_SQL.HD_Down;

public class Home extends AppCompatActivity {

    public static String Server = new Server().name();
    private RecyclerView rv;
    private SwipeRefreshLayout sw;

    private static final String MY_PREFS = "my_prefs";

    private String user_id, user_name, fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("รายการเอกสารตรวจนับสินค้า");


        Intent i = getIntent();
        user_id = i.getExtras().getString("userid");
        user_name = i.getExtras().getString("username");
        fullname = i.getExtras().getString("fullname");



        rv = (RecyclerView) findViewById(R.id.rv_hd);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));


        sw = (SwipeRefreshLayout) findViewById(R.id.sw_hd);
        sw.setColorSchemeResources(
                R.color.yellow,       //This method will rotate
                R.color.red,        //colors given to it when
                R.color.yellow,     //loader continues to
                R.color.green);
        sw.setSize(SwipeRefreshLayout.MEASURED_STATE_TOO_SMALL);
        sw.setProgressBackgroundColorSchemeResource(R.color.white);


        new HD_Down(Home.this, Server+"get_stockHD.php",rv,sw).execute();


        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new HD_Down(Home.this, Server+"get_stockHD.php",rv,sw).execute();
            }
        });


    }


    @Override
    public void onBackPressed() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("ออกจากระบบ");
            dialog.setIcon(R.drawable.king_logo50x50);
            dialog.setCancelable(true);
            dialog.setMessage("คุณต้องการออกจากระบบ หรือไม่?");
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


}
