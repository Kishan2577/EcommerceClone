package com.example.amazoncloneproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.amazoncloneproject.Adapters.ProductAdapter;
import com.example.amazoncloneproject.MenuFile.BaseActivity;
import com.example.amazoncloneproject.MenuFile.CartActivity;
import com.example.amazoncloneproject.MenuFile.SearchActivity;
import com.example.amazoncloneproject.constant.Constant;
import com.example.amazoncloneproject.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends BaseActivity {
    private ViewPager2 viewPager;
    LinearLayout dynamicContent,bottomNavBar;
    Toolbar toolbar;
    CardView shoes1,shoes2,shoes3,shoes4,shoes5;
    TextView oddshoename,oddshoeprice,evenshoename,evenshoeprice,viewAll;
    public static ImageView home_cart;

    FirebaseStorage storage;
    StorageReference storageReference;

    Intent intentcart;
    String getcartupdate;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        dynamicContent = findViewById(R.id.dynamicContent);
        bottomNavBar = findViewById(R.id.bottomNavBar);
        Log.e("HOME","TEST 1");
        View wizard = getLayoutInflater().inflate(R.layout.activity_home,null);
        dynamicContent.addView(wizard);
        Log.e("HOME","TEST 2");
        RadioButton rb = findViewById(R.id.bottom_home);
        RadioGroup rg = findViewById(R.id.radioGroup1);

        Log.e("HOME","TEST 3");
        rb.setBackgroundColor(R.color.item_selected);
//        rb.setTextColor(Color.parseColor("3F5185"));
        Log.e("HOME","TEST 4");
        SlideShow();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.drawable.bg_color);

        shoes1 = findViewById(R.id.shoes1);
        shoes2 = findViewById(R.id.shoes2);
        shoes3 = findViewById(R.id.shoes3);
        shoes4 = findViewById(R.id.shoes4);
        shoes5 = findViewById(R.id.shoes5);

        oddshoename = findViewById(R.id.oddshoename);
        oddshoeprice = findViewById(R.id.oddshoeprice);
        evenshoename = findViewById(R.id.evenshoename);
        evenshoeprice = findViewById(R.id.evenshoeprice);

        viewAll = findViewById(R.id.viewAllProducts);
        home_cart = findViewById(R.id.home_cart);

        storage = FirebaseStorage.getInstance();

        intentcart = getIntent();
        if(intentcart.getStringExtra("cartadd")!=null && intentcart.getStringExtra("cartadd").equals("yes"))
        {
            home_cart.setImageResource(R.drawable.cart_notif);
        }
        else if(intentcart.getStringExtra("cartadd")!=null && intentcart.getStringExtra("cartadd").equals("no"))
        {
            home_cart.setImageResource(R.drawable.cart_notif);
        }


        shoes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("name",oddshoename.getText().toString());
                intent.putExtra("category","MEN's RUNNING SHOES");
                intent.putExtra("price",oddshoeprice.getText().toString());
                intent.putExtra("uniqueId",oddshoename.getText().toString());
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        shoes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("name",evenshoename.getText().toString());
                intent.putExtra("category","MEN's RUNNING SHOES");
                intent.putExtra("price",evenshoeprice.getText().toString());
                intent.putExtra("uniqueId",evenshoename.getText().toString());
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });

        shoes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("name",oddshoename.getText().toString());
                intent.putExtra("category","MEN's RUNNING SHOES");
                intent.putExtra("price",oddshoeprice.getText().toString());
                intent.putExtra("uniqueId",oddshoename.getText().toString());
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        shoes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("name",evenshoename.getText().toString());
                intent.putExtra("category","MEN's RUNNING SHOES");
                intent.putExtra("price",evenshoeprice.getText().toString());
                intent.putExtra("uniqueId",evenshoename.getText().toString());
                intent.putExtra("id",2);
                startActivity(intent);
            }
        });

        shoes5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("name",oddshoename.getText().toString());
                intent.putExtra("category","MEN's RUNNING SHOES");
                intent.putExtra("price",oddshoeprice.getText().toString());
                intent.putExtra("uniqueId",oddshoename.getText().toString());
                intent.putExtra("id",1);
                startActivity(intent);
            }
        });

        ListView lvProducts = findViewById(R.id.productslist);
        ProductAdapter productAdapter = new ProductAdapter(this);
        productAdapter.updateProduct(Constant.PRODUCT_LIST);
        lvProducts.setAdapter(productAdapter);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product = Constant.PRODUCT_LIST.get(i);
                Intent intent = new Intent(HomeActivity.this, ProductDetails.class);
                intent.putExtra("id",3);
                intent.putExtra("uniqueId",product.getName());
                intent.putExtra("name",product.getName());
                intent.putExtra("description",  product.getDescription());
                intent.putExtra("category","Smartphone");
                intent.putExtra("pprice",Constant.CURRENCY+ String.valueOf(product.getPrice().setScale(0, BigDecimal.ROUND_HALF_UP)));
                intent.putExtra("imageName",product.getImageName());
                startActivity(intent);
            }
        });

        home_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        //addingToProdList();
    }


    private void autoScrollViewPager() {
        final int interval = 2500; // 2.5 seconds
        viewPager.post(new Runnable() {
            @Override
            public void run() {
                int nextItem = (viewPager.getCurrentItem() + 1) % viewPager.getAdapter().getItemCount();
                viewPager.setCurrentItem(nextItem, true);
                viewPager.postDelayed(this, interval);
            }
        });
    }


    private void SlideShow()
    {
        viewPager = findViewById(R.id.viewPager);
        // List of image URLs
        List<String> imageUrls = Arrays.asList(
                "https://www.livemint.com/lm-img/img/2025/01/15/original/tv_1736931346111.png",
                "https://sm.ign.com/ign_in/screenshot/default/prime-cover_fu3a.png",
                "https://img.etimg.com/thumb/width-420,height-315,imgsize-40818,resizemode-75,msid-102420790/top-trending-products/news/amazon-great-freedom-festival-sale-2023-live-now-up-to-75-off-on-electronics-and-accessories-from-top-brands/cepc_600x450.jpg"
        );

        ImageAdapter adapter = new ImageAdapter(this, imageUrls);

        viewPager.setAdapter(adapter);
        // Set auto-scroll behavior
        autoScrollViewPager();
    }

    private void addingToProdList()
    {
        String saveCurrentDate, saveCurrentTime;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference prodListRef = FirebaseDatabase.getInstance().getReference().child("View All");
        String name = "Samsung Galaxy F42";

        final HashMap<String, Object> prodmap = new HashMap<>();
        prodmap.put("pid",name);
        prodmap.put("name",name);
        prodmap.put("price","₹17899");
        prodmap.put("category","Smartphone");
        prodmap.put("description","6 GB RAM\n128 GB ROM\nExpandable Upto 1 TB\n16.76 cm (6.6 inch) Full HD+ Display\n 64MP + 5MP + 2MP | 8MP Front Camera\n 5000 mAh Lithium-ion Battery\nMediaTek Dimensity 700 Processor\nsamsung_galaxy_f42");
        prodmap.put("date",saveCurrentDate);
        prodmap.put("time",saveCurrentTime);

        prodListRef.child("User View").child("Products")
                .child(name).updateChildren(prodmap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("TEST","SUCCESSFULLY ADDED TO DATABASE");
                        }
                    }
                });


    }
}