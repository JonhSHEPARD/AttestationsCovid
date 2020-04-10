package ovh.jonhshepard.attestations.parts;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import ovh.jonhshepard.attestations.R;
import ovh.jonhshepard.attestations.Util;
import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.wrappers.ActivityWithCertificateList;

/**
 * Extension of {@link ArrayAdapter} for showing certificates in ListView
 */
public class CertificateAdapter extends ArrayAdapter<Certificate> {

    /**
     * Context variable
     */
    private final ActivityWithCertificateList context;

    /**
     * Create a new Adapter
     *
     * @param context Context of the app
     * @param items   Default items of the adapter
     */
    public CertificateAdapter(ActivityWithCertificateList context, List<Certificate> items) {
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
        Certificate certificate = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.certificatelist_item, parent, false);
        }

        if (certificate == null)
            return convertView;

        // Adding tag to the view
        convertView.setTag(certificate.getId());

        // Getting all elements of the view
        TextView txtName = convertView.findViewById(R.id.certifIdentityName);
        TextView txtBirthday = convertView.findViewById(R.id.certifIdentityBirthday);
        TextView txtAddress = convertView.findViewById(R.id.certifIdentityAddress);
        TextView txtReason = convertView.findViewById(R.id.certifReason);
        TextView txtDate = convertView.findViewById(R.id.certifDateHour);

        // Setting basic text values
        txtName.setText(certificate.getIdentity().getLastName() + " " + certificate.getIdentity().getFirstName());
        txtBirthday.setText(Util.formatDate(certificate.getIdentity().getBirthday()));
        txtAddress.setText(certificate.getIdentity().getLivingAddress() + ", "
                + certificate.getIdentity().getLivingPostalCode() + " " + certificate.getIdentity().getLivingCity());
        txtReason.setText(Util.join("/", certificate.getReasons()));
        txtDate.setText(Util.formatDateHour(certificate.getDate()));

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 30);
        Calendar maxD = Calendar.getInstance();
        maxD.add(Calendar.DAY_OF_YEAR, -2);

        if (convertView.isSelected())
            convertView.setBackgroundColor(context.getColor(R.color.selectedColor));
        else {
            if (certificate.getDate().after(now.getTime())) {
                convertView.setBackgroundColor(context.getColor(R.color.coming));
            } else if (certificate.getDate().before(maxD.getTime())) {
                convertView.setBackgroundColor(context.getColor(R.color.past));
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT);
            }
        }

        return convertView;
    }
}