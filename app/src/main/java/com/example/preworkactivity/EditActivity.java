package com.example.preworkactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText et_EItem;
    Button btn_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        et_EItem = findViewById(R.id.et_EItem);
        btn_Save = findViewById(R.id.btn_Save);

        //Title for the screen
        getSupportActionBar().setTitle("Editor");
        //Method to use the data passed in the Intent in this activity
        et_EItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT)); //get the current text from the item

        //When the user is done editing, they click the save button
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent which will contain the results
                Intent intent = new Intent();
                //pass the data (result of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,et_EItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //set the results of the intent
                setResult(RESULT_OK,intent);//Define what was the result
                //finish activity, close the screen and return
                finish();
            }
        });
    }
}