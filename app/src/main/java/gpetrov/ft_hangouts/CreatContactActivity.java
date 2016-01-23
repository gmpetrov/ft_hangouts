package gpetrov.ft_hangouts;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class CreatContactActivity extends AppCompatActivity{


    // UI references.
    private DatabaseHandler _dbHandler;
    private EditText        mFirstname;
    private EditText        mLastname;
    private EditText        mEmail;
    private EditText        mPhone;
    private Button          mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creat_contact);
        setupActionBar();

        _dbHandler = new DatabaseHandler(getApplicationContext());

        mFirstname = (EditText)findViewById(R.id.create_form_firstname);
        mLastname  = (EditText)findViewById(R.id.create_form_lastname);
        mEmail     = (EditText)findViewById(R.id.create_form_email);
        mPhone     = (EditText)findViewById(R.id.create_form_phone);
        mSave      = (Button)findViewById(R.id.create_form_button_save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = mFirstname.getText().toString().trim();
                String lastname  = mLastname.getText().toString().trim();
                String email     = mEmail.getText().toString().trim();
                String phone     = mPhone.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), R.string.create_form_msg_fields_error, Toast.LENGTH_SHORT).show();
                    return ;
                }

                Contact contact = new Contact(_dbHandler.getContactsCount() + 1, firstname, lastname, email, phone);

                _saveContact(contact);
                finish();
            }
        });
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void _saveContact(Contact contact){

        _dbHandler.creatContact(contact);

        Toast.makeText(getApplicationContext(), R.string.create_form_save_contact_success, Toast.LENGTH_SHORT).show();
    }

}

