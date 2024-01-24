package com.amigos.tadjir.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amigos.tadjir.FragmentReplaceActivity2;
import com.amigos.tadjir.Model.PostImageModel;
import com.amigos.tadjir.R;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class Profile extends Fragment {
    boolean isMyProfile = true;
    String userUID;
  FirestoreRecyclerAdapter<PostImageModel, PostImageHolder> adapter;
    List<String> followersList, followingList, followingList_2;
    boolean isFollowed;
    private static final int PICK_IMAGE = 1;

    private ImageButton editProfileImage,sendbtnn;
    DocumentReference userRef, myRef;
    int count;
    private TextView nameTv, toolbarNameTv, statusTv, followingCountTv, followersCountTv, postCountTv;
    private CircleImageView profileImage;
    private Button followBtn, startChatBtn;
    private RecyclerView recyclerView;
    private LinearLayout countLayout;
    private FirebaseUser user;
    private ImageButton editProfileBtn;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       init(view);
        if (isMyProfile) {
            followBtn.setVisibility(View.GONE);
        countLayout.setVisibility(View.VISIBLE);


        } else {

            followBtn.setVisibility(View.VISIBLE);
      countLayout.setVisibility(View.GONE);
        }

       loadBasicData();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
   //     loadPostImages();
        recyclerView.setAdapter(adapter);
    }
    private void  init(View view){
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        assert getActivity() != null;
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        editProfileImage = view.findViewById(R.id.edit_profileImage);
        editProfileImage.setOnClickListener(view1 -> openGallery());

        // Rest of your onCreateView() code...


        nameTv = view.findViewById(R.id.nameTv);
        statusTv = view.findViewById(R.id.statusTV);
        toolbarNameTv = view.findViewById(R.id.toolbarNameTV);
        followersCountTv = view.findViewById(R.id.followersCountTv);
        followingCountTv = view.findViewById(R.id.followingCountTv);
        postCountTv = view.findViewById(R.id.postCountTv);
        profileImage = view.findViewById(R.id.profileImage);
        followBtn = view.findViewById(R.id.followBtn);
        recyclerView = view.findViewById(R.id.recyclerView);
        countLayout = view.findViewById(R.id.countLayout);
sendbtnn=view.findViewById(R.id.sendBtn);
sendbtnn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        // تسجيل الخروج من Firebase Auth
        FirebaseAuth.getInstance().signOut();

        // إعادة توجيه المستخدم إلى شاشة البداية أو أي شاشة أخرى بعد تسجيل الخروج
        Intent intent = new Intent(requireContext(), FragmentReplaceActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
});
        FirebaseAuth auth=FirebaseAuth.getInstance();
 user =auth.getCurrentUser();

    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    private void loadBasicData(){
DocumentReference userRef=FirebaseFirestore.getInstance().collection("Users").document(user.getUid());
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    String errMsg =error.toString();
                    Toast.makeText(getContext(), "Error: "+errMsg, Toast.LENGTH_SHORT).show();
                    nameTv.setText(errMsg);

                    return;
                   }
                if (value.exists()){
                    String name=value.getString("name");
                    nameTv.setText(name);

              String city=value.getString("city");
                    String Number_of_bookings=value.getString("Number of bookings");
                    String Rating=value.getString("Rating");
                    String posts=value.getString("posts");
                    String profileURL=value.getString("profileImage");


                    Glide.with(getContext())
                            .load(profileURL)
                            .into(profileImage);
                    postCountTv.setText(posts);
                    nameTv.setText(name);
                    toolbarNameTv.setText(name);
                    statusTv.setText(city);
                    followersCountTv.setText(String.valueOf(Number_of_bookings));
                    followingCountTv.setText(String.valueOf(Rating));
                                //  Glide.with(getContext().getApplicationContext()).load(profileURL)
                //          .placeholder(R.drawable.ic_person).timeout(6500).into(profileImage);


                }
            }
        });


    }

    private static class PostImageHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public PostImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);

        }

    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                uploadImageToFirebaseStorage(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }}}
    private void uploadImageToFirebaseStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a reference to 'profileImages/[UID].jpg'
        StorageReference profileImageRef = storageRef.child("profileImages/" + user.getUid() + ".jpg");

        profileImageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get the uploaded image URL
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the 'profileImage' field in Firestore with the image URL
                        updateProfileImageInFirestore(uri.toString());
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateProfileImageInFirestore(String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userDocRef = db.collection("Users").document(user.getUid());

        userDocRef.update("profileImage", imageUrl)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Update failed
                    Toast.makeText(getContext(), "Failed to update profile image", Toast.LENGTH_SHORT).show();
                });
    }
}