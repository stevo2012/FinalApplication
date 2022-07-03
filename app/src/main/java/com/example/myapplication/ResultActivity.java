package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ResultActivity extends AppCompatActivity {
    TextView loadList, utilities, loadList2, costView;
    Double BTUPerGal = 91547.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activty);


        DBHelper mDBHlpr = new DBHelper(this, null, null, 2);

        String finalStr = getIntent().getStringExtra("key");
        String utilName = getIntent().getStringExtra(("key1"));
        String rate = getIntent().getStringExtra("key2");
        String waterTank = getIntent().getStringExtra("key3");
        String onDemandHW = getIntent().getStringExtra("key4");
        String Microwave = getIntent().getStringExtra("key5");
        String Dishwasher = getIntent().getStringExtra("key6");
        String Bath = getIntent().getStringExtra("key7");
        String Washer = getIntent().getStringExtra("key8");
        String Dryer = getIntent().getStringExtra("key9");
        String Stove = getIntent().getStringExtra("key10");
        String EVChager = getIntent().getStringExtra("key11");
        String Propane = getIntent().getStringExtra("key12");

        Double propaneAmt = Double.valueOf(Propane);


        loadList = (TextView) findViewById(R.id.resultLoadCalc);
        loadList.setText(mDBHlpr.loadHandler("id,appName","AppLoads", finalStr));

        loadList2 = (TextView) findViewById(R.id.resultLoadCalc2);
        loadList2.setText(mDBHlpr.loadHandler("id,null,loadElectric,loadPropane", "AppLoads", finalStr));

        utilities = (TextView) findViewById(R.id.rateView);
        utilities.setText("Utility Company: " + utilName + "\nUtility Rate = " + rate);

        costView = (TextView) findViewById(R.id.resultCosts);
        costView.setText(mDBHlpr.loadHandler2("id,appName,kwhday,btuday", "AppCost", finalStr + ",11,12", rate));


    }
}