package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button button2;
    TextView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);

        button.setOnClickListener(v -> openApplianceActivity());

        DBHelper mDBHlpr = new DBHelper(this, null, null, 2);


        lst = (TextView) findViewById(R.id.list);
        lst.setText(mDBHlpr.loadHandler("*", "AppLoads", "1, 2, 3, 4, 5, 6, 7, 8, 9"));





       button2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view){

            final String[] utilName = {""};
            final String[] resRate = {""};
            String url = "https://developer.nrel.gov/api/utility_rates/v3.json?api_key=yTPGooqJI6UbcdJtvLjkxMwNEf6gKRxaxEf98PPr&address=Telluride,CO";
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject utils = response.getJSONObject("outputs");
                        utilName[0] = utils.get("utility_name").toString();
                        resRate[0] = utils.get("residential").toString();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (resRate[0].equals("no data")) {
                        Toast.makeText(MainActivity.this, "Not a Valid Location\nPlease enter a city and state.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "Utility Name = " + utilName[0] + " Utility Rate = " + resRate[0], Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(request);
        }
       });

    }
    public void openApplianceActivity() {
        Intent intent = new Intent(this, ApplianceActivity.class);
        startActivity(intent);
    }
}