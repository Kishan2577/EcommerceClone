package com.example.amazoncloneproject.MenuFile;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import com.example.amazoncloneproject.HomeActivity;
import com.example.amazoncloneproject.R;
import com.google.android.gms.fido.fido2.api.common.GoogleThirdPartyPaymentExtension;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {

    EditText shipName, shipPhone, shipAddress, shipCity;
    AppCompatButton confirmOrder;
    FirebaseAuth auth;
    Intent intent;
    String totalAmount;
    TextView cartpricetotal;
    Toolbar cartToolbar;
    int amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_place_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        shipName=findViewById(R.id.shipName);

        shipPhone=findViewById(R.id.shipPhone);
        Log.e("TEST PLACE","TEST 0");
        shipAddress=findViewById(R.id.shipAddress);
        Log.e("TEST PLACE","TEST 1");
        shipCity=findViewById(R.id.shipCity);
        Log.e("TEST PLACE","TEST 2");
        confirmOrder=findViewById(R.id.confirmOrder);
        Log.e("TEST PLACE","TEST 3");
        cartpricetotal=findViewById(R.id.cartpricetotal);
        Log.e("TEST PLACE","TEST 4");
        //cartToolbar=findViewById(R.id.cart_toolbar);
        Log.e("TEST PLACE","TEST 5");
        auth= FirebaseAuth.getInstance();

//        cartToolbar.setBackgroundResource(R.drawable.bg_color);
//        confirmOrder.setBackgroundResource(R.drawable.bg_color);

        intent=getIntent();
        totalAmount = intent.getStringExtra("totalAmount");

        cartpricetotal.setText(totalAmount);

        String sAmount="100";

        //convert and round off
        amount=Math.round(Float.parseFloat(sAmount)*100);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check();
            }
        });


    }

    private void check() {

        if(TextUtils.isEmpty(shipName.getText().toString())){
            shipName.setError("Enter name");
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipPhone.getText().toString())){
            shipPhone.setError("Enter phone no.");
            Toast.makeText(this, "Please enter your phone no.", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipAddress.getText().toString())){
            shipAddress.setError("Enter address");
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(shipCity.getText().toString())){
            shipCity.setError("Enter phone no.");
            Toast.makeText(this, "Please enter your city", Toast.LENGTH_SHORT).show();
        }else{
            paymentFunc();
        }

    }

    private void paymentFunc(){

        Checkout checkout= new Checkout();

        //set key id
        checkout.setKeyID("rzp_test_k2xvsKoKqhTaa0");

        //set image
        checkout.setImage(R.drawable.rzp_logo);

        //initialize JSON object
        JSONObject object= new JSONObject();

        try {
            //put name
            object.put("name","Android User");

            //put description
            object.put("description","Test Payment");

            //put currency unit
            object.put("currency","INR");

            //put amount
            object.put("amount",amount);

            //put mobile number
            object.put("prefill.contact","8668829569");

            //put email
            object.put("prefill.email","androiduser@rzp.com");

            //open razorpay checkout activity
            checkout.open(PlaceOrderActivity.this,object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void confirmOrderFunc() {
        final String saveCurrentDate, saveCurrentTime;

        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(auth.getCurrentUser().getUid()).child("History")
                .child(saveCurrentDate.replaceAll("/","-")+" "+saveCurrentTime);

        HashMap<String, Object> ordersMap= new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",shipName.getText().toString());
        ordersMap.put("phone",shipPhone.getText().toString());
        ordersMap.put("address",shipAddress.getText().toString());
        ordersMap.put("city",shipCity.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                //empty user's cart after confirming order
                if(task.isSuccessful()) {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View").child(auth.getCurrentUser().getUid())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(PlaceOrderActivity.this, "Your order has been placed successfully", Toast.LENGTH_SHORT).show();
                                        Intent intentcart= new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                        intentcart.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentcart);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }


    @Override
    public void onPaymentSuccess(String s) {
        confirmOrderFunc();
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Payment ID");
        builder.setMessage(s);
        builder.show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}