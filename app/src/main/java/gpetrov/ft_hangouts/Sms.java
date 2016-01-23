package gpetrov.ft_hangouts;

import android.util.Log;

/**
 * Created by gmp on 23/01/16.
 */
public class Sms implements Comparable<Sms> {
    private String _body, _date;
    private boolean _me;

    public Sms(String body, String date, boolean me){
        _body = body;
        _date = date;
        _me = me;
    }

    public String getBody(){ return _body; }

    public String getDate(){ return _date; }

    public boolean isMe(){ return _me; }

    @Override
    public int compareTo(Sms another) {

        try {
//            SimpleDateFormat sdf = new SimpleDateFormat();
//            Date date1 = sdf.parse(_date);
//            Date date2 = sdf.parse(another.getDate());

//            if (date1.after(date2)){ return 1; }
            return (int)(Float.parseFloat(_date) - Float.parseFloat(another.getDate()));
        }
        catch (Exception e){
            Log.e("SMS COMPARE", e.toString());
        }

        return -1;
    }
}
