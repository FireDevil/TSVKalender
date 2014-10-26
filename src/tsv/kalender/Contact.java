package tsv.kalender;

public class Contact {

    int _id;
    String name;
    String function;
    int adressId;
    String mail;
    String tel;
    String mobil;
    String fax;
    int sportId;


    public Contact(int _id, String name, String function,
                   String mail, String tel, String mobil, String fax, int adressId, int sportId) {
        super();
        this._id = _id;
        setName(name);
        setFunction(function);
        setMail(mail);
        setTel(tel);
        setMobil(mobil);
        setFax(fax);
        setAdressId(adressId);
        setSportId(sportId);
    }

    public int getSportId() {
        return sportId;
    }

    public void setSportId(int sportId) {
        this.sportId = sportId;
    }

    public int get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getFunction() {
        return function;
    }

    public int getAdressId() {
        return adressId;
    }

    public String getMail() {
        return mail;
    }

    public String getTel() {
        return tel;
    }

    public String getMobil() {
        return mobil;
    }

    public String getFax() {
        return fax;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setName(String name) {
        if (name == null || name.equals("")) {
            this.name = "";
        } else {
            this.name = name;
        }

    }

    public void setFunction(String function) {

        if (function == null || function.equals("")) {
            this.function = "";
        } else {
            this.function = function;
        }

    }

    public void setAdressId(int adressId) {
        this.adressId = adressId;
    }

    public void setMail(String mail) {

        if (mail == null || mail.equals("")) {
            this.mail = "";
        } else {
            this.mail = mail;
        }

    }

    public void setTel(String tel) {

        if (tel == null || tel.equals("")) {
            this.tel = "";
        } else {
            this.tel = tel;
        }

    }

    public void setMobil(String mobil) {

        if (mobil == null || mobil.equals("")) {
            this.mobil = "";
        } else {
            this.mobil = mobil;
        }

    }

    public void setFax(String fax) {

        if (fax == null || fax.equals("")) {
            this.fax = "";
        } else {
            this.fax = fax;
        }

    }

}
