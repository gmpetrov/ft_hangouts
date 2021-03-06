package gpetrov.ft_hangouts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DatabaseHandler _dbHandler;
    private EditText        mFirstname;
    private EditText        mLastname;
    private EditText        mPhone;
    private EditText        mEmail;
    private Contact         mContact = null;
    private Button          mSave;
    private ImageButton     mCallAction;
    private ImageButton     mSendSmsAction;
    private ImageButton     mEditAction;
    private ImageButton     mDeleteAction;
    private CoordinatorLayout  mGlobalLayout;
    private RelativeLayout mProfileLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        _dbHandler = new DatabaseHandler(getApplicationContext());
        mGlobalLayout = (CoordinatorLayout)findViewById(R.id.layout_profile_global);
        mProfileLayout = (RelativeLayout)findViewById(R.id.layout_profile);

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
                Intent intent = new Intent(ProfileActivity.this, SendSmsActivity.class);

                intent.putExtra(Constants.KEY_INTENT_PARAM_ID, mContact.getId());

                startActivity(intent);
            }
        });

        // Call
        mCallAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        mSave.setVisibility(View.INVISIBLE);
    }

    private void _enableFields(){
        mSave.setVisibility(View.VISIBLE);
    }
}
