package hue.com.myapplication.activityuser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import hue.com.myapplication.R;
import hue.com.myapplication.dao.HdctDao;
import hue.com.myapplication.dao.UserDao;
import hue.com.myapplication.model.Billdetail;

public class ThanhToan_Activity extends AppCompatActivity {
Spinner spnthanhtoan;
Button btnPay;
EditText edtdiachi;
ImageView imgbackt;
String ht,diachi,ngay,username,detailid;
    FirebaseDatabase database;
    DatabaseReference bill;
    DatabaseReference billdetail;
    int total;
    List<Billdetail> cart = new ArrayList<>();
    List<String> billdetails = new ArrayList<>();

    HdctDao hdctDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan_);
        hdctDao= new HdctDao(this);
        spnthanhtoan= findViewById(R.id.spnthanhtoan);
        btnPay = findViewById(R.id.btnPay);
        edtdiachi = findViewById(R.id.edtdiachi);
        imgbackt=findViewById(R.id.imgbackt);
        database= FirebaseDatabase.getInstance();
        bill= database.getReference("Bill");
        billdetail= database.getReference("Billdetail");
        total= getIntent().getIntExtra("tt",0);
        imgbackt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        List<String> listtt = new ArrayList<String>();
        listtt.add("CASH ON DELIVERY");
        listtt.add("PAYAL");
        ArrayAdapter arrayAdapter = new ArrayAdapter(ThanhToan_Activity.this, R.layout.support_simple_spinner_dropdown_item, listtt);
        spnthanhtoan.setAdapter(arrayAdapter);

        spnthanhtoan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ht = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();
                SimpleDateFormat curentDate=new SimpleDateFormat("dd-MM-yyyy");
                ngay=curentDate.format(calendar.getTime());

                diachi= edtdiachi.getText().toString();
                username= UserDao.cruser.getUsname();
                cart= new ArrayList<>();
                cart=hdctDao.getall();

                if (ht.equals("CASH ON DELIVERY")) {
                    final String billid= UserDao.cruser.getPhone();

                    final String a1= String.valueOf(System.currentTimeMillis());
                    HashMap<String,Object>objectHashMap= new HashMap<>();
                    objectHashMap.put("billid",a1);
                    objectHashMap.put("date",ngay);
                    objectHashMap.put("payments",ht);
                    objectHashMap.put("address",diachi);
                    objectHashMap.put("state","Unpaid");
                    objectHashMap.put("total",total);
                    objectHashMap.put("username",username);
                    objectHashMap.put("foods",cart);


                    bill.child(a1).updateChildren(objectHashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull final Task<Void> task) {
                            if(task.isSuccessful()){
                                String a= UserDao.cruser.getPhone();

                                hdctDao.deleteItem();
                                Toast.makeText(ThanhToan_Activity.this, "tc", Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(ThanhToan_Activity.this, BillUserActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
//                                billdetail.child("Admin View").child(a).child("Products").addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        for (DataSnapshot addressSnapshot: dataSnapshot.getChildren()){
//                                            billdetails.add(String.valueOf(addressSnapshot.getKey()));
//
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });



                            }
                        }
                    });

                }else {
                    if (ContextCompat.checkSelfPermission(ThanhToan_Activity.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ThanhToan_Activity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                    }                }
            }
        });
    }


}
