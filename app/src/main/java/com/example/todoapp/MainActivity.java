package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getSimpleName();

    private List<Todo> todoList = new ArrayList<Todo>();

    private RecyclerView recyclerView;
    ImageView noDataImage;
    private TodoAdapter adapter;
    RoomDB database;

    FloatingActionButton addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        addItemButton = findViewById(R.id.add_item_button);

        adapter = new TodoAdapter(todoList, this);

        recyclerView.setAdapter(adapter);

        noDataImage = findViewById(R.id.no_data_image);


        database = RoomDB.getInstance(this);
        this.todoList.addAll(this.database.todoDao().getAllTodos());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        if(!todoList.isEmpty()){

            noDataImage.setVisibility(View.GONE);
        }
        else{
            noDataImage.setVisibility(View.VISIBLE);
        }

        addItemButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent addIntent = new Intent(MainActivity.this, AddTodoActivity.class);
                startActivity(addIntent);
            }
        });
    }
    //Reordertasks

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {

        //This onMove method helps to drag to-do list items, and change their display order.
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int startPosition = viewHolder.getAdapterPosition();
            int endPosition = target.getAdapterPosition();


            Collections.swap(todoList, startPosition, endPosition);
            recyclerView.getAdapter().notifyItemMoved(startPosition, endPosition);

            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        this.todoList.clear();
        this.todoList.addAll(this.database.todoDao().getAllTodos());

        if(!todoList.isEmpty()){

            noDataImage.setVisibility(View.GONE);
        }
        else{
            noDataImage.setVisibility(View.VISIBLE);
        }


        adapter.notifyDataSetChanged();
    }
}