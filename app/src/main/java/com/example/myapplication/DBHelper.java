package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class DBHelper extends SQLiteAssetHelper {

    public static final String DBNAME = "housedatabase.db"; //<<<< must be same as file name
    public static final int DBVERSION = 1;
    SQLiteDatabase myDataBase;
    private final Context mContext;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME,null, DBVERSION);
        this.mContext = context;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}

    private boolean checkDatabase(){
        try {
            final String mPath = DBNAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        }   catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }
    private void copyDatabase() throws IOException {
        try{ InputStream mInputStream = mContext.getAssets().open(DBNAME);
            String outFileName = DBNAME;
            OutputStream mOutputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while((length = mInputStream.read(buffer)) > 0){
                mOutputStream.write(buffer,0,length);
            }
            mOutputStream.flush();
            mOutputStream.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createDatabase() throws IOException{
        boolean mDatabaseExist = checkDatabase();
        if (!mDatabaseExist){

            try{
                copyDatabase();
            }
            catch(IOException mIOException){
                mIOException.printStackTrace();
                throw new Error("Error copying Database");
            } finally {
                this.close();
            }
        }
    }
    @Override
    public synchronized  void close(){
        if (myDataBase != null)
            myDataBase.close();
        SQLiteDatabase.releaseMemory();
        super.close();
    }
    public String loadHandler(String app1, String app2, String app3, String app4, String app5, String app6,
                              String app7, String app8, String app9){
        String result_name = null;
        Number result_elec = null;
        Number result_prop = null;

        try{
            createDatabase();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select round(sum(\n" +
                        "(select loadElectric from AppLoads where appName='Hot Water Tank') * " + app1 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='On Demand Hot Water')*" + app2 + "\n" +
                        " +\n" +
                        "(select loadElectric from AppLoads where appName='Microwave')*" + app3 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='Dishwasher')*" + app4 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='Bath')*" + app5 + "\n" +
                        "+ \n" +
                        "(select loadElectric from AppLoads where appName='Laundry')*" + app6 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='Dryer')*" + app7 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='Stove')*" + app8 + "\n" +
                        "+\n" +
                        "(select loadElectric from AppLoads where appName='EV Charger')*" + app9 + "\n" +
                        "),2) as total_electric_load\n" +
                        ",\n" +
                        "sum(\n" +
                        "(select loadPropane from AppLoads where appName='Hot Water Tank')*" + app1 + " \n" +
                        " +\n" +
                        "(select loadPropane from AppLoads where appName='Microwave')*" + app3 + "\n" +
                        "+\n" +
                        "(select loadPropane from AppLoads where appName='Dishwasher')*" + app4 + "\n" +
                        "+\n" +
                        "(select loadPropane from AppLoads where appName='Bath')*" + app5 + "\n" +
                        "+ \n" +
                        "(select loadPropane from AppLoads where appName='Laundry')*" + app6 + "\n" +
                        "+\n" +
                        "(select loadPropane from AppLoads where appName='Dryer')*" + app7 + "\n" +
                        "+\n" +
                        "(select loadPropane from AppLoads where appName='Stove')*" + app8 + "\n" +
                        ") as total_propane_load"
                , null);
//                "where appName='Hot Water Tank') * 2 " +
//                "+" +
//                "()select loadElectric " +
//                "from AppLoads " +
//                "where appName='On Demand Hot Water')*2", null);
        //Cursor c = db.rawQuery("select " + column + " from " + tblName + " where id IN (" + sqlinput + ")", null);

        while(c.moveToNext()) {
            result_elec = c.getDouble(0);
            result_prop = c.getDouble(1);
            result += result_elec + " " + result_prop + System.getProperty("line.separator");
        }
//        while(c.moveToNext()) {
//            if (column.contains("appName")) {
//                result_name = c.getString(1);
//                result += result_name + System.getProperty("line.separator");
//
//            } else if (column.contains("*")) {
//                result_name = c.getString(1);
//                result_elec = c.getDouble(2);
//                result_prop = c.getDouble(3);
//
//                result += result_name + " " + result_elec + " " + result_prop + System.getProperty("line.separator");
//            } else {
//                result_elec = c.getDouble(2);
//                result_prop = c.getDouble(3);
//
//                result += result_elec + " " + result_prop + System.getProperty("line.separator");
//            }


        c.close();
        db.close();

        return result;
    }
    public String loadHandler2(String column, String tblName, String sqlinput, String rate, String btuPerDollar){
        try{
            createDatabase();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        String result = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cost = db.rawQuery("select Round(SUM(kwhday * 30 * " + rate + "),2), round(SUM(btuday * 30 / "+ btuPerDollar +") + (sum((select kwhday from appCost where appName = 'EV Charger') +" +
                "(select kwhday from appCost where appName = 'Exterior Outlets') + " +
                "(select kwhday from appCost where appName = 'House Load')) * " + rate +"),2) from appCost", null);

        while(cost.moveToNext()){
            Number sumkwh = cost.getDouble(0);
            Number sumbtu = cost.getDouble(1);
            result += "Cost of Electricity per month = " + sumkwh + "\nCost of Propane per month = "  + sumbtu;
        }
        //Cursor cost = db.rawQuery("select " + column + " from " + tblName + " where id IN (" + sqlinput + ")", null);

//        while(cost.moveToNext()){
//            String cost_name = cost.getString(1);
//            Number cost_elec = cost.getDouble(2);
//            Number cost_prop = cost.getDouble(3);
//
//            result += cost_name + " " + cost_elec + " " + cost_prop + System.getProperty("line.separator");
//        }
        cost.close();
        db.close();

        return result;
    }
}
