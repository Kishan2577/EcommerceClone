package com.example.amazoncloneproject.MenuFile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazoncloneproject.Adapters.OrdersViewHolder;
import com.example.amazoncloneproject.R;
import com.example.amazoncloneproject.model.Orders;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

public class ShowHistory extends AppCompatActivity {

    RecyclerView ordersList;
    ImageView backProfile;
    FirebaseAuth auth;
    Toolbar cartToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        backProfile=findViewById(R.id.backProfile);
        ordersList=findViewById(R.id.orders_list);
        auth=FirebaseAuth.getInstance();
        ordersList.setLayoutManager(new LinearLayoutManager(ShowHistory.this));
        onStart();

//        cartToolbar=findViewById(R.id.cart_toolbar);
//        cartToolbar.setBackgroundResource(R.drawable.bg_color);

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShowHistory.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference();

        FirebaseRecyclerOptions<Orders> options= new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(ordersRef.child("Orders").child(auth.getCurrentUser().getUid()).child("History"),Orders.class).build();

        FirebaseRecyclerAdapter<Orders, OrdersViewHolder> adapter=
                new FirebaseRecyclerAdapter<Orders, OrdersViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull @NotNull OrdersViewHolder holder, int position, @NonNull @NotNull Orders model) {
                        holder.orderName.setText(model.getName());
                        holder.orderPhone.setText(model.getPhone());
                        holder.orderAddr.setText(model.getAddress());
                        holder.orderCity.setText(model.getCity());
                        holder.orderDate.setText(model.getDate());
                        holder.orderPrice.setText(model.getTotalAmount());
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public OrdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orderhistory_layout,parent,false);
                        OrdersViewHolder holder=new OrdersViewHolder(view);
                        return holder;
                    }
                };

        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

}