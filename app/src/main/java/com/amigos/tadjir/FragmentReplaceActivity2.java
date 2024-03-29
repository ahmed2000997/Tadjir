package com.amigos.tadjir;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.amigos.tadjir.fragments.CreatAccountFragment;
import com.amigos.tadjir.fragments.loginFragment;

public class FragmentReplaceActivity2 extends AppCompatActivity {
private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_replace2);
        frameLayout=findViewById(R.id.framelayout);

        setFragment(new loginFragment());
    }
    public void setFragment(Fragment frament){
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_in_left);
        if (frament instanceof CreatAccountFragment){
fragmentTransaction.addToBackStack(null);}

        fragmentTransaction.replace(frameLayout.getId(),frament);
        fragmentTransaction.commit();

    }
}