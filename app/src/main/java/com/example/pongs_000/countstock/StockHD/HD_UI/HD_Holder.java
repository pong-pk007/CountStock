package com.example.pongs_000.countstock.StockHD.HD_UI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.pongs_000.countstock.R;

/**
 * Created by pongs_000 on 28/4/2560.
 */


public class HD_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView docudate, docuno , wh;
    ItemClickListener itemClickListener;


    public HD_Holder(View itemView){
        super(itemView);

        docudate = (TextView) itemView.findViewById(R.id.tvdocudate);
        docuno = (TextView) itemView.findViewById(R.id.tvdocuno);
        wh = (TextView) itemView.findViewById(R.id.tvwh);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        this.itemClickListener.onItemClick();
    }

    public void setItemClickListener (ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;

    }
}
