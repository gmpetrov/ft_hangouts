package gpetrov.ft_hangouts;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ContactListActivity extends AppCompatActivity {

    // Needed for the Recycler
    private RecyclerView                mRecyclerView;
    private ContactListAdapter          mAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;

    // Database
    private DatabaseHandler             _dbHandler;

    // Fields
    private TextView                    mEmptyListText;
    private String                      mDate;

    private ArrayList<Contact> mContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDate = null;

        mEmptyListText = (TextView)findViewById(R.id.text_empty_list);
        _dbHandler = new DatabaseHandler(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mContacts = _dbHandler.getAllContacts();

        if (mContacts.isEmpty()){ mEmptyListText.setVisibility(View.VISIBLE); }


        // specify an adapter (see also next example)
        mAdapter = new ContactListAdapter(getApplicationContext(), mContacts);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start Creat Contact Activity
                _goToCreateContactActivity();
            }
        });


    }

    private void _goToCreateContactActivity(){
        Intent intent = new Intent(ContactListActivity.this, CreatContactActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_red) {
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#CF362A"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            return true;
        }
        else if (id == R.id.menu_item_green){
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#27A668"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            return true;
        }
        else if (id == R.id.menu_item_blue){
            ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#3035AE"));
            getSupportActionBar().setBackgroundDrawable(colorDrawable);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mDate = getTime();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDate != null){
            Toast.makeText(getApplicationContext(), mDate, Toast.LENGTH_SHORT).show();
        }

        populateList();
    }

    private void populateList(){
        mContacts = _dbHandler.getAllContacts();

        if (mContacts.isEmpty()){ mEmptyListText.setVisibility(View.VISIBLE); }
        else{ mEmptyListText.setVisibility(View.INVISIBLE); }

        mAdapter.setDataSet(mContacts);
    }

    private String getTime(){

        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
