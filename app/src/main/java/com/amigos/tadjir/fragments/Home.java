package com.amigos.tadjir.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amigos.tadjir.Model.HomeModel;
import com.amigos.tadjir.R;
import com.amigos.tadjir.adapter.HomeAdapter;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Home extends Fragment {
private RecyclerView recyclerView;
HomeAdapter adapter;
private List<HomeModel> list;
private FirebaseUser user;
DocumentReference reference;
    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       init(view);
        list =new ArrayList<>();
        adapter=new HomeAdapter(list,getContext());
        recyclerView.setAdapter(adapter);
 loadDataFromFirestore();

    }
   private void init(View view){
Toolbar toolbar=view.findViewById(R.id.toolbar);
if (getActivity()!=null)  ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseAuth auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

   }
   private void loadDataFromFirestore()
   {/*
list.add(new HomeModel("TADJIR","01/11/2000","","","12345",12));
       list.add(new HomeModel("ALI","01/11/2000","","","12345",12));
       list.add(new HomeModel("AHMED","01/11/2000","","","12345",12));
       list.add(new HomeModel("MANSOUR","01/11/2000","","","12345",12));
// الحصول على مرجع للمستند الذي تريد تحديث بياناته
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       DocumentReference docRef = db.collection("Users").document(user.getUid()).collection("Post Images").document("TZYrkIyqwM2xRKBNJX9O");

// بناء البيانات الجديدة التي تريد تحديثها
       Map<String, Object> updatedData = new HashMap<>();
       updatedData.put("description", "HHHHHHHHHHHHHH"); // تعديل الوصف

// تحديث البيانات في المستند
       docRef.update(updatedData)
               .addOnSuccessListener(aVoid -> {
                   // عملية التحديث ناجحة
                   Toast.makeText(getContext(), "تم تحديث البيانات بنجاح", Toast.LENGTH_SHORT).show();
               })
               .addOnFailureListener(e -> {
                   // حدث خطأ أثناء عملية التحديث
                   Toast.makeText(getContext(), "خطأ في تحديث البيانات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
               });
*/
     /*  FirebaseFirestore db = FirebaseFirestore.getInstance();
       CollectionReference postImagesRef = db.collection("Users").document(user.getUid()).collection("Post Images");

       postImagesRef.get()
               .addOnSuccessListener(queryDocumentSnapshots -> {
                   // استعراض المستندات واستخراج البيانات
                   for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                       String id = document.getString("id");
                       String name = document.getString("name");
                       String imageUrl = document.getString("imageUrl");
                       String PROFILE = document.getString("imageUrl");

                       list.add(new HomeModel(name,"01/11/2000",PROFILE,imageUrl,"12345",12));
                       adapter.notifyDataSetChanged();

                   }
               })
               .addOnFailureListener(e -> {
                   // حدث خطأ أثناء جلب البيانات
                   Toast.makeText(getContext(), "خطأ في جلب البيانات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
               });
*/
       FirebaseFirestore db = FirebaseFirestore.getInstance();
       CollectionReference postImagesRef = db.collection("Users"); // تغيير هنا للحصول على جميع المستخدمين

       postImagesRef.get()
               .addOnSuccessListener(queryDocumentSnapshots -> {
                   for (QueryDocumentSnapshot userDocument : queryDocumentSnapshots) {
                       // جلب مسار الصورة الشخصية لكل مستخدم من مجموعة "Users"
                       String profileImageUrl = userDocument.getString("profileImage");
                       String city = userDocument.getString("city");

                       // جلب مجموعة "Post Images" لكل مستخدم
                       CollectionReference userPostImagesRef = userDocument.getReference().collection("Post Images");
                       userPostImagesRef.get()
                               .addOnSuccessListener(postSnapshots -> {
                                   // استعراض المستندات واستخراج البيانات
                                   for (QueryDocumentSnapshot document : postSnapshots) {
                                       String id = document.getString("id");
                                       String name = document.getString("name");
                                       String imageUrl = document.getString("imageUrl");
                                       String Raitng_post = document.getString("Raitng_post");
                                       Date timestamp = document.getTimestamp("timestamp").toDate();                                       SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

                                       // تحويل التاريخ إلى الصيغة المطلوبة كـ String
                                       String formattedDate = dateFormat.format(timestamp);

                                       // استخدام مسار الصورة الشخصية من "Users"
                                       // في إنشاء HomeModel
                                       list.add(new HomeModel(name, timestamp.toString(), profileImageUrl, imageUrl, id,Raitng_post,city));
                                       adapter.notifyDataSetChanged();
                                   }
                               })
                               .addOnFailureListener(e -> {
                                   // حدث خطأ أثناء جلب البيانات
                                   Toast.makeText(getContext(), "خطأ في جلب البيانات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                               });
                   }
               })
               .addOnFailureListener(e -> {
                   // حدث خطأ أثناء جلب البيانات
                   Toast.makeText(getContext(), "خطأ في جلب البيانات: " + e.getMessage(), Toast.LENGTH_SHORT).show();
               });




   }


}