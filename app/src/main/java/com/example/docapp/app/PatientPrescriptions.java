package com.example.docapp.app;

import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PatientPrescriptions extends ListActivity implements AdapterView.OnItemClickListener {

    ListView mlistview;
    ArrayAdapter<String> adapter;
    ArrayList<String> entries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_prescriptions);
        mlistview = (ListView)findViewById(android.R.id.list);

        entries = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, entries);

        mlistview.setAdapter(adapter);
        mlistview.setOnItemClickListener(this);
        updateData();
    }


    public void updateData(){

        ParseUser cuser = ParseUser.getCurrentUser();

        final String[] response = new String[5];
        final Date[] date = new Date[1];
        final Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        ParseQuery<ParseObject> oQuery = ParseQuery.getQuery("Prescriptions");
        oQuery.whereEqualTo("patientId", cuser.getObjectId());
        oQuery.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> results, ParseException e) {
                // results has the list of users with a hometown team with a winning record
                int i = 0;
                for (ParseObject result : results) {
                    // This does not require a network access.
                    response[0] = result.getString("medicines");
                    date[0] = result.getCreatedAt();
                    //response[2] = result.getString("date");
                    response[2] = result.getString("docId");
                    ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                    userQuery.whereEqualTo("objectId", response[2]);
                    userQuery.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> results, ParseException e) {
                            // results has the list of users with a hometown team with a winning record
                            int i=0;
                            for (ParseObject result : results) {
                                // This does not require a network access.
                                //ParseObject user = result.getParseObject("userType");
                                String fname = result.getString("Fname");
                                String lname = result.getString("Lname");

                                //where.add(fname+" "+lname);
                                //values[i]=fname+" "+lname;
                                //i++;
                                response[3]=fname+" "+lname;
                                adapter.setNotifyOnChange(true);
                                adapter.add(response[3] + " Prescribed : " + response[0] + " at " + formatter.format(date[0]));
                                Log.d("post", "retrieved a related post " + fname + " " + lname);
                            }
                        }
                    });
                    //ParseObject user = result.getParseObject("userType");


                    //where.add(fname+" "+lname);
                    //values[i]=fname+" "+lname;
                    //i++;
                    //adapter.setNotifyOnChange(true);
                    //adapter.add("Appointment "+"with "+response[4]+);

                    //Log.d("post", "retrieved a related post " + fname + " " + lname);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_prescriptions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
