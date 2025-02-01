package com.example.amazoncloneproject.Adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazoncloneproject.R;
import com.example.amazoncloneproject.interfaces.ItemClickListener;
import com.google.firebase.database.annotations.NotNull;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderName, orderDate, orderAddr, orderCity, orderPrice, orderPhone;
    private ItemClickListener itemClickListener;

    public OrdersViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);

        orderName=itemView.findViewById(R.id.orderName);
        orderPhone=itemView.findViewById(R.id.orderPhone);
        orderAddr=itemView.findViewById(R.id.orderAddr);
        orderCity=itemView.findViewById(R.id.orderCity);
        orderDate=itemView.findViewById(R.id.orderDate);
        orderPrice=itemView.findViewById(R.id.orderPrice);

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
