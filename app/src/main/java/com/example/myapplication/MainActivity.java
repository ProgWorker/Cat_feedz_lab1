package com.example.myapplication;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    Button a1;
    Button a2;
    TextView t1;
    ImageView imga;
    int counter = -1;
    SharedPreferences sp;
    ActionBar ab;
    String nick;
    int satiety;
    Calendar calendar;
    SimpleDateFormat dateFormat;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        sp = getPreferences(MODE_PRIVATE);
        ab = getSupportActionBar();
        /*Intent l = getIntent();
        nick = l.getStringExtra("nick");*/
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        //sp.edit().clear().apply();
        satiety = sp.getInt(date, 0);
        ab.setTitle("CatBoy");
        a1 = findViewById(R.id.a1);
        t1 = findViewById(R.id.textView);
        imga = findViewById(R.id.imga);
        a1.setOnClickListener(view -> {
            counter++;
            if (counter==15){
                counter = 0;
                Animation anim;
                anim = AnimationUtils.loadAnimation (getApplicationContext(), R.anim.joy);
                imga.startAnimation(anim);
            }
            t1.setText("Satiety: " + satiety);
            satiety++;

        });
        a2 = findViewById(R.id.share);
        a2.setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String[] satmas =  t1.getText().toString().split(": ");

                int message = Integer.parseInt(satmas[satmas.length - 1]);

                sendIntent.putExtra(Intent.EXTRA_TEXT, "Ух ты! Мой рекорд " + message + " очков в лабе 1");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub

        menu.add("Обо мне");
        menu.add("Счёт");

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        if (item.getTitle() == "Обо мне"){
            launch_popup("Вячеслав Жинь 981073 сделал это приложение.");
        } else if (item.getTitle() == "Счёт"){
            launch_popup((HashMap<String, Integer>) sp.getAll());
        }
        return super.onOptionsItemSelected(item);
    }

    private void launch_popup(String text){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_about, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        TextView tvp = popupView.findViewById(R.id.tvp);
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        tvp.setText(text);
        popupWindow.showAtLocation(new View(getApplicationContext()), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
    }

    private void launch_popup(HashMap<String, Integer> data){
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_results, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        RecyclerView reqa = popupView.findViewById(R.id.recycler);
        reqa.setLayoutManager(new LinearLayoutManager(popupView.getContext()));
        reqa.setAdapter(new MyAdapter(data));
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(new View(getApplicationContext()), Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        /*popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });*/
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    @Override
    protected void onStop() {
        super.onStop();
        /*calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());*/
        SharedPreferences.Editor ed = sp.edit();
        //ed.clear();
        ed.putInt(date, satiety == 0 ? 0 : satiety - 1);
        ed.apply();
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Map.Entry<String, Integer>> listItems;

    public MyAdapter(HashMap<String, Integer> listItems) {
        Set<Map.Entry<String, Integer>> entrySet = listItems.entrySet();
        ArrayList<Map.Entry<String, Integer>> listOfEntry = new ArrayList<>(entrySet);
        this.listItems = listOfEntry;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_node, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Map.Entry<String, Integer> itemList = listItems.get(position);
        holder.date.setText(itemList.getKey());
        holder.satiety.setText(itemList.getValue().toString());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView date;
        public TextView satiety;
        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date_title);
            satiety = (TextView) itemView.findViewById(R.id.satiety_title);
        }
    }
}
