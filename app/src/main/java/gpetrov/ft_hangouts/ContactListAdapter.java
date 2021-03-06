package gpetrov.ft_hangouts;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gmp on 22/01/16.
 */
public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<Contact>  mDataset;
    private Context             mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mPhone;
        public TextView mName;
        public RelativeLayout mLayout;
        public TextView mId;

        public ViewHolder(View v) {
            super(v);
            mPhone = (TextView)v.findViewById(R.id.secondLine);
            mName  = (TextView)v.findViewById(R.id.firstLine);
            mId = (TextView)v.findViewById(R.id.contact_id);
            mLayout = (RelativeLayout)v.findViewById(R.id.contact_list_row_layout);

            mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(v.getContext(), mName.getText().toString(), Toast.LENGTH_SHORT).show();

                    int id = Integer.parseInt(mId.getText().toString());

                    Intent intent = new Intent(v.getContext(), ProfileActivity.class);

                    intent.putExtra(Constants.KEY_INTENT_PARAM_ID, id);

                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactListAdapter(Context context, ArrayList<Contact> dataSet) {
        mDataset = dataSet;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Contact contact = mDataset.get(position);

        holder.mPhone.setText(contact.getPhone());
        holder.mName.setText(contact.getFirstname() + " " + contact.getLastname());
        holder.mId.setText(String.valueOf(contact.getId()));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void add(int position, Contact contact) {
        mDataset.add(position, contact);
        notifyItemInserted(position);
    }

    public void setDataSet(ArrayList<Contact> data){
        mDataset = data;
        notifyDataSetChanged();
    }
}
