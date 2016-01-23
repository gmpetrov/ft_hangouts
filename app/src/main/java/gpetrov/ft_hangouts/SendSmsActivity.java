package gpetrov.ft_hangouts;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class SendSmsActivity extends AppCompatActivity {

    private static SendSmsActivity _inst;
    private DatabaseHandler _dbHandler;
    private Contact         mContact;
    private EditText        mInput;

    private RecyclerView mRecyclerView;
    private SmsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Sms>              mSmsList = new ArrayList<Sms>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        _dbHandler = new DatabaseHandler(getApplicationContext());

        int id = getIntent().getIntExtra(Constants.KEY_INTENT_PARAM_ID, 0);

        mContact = _dbHandler.getContact(id);
        mInput = (EditText)findViewById(R.id.sms_input);

        getSupportActionBar().setTitle(mContact.getFirstname() + " " + mContact.getLastname());

        FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.button_sms_send);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = mInput.getText().toString().trim();

                if (msg.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.send_sms_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                sendSms(mContact.getPhone(), msg);
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.sms_list_recycler);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SmsListAdapter(mSmsList);
        mRecyclerView.setAdapter(mAdapter);

        getAll();
    }

    private void sendSms(String number, String msg){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, msg, null, null);
            Toast.makeText(getApplicationContext(), R.string.send_sms_success, Toast.LENGTH_SHORT).show();

            mSmsList.add(new Sms(msg, new Date().toString(), true));
            mAdapter.notifyDataSetChanged();

            scrollToBottom();

            mInput.setText("");
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllSentSms(){
        Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");

        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);

        startManagingCursor(cursor1);
        String[] columns = new String[] { "address", "person", "date", "body","type" };

        if (cursor1.getCount() > 0) {

            String count = Integer.toString(cursor1.getCount());

            while (cursor1.moveToNext()){

                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));

                if(address.equalsIgnoreCase(mContact.getPhone())){ //put your number here
                    String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
                    String dateString = cursor1.getString(cursor1.getColumnIndex(columns[2]));
                    String body = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                    String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));

                    DateFormat format = new SimpleDateFormat("MMddHHmm");

                    try {
                        Date date = format.parse(dateString);

                        Sms sms = new Sms(body, dateString, true);
                        mSmsList.add(sms);
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void getAllReceivedSms(){
        Uri mSmsinboxQueryUri = Uri.parse("content://sms/inbox");

        Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri, new String[]{"_id", "thread_id", "address", "person", "date", "body", "type"}, null, null, null);

        startManagingCursor(cursor1);
        String[] columns = new String[] { "address", "person", "date", "body","type" };


        if (cursor1.getCount() > 0) {

            String count = Integer.toString(cursor1.getCount());

            while (cursor1.moveToNext()){

                String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));

                if(address.equalsIgnoreCase(mContact.getPhone())){ //put your number here
                    String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
                    String dateString = cursor1.getString(cursor1.getColumnIndex(columns[2]));
                    String body = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                    String type = cursor1.getString(cursor1.getColumnIndex(columns[4]));

                    DateFormat format = new SimpleDateFormat("MMddHHmm");

                    try {
                        Date date = format.parse(dateString);
                        Sms sms = new Sms(body, dateString, false);

                        mSmsList.add(sms);
                    }
                    catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }


            }
        }
    }

    private void scrollToBottom(){
        mLayoutManager.scrollToPosition(mSmsList.size() - 1);
    }

    public static SendSmsActivity instance() {
        return _inst;
    }

    @Override
    protected void onStart() {
        super.onStart();
        _inst = this;
    }

    public void updateList(Sms sms){
        mAdapter.add(sms);
        mAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    public void getAll(){

        mSmsList.clear();

        getAllSentSms();
        getAllReceivedSms();

        Collections.sort(mSmsList);

        mAdapter.notifyDataSetChanged();

        scrollToBottom();
    }
}
