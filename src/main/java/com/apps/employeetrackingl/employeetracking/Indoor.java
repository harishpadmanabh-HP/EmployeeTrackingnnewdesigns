package com.apps.employeetrackingl.employeetracking;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import java.util.Calendar;
import java.util.Date;

public class Indoor extends AppCompatActivity {
    String status;
    TextView taskhead,taskdetailstv;
    RadioGroup rg;
    RadioButton rb;
    Button Starttask,endtask;
    String  starttime,endtime;
    AsyncHttpClient client;
    JSONArray jarray;
    JSONObject jobject;
    RequestParams params;
    ImageButton backimage;


    String updatetaskurl="http://srishti-systems.info/projects/ticketbooking/api/emp_taskcompleted.php?";
 //   String currenttime;
  String taskheading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor);
        backimage=findViewById(R.id.backimagebutton);
        client = new AsyncHttpClient();
        params = new RequestParams();

        taskhead=findViewById(R.id.taskheading);
        taskdetailstv=findViewById(R.id.taskdetails);
        SharedPreferences shared = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        taskheading=shared.getString("taskheading",null);
        String taskdetails=shared.getString("taskdetails",null);
        String taskstarttime=shared.getString("taskstarttime",null);
        String taskendtime=shared.getString("taskendtime",null);
        String taskstartdate=shared.getString("taskstartdate",null);
        final String taskid=shared.getString("taskid",null);
        taskhead.setText(taskheading);
        taskdetailstv.setText("Details: "+taskdetails+"\nStart date:  "+taskstartdate+"\nend time:  "+taskendtime);

        Starttask=findViewById((R.id.starttask));
        endtask=findViewById(R.id.buttonendtime);
        rg = (RadioGroup) findViewById(R.id.radiogroup);



        Starttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //current date and time
                Calendar c = Calendar.getInstance();
                System.out.println("Current time =&gt; "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                starttime = df.format(c.getTime());
                Toast.makeText(Indoor.this, "Starting time is "+starttime, Toast.LENGTH_SHORT).show();


            }
        });

         endtask.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Calendar c = Calendar.getInstance();
                 System.out.println("Current time =&gt; "+c.getTime());

                 SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                 endtime = df.format(c.getTime());
//                 SimpleDateFormat dfdate = new SimpleDateFormat("yyyy-mm-dd");
//                 String currentdate=dfdate.format(c.getTime());
                 Date c1 = Calendar.getInstance().getTime(); System.out.println("Current date => " + c1);
                 SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                 String currentdate = df1.format(c1);

//                 Toast.makeText(Indoor.this, "Ending time is "+endtime, Toast.LENGTH_SHORT).show();
               // Toast.makeText(Indoor.this, "Date is "+currentdate, Toast.LENGTH_SHORT).show();


//                 Date c1new = Calendar.getInstance().getTime(); System.out.println("Current date => " + c1new);
//                 SimpleDateFormat df1new = new SimpleDateFormat("yyyy-MM-dd");
//                 String current_Date = df1new.format(c);


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

  //  String status = (String) rb.getText();

//                 Toast.makeText(Indoor.this,"SELCTED RB"+
//                         status, Toast.LENGTH_SHORT).show();
if(status.equalsIgnoreCase("yes")||status.equalsIgnoreCase("no")) {
    params.put("task_id", taskid);  //task id
    params.put("time", endtime);     //end time
    params.put("date", currentdate);//date
    params.put("status", status);     //slected radiobutton

    client.get(updatetaskurl, params, new AsyncHttpResponseHandler() {
        @Override
        public void onSuccess(String content) {
            super.onSuccess(content);
            try {
                jobject = new JSONObject(content);
                String s = jobject.getString("Status");
                if (s.equalsIgnoreCase("success")) {
                   // showalertbox("Task Updated");
                    Toast.makeText(Indoor.this, "Task Updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Indoor.this,NavActivity.class));
                } else if (s.equalsIgnoreCase("Already Updated")) {
                   // showalertbox("Task Already Updated!");
                    Toast.makeText(Indoor.this, "Task Already Updated!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Indoor.this,NavActivity.class));

                } else {
                    Toast.makeText(Indoor.this, "Something Went wrong!Check your details and try again", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(Indoor.this, "Exception caught" + e, Toast.LENGTH_SHORT).show();
            }


        }
    });
}//ends nuul check if
                 else{
         Toast.makeText(Indoor.this, ""+"Fill all fields", Toast.LENGTH_SHORT).show();
                 }



             }
         });

    }
    private void showalertbox(String s) {
        android.app.AlertDialog.Builder b=  new  android.app.AlertDialog.Builder(Indoor.this)
                .setTitle(taskheading)
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
        alertDialog.setTitle(taskheading);
        alertDialog.setMessage(s);
        // alertDialog.setIcon(R.drawable.todaystaskicon);
        alertDialog.show();

    }

    public void backimagepress(View view) {
        startActivity(new Intent(Indoor.this,NavActivity.class));
    }
}
