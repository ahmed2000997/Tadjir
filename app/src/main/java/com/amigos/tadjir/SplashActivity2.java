package com.amigos.tadjir;

import static java.security.AccessController.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.amigos.tadjir.Model.User_control;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashActivity2 extends AppCompatActivity {

   public FirebaseUser user;



    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash2);

        FirebaseAuth auth = FirebaseAuth.getInstance();
       user = auth.getCurrentUser();

        // التأكد من وجود مستخدم قبل جلب البيانات
        if (user != null) {
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
            userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {
                        Toast.makeText(SplashActivity2.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (value != null && value.exists()) {
                        String type = value.getString("TYPE");
                        if (type != null && type.equals("admin")) {

                        }
                        else  {  User_control USES = new User_control(); USES.setTest(false);}
                    }
                }
            });
        }









        new Handler().postDelayed(new Runnable() {
    @Override
    public void run() {


        if (user==null){

            startActivity(new Intent(SplashActivity2.this, FragmentReplaceActivity2.class));
        }
        else {

            startActivity(new Intent(SplashActivity2.this, MainActivity.class));}
        finish();

    }
},2500);


    }
}