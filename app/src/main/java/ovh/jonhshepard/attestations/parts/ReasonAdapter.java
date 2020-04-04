package ovh.jonhshepard.attestations.parts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ovh.jonhshepard.attestations.R;
import ovh.jonhshepard.attestations.storage.EnumReason;

/**
 * Extension of {@link ArrayAdapter} for showing identities in ListView
 */
public class ReasonAdapter extends ArrayAdapter<EnumReason> {

    /**
     * Create a new Adapter
     *
     * @param context Context of the app
     */
    public ReasonAdapter(Context context) {
        super(context, 0, EnumReason.values());
    }

    /**
     * Getting the view of each identity item
     *
     * @param position    Position of the item
     * @param convertView If existing, the view of the item
     * @param parent      Parent of this view (ListView)
     * @return {@link View} of the item
     */
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EnumReason reason = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.enumreasonlist_item, parent, false);
        }

        if (reason == null)
            return convertView;

        // Adding tag to the view
        convertView.setTag(reason.name());

        // Getting all elements of the view
        TextView txtName = convertView.findViewById(R.id.reasonText);
        // Setting basic text values
        txtName.setText(reason.getLongMsg());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}