package com.example.webmobiweather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    TextView city;
    TextView typet;
    EditText search;
    TextView temp;
    ImageView sicon;
    ImageView button;
    private RecyclerView recyc;
    private ArrayList<Model> list1;
    ProgressBar pb;
    ConstraintLayout lay1;
    RelativeLayout lay3;
    private WAdapter wad;
    private String cityName;
    ScrollView lay2;
    ImageView img3;
    TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        city=findViewById(R.id.city);
        typet=findViewById(R.id.typet);
        search=findViewById(R.id.search);
        temp=findViewById(R.id.temp);
        sicon=findViewById(R.id.sicon);
        pb = findViewById(R.id.progressBar2);
        recyc=findViewById(R.id.recyc);
        button = findViewById(R.id.imageView2);
        lay1=findViewById(R.id.lay1);
        lay2 = findViewById(R.id.lay2);
        text = findViewById(R.id.textView);
        img3 = findViewById(R.id.imageView3);

        showImage();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideImage();
            }
        }, 1500);

        list1 = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyc.setLayoutManager(layoutManager);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        wad= new WAdapter(this , list1);
        recyc.setAdapter(wad);

        response(MyDatabase.getString(getApplicationContext()));




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = search.getText().toString();
                MyDatabase.saveString(getApplicationContext() ,a);
                pb.setVisibility(View.VISIBLE);
                response(a);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // Hide the keyboard
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);






            }
        });



    }
    private void showImage() {
        img3.setVisibility(View.VISIBLE);

    }

    private void hideImage() {
        img3.setVisibility(View.GONE);

    }
    private void response(String b)
    {


        String url ="http://api.weatherapi.com/v1/forecast.json?key=f1cc3362873148028b152003232805&q="+b+"&days=1&aqi=yes&alerts=yes";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , url ,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                pb.setVisibility(View.GONE);



                int temper = 0;
                try {
                    temper = response.getJSONObject("current").getInt("temp_c");
                    temp.setText(Integer.toString(temper));
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    typet.setText(condition);
                    String icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Glide.with(MainActivity.this)
                            .load("http:"+icon)
                            .into(sicon);
                    city.setText(response.getJSONObject("location").getString("name"));
                    int a = response.getJSONObject("current").getInt("is_day");


                    Window window = getWindow();


                    if(a==1)
                    {
                        lay2.setBackgroundResource(R.drawable.img1);
                        lay1.setBackgroundResource(R.drawable.img1);
                        window.setBackgroundDrawableResource(R.drawable.img_2);
                    }
                    else{

                        lay2.setBackgroundResource(R.drawable.img);
                        lay1.setBackgroundResource(R.drawable.img);
                        window.setBackgroundDrawableResource(R.drawable.img);
                    }
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastO.getJSONArray("hour");
                    for(int i =0 ; i<hourArray.length() ; i++) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        int temperr = hourObj.getInt("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String wSpeed = hourObj.getString("wind_kph");
                        String humidity = hourObj.getString("humidity");

                        list1.add(new Model(temperr, humidity, wSpeed, img, time));
                        Log.d("hello", humidity);


                    }
                    wad.notifyDataSetChanged();




                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Network Issue or City Not Found", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
        );
        requestQueue.add(jsonObjectRequest);

    }
}