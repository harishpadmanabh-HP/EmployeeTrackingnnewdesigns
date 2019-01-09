package com.apps.employeetrackingl.employeetracking.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.employeetrackingl.employeetracking.Indoor;
import com.apps.employeetrackingl.employeetracking.NavActivity;
import com.apps.employeetrackingl.employeetracking.Outdoor;
import com.apps.employeetrackingl.employeetracking.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayTaskFragment extends Fragment {
    AsyncHttpClient client;
    JSONArray jarray;
    JSONObject jobject;
    RequestParams params;
    ListView lv;

    private ShimmerFrameLayout mShimmerViewContainer;

    ArrayList<String> today_task_id_array;
    ArrayList<String> emp_id_array;
    ArrayList<String> task_array;
    ArrayList<String> taskdetails_array;
    ArrayList<String> org_array;
    ArrayList<String> startdate_array;
    ArrayList<String> date_array;
    ArrayList<String> type_array;
    ArrayList<String> starttime_array;
    ArrayList<String> endtime_array;
    ArrayList<String> location_array;
    ArrayList<String> todaydate_array;
    ArrayList<String> status_array;

    ArrayList<String> formatdate;
    ArrayList<String> datesperated;



    public TodayTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainview = inflater.inflate(R.layout.fragment_today_task, container, false);


        client = new AsyncHttpClient();
        params = new RequestParams();

        lv = mainview.findViewById(R.id.view_today_task_list);
        mShimmerViewContainer = mainview.findViewById(R.id.shimmer_view_container);

        today_task_id_array = new ArrayList<String>();
        emp_id_array = new ArrayList<String>();
        task_array = new ArrayList<String>();
        taskdetails_array = new ArrayList<String>();
        org_array = new ArrayList<String>();
        startdate_array = new ArrayList<String>();
        date_array = new ArrayList<String>();
        type_array = new ArrayList<String>();
        starttime_array = new ArrayList<String>();
        endtime_array = new ArrayList<String>();
        location_array = new ArrayList<String>();
        todaydate_array = new ArrayList<String>();
        status_array = new ArrayList<String>();

        formatdate = new ArrayList<String>();
        datesperated = new ArrayList<String>();


        Date c = Calendar.getInstance().getTime(); System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String current_Date = df.format(c);

        // Toast.makeText(getActivity(), ""+formattedDate, Toast.LENGTH_SHORT).show();

        SharedPreferences shared = getActivity().getSharedPreferences("Pref",MODE_PRIVATE);
        String user_id=shared.getString("user_id",null);



        params.put("date",current_Date);  //current_Date
        params.put("emp_id",user_id);     //user_id

        client.get("http://srishti-systems.info/projects/ticketbooking/api/emp_taskdetails.php?",params,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);

              //  Toast.makeText(getActivity(), ""+content, Toast.LENGTH_SHORT).show();

                try {

                    jobject = new JSONObject(content);
                    String s = jobject.getString("status");


                    if(s.equals("success")){

                        jarray = jobject.getJSONArray("Emp_details");
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject obj = jarray.getJSONObject(i);

                            String pre_id = obj.getString("id");
                            today_task_id_array.add("id:" + pre_id);

                            String p_id = obj.getString("emp_id");
                            emp_id_array.add("emp_id:" + p_id);

                            String d_id = obj.getString("task");
                            task_array.add("task:" + d_id);

                            String date = obj.getString("taskdetails");
                            taskdetails_array.add("taskdetails" + date);

                            String time = obj.getString("org");
                            org_array.add("org" + time);

                            String report = obj.getString("startdate");
                            startdate_array.add("" + report);

//month and date
                            String formatteddate=report;
                            // Toast.makeText(getContext(), "fncall"+getfrmdate(formatteddate), Toast.LENGTH_SHORT).show();
                            String finaldateformat=getmonthinwords(formatteddate);
                            formatdate.add(finaldateformat);

                            String datesperatedfromoriginal=getdateseperated(formatteddate);
                            datesperated.add(datesperatedfromoriginal);
//month and date end
                            String pre_time = obj.getString("date");
                            date_array.add("date" + pre_time);

                            String med_mrn = obj.getString("type");
                            type_array.add("" + med_mrn);

                            String med_aff = obj.getString("starttime");
                            starttime_array.add("" + med_aff);

                            String med_eve = obj.getString("endtime");
                            endtime_array.add("endtime" + med_eve);

                            String time_mrn = obj.getString("location");
                            location_array.add("location" + time_mrn);

                            String time_aff = obj.getString("todaydate");
                            todaydate_array.add("todaydate" + time_aff);

                            String time_eve = obj.getString("status");
                            status_array.add("status" + time_eve);
                        }

                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }

                    else if(s.equalsIgnoreCase("fail"))   {
                        AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                                .setTitle("You have no pending tasks !")
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                // do something...
                                                startActivity(new Intent(getActivity(), NavActivity.class));
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
                        AlertDialog alertDialog=b.create();
                        alertDialog.setTitle("Today Tasks");
                        alertDialog.setMessage("You have no  tasks assigned today yet. !");
                        alertDialog.setIcon(R.drawable.todaystaskicon);
                        alertDialog.show();


                        //Toast.makeText(getActivity(), "" + "You have no pending task", Toast.LENGTH_SHORT).show();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                    }

                    adapter adpt = new adapter();
                    lv.setAdapter(adpt);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String tasktype=type_array.get(position);
                            String taskheading=task_array.get(position);
                            String taskdetails=taskdetails_array.get(position);
                            String taskstarttime=starttime_array.get(position);
                            String taskendtime=endtime_array.get(position);
                            String taskstartdate=startdate_array.get(position);
                            String taskid= today_task_id_array.get(position);
                            String taskstatus=status_array.get(position);
                            Toast.makeText(getContext(), "TTTASK"+tasktype, Toast.LENGTH_SHORT).show();
                            if(tasktype.equalsIgnoreCase("indoor task") && taskstatus.equalsIgnoreCase("pending"))
                            { SharedPreferences shared = getContext().getSharedPreferences("Pref",MODE_PRIVATE);

                                SharedPreferences.Editor editor=shared.edit();
                                editor.putString("taskheading",taskheading);
                                editor.putString("taskdetails",taskdetails);
                                editor.putString("taskstarttime",taskstarttime);
                                editor.putString("taskendtime",taskendtime);
                                editor.putString("taskstartdate",taskstartdate);
                                editor.putString("taskid",taskid);
                                editor.putString("taskstatus",taskstatus);

                                editor.apply();
                                startActivity(new Intent(getContext(), Indoor.class));
                            }
                            else if(tasktype.equalsIgnoreCase("outdoor task")&& taskstatus.equalsIgnoreCase("pending"))
                            {
                                SharedPreferences shared = getContext().getSharedPreferences("Pref",MODE_PRIVATE);

                                SharedPreferences.Editor editor=shared.edit();
                                editor.putString("taskheading",taskheading);
                                editor.putString("taskdetails",taskdetails);
                                editor.putString("taskstarttime",taskstarttime);
                                editor.putString("taskendtime",taskendtime);
                                editor.putString("taskstartdate",taskstartdate);
                                editor.putString("taskid",taskid);
                                editor.putString("taskstatus",taskstatus);

                                editor.apply();
                                startActivity(new Intent(getContext(), Outdoor.class));

                            }
                            //for completed tasks show alert
                            else if(tasktype.equalsIgnoreCase("outdoor task")&& taskstatus.equalsIgnoreCase("completed"))
                            {
                                AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                                        .setTitle("You have completed this task!")
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        // do something...
                                                        startActivity(new Intent(getActivity(), NavActivity.class));
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
                                AlertDialog alertDialog=b.create();
                                alertDialog.setTitle(taskheading);
                                alertDialog.setMessage("You have completed this task !");
                                alertDialog.setIcon(R.drawable.alltaskicon);
                                alertDialog.show();



                            }
                            else if(tasktype.equalsIgnoreCase("indoor task")&& taskstatus.equalsIgnoreCase("completed"))
                            {
                                AlertDialog.Builder b=  new  AlertDialog.Builder(getActivity())
                                        .setTitle("You have completed this task!")
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int whichButton) {
                                                        // do something...
                                                        startActivity(new Intent(getActivity(), NavActivity.class));
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
                                AlertDialog alertDialog=b.create();
                                alertDialog.setTitle(taskheading);
                                alertDialog.setMessage("You have completed this task !");
                                alertDialog.setIcon(R.drawable.alltaskicon);
                                alertDialog.show();



                            }



                        }
                    });

                }catch (Exception e){

                    Toast.makeText(getActivity(), ""+e, Toast.LENGTH_SHORT).show();

                }





            }
        });

        return mainview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Today's Task");
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
    }

    class  adapter extends BaseAdapter {
        LayoutInflater Inflater;
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return date_array.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Inflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView= Inflater.inflate(R.layout.today_task_list,null);
            Viewholder holder=new Viewholder();

            holder.task=(TextView)convertView.findViewById(R.id.textView_task);
            holder.task.setText(task_array.get(position));

            holder.task_details=(TextView)convertView.findViewById(R.id.textView_task_details);
            holder.task_details.setText(taskdetails_array.get(position));

            holder.type=(TextView)convertView.findViewById(R.id.textView_type);
            holder.type.setText(type_array.get(position));

            holder.type=(TextView)convertView.findViewById(R.id.textView_type);
            holder.type.setText(type_array.get(position));

//            holder.today_task_date=(TextView)convertView.findViewById(R.id.textView_date);
//            holder.today_task_date.setText(startdate_array.get(position));

            holder.t_month=(TextView)convertView.findViewById(R.id.textView_monthtodaytask);
            holder.t_month.setText(formatdate.get(position));

            holder.t_date=(TextView)convertView.findViewById(R.id.texView_datetodaytask);
            holder.t_date.setText(datesperated.get(position));

            holder.starttime=(TextView)convertView.findViewById(R.id.textView_start_time);
            holder.starttime.setText("Time : "+starttime_array.get(position)+" To "+endtime_array.get(position));



            return convertView;
        }

        class Viewholder{
            TextView task;
            TextView task_details;
            TextView type;
            TextView today_task_date;
            TextView t_month;
            TextView t_date;
            TextView starttime;

        }
    }
    public String getmonthinwords(String date)

    {   String month=date.substring(5,7);
        String dateaftermonth=date.substring(8,10);
     //   Toast.makeText(getContext(), "getrfm date substring"+month+"datee  "+dateaftermonth, Toast.LENGTH_SHORT).show();
        StringBuilder monthinwords=new StringBuilder();
        if(month.equals("10"))
        {
            monthinwords.append("Oct ");
        }
        else if(month.equals("11"))
        {
            monthinwords.append("Nov ");
        }
        else if(month.equals("12"))
        {
            monthinwords.append("Dec ");
        }
        else if(month.equals("01"))
        {
            monthinwords.append("Jan ");
        }
        else if(month.equals("02"))
        {
            monthinwords.append("Feb ");
        }
        else if(month.equals("03"))
        {
            monthinwords.append("Mar ");
        }
        else if(month.equals("04"))
        {
            monthinwords.append("Apr ");
        }
        else if(month.equals("05"))
        {
            monthinwords.append("May ");
        }
        else if(month.equals("06"))
        {
            monthinwords.append("Jun ");
        }
        else if(month.equals("07"))
        {
            monthinwords.append("Jul ");
        }
        else if(month.equals("08"))
        {
            monthinwords.append("Aug ");
        }
        else if(month.equals("09"))
        {
            monthinwords.append("Sep ");
        }
      //  Toast.makeText(getContext(), ""+monthinwords, Toast.LENGTH_SHORT).show();

        return String.valueOf(monthinwords);
    }

    public  String getdateseperated(String date)
    {       String dateaftermonth=date.substring(8,10);

        return dateaftermonth;
    }
}

