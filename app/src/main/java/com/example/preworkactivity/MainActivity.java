package com.example.preworkactivity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
//import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Keyword declaration for starting editActivity
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20; //According to the the activities

    List<String> items;

    Button btn_Add;
    EditText et_Item;
    RecyclerView rv_Items;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_Add = findViewById(R.id.btn_Add);
        et_Item = findViewById(R.id.et_Item);
        rv_Items = findViewById(R.id.rv_Items);

        //et_Item.setText ("Texto que ya queda escrito");
        /*
                Mock Data
        items = new ArrayList<>();
        items.add("Task 1");
        items.add("Task 2");*/

        //Instead of using an empty Array lis the function loadItems is called to take what was saved in the data file
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
            //Delete the item from the model
            items.remove(position);
            //Notify the adapter
            itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item Removed Succesfully", Toast.LENGTH_SHORT).show();
                //Call funciton to save changes in the list
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position" + position);
                //Create the new activity (Edition Screen)
                Intent i = new Intent (MainActivity.this,EditActivity.class); //Context from which you are calling, destination
                //Pass the data that required the edition (Also needed to know what was been updated)
                //Passing data with intent (key *always a String*,value)
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                //Display the editActivity
                //startActivityForResult because we are expecting the updated item
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };

        itemsAdapter= new ItemsAdapter(items, onLongClickListener, onClickListener);
        rv_Items.setAdapter(itemsAdapter);
        rv_Items.setLayoutManager(new LinearLayoutManager(this));

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = et_Item.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify adapter that an item is inserted
                itemsAdapter.notifyItemInserted(items.size()-1);
                et_Item.setText("");
                Toast.makeText(getApplicationContext(), "Item Added Succesfully", Toast.LENGTH_SHORT).show();
                //Call the function to save the item in the data file
                saveItems();
            }
        });
    }
    // //Handle the result of the Edit activity by over writing a method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if the result code is okay
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve the updated text Value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract the original position of the edit item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //Update the model at the right position with new item text
            items.set(position,itemText); //modifying the items of the list
            //Notify the adaptor for the recycleview to know about the changes
            itemsAdapter.notifyItemChanged(position);
            //Persist the changes
            saveItems();
            //Notify the update to the user
            Toast.makeText(getApplicationContext(), "Item Updated Succesfully", Toast.LENGTH_SHORT).show();
        }else{
            Log.w("MainActivity","Unknown call to onActivityResult"); //If results is not okay give a WARNING
        }
    }

    //Method for index to store list of items
    private File getDataFile(){
        //Method to return a File
        return new File(getFilesDir(),"data.txt"); //(Directory, name of the file)
    }
    //Method to read the data file
    //This function will load items by reading every line of the data file
    //Needs to be CALLED only once when the APP STARTS UP
    private void loadItems(){
        //Read all the line of the data file and populate that into an Array list
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            e.printStackTrace();
            //Log to identity what is happening in the program
            Log.e("MainActivity","Error reading items",e);
            items = new ArrayList<>(); //Set an empty ArrayList for not leaving the items ArrayList uninitialized
        }
    }
    //This function saves items by writing them into the data file
    //Need to be CALLED when ever a CHANGE IS MADE TO THE LIST (item added or removed)
    private void saveItems() {
        try{
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error reading items",e);
        }
    }
}