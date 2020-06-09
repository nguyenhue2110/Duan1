package hue.com.myapplication.Fragment;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hue.com.myapplication.activityuser.MonAnActivity;
import hue.com.myapplication.R;
import hue.com.myapplication.adapter.RecyclerView_Type;
import hue.com.myapplication.model.Category;


public class TypefoodFragment extends Fragment {
    FirebaseDatabase database;
    DatabaseReference category;
    Button btnsearchlma;
    EditText edtSearchlma;
    RecyclerView ryclelma;
     FirebaseRecyclerAdapter<Category, RecyclerView_Type> firebaseRecyclerAdapte;
    public TypefoodFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_typefood, container, false);
        btnsearchlma=view.findViewById(R.id.btnsearchlma);
        edtSearchlma=view.findViewById(R.id.edtSearchlma);


        ryclelma= view.findViewById(R.id.ryclelma);

        database= FirebaseDatabase.getInstance();
        category= database.getReference("Category");

        ryclelma.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
       // ryclelma.setLayoutManager(layoutManager);
        ryclelma.setLayoutManager(new GridLayoutManager(getContext(),2));

        loadtl();


        btnsearchlma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchtext= edtSearchlma.getText().toString();
                if(edtSearchlma.getText().toString().length()==0){
                    loadtl();
                }else {
                    search(searchtext);
                }


            }
        });


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
                            Intent foodlist= new Intent(getContext(), MonAnActivity.class);
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

        ryclelma.setAdapter(firebaseRecyclerAdapte);
        firebaseRecyclerAdapte.startListening();
        firebaseRecyclerAdapte.notifyDataSetChanged();
    }

    public void search(String searchText){
        Query firebaseSearch= category.orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");

        FirebaseRecyclerOptions<Category> options = new FirebaseRecyclerOptions.Builder<Category>().setQuery(category.orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff"), Category.class).build();

        FirebaseRecyclerAdapter<Category, RecyclerView_Type> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Category, RecyclerView_Type>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RecyclerView_Type holder, int position, @NonNull Category model) {
                holder.setDetails(getContext().getApplicationContext(),model.getCategoryid(),model.getName(),model.getImage());
            }

            @NonNull
            @Override
            public RecyclerView_Type onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                RecyclerView_Type viewholder= new RecyclerView_Type(view);


                viewholder.setOnClickListener(new RecyclerView_Type.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final   String categoryid = getItem(position).getCategoryid();
                        String tenloai= getItem(position).getName();
                        Intent foodlist= new Intent(getContext(), MonAnActivity.class);
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

        ryclelma.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
    }

}
