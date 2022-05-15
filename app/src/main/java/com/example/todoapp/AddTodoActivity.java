package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class AddTodoActivity extends AppCompatActivity {

    Button saveButton, deleteButton;
    EditText todoTitle, todoDescription;
    RoomDB database;
    //Dialog dialog = new Dialog(this);

    Todo todo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        saveButton = findViewById(R.id.add_item_button);
        deleteButton = findViewById(R.id.delete_item_button);
        deleteButton.setVisibility(View.GONE);

        todoTitle = findViewById(R.id.todo_title);
        todoDescription = findViewById(R.id.todo_description);

        // create db instance
        database = RoomDB.getInstance(this);

        try {
            Intent intent = getIntent();
            todo = (Todo) intent.getSerializableExtra("TODO_CLASS");

            // set old data
            todoTitle.setText(todo.getTitle());
            todoDescription.setText(todo.getDescription());
            deleteButton.setVisibility(View.VISIBLE);
             } catch (Exception e) {
            todo = new Todo();
             }


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog deleteDialog = new Dialog(AddTodoActivity.this);
                deleteDialog.setContentView(R.layout.dialog_delete_theme);

                TextView closeButton = deleteDialog.findViewById(R.id.noButton);
                TextView okButton = deleteDialog.findViewById(R.id.yesButton);

                deleteDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                deleteDialog.setCancelable(true);
                deleteDialog.show();


                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                });

                okButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        database.todoDao().delete(todo);
                        Toast.makeText(AddTodoActivity.this, "Task Deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String description = todoDescription.getText().toString();
                String title = todoTitle.getText().toString();

                if(description.length()==0 || title.length()==0){
                    Toast.makeText(AddTodoActivity.this, "Field cannot be left empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (todo.getTitle() == null) {

                    // add todo to db
                    database.todoDao().insertTodo(new Todo(title, description, new Date().toString(), false));

                    Dialog dialog = new Dialog(AddTodoActivity.this);
                    dialog.setContentView(R.layout.dialog_completed_theme);

                    Button closeButton = dialog.findViewById(R.id.closeButton);

                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(true);
                    dialog.show();


                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();

                        }
                    });
                }

                else {

                    todo.setTitle(title);
                    todo.setDescription(description);
                    todo.setCompleted(false);

                    // to update todo
                    database.todoDao().update(todo);
                    Toast.makeText(AddTodoActivity.this, "Todo updated", Toast.LENGTH_SHORT).show();
                    finish();
                }


            }
        });


    }
}