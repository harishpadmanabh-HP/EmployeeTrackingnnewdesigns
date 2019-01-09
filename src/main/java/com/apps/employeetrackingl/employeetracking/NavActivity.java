package com.apps.employeetrackingl.employeetracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.apps.employeetrackingl.employeetracking.Fragment.AllTaskFragment;
import com.apps.employeetrackingl.employeetracking.Fragment.PendinFragment;
import com.apps.employeetrackingl.employeetracking.Fragment.TodayTaskFragment;
import com.apps.employeetrackingl.employeetracking.Fragment.UpcomingFragment;

public class NavActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displaySelectedScreen(R.id.nav_all_task);
        View header = navigationView.getHeaderView(0);
        TextView text_usr_name = (TextView) header.findViewById(R.id.header_user_name);


        SharedPreferences shared = getApplicationContext().getSharedPreferences("Pref",MODE_PRIVATE);
        final String s_name=shared.getString("username",null);
        text_usr_name.setText(s_name);




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            prefs = PreferenceManager.getDefaultSharedPreferences(NavActivity.this);
            prefs.edit().clear().commit();
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
      /*  int id = item.getItemId();

        if (id == R.id.nav_all_task) {
            // Handle the camera action
        } else if (id == R.id.nav_today_task) {

        } else if (id == R.id.nav_upcoming_task) {

        } else if (id == R.id.nav_pending_task) {

        }*/

        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;

        switch (itemId) {
            case R.id.nav_all_task:
                fragment = new AllTaskFragment();
                break;
            case R.id.nav_today_task:
                fragment = new TodayTaskFragment();
                break;


            case R.id.nav_upcoming_task:
                fragment = new UpcomingFragment();
                break;

           /* case R.id.nav_pending_task:
                fragment = new ();
                break;*/

            case R.id.nav_pending_task:
                fragment = new PendinFragment();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_nav, fragment);
            ft.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

}
