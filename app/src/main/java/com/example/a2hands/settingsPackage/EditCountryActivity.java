package com.example.a2hands.settingsPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditCountryActivity extends AppCompatActivity {

    private CountryCodePicker ccpCountry;
    private Button saveCountry;

    //firebase
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_edit_country);

        saveCountry = findViewById(R.id.saveNewCountry_btn);

        ccpCountry = findViewById(R.id.ccpCountry_editCountry);
        ccpCountry.setAutoDetectedCountry(true);

        saveCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newCountry = new HashMap<>();
                newCountry.put("country", ccpCountry.getSelectedCountryName());

                    db.collection("users").document(user.getUid()).update(newCountry)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("saveNewCountry", "Done");
                                    startActivity(new Intent(EditCountryActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("saveNewCountry", e.toString());
                                    startActivity(new Intent(EditCountryActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            });


            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditCountryActivity.this, SettingsActivity.class));
    }


    //for changing app language
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save the data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("My_Language", lang);
        editor.apply();
    }

    public void loadLocale (){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Language", "");
        setLocale(language);
    }

}
