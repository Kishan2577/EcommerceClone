package com.example.amazoncloneproject.MenuFile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amazoncloneproject.Adapters.ViewProductsHolder;
import com.example.amazoncloneproject.HomeActivity;
import com.example.amazoncloneproject.ProductDetails;
import com.example.amazoncloneproject.R;
import com.example.amazoncloneproject.model.AddProdModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SearchActivity extends BaseActivity {
    LinearLayout dynamicContent,bottomNavBar;
    EditText inputText;
    AppCompatButton searchBtn;
    RecyclerView searchList;
    String searchInput;
    ImageView backHome;
    Toolbar viewToolbar;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dynamicContent = findViewById(R.id.dynamicContent);
        bottomNavBar = findViewById(R.id.bottomNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_search,null);
        dynamicContent.addView(wizard);

        RadioButton rb = findViewById(R.id.bottom_search);
        RadioGroup rg = findViewById(R.id.radioGroup1);


          rb.setBackgroundColor(R.color.item_selected);
//        rb.setTextColor(Color.parseColor("3F5185"));


        viewToolbar=findViewById(R.id.viewtoolbar);
        viewToolbar.setBackgroundResource(R.drawable.bg_color);

        inputText = findViewById(R.id.searchEditText);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setBackgroundResource(R.drawable.intro_signin);

        searchList = findViewById(R.id.searchList);
        backHome=findViewById(R.id.backHome);
        searchList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchInput = inputText.getEditableText().toString();
                onStart();
            }
        });

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All")
                .child("User View").child("Products");


        FirebaseRecyclerOptions<AddProdModel> options = new FirebaseRecyclerOptions.Builder<AddProdModel>()
                .setQuery(prodListRef.orderByChild("name").startAt(searchInput),AddProdModel.class).build();


        FirebaseRecyclerAdapter<AddProdModel, ViewProductsHolder> adapter = new FirebaseRecyclerAdapter<AddProdModel, ViewProductsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewProductsHolder holder, int position, @NonNull AddProdModel model) {
                String name = model.getName().replaceAll("\n"," ");
                String price = model.getPrice();
                String imgUri = model.getImg();

                holder.addProductName.setText(name);
                holder.addProductPrice.setText(price);

                Picasso.get().load(imgUri).into(holder.addProductImg);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent  = new Intent(SearchActivity.this, ProductDetails.class);
                        intent.putExtra("id", 4);
                        intent.putExtra("uniqueId",name);
                        intent.putExtra("addProdName",name);
                        intent.putExtra("addProdPrice",price);
                        intent.putExtra("addProdDesc", model.getDescription());
                        intent.putExtra("addProdCategory", model.getCategory());
                        intent.putExtra("img",imgUri);
                        startActivity(intent);


                    }
                });

            }

            @NonNull
            @Override
            public ViewProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_products_adapter,parent,false);
                ViewProductsHolder viewProductsHolder = new ViewProductsHolder(view);
                return viewProductsHolder;
            }
        };
        searchList.setAdapter(adapter);
        adapter.startListening();

    }


}