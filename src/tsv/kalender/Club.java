package tsv.kalender;

public class Club {
	
	int _id;
	String name;
	String sport;
	int contactId;
	int adressId;
	String internet;
	int sportId;
	
	public Club(){
		
	}

	public Club(int _id, String name, String sport, int contactId, int adressId, String internet, int sportId) {
		super();
		set_id(_id);
		setName(name);
		setSport(sport);
        setContactId(contactId);
        setAdressId(adressId);
        setInternet(internet);
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

	public String getSport() {
		return sport;
	}

	public int getContactId() {
		return contactId;
	}

	public int getAdressId() {
		return adressId;
	}

	public String getInternet() {
		return internet;
	}

	public void set_id(int _id) {
        this._id = _id;
	}

	public void setName(String name) {

        if(name == null || name.equals("")){
            this.name = "";
        }else{
            this.name = name;
        }
	}

	public void setSport(String sport) {

        if(sport == null || sport.equals("")){
            this.sport = "";
        }else {
            this.sport = sport;
        }
	}

	public void setContactId(int contactId) {
		this.contactId = contactId;
	}

	public void setAdressId(int adressId) {
		this.adressId = adressId;
	}

	public void setInternet(String internet) {

        if(internet == null || internet.equals("")){
            this.internet = "";
        }else{
            this.internet = internet;
        }

	}
	
}
