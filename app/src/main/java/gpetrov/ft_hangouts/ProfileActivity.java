package gpetrov.ft_hangouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHandler _dbHandler;
    private EditText        mFirstname;
    private EditText        mLastname;
    private EditText        mPhone;
    private EditText        mEmail;
    private Contact         mContact;
    private Button          mSave;
    private ImageButton     mCallAction;
    private ImageButton     mSendSmsAction;
    private ImageButton     mEditAction;
    private ImageButton     mDeleteAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _dbHandler = new DatabaseHandler(getApplicationContext());

        int id = getIntent().getIntExtra(Constants.KEY_INTENT_PARAM_ID, 0);

        mContact = _dbHandler.getContact(id);

        getSupportActionBar().setTitle(mContact.getFirstname() + " " + mContact.getLastname());

        _initFields();
        _initActions();
    }

    private void _initActions(){
        mCallAction = (ImageButton)findViewById(R.id.profile_actions_call);
        mSendSmsAction = (ImageButton)findViewById(R.id.profile_actions_sms);
        mEditAction = (ImageButton)findViewById(R.id.profile_actions_edit);
        mDeleteAction = (ImageButton)findViewById(R.id.profile_actions_delete);

        // Send SMS
        mSendSmsAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "SEND SMS ACTION", Toast.LENGTH_SHORT).show();
            }
        });

        // Call
        mCallAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CALL ACTION", Toast.LENGTH_SHORT).show();
                String phone = mPhone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));

                try {
                    startActivity(intent);
                } catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Edit
        mEditAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _enableFields();
            }
        });

        // Delete
        mDeleteAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dbHandler.deleteContact(mContact);
                Toast.makeText(getApplicationContext(), R.string.profile_delete_success, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Save
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = mFirstname.getText().toString().trim();
                String lastname = mLastname.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String email = mEmail.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || phone.isEmpty() || email.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.profile_update_error, Toast.LENGTH_SHORT);
                    return;
                }

                Contact contact = new Contact(mContact.getId(), firstname, lastname, email, phone);
                _dbHandler.updateContact(contact);

                Toast.makeText(getApplicationContext(), R.string.profile_update_success, Toast.LENGTH_SHORT).show();
                _disableFields();
            }
        });
    }

    private void _initFields(){
        mFirstname = (EditText)findViewById(R.id.profile_form_firstname);
        mLastname = (EditText)findViewById(R.id.profile_form_lastname);
        mEmail = (EditText)findViewById(R.id.profile_form_email);
        mPhone = (EditText)findViewById(R.id.profile_form_phone);
        mSave  = (Button)findViewById(R.id.profile_form_button_save);

        mFirstname.setText(mContact.getFirstname());
        mLastname.setText(mContact.getLastname());
        mEmail.setText(mContact.getEmail());
        mPhone.setText(mContact.getPhone());

        _disableFields();
    }

    private void _disableFields(){

//        mFirstname.setEnabled(false);
//        mFirstname.setFocusable(false);
//
//        mLastname.setEnabled(false);
//        mLastname.setFocusable(false);
//
//        mPhone.setEnabled(false);
//        mPhone.setFocusable(false);
//
//        mEmail.setEnabled(false);
//        mEmail.setFocusable(false);

        mSave.setVisibility(View.INVISIBLE);
    }

    private void _enableFields(){
//        mFirstname.setEnabled(true);
//        mFirstname.setFocusable(true);
//
//        mLastname.setEnabled(true);
//        mLastname.setFocusable(true);
//
//        mPhone.setEnabled(true);
//        mPhone.setFocusable(true);
//
//        mEmail.setEnabled(true);
//        mEmail.setFocusable(true);

        mSave.setVisibility(View.VISIBLE);
    }
}
