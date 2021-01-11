package com.example.gettime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    Calendar calendar = Calendar.getInstance();
    long startTime;
    long imp;
    String impK;


    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView listView;

    long timestamp = System.currentTimeMillis();



    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "";
    public static final String SWITCH1 = "switch1";
    private String text;
    private boolean switchOnOff;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("checkTAG onCreate","IS Working");     //working when app is staring for the first time
        startTime = System.currentTimeMillis();
        Log.i("checkTAG Time Class ", " Time value in milliseconds "+ startTime);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);



        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String string = "    "+snapshot.getKey() + "                     Seconds:   ";
                long value = snapshot.getValue(long.class);
                string += Long.toString(value);
//                string += snapshot.getValue(String.class);

                String newOne = snapshot.getKey();
                String cd = DateFormat.getDateInstance().format(calendar.getTime());
//                Log.d("cd",cd);
                Log.d("checkTAG newcd-------------------------",newOne);

                if (newOne.equals(cd)){
//                    Log.d("cdWorking",cd);
//                    Log.i("value","valIMP"+ value);
//                    Log.i("value","key"+ snapshot.getKey());
                    imp = value;
                    impK = String.valueOf(cd);
                    Log.d("checkTAG impK--", "impK--"+impK);
                }
//                Log.i("checkTAG value","val"+ value);
//                Log.i("checkTAG value","key"+ snapshot.getKey());

                arrayList.add(string);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String string = "    "+snapshot.getKey() + "                     Seconds:   ";
//                string += snapshot.getValue(String.class);
                long value = snapshot.getValue(long.class);
                string += Long.toString(value);
                arrayList.add(string);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String string = "    "+snapshot.getKey() + "                     Seconds:   ";
                long value = snapshot.getValue(long.class);
                string += Long.toString(value);
                arrayList.add(string);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String string = "    "+snapshot.getKey() + "                     Seconds:   ";
//                string += snapshot.getValue(String.class);
                long value = snapshot.getValue(long.class);
                string += Long.toString(value);
                arrayList.add(string);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String time = format.format(calendar.getTime());

        Log.d("checkTAG currentDate", currentDate);
        Log.i("checkTAG currIMP","currimp"+ impK);
        Log.d("checkTAG imp","imp"+imp);


//        mDatabase = database.getReference(currentDate);
        if (currentDate.equals("11-Jan-2021")){
            mDatabase = database.getReference(currentDate);
//            mDatabase.setValue(0);
        }else{
            mDatabase = database.getReference(currentDate);
            mDatabase.setValue(10);                          //here we initialize it to 0
        }
//        mDatabase.setValue(12);                          //here we initialize it to 0

    }

    @Override
    protected void onRestart() {
        Log.d("checkTAG onRestart", "IS Working");          //working when app is in background
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d("checkTAG onDestroy", "IS Working");          //working when app is cleared from background

        long endTime = System.currentTimeMillis();
        long total = (endTime-startTime)/1000;

        Log.i("checkTAG Time Class--2", " val in seconds: "+ total);

        String cd = DateFormat.getDateInstance().format(calendar.getTime());
        Log.d("cd",cd);
        mDatabase = database.getReference(cd);

//        Log.d("checkTAG","onLoad"+loadData());
        imp = loadData();

        total = imp + total;
        Log.i("checkTAG IMP","IMP---------------" + imp);
        Log.i("checkTAG total","total" + total);
        saveData(total);
        mDatabase.setValue(total);

        super.onDestroy();
    }

    private long loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "10");
        Log.d("checkTAG","text:" + text);

        if (text.equals("")){
            Log.d("checkTAG checkTAG1", "check--IF");
            return 0;
        }
        else{
            Log.d("checkTAG checkTAG2", "check--Else");
            return Long.parseLong(text);
        }
    }

    public void saveData(long total) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String string = String.valueOf(total);
        Log.d("checkTAG", "onSave: "+string);

        editor.putString(TEXT, string);
        editor.apply();

        Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        Log.d("checkTAG onStop", "IS Working");            //working when tap home button on open app
        super.onStop();
    }


}