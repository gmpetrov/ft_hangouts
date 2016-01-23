package gpetrov.ft_hangouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gmp on 22/01/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    public DatabaseHandler(Context context){
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Constants.TABLE_CONTACTS + "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.KEY_FISTNAME + " TEXT," + Constants.KEY_LASTNAME + " TEXT," + Constants.KEY_EMAIL + " TEXT," + Constants.KEY_PHONE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_CONTACTS);

        onCreate(db);
    }

    public void creatContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_FISTNAME, contact.getFirstname());
        values.put(Constants.KEY_LASTNAME, contact.getLastname());
        values.put(Constants.KEY_EMAIL, contact.getEmail());
        values.put(Constants.KEY_PHONE, contact.getPhone());

        db.insert(Constants.TABLE_CONTACTS, null, values);
        db.close();
    }

    public Contact getContact(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_CONTACTS, new String[]{ Constants.KEY_FISTNAME, Constants.KEY_LASTNAME, Constants.KEY_EMAIL, Constants.KEY_PHONE }, Constants.KEY_ID + "=?", new String[]{ String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();


        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        db.close();
        return contact;
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(Constants.TABLE_CONTACTS, Constants.KEY_ID + "=?", new String[]{ String.valueOf(contact.getId()) });

        db.close();
    }

    public int getContactsCount(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_CONTACTS, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Constants.KEY_FISTNAME, contact.getFirstname());
        values.put(Constants.KEY_LASTNAME, contact.getLastname());
        values.put(Constants.KEY_EMAIL, contact.getEmail());
        values.put(Constants.KEY_PHONE, contact.getPhone());

        return db.update(Constants.TABLE_CONTACTS, values, Constants.KEY_ID + "=?", new String[]{String.valueOf(contact.getId())});
    }

    public List<Contact> getAllContacts(){
        List<Contact> contacts = new ArrayList<Contact>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Constants.TABLE_CONTACTS, null);

//        if (cursor.moveToFirst()){
            while (cursor.moveToNext()) {
                Log.d("DATABASE HANDLER", "YOLO");
                Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
                contacts.add(contact);
            }

//        }
        cursor.close();
        return contacts;
    }
}
