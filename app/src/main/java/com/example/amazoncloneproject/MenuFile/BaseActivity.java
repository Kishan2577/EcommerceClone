package com.example.amazoncloneproject.MenuFile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amazoncloneproject.HomeActivity;
import com.example.amazoncloneproject.R;

public class BaseActivity extends AppCompatActivity {


    RadioGroup radioGroup1;
    RadioButton home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroup1 = findViewById(R.id.radioGroup1);
        home = findViewById(R.id.bottom_home);

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Intent intent;

                if(i == R.id.bottom_home) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                else if(i == R.id.bottom_addprod)
                {
                    intent = new Intent(getApplicationContext(), AddProduct.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);

                }
                else if(i == R.id.bottom_search)
                {

                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                else if(i == R.id.bottom_cart) {
                    intent = new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
                else if(i==R.id.bottom_profile) {
                    intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);

                }
            }
        });
    }
}