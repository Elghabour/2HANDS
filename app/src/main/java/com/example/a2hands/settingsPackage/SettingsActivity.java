package com.example.a2hands.settingsPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.R;
import com.example.a2hands.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    String[] generalSettingsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_settings);

        //account
        TextView editNamebtn = findViewById(R.id.btn_editName);
        TextView editEmailbtn = findViewById(R.id.btn_editEmail);
        TextView editPhonebtn = findViewById(R.id.btn_editPhone);
        TextView editCountrybtn = findViewById(R.id.btn_editCountry);
        TextView editPassbtn = findViewById(R.id.btn_editPass);
        final TextView editPhoneTxt = findViewById(R.id.txtView_editPhone);
        final TextView editNameTxt = findViewById(R.id.txtView_editName);
        final TextView editCountryTxt = findViewById(R.id.txtView_editCountry);
        final TextView editEmailTxt = findViewById(R.id.txtView_editEmail);
        TextView editPassTxt = findViewById(R.id.textView_editPass);
        TextView deleteAccTxt = findViewById(R.id.textView_deleteAcc);
        deleteAccTxt.setBackgroundColor(Color.TRANSPARENT);

        //general
        ListView generalListView = findViewById(R.id.listView_generalSettings);

        generalSettingsItems = getResources().getStringArray(R.array.generalSettings);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_item_in_listview,generalSettingsItems);
        generalListView.setAdapter(adapter);

        generalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String itemSelected = generalSettingsItems[position];

                switch (position){
                    case 0:
                        startActivity(new Intent(SettingsActivity.this , LanguageActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(SettingsActivity.this , NotificationActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(SettingsActivity.this , BlockedAccsActivity.class));
                        break;
                    case 3:

                        break;
                    case 4:
                        startActivity(new Intent(SettingsActivity.this , AboutAppActivity.class));
                        break;
                }
            }
        });

        editNamebtn.setOnClickListener(this);
        editEmailbtn.setOnClickListener(this);
        editPhonebtn.setOnClickListener(this);
        editPassbtn.setOnClickListener(this);
        editCountrybtn.setOnClickListener(this);
        editPassTxt.setOnClickListener(this);
        deleteAccTxt.setOnClickListener(this);

        //drawer header data
        FirebaseFirestore.getInstance().collection("users/").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);

                editNameTxt.setText(user.first_name+" "+user.last_name);
                editCountryTxt.setText(user.country);
                editPhoneTxt.setText(user.phone);
                editEmailTxt.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
        });


    }// end of onCreate method


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_editName:
                startActivity(new Intent(SettingsActivity.this , EditNameActivity.class));
                break;
            case R.id.btn_editEmail:
                startActivity(new Intent(SettingsActivity.this , EditEmailActivity.class));
                break;
            case R.id.btn_editPhone:
                startActivity(new Intent(SettingsActivity.this , EditPhoneActivity.class));
                break;
            case R.id.btn_editPass:
                startActivity(new Intent(SettingsActivity.this , EditPassActivity.class));
                break;
            case R.id.textView_editPass:
                startActivity(new Intent(SettingsActivity.this , EditPassActivity.class));
                break;
            case R.id.btn_editCountry:
                startActivity(new Intent(SettingsActivity.this , EditCountryActivity.class));
                break;
            case R.id.textView_deleteAcc:
                startActivity(new Intent(SettingsActivity.this , DeleteAccActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this, com.example.a2hands.homePackage.homeActivity.class));
    }

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
