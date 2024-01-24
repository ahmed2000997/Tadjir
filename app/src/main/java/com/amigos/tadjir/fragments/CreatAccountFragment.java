package com.amigos.tadjir.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amigos.tadjir.FragmentReplaceActivity2;
import com.amigos.tadjir.MainActivity;
import com.amigos.tadjir.R;
import com.amigos.tadjir.SplashActivity2;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreatAccountFragment extends Fragment {


    private EditText nameet,emailet,paswordet,confirmpaswordet,cityet,type_user;

    private TextView logintv;
    private Button singupbtn;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    public  static final String EMAIL_REGX="^(.+)@(.+)$";
    public CreatAccountFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_creat_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        clicklisner();
    }
    private void init(View view){
        type_user=view.findViewById(R.id.type);
        cityet=view.findViewById(R.id.city);
        nameet=view.findViewById(R.id.NAME);
        emailet=view.findViewById(R.id.EMAIL);
        paswordet=view.findViewById(R.id.PASWORD);
        confirmpaswordet=view.findViewById(R.id.CONFIRMPASWORD);
        singupbtn=view.findViewById(R.id.SIGNUP);
        logintv=view.findViewById(R.id.logintv);
        auth=FirebaseAuth.getInstance();
progressBar=view.findViewById(R.id.progressBar);
    }
    private  void clicklisner(){
        logintv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplaceActivity2)getActivity()).setFragment(new loginFragment());
            }
        });
        singupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type_User=type_user.getText().toString();
                String name=nameet.getText().toString();
                String City=cityet.getText().toString();
                String email=emailet.getText().toString();
                String pasword=paswordet.getText().toString();
                String confirmpasword=confirmpaswordet.getText().toString();
                if (name.isEmpty()||name.equals(""))
                {
                    nameet.setError("Please input valide name");
return;
                }
                if (City.isEmpty()||City.equals(""))
                {
                    cityet.setError("Please input valide city");
                    return;
                }
                if (email.isEmpty()||!email.matches(EMAIL_REGX))
                {
                    emailet.setError("Please input valide email");
                    return;
                }
                if (pasword.isEmpty()||pasword.length()<6)
                {
                    paswordet.setError("Please input valide pasword");
                    return;
                }
                if (!pasword.equals(confirmpasword))
                {
                    paswordet.setError("pasword no match");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                createAccount(name,email,pasword,City,type_User);


            }
        });


    }
    private void createAccount(final String name, final String email, String password,String City,String type_user) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            FirebaseUser user = auth.getCurrentUser();

                            String image = "https://firebasestorage.googleapis.com/v0/b/tadjir-a8f3c.appspot.com/o/unknown-person-icon-Image-from.png?alt=media&token=c3f464b5-d1ce-46c9-8a37-a971e77337b6";

                            UserProfileChangeRequest.Builder request = new UserProfileChangeRequest.Builder();
                            request.setDisplayName(name);
                            request.setPhotoUri(Uri.parse(image));

                            user.updateProfile(request.build());

                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Email verification link send", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            uploadUser(user, name, email,City,type_user);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            String exception = task.getException().getMessage();
                            Toast.makeText(getContext(), "Error: " + exception, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    private void uploadUser(FirebaseUser user, String name, String email,String City,String type_User) {

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();

        Map<String, Object> map = new HashMap<>();

        map.put("name", name);
        map.put("email", email);
        map.put("profileImage", "https://firebasestorage.googleapis.com/v0/b/tadjir-a8f3c.appspot.com/o/unknown-person-icon-Image-from.png?alt=media&token=c3f464b5-d1ce-46c9-8a37-a971e77337b6");
        map.put("uid", user.getUid());
        map.put("search", name.toLowerCase());
        map.put("city",City);
        map.put("Number of bookings", "0");
        map.put("Rating", "0");
        map.put("posts", "0");
        map.put("TYPE",type_User);

        FirebaseFirestore.getInstance().collection("Users").document(user.getUid())
                .set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            assert getActivity() != null;
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(getActivity().getApplicationContext(),SplashActivity2.class));
                            getActivity().finish();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }



}