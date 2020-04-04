package ovh.jonhshepard.attestations;

import android.content.Context;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.storage.Identity;

public class Util {

    public static String formatDate(Date date) {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        return sdf.format(date);
    }

    public static String formatTime(Date date) {
        String myFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        return sdf.format(date);
    }

    public static String formatDateHour(Date date) {
        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);
        return sdf.format(date);
    }

    public static Calendar calendarFromString(String str) {
        if(str.length() != 10)
            return null;
        String[] spl = str.split("/");
        if(spl.length != 3)
            return null;
        int days;
        int month;
        int year;
        try {
            days = Integer.parseInt(spl[0]);
            month = Integer.parseInt(spl[1]);
            year = Integer.parseInt(spl[2]);
        } catch (Exception e) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, days);
        return c;
    }

    public static String manipulatePdf(Certificate certificate) {
        try {
            PdfReader reader = new PdfReader("origin.pdf");
            String name = System.currentTimeMillis() + ".pdf";
            PdfStamper stamper = new PdfStamper(reader,
                    new FileOutputStream(name));
            AcroFields fields = stamper.getAcroFields();
            Identity identity = certificate.getIdentity();
            fields.setField("name", identity.getFirstName() + " " + identity.getLastName());
            fields.setField("birthday", formatDate(identity.getBirthday()));
            fields.setField("birthplace", identity.getBirthplace());
            fields.setField("living", identity.getLivingAddress() + ", " + identity.getLivingPostalCode() + " " + identity.getLivingCity());
            fields.setField("reason", "check");
            fields.setField("Do at", "???");
            fields.setField("date", formatDate(certificate.getDate()));
            fields.setField("time", formatTime(certificate.getDate()));
            stamper.setFormFlattening(true);
            stamper.close();
            reader.close();

            return name;
        } catch (Exception e) {
            return null;
        }
    }
}
