package com.example.pongs_000.countstock.StockHD.HD_UI;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pongs_000.countstock.Check;
import com.example.pongs_000.countstock.R;
import com.example.pongs_000.countstock.StockHD.HD_Object.HD_Object;

import java.util.ArrayList;

/**
 * Created by pongs_000 on 28/4/2560.
 */

public class HD_Adapter extends RecyclerView.Adapter<HD_Holder> {

    Context c;
    ArrayList<HD_Object> hd_objects;

    public HD_Adapter (Context c, ArrayList<HD_Object> hd_objects){
        this.c = c;
        this.hd_objects = hd_objects;
    }


    @Override
    public HD_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.item_hd,parent,false);
        return new HD_Holder(v);
    }

    @Override
    public void onBindViewHolder(HD_Holder holder, int position) {
        final HD_Object hdObj = hd_objects.get(position);
        holder.docudate.setText(hdObj.getDocudate());
        holder.docuno.setText(hdObj.getDocuno());
        holder.wh.setText(hdObj.getWH());

        if ((position % 2) == 0){
            holder.docudate.setBackgroundColor(R.color.blue);
            holder.docuno.setBackgroundColor(R.color.blue);
            holder.wh.setBackgroundColor(R.color.blue);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick() {
                OpenCheckStock(hdObj.getHd_id(),hdObj.getDocudate(),hdObj.getDocuno(),hdObj.getWH());
            }
        });
    }

    @Override
    public int getItemCount() {
        return hd_objects.size();
    }

        private void OpenCheckStock(String hd_id,String docudate, String docuno, String wh){
            Intent i = new Intent(c, Check.class);
            i.putExtra("id",hd_id);
            i.putExtra("docudate",docudate);
            i.putExtra("docuno",docuno);
            i.putExtra("wh",wh);
            c.startActivity(i);

        }

}
