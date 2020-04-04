package ovh.jonhshepard.attestations.storage;

import java.util.Date;

public class Identity {

    private int id;
    private String lastname;
    private String firstname;
    private Date birthday;
    private String birthplace;
    private String living_address;
    private String living_city;
    private String living_postal_code;

    public Identity(int id, String lastname, String firstname, Date birthday, String birthplace, String living_address, String living_city, String living_postal_code) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthday = birthday;
        this.birthplace = birthplace;
        this.living_address = living_address;
        this.living_city = living_city;
        this.living_postal_code = living_postal_code;
    }

    public Identity(String lastname, String firstname, Date birthday, String birthplace, String living_address, String living_city, String living_postal_code) {
        this.id = -1;
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthday = birthday;
        this.birthplace = birthplace;
        this.living_address = living_address;
        this.living_city = living_city;
        this.living_postal_code = living_postal_code;
    }

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public void setBirthplace(String birthplace) {
        this.birthplace = birthplace;
    }

    public String getLivingAddress() {
        return living_address;
    }

    public void setLivingAddress(String living_address) {
        this.living_address = living_address;
    }

    public String getLivingCity() {
        return living_city;
    }

    public void setLivingCity(String living_city) {
        this.living_city = living_city;
    }

    public String getLivingPostalCode() {
        return living_postal_code;
    }

    public void setLivingPostalCode(String living_postal_code) {
        this.living_postal_code = living_postal_code;
    }
}
