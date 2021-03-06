package com.example.a2hands.settingsPackage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.R;
import com.example.a2hands.signupPackage.signupActivity;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    private RadioGroup langGroup;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //loading locale before setting the content view
        loadLocale();
        setContentView(R.layout.activity_language);

        langGroup = findViewById(R.id.languageRadioGroup);
        btnDone = findViewById(R.id.changeLang_btn);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (langGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(LanguageActivity.this, "Select Your Language", Toast.LENGTH_SHORT).show();
                }else{
                    int langID = langGroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(langID);

                    if(selectedRadioButton.getText().equals(getResources().getString(R.string.arabic))){
                        setLocale("ar");
                    }else if(selectedRadioButton.getText().equals(getResources().getString(R.string.english))){
                        setLocale("en");
                    }else if(selectedRadioButton.getText().equals(getResources().getString(R.string.french))){
                        setLocale("fr");
                    }
                    startActivity(new Intent(LanguageActivity.this, SettingsActivity.class));
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(LanguageActivity.this, SettingsActivity.class));
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

//        Resources resources = getResources();
//        Configuration configuration = resources.getConfiguration();
//        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
//            configuration.setLocale(locale);
//        } else{
//            configuration.locale=locale;
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
//            getApplicationContext().createConfigurationContext(configuration);
//        } else {
//            resources.updateConfiguration(configuration,displayMetrics);
//        }
//        //save the data to shared preferences
//        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
//        editor.putString("My_Language", lang);
//        editor.apply();

    }

    public void loadLocale (){
        SharedPreferences prefs = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Language", "");
        setLocale(language);
    }


}
