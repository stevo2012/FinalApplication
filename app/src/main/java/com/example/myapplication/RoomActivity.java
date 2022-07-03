package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class RoomActivity extends AppCompatActivity {
    TextView lst;
    EditText sqFtEditText;
    EditText bathEditText;
    EditText kitchenEditText;
    EditText laundryEditText;
    EditText addressEditText;
    String address = "";
    String sqFtText = "";
    String bathText = "";
    String kitchenText = "";
    String laundryText = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_activity);

        DBHelper mDBHlpr = new DBHelper(this, null, null, 2);

        String finalString = getIntent().getStringExtra("key");
        sqFtEditText = findViewById(R.id.squareFootEdit);
        bathEditText = findViewById(R.id.bathroomEdit);
        kitchenEditText = findViewById(R.id.kitchenEdit);
        laundryEditText = findViewById(R.id.laundryEdit);
        addressEditText = findViewById(R.id.AddressEdit);
        Button submitButton = findViewById(R.id.submit_button);

        lst = (TextView) findViewById(R.id.list2);
        lst.setText(mDBHlpr.loadHandler( "*","AppLoads" ,finalString));






        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sqFtText = sqFtEditText.getText().toString();
                bathText = bathEditText.getText().toString();
                kitchenText = kitchenEditText.getText().toString();
                laundryText = laundryEditText.getText().toString();
                address = addressEditText.getText().toString();

                final String[] utilName = {""};
                final String[] resRate = {""};
                RequestQueue queue = Volley.newRequestQueue(RoomActivity.this);
                String url = "https://developer.nrel.gov/api/utility_rates/v3.json?api_key=yTPGooqJI6UbcdJtvLjkxMwNEf6gKRxaxEf98PPr&address="+address;


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
                            Toast.makeText(RoomActivity.this, "Not a Valid Location\nPlease enter a city and state.", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(RoomActivity.this, "Utility Name = " + utilName[0] + " Utility Rate = " + resRate[0], Toast.LENGTH_SHORT).show();
                        openResultActivity(finalString, resRate[0]);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RoomActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(request);
            }
        });
    }
    public void openResultActivity(String finalString, String resRate) {
        Intent i = new Intent(RoomActivity.this, ResultActivity.class);
        i.putExtra("key1", (Serializable) finalString);
        i.putExtra("key2", (Serializable) resRate);
        i.putExtra("key3", (Serializable) sqFtText);
        i.putExtra("key4", (Serializable) bathText);
        i.putExtra("key5", (Serializable) kitchenText);
        i.putExtra("key6", (Serializable) laundryText);
        startActivity(i);
    }
}
