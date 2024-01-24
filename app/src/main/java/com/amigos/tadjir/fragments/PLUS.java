package com.amigos.tadjir.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;



import com.amigos.tadjir.R;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

public class PLUS extends AppCompatActivity {

private Button call,reservation,send_raiting;
    private TextView prix,location,des,telp,type;
    private RatingBar rtingBar;
    private FirebaseAuth mAuth;
    private ImageView imgeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus);
        prix=findViewById(R.id.prix);
        location=findViewById(R.id.locationET);
        des=findViewById(R.id.descriptionET);
        telp=findViewById(R.id.tel);
        type=findViewById(R.id.citytv);
        imgeView=findViewById(R.id.imageView);
        send_raiting=findViewById(R.id.send);
        rtingBar=findViewById(R.id.ratingBar);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String postId = extras.getString("id");

                CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                        .document(currentUser.getUid())
                        .collection("Post Images");

                reference.document(postId).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String description = document.getString("description");
                                    String imageURL = document.getString("imageUrl");
                                    String tyype = document.getString("type_post");
                                    String nemro_de_telphone = document.getString("nemro_de_telphone");
                                    String locationn = document.getString("location");
                                    String priix = document.getString("prix");
                                    String Raitng_post = document.getString("Raitng_post");
                                    prix.setText(priix);
                                    telp.setText(nemro_de_telphone);
                                    type.setText(tyype);
                                    location.setText(locationn);
                                    des.setText(description);
                                    Glide.with(PLUS.this)
                                            .load(imageURL)
                                            .placeholder(new ColorDrawable(Color.GRAY)) // Placeholder if image loading takes time
                                            .error(new ColorDrawable(Color.RED)) // Placeholder if image fails to load
                                            .into(imgeView);

                                } else {
                                    // لا توجد بيانات متوفرة لهذا الـ ID
                                }
                            } else {
                                // حدث خطأ أثناء جلب البيانات
                                Toast.makeText(PLUS.this, "Error: " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        send_raiting.setOnClickListener(view -> {
            float userRating = rtingBar.getRating();

            if (currentUser != null) {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String postId = extras.getString("id");

                    CollectionReference reference = FirebaseFirestore.getInstance().collection("Users")
                            .document(currentUser.getUid())
                            .collection("Post Images");

                    reference.document(postId).get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                float currentRating = 0f;
                                String Raitng_post = document.getString("Raitng_post");
                                if (Raitng_post != null && !Raitng_post.isEmpty()) {
                                    currentRating = Float.parseFloat(Raitng_post);
                                }

                                // Calculate the new rating as an average
                                float averageRating = (currentRating + userRating) / 2;

                                // Update Raitng_post in Firestore
                                reference.document(postId).update("Raitng_post", String.valueOf(averageRating))
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(PLUS.this, "Rating updated successfully!", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(PLUS.this, "Failed to update rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                // Document doesn't exist
                            }
                        } else {
                            // Error fetching document
                        }
                    });
                }
            }
        });


    }}