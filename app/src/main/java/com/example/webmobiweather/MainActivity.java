    package com.example.webmobiweather;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.constraintlayout.widget.ConstraintLayout;
    import androidx.core.splashscreen.SplashScreen;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;
    import android.content.Context;
    import android.os.Bundle;
    import android.os.Handler;
    import android.os.Looper;
    import android.util.Log;
    import android.view.View;
    import android.view.WindowManager;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.ProgressBar;
    import android.widget.RelativeLayout;
    import android.widget.ScrollView;
    import android.widget.TextView;
    import java.util.ArrayList;
    import java.util.Objects;

    import android.widget.Toast;
    import com.android.volley.Request;
    import com.android.volley.RequestQueue;

    import com.android.volley.toolbox.JsonObjectRequest;
    import com.android.volley.toolbox.Volley;
    import com.bumptech.glide.Glide;
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
        RecyclerView recyc;
        ArrayList<Model> list1;
        ProgressBar pb;
        ConstraintLayout lay1;
        WAdapter wad;

        ScrollView lay2;


        TextView dc;
        LinearLayout tempBottomLay;
        TextView today;
        RelativeLayout lay3;
        private SplashScreen splashScreen;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            splashScreen = SplashScreen.installSplashScreen(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Objects.requireNonNull(getSupportActionBar()).hide();
            //keep splash till time
            splashScreen.setKeepOnScreenCondition(() -> true);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Hide the splash screen
                splashScreen.setKeepOnScreenCondition(()-> false);

            }, 2000);
            city=findViewById(R.id.city);
            lay3 = findViewById(R.id.lay3);
            typet=findViewById(R.id.typet);
            search=findViewById(R.id.search);
            temp=findViewById(R.id.temp);
            sicon=findViewById(R.id.sicon);
            pb = findViewById(R.id.progressBar2);
            recyc=findViewById(R.id.recyc);
            button = findViewById(R.id.imageView2);
            lay1=findViewById(R.id.lay1);
            lay2 = findViewById(R.id.lay2);
            dc = findViewById(R.id.textView2);
            tempBottomLay = findViewById(R.id.linearLayout);
            today = findViewById(R.id.textView);
            temp.setVisibility(View.GONE);
            dc.setVisibility(View.GONE);
            tempBottomLay.setVisibility(View.GONE);
            today.setVisibility(View.GONE);
            list1 = new ArrayList<>();
            //for keyboard white bg problem
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
            recyc.setLayoutManager(layoutManager);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            wad= new WAdapter(MainActivity.this , list1);
            recyc.setAdapter(wad);
            response(MyDatabase.getString(getApplicationContext()));
            button.setOnClickListener(v -> {
                String a = search.getText().toString();
                MyDatabase.saveString(getApplicationContext() ,a);
                pb.setVisibility(View.VISIBLE);
                list1.clear();
                response(a);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // Hide the keyboard
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            });
        }

        private void response(String b)
        {
            String url ="http://api.weatherapi.com/v1/forecast.json?key=f1cc3362873148028b152003232805&q="+b+"&days=1&aqi=yes&alerts=yes";
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET , url ,null, response -> {
                pb.setVisibility(View.GONE);
                int temper;
                try {
                    temp.setVisibility(View.VISIBLE);
                    dc.setVisibility(View.VISIBLE);
                    tempBottomLay.setVisibility(View.VISIBLE);
                    today.setVisibility(View.VISIBLE);

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

                    if(a==1)
                    {
                        lay3.setBackgroundResource(R.drawable.img1);

                    }
                    else{
                        lay3.setBackgroundResource(R.drawable.img);


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
                        wad.notifyDataSetChanged();
                    }
                }
                catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }, error -> {
                Toast.makeText(MainActivity.this, "Enter Your City And Click Search ", Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Network Issue or City Not Found", Toast.LENGTH_SHORT).show();

            }
            );
            requestQueue.add(jsonObjectRequest);
        }
    }