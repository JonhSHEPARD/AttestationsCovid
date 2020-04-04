package ovh.jonhshepard.attestations.storage;

import java.util.Date;

public class Certificate implements Comparable<Certificate> {

    private int id;
    private Identity identity;
    private EnumReason reason;
    private Date date;
    private String file;

    public Certificate(int id, Identity identity, EnumReason reason, Date date, String file) {
        this.id = id;
        this.identity = identity;
        this.reason = reason;
        this.date = date;
        this.file = file;
    }

    public Certificate(Identity identity, EnumReason reason, Date date, String file) {
        this.id = -1;
        this.identity = identity;
        this.reason = reason;
        this.date = date;
        this.file = file;
    }

    public int getId() {
        return id;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public EnumReason getReason() {
        return reason;
    }

    public void setReason(EnumReason reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int compareTo(Certificate o) {
        return this.date.before(o.date) ? -1 :
                (this.date.after(o.date) ? 1 : 0);
    }
}
