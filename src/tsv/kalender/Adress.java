package tsv.kalender;

public class Adress {

    int _id;
    String street;
    String postCode;
    String city;
    String extra;

    public Adress() {

    }

    public Adress(int _id, String street, String postCode, String city,
                  String extra) {
        super();
        this._id = _id;
        setStreet(street);
        setPostCode(postCode);
        setCity(city);
        setExtra(extra);
    }


    public int get_id() {
        return _id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getExtra() {
        return extra;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setStreet(String street) {

        if (street == null || street.equals("")) {
            this.street = "";
        } else {
            this.street = street;
        }

    }

    public void setPostCode(String postCode) {

        if (postCode == null || postCode.equals("")) {
            this.postCode = "";
        } else {
            this.postCode = postCode;
        }

    }

    public void setCity(String city) {

        if (city == null || city.equals("")) {
            this.city = "";
        } else {
            this.city = city;
        }

    }

    public void setExtra(String extra) {

        if (extra == null || extra.equals("")) {
            this.extra = "";
        } else {
            this.extra = extra;
        }

    }


}
