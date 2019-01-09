package com.apps.employeetrackingl.employeetracking;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Outdoor extends AppCompatActivity {
    String current_date;String status;
    TextView taskhead,taskdetailstv;
    RadioGroup rg;
    RadioButton rb;
    Button Starttask,endtask;
    String  starttime,endtime;
    AsyncHttpClient client,client2;
    JSONArray jarray;
    JSONObject jobject;
    RequestParams params,params2;
    //final Context context = this;
    //forloc
    String s_longitude,s_latitude;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    //forloc
   String urlformail="http://srishti-systems.info/projects/ticketbooking/api/emp_sentmail.php?";

    String url="http://srishti-systems.info/projects/ticketbooking/api/emp_locationsave.php?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outdoor);
        //for loc perm
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        //for loc perm
        client = new AsyncHttpClient();
        client2 = new AsyncHttpClient();

        params = new RequestParams();
        params2=new RequestParams();
        taskhead=findViewById(R.id.taskheadingoutdoor);
        taskdetailstv=findViewById(R.id.taskdetailsoutdoor);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        String taskheading=shared.getString("taskheading",null);
        String taskdetails=shared.getString("taskdetails",null);
        String taskstarttime=shared.getString("taskstarttime",null);
        String taskendtime=shared.getString("taskendtime",null);
        String taskstartdate=shared.getString("taskstartdate",null);
        final String taskid=shared.getString("taskid",null);
        taskhead.setText(taskheading);
        taskdetailstv.setText("Details: "+taskdetails+"\nStart date:  "+taskstartdate+"\nend time:  "+taskendtime);
        Starttask=findViewById((R.id.starttaskoutdoor));
        endtask=findViewById(R.id.buttonendtimeoutdoor);
        rg = (RadioGroup) findViewById(R.id.radiogroupoutdoor);
        Starttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //current date and time
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                starttime = df.format(c.getTime());
                Toast.makeText(Outdoor.this, "Starting time is "+starttime, Toast.LENGTH_SHORT).show();


            }
        });
        endtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //get current location

                locationTrack = new LocationTrack(Outdoor.this);


                if (locationTrack.canGetLocation()) {


                    double longitude = locationTrack.getLongitude();
                    double latitude = locationTrack.getLatitude();
                    s_longitude=Double.toString(longitude);
                     s_latitude=Double.toString(latitude);

                   // Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
                } else {

                    locationTrack.showSettingsAlert();
                }
               //  Toast.makeText(getApplicationContext(), "Longitude:" + s_longitude + "\nLatitude:" + s_latitude, Toast.LENGTH_SHORT).show();

//parse
                Calendar coutdoor = Calendar.getInstance();
                System.out.println("Current time =&gt; "+coutdoor.getTime());

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                endtime = df.format(coutdoor.getTime());
               // SimpleDateFormat dfdate = new SimpleDateFormat("yyyy-mm-dd");
                //String currentdate=dfdate.format(coutdoor.getTime());
               // Outdoor.this, "Ending time is "+endtime, Toast.LENGTH_SHORT).show();

//
                 Date c1 = Calendar.getInstance().getTime(); System.out.println("Current date => " + c1);
                 SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                 String current_Date = df1.format(c1);


                // get selected radio button from radioGroup
                int selectedId = rg.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rb = (RadioButton) findViewById(selectedId);
                //..check if rb selected
                if (rg.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    // Toast.makeText(Indoor.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                    status="nothing_selected";
                }
                else
                {
                    // one of the radio buttons is checked
                    status = (String) rb.getText();

                }

                //..
                // status= (String) rb.getText();
               // Toast.makeText(Outdoor.this,"SELCTED RB"+
                       // status, Toast.LENGTH_SHORT).show();
                if(status.equalsIgnoreCase("yes")||status.equalsIgnoreCase("no")) {

                params.put("task_id",taskid);  //current_Date
                params.put("time",endtime);     //user_id
                params.put("date",current_Date);
                params.put("status",status);     //
                params.put("lat",s_latitude);     //
                params.put("log",s_longitude);     //
//showalertbox(params.toString());
                client.post(url,params,new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(String content) {
                        super.onSuccess(content);
                        Log.e("tessstparams",url+params);
                        try{
                            jobject = new JSONObject(content);
                            String s = jobject.getString("Status");
                            Log.e("STATUS",content);
                            if(s.equalsIgnoreCase("success"))
                            {
                                Toast.makeText(Outdoor.this, "Task Updated", Toast.LENGTH_SHORT).show();
                                //alert
                                final Dialog dialog = new Dialog(Outdoor.this);
                                dialog.setContentView(R.layout.customalert);
                                Button dialogButton = (Button) dialog.findViewById(R.id.submitinalert);
                                Button canceldialog = (Button) dialog.findViewById(R.id.cancelinalert);
                                final EditText emailalert=(EditText)dialog.findViewById(R.id.emailinalert);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
//                        dialog.dismiss();
//                        Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                                        String emailalert_s=emailalert.getText().toString();
                                        Toast.makeText(Outdoor.this, ""+emailalert_s, Toast.LENGTH_SHORT).show();
                                        params2.put("email",emailalert_s);
                                        params2.put("task_id",taskid);
                  client2.get(urlformail,params2,new AsyncHttpResponseHandler(){
                      @Override
                      public void onSuccess(String content) {
                          super.onSuccess(content);
                          try{
                              jobject = new JSONObject(content);
                              String s = jobject.getString("status");
                              if(s.equalsIgnoreCase("Mail Senting successfully"))
                              {
                                  Toast.makeText(Outdoor.this, "Please visit the link in your inbox to rate us . Thank you ", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(Outdoor.this,NavActivity.class));
                                  dialog.dismiss();;
                              }
                          }catch(Exception e){

                          }
                      }
                  })       ;
                                    }
                                });
                                canceldialog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show();
                                dialog.getWindow().setLayout(900, 800);
                                //alert

                            }
                            else  if(s.equalsIgnoreCase("Already Updated"))
                            {

                                Toast.makeText(Outdoor.this, "Task Already Updated!", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(Outdoor.this,NavActivity.class));
                            }
                            else
                            {
                                Toast.makeText(Outdoor.this, "Something Went wrong!Check your details and try again", Toast.LENGTH_SHORT).show();
                            }


                        }catch (Exception e){
                            Toast.makeText(Outdoor.this, "Exception caught"+e, Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }//ends nuul check if
                 else{
                Toast.makeText(Outdoor.this, ""+"Fill all fields", Toast.LENGTH_SHORT).show();
            }

                //parse
















                //get current loc



            }
        });




    }

    private void showalertbox(String s) {
        android.app.AlertDialog.Builder b=  new  android.app.AlertDialog.Builder(Outdoor.this)
                .setTitle("Request params")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
                                dialog.dismiss();
                                // startActivity(new Intent(getApplicationContext(), NavActivity.class));
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        android.app.AlertDialog alertDialog=b.create();
        alertDialog.setTitle("params");
        alertDialog.setMessage(s);
       // alertDialog.setIcon(R.drawable.todaystaskicon);
        alertDialog.show();

    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Outdoor.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationTrack = new LocationTrack(Outdoor.this);

        locationTrack.stopListener();
    }

    public void backimagepress(View view) {
        startActivity(new Intent(Outdoor.this,NavActivity.class));

    }
}
