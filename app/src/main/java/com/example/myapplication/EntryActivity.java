package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EntryActivity extends AppCompatActivity {

    TextView t1;
    Button b1;
    EditText e1;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        t1 = findViewById(R.id.nick_label);
        b1 = findViewById(R.id.button);
        e1 = findViewById(R.id.name_edit);
        b1.setOnClickListener(v -> {
            String text = e1.getText().toString();
            if(text.length()==0){
                Toast.makeText(this,"Введите хоть что-нибудь.", Toast.LENGTH_SHORT).show();
            } else {
                sp = getPreferences(MODE_PRIVATE);
                sp.getAll();
                int putting_val = sp.getInt("text", 0);
                SharedPreferences.Editor ed = sp.edit();
                ed.putInt(text, putting_val);
                ed.apply();
                Intent go = new Intent(EntryActivity.this, MainActivity.class);
                go.putExtra("nick", text);
                startActivity(go);
                finish();
            }
        });
    }
}