package ovh.jonhshepard.attestations.storage;

import java.util.Date;
import java.util.List;

public class Certificate implements Comparable<Certificate> {

    private int id;
    private Identity identity;
    private List<EnumReason> reasons;
    private Date date;
    private String file;

    public Certificate(int id, Identity identity, List<EnumReason> reasons, Date date, String file) {
        this.id = id;
        this.identity = identity;
        this.reasons = reasons;
        this.date = date;
        this.file = file;
    }

    public Certificate(Identity identity, List<EnumReason>  reasons, Date date, String file) {
        this.id = -1;
        this.identity = identity;
        this.reasons = reasons;
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

    public List<EnumReason> getReasons() {
        return reasons;
    }

    public void setReasons(List<EnumReason> reasons) {
        this.reasons = reasons;
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
        return this.date.before(o.date) ? 1 :
                (this.date.after(o.date) ? -1 : 0);
    }
}
