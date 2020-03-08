package com.example.a2hands.settingsPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditNameActivity extends AppCompatActivity {

    EditText fName;
    EditText lName;

    //firebase
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_edit_name);

        fName = findViewById(R.id.editTxt_changeFName);
        lName = findViewById(R.id.editTxt_changeLName);
        Button saveName = findViewById(R.id.saveNewName_btn);



        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> newName = new HashMap<>();
                newName.put("first_name", fName.getText().toString().trim());
                newName.put("last_name", lName.getText().toString().trim());

                if(TextUtils.isEmpty(fName.getText().toString().trim())){
                    fName.setError("Enter your First Name");
                } else if(TextUtils.isEmpty(lName.getText().toString().trim())){
                    lName.setError("Enter your Last Name");
                } else {
                    db.collection("users").document(user.getUid()).update(newName)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("saveNewName", "Done");
                                    startActivity(new Intent(EditNameActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("saveNewName", e.toString());
                                    startActivity(new Intent(EditNameActivity.this , SettingsActivity.class));
                                    finish();
                                }
                            });
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditNameActivity.this, SettingsActivity.class));
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
