package com.example.testingproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.auth.providers.anonymous.AnonymousCredential;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ledControl extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5,btn6,btn7,btn8, btnDis;
    String address = null;
    TextView lumn;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String format,format1,format2,format3,result;
    Date d1,d2;
    int minutes,sec;
    float res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);
        setContentView(R.layout.led_layout_control);


        final StitchAppClient stitchAppClient = Stitch.initializeDefaultAppClient("program-pupml");
        final RemoteMongoClient mongoClient = stitchAppClient.getServiceClient(RemoteMongoClient.factory,"mongodb-atlas");
        RemoteMongoCollection<Document> myCollection=mongoClient.getDatabase("current").getCollection("Distributed");
        Stitch.getDefaultAppClient().getAuth().loginWithCredential(new AnonymousCredential()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("stitch", "logged in anonymously");
            } else {
                Log.e("stitch", "failed to log in anonymously", task.getException());
            }
        });


        btn1 = (Button) findViewById(R.id.button2);
        btn2 = (Button) findViewById(R.id.button3);
        btn3 = (Button) findViewById(R.id.button5);
        btn4 = (Button) findViewById(R.id.button6);
        btn5 = (Button) findViewById(R.id.button7);
        btn6 = (Button) findViewById(R.id.button8);
        btn7 = (Button) findViewById(R.id.button9);
        btn8 = (Button) findViewById(R.id.button10);
        btnDis = (Button) findViewById(R.id.button4);
        lumn = (TextView) findViewById(R.id.textView2);

        new ConnectBT().execute();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("1");
                turnOnLed1();
                btn1.setBackgroundColor(Color.parseColor("#CDE95A"));

                Toast.makeText(ledControl.this, "BULB IS ONN", Toast.LENGTH_LONG).show();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                format = simpleDateFormat.format(new Date());


                //Toast.makeText(ledControl.this, format , Toast.LENGTH_LONG).show();

            }
        });


        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v)
            {
                // sendSignal("2");
                turnOffLed2();
                Toast.makeText(ledControl.this, "BULB IS SWITCHED OFF", Toast.LENGTH_LONG).show();
                btn2.setBackgroundColor(Color.parseColor("#FD4E09"));
                btn1.setBackgroundColor(Color.parseColor("#ff33b5e5"));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                format1 = simpleDateFormat.format(new Date());


                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // btn1.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        btn2.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                },3000);
                //btn2.setBackgroundColor(Color.parseColor("#ff33b5e5"));


                try
                {
                    Date d1 = simpleDateFormat.parse(format1);
                    Date d2 = simpleDateFormat.parse(format);
                    long difference = d1.getTime() - d2.getTime();
                    sec = (int) (difference / 1000 % 60 );
                    minutes = (int) ((difference / (1000 * 60)) % 60);
                    int hours = (int) ((difference / (1000 * 60 * 60)) % 24) - 1;
                    Log.d("Minutes"," : "+minutes);



                    if(minutes < 60){
                        float bulb = 1 * (30) * (minutes) * (60);
                        res = bulb / 3600000 ;

                        result = Float.toString(res);
                        Log.d("result",result);
                    }else{
                        Log.d("out", String.format("error"));
                    }

                }catch (ParseException e) {

                    e.printStackTrace();
                }

                try
                {
                    Document doc1 = new Document();
                    doc1.append("Name","Bulb1");
                    doc1.append("Seconds",sec);
                    doc1.append("Minutes",minutes);
                    doc1.append("Result",res);
                    //doc1.append("End_Time",new Date().getTime());
                    myCollection.insertOne(doc1);
                }catch (Exception e){
                    e.printStackTrace();
                }

                RemoteFindIterable findIterable = myCollection.find(new Document("Name","Bulb1")).projection(new Document().append("Result", 1).append("_id",0));

                Task<List<Document>> itemsTask = findIterable.into(new ArrayList<Document>());
                itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Document>> task) {
                        JSONObject jsonObject;
                        new JSONObject();
                        //int sum =0 ;
                        float sum=0;
                        if (task.isSuccessful())
                        {
                            List<Document> items = task.getResult();
                            Log.d("app", String.format("successfully found %d documents", items.size()));
                            /*for (Document item: items) {
                                Log.d("app", String.format("successfully found:  %s", item.toJson()));*/

                            try {
                                JSONArray jsonArray = new JSONArray(items);

                                for (int i = 0 ; i < jsonArray.length() ; i++){
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String k=jsonObject.keys().next();
                                    String second = jsonObject.getString(k);
                                    float fin = Float.parseFloat(second);
                                    sum = sum + fin;

                                    /*int fin = Integer.parseInt(second);
                                    sum = sum + fin;*/


                                    Log.i("Info", "Key: " + k + ", value: " + fin + " Sum " + sum);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Log.e("app", "failed to find documents with: ", task.getException());
                        }

                        Intent intent = new Intent(getBaseContext(), Graph.class);
                        intent.putExtra("RES",sum );
                        startActivity(intent);


                    }
                });


            }
        });




        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("3");
                turnOnLed3();
                btn3.setBackgroundColor(Color.parseColor("#CDE95A"));
                Toast.makeText(ledControl.this, "FAN IS ONN", Toast.LENGTH_LONG).show();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                format2 = simpleDateFormat.format(new Date());
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("4");
                turnOffLed4();
                Toast.makeText(ledControl.this,"FAN IS SWITCHED OFF", Toast.LENGTH_LONG).show();
                btn4.setBackgroundColor(Color.parseColor("#FD4E09"));
                btn3.setBackgroundColor(Color.parseColor("#ff33b5e5"));

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
                format3 = simpleDateFormat.format(new Date());


                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // btn1.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        btn4.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                },3000);


                try
                {
                    Date d1 = simpleDateFormat.parse(format3);
                    Date d2 = simpleDateFormat.parse(format2);
                    long difference = d1.getTime() - d2.getTime();
                    sec = (int) (difference / 1000 % 60 );
                    minutes = (int) ((difference / (1000 * 60)) % 60);
                    int hours = (int) ((difference / (1000 * 60 * 60)) % 24) - 1;
                    Log.d("Minutes"," : "+minutes);



                    if(minutes < 60){
                        float bulb = 1 * (30) * (minutes) * (60);
                        res = bulb / 3600000 ;
                        result = Float.toString(res);
                        Log.d("result",result);
                    }else{
                        Log.d("out", String.format("error"));
                    }

                }catch (ParseException e) {

                    e.printStackTrace();
                }


                try
                {
                    Document doc1 = new Document();
                    doc1.append("Name","FAN");
                    doc1.append("Seconds",sec);
                    doc1.append("Minutes",minutes);
                    doc1.append("Result",res);
                    //doc1.append("End_Time",new Date().getTime());
                    myCollection.insertOne(doc1);
                }catch (Exception e){
                    e.printStackTrace();
                }



                RemoteFindIterable findIterable = myCollection.find(new Document("Name","FAN")).projection(new Document().append("Result", 1).append("_id",0));

                Task<List<Document>> itemsTask = findIterable.into(new ArrayList<Document>());
                itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Document>> task) {
                        JSONObject jsonObject;
                        new JSONObject();
                        //int sum =0 ;
                        float sum=0;
                        if (task.isSuccessful())
                        {
                            List<Document> items = task.getResult();
                            Log.d("app", String.format("successfully found %d documents", items.size()));
                            /*for (Document item: items) {
                                Log.d("app", String.format("successfully found:  %s", item.toJson()));*/

                            try {
                                JSONArray jsonArray = new JSONArray(items);

                                for (int i = 0 ; i < jsonArray.length() ; i++){
                                    jsonObject = jsonArray.getJSONObject(i);
                                    String k=jsonObject.keys().next();
                                    String second = jsonObject.getString(k);

                                    float fin = Float.parseFloat(second);
                                    sum = sum + fin;

                                    /*int fin = Integer.parseInt(second);
                                    sum = sum + fin;*/


                                    Log.i("Info", "Key: " + k + ", value: " + fin + " Sum " + sum);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Log.e("app", "failed to find documents with: ", task.getException());
                        }

                        Intent intent = new Intent(getBaseContext(), Graph.class);
                        intent.putExtra("RES1",sum );
                        startActivity(intent);


                    }
                });



            }
        });


        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("5");
                turnOnLed5();
                btn5.setBackgroundColor(Color.parseColor("#CDE95A"));
                Toast.makeText(ledControl.this,"LIGHT IS ONN", Toast.LENGTH_LONG).show();
            }
        });

        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("5");
                turnOffLed6();
                Toast.makeText(ledControl.this, "LIGHT IS SWITCHED OFF", Toast.LENGTH_LONG).show();
                btn6.setBackgroundColor(Color.parseColor("#FD4E09"));
                btn5.setBackgroundColor(Color.parseColor("#ff33b5e5"));


                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // btn1.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        btn6.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                },3000);
            }
        });



        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("5");
                turnOnLed7();
                btn7.setBackgroundColor(Color.parseColor("#CDE95A"));
                Toast.makeText(ledControl.this,"TV IS ONN", Toast.LENGTH_LONG).show();
            }
        });


        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                //sendSignal("5");
                turnOffLed8();
                Toast.makeText(ledControl.this,"TV IS SWITCHED OFF", Toast.LENGTH_LONG).show();
                btn8.setBackgroundColor(Color.parseColor("#FD4E09"));
                btn7.setBackgroundColor(Color.parseColor("#ff33b5e5"));


                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        // btn1.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                        btn8.setBackgroundColor(Color.parseColor("#ff33b5e5"));
                    }
                },3000);
            }
        });



        btnDis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Disconnect();
            }
        });
    }



    private void turnOnLed1()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("1".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed3()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("3".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed5()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed7()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("7".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOffLed2()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("2".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }


    private void turnOffLed4()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOffLed6()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("6".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOffLed8()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("8".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendSignal ( String number ) {
        if ( btSocket != null ) {
            try {
                btSocket.getOutputStream().write(number.toString().getBytes());
            } catch (IOException e) {
                msg("Error");
            }
        }
    }

    private void Disconnect () {
        if ( btSocket!=null ) {
            try {
                btSocket.close();
            } catch(IOException e) {
                msg("Error");
            }
        }

        finish();
    }

    private void msg (String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }


    private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;

        @Override
        protected  void onPreExecute () {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please Wait!!!");
        }

        @Override
        protected Void doInBackground (Void... devices) {
            try {
                if ( btSocket==null || !isBtConnected ) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                ConnectSuccess = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute (Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            } else {
                msg("Connected");
                isBtConnected = true;
            }

            progress.dismiss();
        }
    }


    }
