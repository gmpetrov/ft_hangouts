package gpetrov.ft_hangouts;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gmp on 23/01/16.
 */
public class SmsListAdapter extends RecyclerView.Adapter<SmsListAdapter.ViewHolder> {

    private ArrayList<Sms> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mBody;
        public TextView mDate;

        public ViewHolder(View v) {
            super(v);
            mBody = (TextView)v.findViewById(R.id.sms_body);
            mDate  = (TextView)v.findViewById(R.id.sms_date);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SmsListAdapter(ArrayList<Sms> dataSet) {
        mDataset = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SmsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Sms sms = mDataset.get(position);

        String dateString = sms.getDate();

//        Date date = new Date((int)Float.parseFloat(dateString));

        long now = new Date().getTime();
        String convertDate = DateUtils.getRelativeTimeSpanString(Long.parseLong(dateString), now, DateUtils.SECOND_IN_MILLIS).toString();

        holder.mBody.setText(sms.getBody());
        holder.mDate.setText(convertDate);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(Sms sms) {
        mDataset.add(mDataset.size() - 1, sms);
        notifyItemInserted(mDataset.size() - 1);
    }

    public void setDataSet(ArrayList<Sms> data){
        mDataset = data;
        notifyDataSetChanged();
    }
}
