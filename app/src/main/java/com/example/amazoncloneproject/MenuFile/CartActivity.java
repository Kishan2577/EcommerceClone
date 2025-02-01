package com.example.amazoncloneproject.MenuFile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazoncloneproject.Adapters.CartViewHolder;
import com.example.amazoncloneproject.R;
import com.example.amazoncloneproject.model.Cart;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends BaseActivity {
    LinearLayout dynamicContent,bottomNavBar;

    RecyclerView recyclerView;
    AppCompatButton nextBtn;
    RecyclerView.LayoutManager layoutManager;

    TextView totalprice;
    private int overallPrice = 0;
    Toolbar toolbar;
    FirebaseAuth auth;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dynamicContent = findViewById(R.id.dynamicContent);
        bottomNavBar = findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_cart,null);
        dynamicContent.addView(wizard);

        RadioButton rb = findViewById(R.id.bottom_cart);
        RadioGroup rg = findViewById(R.id.radioGroup1);


        rb.setBackgroundColor(R.color.item_selected);
//        rb.setTextColor(Color.parseColor("3F5185"));

   //     toolbar = findViewById(R.id.cart_toolbar);
   //     toolbar.setBackgroundResource(R.drawable.bg_color);

        auth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextBtn = findViewById(R.id.next_button);
        totalprice = findViewById(R.id.totalprice);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalprice.getText().toString().equals("₹0") || totalprice.getText().toString().length() == 0)
                {
                    Toast.makeText(CartActivity.this, "CART IS EMPTY", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(CartActivity.this, PlaceOrderActivity.class);
                    intent.putExtra("totalAmount",totalprice.getText().toString());
                    Log.e("TEST CART","TEST 1");
                    startActivity(intent);
                    finish();
                    Log.e("TEST CART","TEST 2");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View").child(auth.getCurrentUser().getUid())
                        .child("Products"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                String name = model.getName().replaceAll("\n"," ");
                holder.cartProductName.setText(name);
                holder.cartProductPrice.setText(model.getPrice());

                String intPrice = model.getPrice().replace("₹","");
                overallPrice += Integer.valueOf(intPrice);

                totalprice.setText("₹"+String.valueOf(overallPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("DELETE PRODUCT");

                        builder.setMessage("DO YOU WANT TO REMOVE THIS PRODUCT FROM THE CART")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        cartListRef.child("User View").child(auth.getCurrentUser().getUid())
                                                .child("Products")
                                                .child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            String intPrice = model.getPrice().replace("₹","");
                                                            overallPrice = overallPrice - Integer.valueOf(intPrice);
                                                            totalprice.setText("₹"+String.valueOf(overallPrice));
                                                            Log.d("NEWTEST", "PID to remove: " + model.getPid());

                                                            Toast.makeText(CartActivity.this, "ITEM REMOVED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                builder.show();


                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}