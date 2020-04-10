package ovh.jonhshepard.attestations;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.barcodes.qrcode.ErrorCorrectionLevel;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.CanvasRenderer;
import com.itextpdf.layout.renderer.IRenderer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ovh.jonhshepard.attestations.storage.Certificate;
import ovh.jonhshepard.attestations.storage.EnumReason;
import ovh.jonhshepard.attestations.storage.Identity;
import ovh.jonhshepard.attestations.storage.SQLiteDatabaseHandler;

public class Util {

    public static String formatDate(Date date) {
        return formatDateObj("dd/MM/yyyy", date);
    }

    public static String formatTime(Date date) {
        return formatDateObj("HH:mm", date);
    }

    public static String formatDateHour(Date date) {
        return formatDateObj("dd/MM/yyyy HH:mm", date);
    }

    public static String getHours(Date date) {
        return formatDateObj("HH", date);
    }

    public static String getMinutes(Date date) {
        return formatDateObj("mm", date);
    }

    private static String formatDateObj(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.FRANCE);
        return sdf.format(date);
    }

    public static Calendar calendarFromString(String str) {
        if (str.length() != 10)
            return null;
        String[] spl = str.split("/");
        if (spl.length != 3)
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

    public static String manipulatePdf(Activity ctx, Certificate certificate) {
        try {

            PdfReader reader = new PdfReader(ctx.getAssets().open("origin.pdf"));
            File folder = new File(Environment.getExternalStorageDirectory(), "CovidCertificates");

            folder.mkdirs();
            File file = new File(folder, System.currentTimeMillis() + ".pdf");
            file.createNewFile();

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(reader, writer);
            PdfPage page = pdfDoc.getFirstPage();

            PdfCanvas pdfCanvas = new PdfCanvas(page);
            Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
            MyCanvasRenderer renderer = new MyCanvasRenderer(canvas);
            canvas.setRenderer(renderer);
            PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);

            Identity identity = certificate.getIdentity();

            drawText(canvas, font, identity.getFirstName() + " " + identity.getLastName(), 123, 686);
            drawText(canvas, font, formatDate(identity.getBirthday()), 123, 661);
            drawText(canvas, font, identity.getBirthplace(), 92, 638);
            drawText(canvas, font, identity.getLivingAddress() + " "
                    + identity.getLivingPostalCode()
                    + " " + identity.getLivingCity(), 134, 613);

            if (certificate.getReasons().contains(EnumReason.WORK)) {
                drawText(canvas, font, "x", 76, 527, 19);
            }
            if (certificate.getReasons().contains(EnumReason.FOOD)) {
                drawText(canvas, font, "x", 76, 478, 19);
            }
            if (certificate.getReasons().contains(EnumReason.MEDIC)) {
                drawText(canvas, font, "x", 76, 436, 19);
            }
            if (certificate.getReasons().contains(EnumReason.ASSIST)) {
                drawText(canvas, font, "x", 76, 400, 19);
            }
            if (certificate.getReasons().contains(EnumReason.SPORT)) {
                drawText(canvas, font, "x", 76, 345, 19);
            }
            if (certificate.getReasons().contains(EnumReason.CONVOC)) {
                drawText(canvas, font, "x", 76, 298, 19);
            }
            if (certificate.getReasons().contains(EnumReason.MISSION)) {
                drawText(canvas, font, "x", 76, 260, 19);
            }


            drawText(canvas, font, identity.getLivingCity(), 111, 227, 12);

            // Date sortie
            drawText(canvas, font, formatDate(certificate.getDate()), 92, 203);
            drawText(canvas, font, getHours(certificate.getDate()), 197, 203);
            drawText(canvas, font, getMinutes(certificate.getDate()), 220, 203);

            // Date création
            drawText(canvas, font, "Date de création:", 464, 150, 7);
            Date d = new Date(certificate.getDate().getTime() - 600000);
            drawText(canvas, font, formatDate(d) + " à " + formatTime(d), 455, 144, 7);

            String qrdata = join("; ", Arrays.asList("Cree le: " + formatDate(d) + " a " + formatTime(d),
                    "Nom: " + identity.getLastName(),
                    "Prenom: " + identity.getFirstName(),
                    "Naissance: " + formatDate(identity.getBirthday()) + " a " + identity.getBirthplace(),
                    "Adresse: " + identity.getLivingAddress() + " " + identity.getLivingPostalCode()
                            + " " + identity.getLivingCity(),
                    "Sortie: " + formatDate(certificate.getDate()) + " a " +
                            getHours(certificate.getDate()) + "h" + getMinutes(certificate.getDate()),
                    "Motifs: " + join("-", certificate.getReasons()))
            );

            Map<EncodeHintType, Object> hints = new HashMap<>();

            // Character set
            hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);

            // Create the QR-code
            BarcodeQRCode qrCode = new BarcodeQRCode(qrdata, hints);
            Image qrCodeAsImage = new Image(qrCode.createFormXObject(pdfDoc));
            qrCodeAsImage.setFixedPosition(page.getPageSize().getWidth() - 170, 155);
            qrCodeAsImage.scaleAbsolute(100, 100);
            canvas.add(qrCodeAsImage);

            canvas.close();

            newPageQr(pdfDoc, qrCode);

            pdfDoc.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void newPageQr(PdfDocument pdfDoc, BarcodeQRCode qrCode) {
        PdfPage page = pdfDoc.addNewPage();
        PdfCanvas pdfCanvas = new PdfCanvas(page);
        Canvas canvas = new Canvas(pdfCanvas, pdfDoc, page.getPageSize());
        MyCanvasRenderer renderer = new MyCanvasRenderer(canvas);
        canvas.setRenderer(renderer);

        Image qrCodeAsImage = new Image(qrCode.createFormXObject(pdfDoc));
        qrCodeAsImage.setFixedPosition(50, page.getPageSize().getHeight() - 350);
        qrCodeAsImage.scaleAbsolute(300, 300);
        canvas.add(qrCodeAsImage);

        canvas.close();
    }

    private static void drawText(Canvas canvas, PdfFont font, String text, int x, int y) {
        drawText(canvas, font, text, x, y, 12);
    }

    private static void drawText(Canvas canvas, PdfFont font, String text, int x, int y, int size) {
        Text txt = new Text(text).setFont(font).setFontSize(size);

        float ascent = font.getAscent(text, size);
        float descent = font.getDescent(text, size);

        Paragraph p = new Paragraph().add(txt);
        float height = ascent - descent;
        p.setFixedPosition(x, y - height, p.getWidth());
        canvas.add(p);
    }

    public static String join(String delimiter, List array) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.size(); i++)
            result.append(i > 0 ? delimiter : "").append(array.get(i).toString());
        return result.toString();
    }

    public static void openPdfFile(Activity ctx, SQLiteDatabaseHandler db, Certificate certificate) {
        File pdf = new File(certificate.getFile());
        if(!pdf.exists()) {
            certificate.setFile(Util.manipulatePdf(ctx, certificate));
            db.updateCertificate(certificate);
        }

        Intent target = new Intent(Intent.ACTION_VIEW);

        Uri uri = FileProvider.getUriForFile(ctx,
                ctx.getPackageName() + ".provider",
                new File(certificate.getFile()));

        target.setDataAndType(uri, "application/pdf");
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        target.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        Intent intent = Intent.createChooser(target, "Open File");
        try {
            ctx.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    static class MyCanvasRenderer extends CanvasRenderer {

        protected boolean full = false;

        private MyCanvasRenderer(Canvas canvas) {
            super(canvas);
        }

        @Override
        public void addChild(IRenderer renderer) {
            super.addChild(renderer);
            full = Boolean.TRUE.equals(getPropertyAsBoolean(Property.FULL));
        }

        public boolean isFull() {
            return full;
        }
    }
}
