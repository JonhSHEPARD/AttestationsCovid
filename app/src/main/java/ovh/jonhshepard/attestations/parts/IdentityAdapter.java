package ovh.jonhshepard.attestations.parts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ovh.jonhshepard.attestations.R;
import ovh.jonhshepard.attestations.Util;
import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.wrappers.ActivityWithIdentityList;

/**
 * Extension of {@link ArrayAdapter} for showing identities in ListView
 */
public class IdentityAdapter extends ArrayAdapter<Identity> {

    /**
     * Context variable
     */
    private final ActivityWithIdentityList context;

    /**
     * Create a new Adapter
     *
     * @param context Context of the app
     * @param items   Default items of the adapter
     */
    public IdentityAdapter(ActivityWithIdentityList context, List<Identity> items) {
        super(context, 0, items);

        this.context = context;
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
        Identity identity = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.identitylist_item, parent, false);
        }

        if (identity == null)
            return convertView;

        // Adding tag to the view
        convertView.setTag(identity.getId());

        // Getting all elements of the view
        TextView txtName = convertView.findViewById(R.id.identityName);
        TextView txtAddress = convertView.findViewById(R.id.identityAddress);

        // Setting basic text values
        txtName.setText(identity.getLastName() + " " + identity.getFirstName() + " | " + Util.formatDate(identity.getBirthday()));
        txtAddress.setText(identity.getLivingAddress() + ", " + identity.getLivingPostalCode() + " " + identity.getLivingCity());

        // If the element is selected, setting the background to grey
        if (convertView.isSelected())
            convertView.setBackgroundColor(context.getColor(R.color.selectedColor));
        else
            convertView.setBackgroundColor(Color.TRANSPARENT);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}