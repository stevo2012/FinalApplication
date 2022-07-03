package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
import java.nio.charset.StandardCharsets;

public class ApplianceActivity extends AppCompatActivity {


    Button submitButton;
    String appliance1string, appliance2string, appliance3string, appliance4string, appliance5string, appliance6string, appliance7string, appliance8string, appliance9string = "";
    String app1String, app2String, app3String, app4String, app5String, app6String, app7String, app8String, app9String = "";
    String allString = "";
    String address = "";
    String utilName = "";
    String resRate = "";
    String propaneText = "2.34";
    EditText app1Text, app2Text, app3Text, app4Text, app5Text, app6Text, app7Text, app8Text, app9Text, addressEditText, propaneEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appliance_selection);

        app1Text = findViewById(R.id.waterTankEdit);
        app2Text = findViewById(R.id.waterSystemEdit);
        app3Text = findViewById(R.id.microwaveEdit);
        app4Text = findViewById(R.id.dishwasherEdit);
        app5Text = findViewById(R.id.bathEdit);
        app6Text = findViewById(R.id.washerEdit);
        app7Text = findViewById(R.id.dryerEdit);
        app8Text = findViewById(R.id.stoveEdit);
        app9Text = findViewById(R.id.evChargerEdit);
        addressEditText = findViewById(R.id.AddressEdit);
        propaneEditText = findViewById(R.id.propaneEdit);
        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiCall();
            }
        });
    }

public void apiCall() {

    address = addressEditText.getText().toString();
    RequestQueue queue = Volley.newRequestQueue(ApplianceActivity.this);
    String url = "https://developer.nrel.gov/api/utility_rates/v3.json?api_key=yTPGooqJI6UbcdJtvLjkxMwNEf6gKRxaxEf98PPr&address=" + address;


    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


        @Override
        public void onResponse(JSONObject response) {
            try {
                JSONObject utils = response.getJSONObject("outputs");
                utilName = utils.get("utility_name").toString();
                resRate = utils.get("residential").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (resRate.equals("no data")) {
                Toast.makeText(ApplianceActivity.this, "Not a Valid Location\nPlease enter a city and state.", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(ApplianceActivity.this, "Utility Name = " + utilName + " Utility Rate = " + resRate, Toast.LENGTH_SHORT).show();
                applianceCheck(utilName, resRate);
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(ApplianceActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
        }
    });
    queue.add(request);

}


    public void applianceCheck(String utilName, String resRate){

        try {
            app1String = app1Text.getText().toString();
            app2String = app2Text.getText().toString();
            app3String = app3Text.getText().toString();
            app4String = app4Text.getText().toString();
            app5String = app5Text.getText().toString();
            app6String = app6Text.getText().toString();
            app7String = app7Text.getText().toString();
            app8String = app8Text.getText().toString();
            app9String = app9Text.getText().toString();
            propaneText = propaneEditText.getText().toString();


        if (app1String.equals("") || app2String.equals("") || app3String.equals("") || app4String.equals("")
                || app5String.equals("") || app6String.equals("") || app7String.equals("") || app8String.equals("") || app9String.equals("")) {
            Toast.makeText(ApplianceActivity.this, "Timer Not Added\nA Field is Missing",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!app1String.equals("0")) {
            appliance1string = "1,";
        }
        if (!app2String.equals("0")) {
            appliance2string = "2,";
        }
        if (!app3String.equals("0")) {
            appliance3string = "3,";
        }
        if (!app4String.equals("0")) {
            appliance4string = "4,";
        }
        if (!app5String.equals("0")) {
            appliance5string = "5,";
        }
        if (!app6String.equals("0")) {
            appliance6string = "6,";
        }
        if (!app7String.equals("0")) {
            appliance7string = "7,";
        }
        if (!app8String.equals("0")) {
            appliance8string = "8,";
        }
        if (!app9String.equals("0")) {
            appliance9string = "9,";
        }
    } catch (Exception e) {
        Toast.makeText(ApplianceActivity.this, "Something went wrong; try again.",
                Toast.LENGTH_SHORT).show();
        return;
    }
                try {
        allString = appliance1string + appliance2string + appliance3string + appliance4string +
                appliance5string + appliance6string + appliance7string + appliance8string + appliance9string;
        StringBuffer finalString = new StringBuffer(allString);

        finalString.deleteCharAt(finalString.length() - 1);

        openResultActivity(finalString, utilName, resRate);
    } catch (Exception e) {
        Toast.makeText(getApplicationContext(), "Select at least one appliance.",
                Toast.LENGTH_SHORT).show();
    }
}
    public void openResultActivity(StringBuffer finalString,String utilName,  String resRate) {
        Intent i = new Intent(ApplianceActivity.this, ResultActivity.class);
        i.putExtra("key", (Serializable) finalString);
        i.putExtra("key1", (Serializable) utilName);
        i.putExtra("key2", (Serializable) resRate);
        i.putExtra("key3", (Serializable) app1String);
        i.putExtra("key4", (Serializable) app2String);
        i.putExtra("key5", (Serializable) app3String);
        i.putExtra("key6", (Serializable) app4String);
        i.putExtra("key7", (Serializable) app5String);
        i.putExtra("key8", (Serializable) app6String);
        i.putExtra("key9", (Serializable) app7String);
        i.putExtra("key10", (Serializable) app8String);
        i.putExtra("key11", (Serializable) app9String);
        i.putExtra("key12", (Serializable) propaneText);
        startActivity(i);
    }
}

