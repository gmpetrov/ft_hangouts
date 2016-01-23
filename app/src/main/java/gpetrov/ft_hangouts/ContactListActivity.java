package gpetrov.ft_hangouts;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHandler _dbHandler;

    private String[] myDataset = {"Jean", "Claude", "Jacques"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _dbHandler = new DatabaseHandler(getApplicationContext());

        mRecyclerView = (RecyclerView) findViewById(R.id.list_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        //==============================================================

        List<Contact> addableContacts = _dbHandler.getAllContacts();

        int contactCount = _dbHandler.getContactsCount();
        Log.d("CONTACT COUNT", String.valueOf(contactCount));

        List<String> tmp = new ArrayList<String>();
        for (int i = 0; i < contactCount; i++){
            String firstname = addableContacts.get(i).getFirstname();
            tmp.add(firstname);
        }

        String[] tmpArray = new String[tmp.size()];
        String[] yolo = tmp.toArray(tmpArray);

        //==============================================================

        // specify an adapter (see also next example)
        mAdapter = new ContactListAdapter(yolo);
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                _goToCreateContactActivity();
                _saveContact();
            }
        });


    }

    private void _saveContact(){
        Contact contact = new Contact(_dbHandler.getContactsCount() + 1, "Jean", "jaques", "test@test.fr", "0102030405");

        _dbHandler.creatContact(contact);

        Toast.makeText(getApplicationContext(), "Contact Created", Toast.LENGTH_SHORT).show();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
