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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2hands.LoginActivity;
import com.example.a2hands.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPassActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText currentPass, newPass,  confirmPass;
    private Button savePass;

    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_edit_pass);

        currentPass = findViewById(R.id.editTxt_changePass_current);
        newPass = findViewById(R.id.editTxt_changePass_new);
        confirmPass = findViewById(R.id.editTxt_changePass_confirm);
        savePass = findViewById(R.id.saveNewPass_btn);

        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().length() < 8 ){
                    if(TextUtils.isEmpty(newPass.getText().toString())){
                        newPass.setError("Enter your Password");
                    }else{
                        newPass.setError("Too short password");
                    }
                } else if(TextUtils.isEmpty(confirmPass.getText().toString())){
                    confirmPass.setError("Enter your Confirm Password");
                } else if(! confirmPass.getText().toString().equals(newPass.getText().toString())){
                    confirmPass.setError("Password doesn't match");
                } else if (! isValidPassword(confirmPass.getText().toString())){
                    Toast.makeText(EditPassActivity.this, "Please add at least 1 Alphabet," + "\n" +" 1 Number and 1 Special Character", Toast.LENGTH_LONG).show();
                } else {
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), currentPass.getText().toString()); // Current Login Credentials \\
                    // Prompt the user to re-provide their sign-in credentials
                    user.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("", "User re-authenticated.");
                                    //----------------Code for Changing Password----------\\
                                    user.updatePassword(newPass.getText().toString().trim())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("saveNewPass", "Done");
                                                        FirebaseAuth.getInstance().signOut();
                                                        startActivity(new Intent(EditPassActivity.this , LoginActivity.class));
                                                        finish();
                                                    }
                                                }
                                            });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("saveNewEmail", e.toString());
                            currentPass.setError("Wrong Password");
                        }
                    });
                }
            }
        });

    }

    //password validation
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditPassActivity.this, SettingsActivity.class));
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
