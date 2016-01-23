package gpetrov.ft_hangouts;

/**
 * Created by gmp on 22/01/16.
 */
public class Contact {

    private String _fistname, _lastname, _email, _phone;
    private int _id;

    public Contact(int id, String fistname, String lastname, String email, String phone){
        _id = id;
        _fistname = fistname;
        _lastname = lastname;
        _email = email;
        _phone = phone;
    }

    public int getId(){ return _id; }

    public String getFirstname(){ return _fistname; }

    public String getLastname(){ return _lastname; }

    public String getEmail(){ return _email; }

    public String getPhone(){ return _phone; }
}
