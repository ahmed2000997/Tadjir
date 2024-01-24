package com.amigos.tadjir.fragments;

import static com.amigos.tadjir.fragments.CreatAccountFragment.EMAIL_REGX;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class loginFragment extends Fragment {

private EditText emailet,paswordet;
private TextView creat,forgotpaswortv;
private Button loginbutn,signupbtn;
private ProgressBar progressBar;
private FirebaseAuth auth;
private static final int RC_SIGN_IN=1;
GoogleSignInClient mGoogleSignInClient;
    public loginFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);


        clicklisner();
    }
    private void init(View view){

        emailet=view.findViewById(R.id.EMAIL);
        paswordet=view.findViewById(R.id.PASWORD);
        loginbutn=view.findViewById(R.id.SIGNUP);
        signupbtn=view.findViewById(R.id.SIGNUPGOOGLE);
        creat=view.findViewById(R.id.creat);
        forgotpaswortv=view.findViewById(R.id.forgot);
progressBar=view.findViewById(R.id.progressBar);
        auth=FirebaseAuth.getInstance();
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient =GoogleSignIn.getClient(getActivity(),gso);

    }
    private void clicklisner(){
        forgotpaswortv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentReplaceActivity2) getActivity()).setFragment(new ForgotPassword());
            }
        });
        loginbutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emailet.getText().toString();
                String pass=paswordet.getText().toString();
                if (email.isEmpty()||!email.matches(EMAIL_REGX)){
emailet.setError("input valid email");

return;
                }
                if (pass.isEmpty()||pass.length()<6){
                    paswordet.setError("input valid pasword 6 digits");
return;

                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful()) {

    FirebaseUser user = auth.getCurrentUser();
    if (!user.isEmailVerified()) {
        Toast.makeText(getContext(), "please verify your email", Toast.LENGTH_SHORT).show();
    }


    sendusertomainactivity();
}

else{
    String EXCEPTION="ERROR"+task.getException().getMessage();
    Toast.makeText(getContext(), EXCEPTION, Toast.LENGTH_SHORT).show();
    progressBar.setVisibility(View.GONE);

}
                    }
                });



            }

        });

signupbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        singIn();
    }
});

creat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        ((FragmentReplaceActivity2)getActivity()).setFragment(new CreatAccountFragment());
    }
});
    }
    private void sendusertomainactivity(){
if (getActivity()==null) {
    return;
}progressBar.setVisibility(View.GONE);
startActivity(new Intent(getActivity().getApplicationContext(),MainActivity.class));
getActivity().finish();


    }
    private void singIn(){
Intent singinIntent=mGoogleSignInClient.getSignInIntent();
startActivityForResult(singinIntent,RC_SIGN_IN);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_SIGN_IN){
Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
try {
    GoogleSignInAccount account=task.getResult(ApiException.class);
    assert account!=null;
    firebaseAuthWithGoogle(account.getIdToken());
} catch (ApiException e) {
e.printStackTrace();
}

        }


    }
private void firebaseAuthWithGoogle(String idToken){
    AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
    auth.signInWithCredential(credential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
if (task.isSuccessful()){

FirebaseUser user=auth.getCurrentUser();
updateUI(user);
}else {


    Log.w("Tag","signwith:failer",task.getException());
}
        }
    });

}
private void updateUI(FirebaseUser user){
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(getActivity());
    Map<String,Object> map=new HashMap<>();
    map.put("name",account.getDisplayName());
    map.put("email",account.getEmail());
    map.put("profileImage",String.valueOf(account.getPhotoUrl()));
    map.put("uid",user.getUid());
    map.put("following",0);
    map.put("search", "");
    map.put("followers",0);
    map.put("status"," ");

    FirebaseFirestore.getInstance().collection("Users").document(user.getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                assert getActivity()!=null;
                progressBar.setVisibility(View.GONE);
             sendusertomainactivity();
            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "ERROR"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    });


}
}