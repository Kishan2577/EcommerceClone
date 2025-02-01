package com.example.amazoncloneproject.Adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazoncloneproject.R;
import com.example.amazoncloneproject.interfaces.ItemClickListener;

public class ViewProductsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView addProductName, addProductPrice;
    private ItemClickListener itemClickListener;
    public ImageView addProductImg;


    public ViewProductsHolder(@NonNull View v) {
        super(v);

        addProductName = v.findViewById(R.id.prod_name);
        addProductPrice = v.findViewById(R.id.prod_price);
        addProductImg = v.findViewById(R.id.prod_image);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
