package hue.com.myapplication.fragmentadmin;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import hue.com.myapplication.R;
import hue.com.myapplication.activityadmin.MonAnad1Activity;
import hue.com.myapplication.adapter.RecyclerView_Type;
import hue.com.myapplication.model.Category;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonAnad1Fragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView ryclelma1;
    FirebaseRecyclerAdapter<Category, RecyclerView_Type> firebaseRecyclerAdapte;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_mon_anad1, container, false);
        ryclelma1= view.findViewById(R.id.ryclelma1);

        database= FirebaseDatabase.getInstance();
        category= database.getReference("Category");

        ryclelma1.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        // ryclelma.setLayoutManager(layoutManager);
        ryclelma1.setLayoutManager(new GridLayoutManager(getContext(),2));

        loadtl();
        return view;
    }
    public void loadtl(){
        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(category, Category.class).build();

        firebaseRecyclerAdapte=new FirebaseRecyclerAdapter<Category, RecyclerView_Type>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView_Type holder, int position, @NonNull Category model) {
                holder.setDetails(getContext().getApplicationContext(),model.getCategoryid(),model.getName(),model.getImage());
            }

            @NonNull
            @Override
            public RecyclerView_Type onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                ImageView imagelm = view.findViewById(R.id.imagelm);
                TextView tvLoaiMA= view.findViewById(R.id.tvLoaiMA);
                RecyclerView_Type viewholder= new RecyclerView_Type(view);

                viewholder.setOnClickListener(new RecyclerView_Type.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final   String categoryid = getItem(position).getCategoryid();
                        String tenloai= getItem(position).getName();
                        Intent foodlist= new Intent(getContext(), MonAnad1Activity.class);
                        foodlist.putExtra("tenl",tenloai);
                        foodlist.putExtra("categoryid",categoryid);
                        //  String k= firebaseRecyclerAdapte.getRef(position).getKey();

                        startActivity(foodlist);
                    }

                    @Override
                    public void onItemLongClick(View view, int postion) {

                    }
                });

                return viewholder;
            }

        };

        ryclelma1.setAdapter(firebaseRecyclerAdapte);
        firebaseRecyclerAdapte.startListening();
        firebaseRecyclerAdapte.notifyDataSetChanged();
    }
}
