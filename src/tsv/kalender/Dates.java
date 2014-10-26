package tsv.kalender;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Dates {

    int _id;
    Date start;
    Date end;
    DateFormat sdf = new SimpleDateFormat("dd.MM.yy", Locale.GERMANY);
    SimpleDateFormat weekday = new SimpleDateFormat("EEE", Locale.GERMANY);
    String date;
    String day;
    String description;
    int clubId;
    String form;
    String starters;
    String location;
    String series;
    String startDate;
    String endDate;
    int sportId;
    boolean checked = false;


    public Dates(int _id, String description, String location, String startDate, String endDate, String series,
                 String form, String starters, int club, int sportId, String day) {

        this._id = _id;
        setStartDate(startDate);
        setEndDate(endDate);
        setDescription(description);
        setLocation(location);
        setSeries(series);
        setForm(form);
        setStarters(starters);
        setClubId(club);
        setSportId(sportId);

        if (startDate != null && endDate != null && !startDate.equals("") && !endDate.equals("")) {
            try {
                start = sdf.parse(startDate);
            } catch (ParseException e) {
            }

            try {
                end = sdf.parse(endDate);
            } catch (ParseException e) {
            }

            if (!startDate.equals("")) {
                if (!sdf.format(start).equals(sdf.format(end))) {
                    setDate(" " + sdf.format(start) + "- " + sdf.format(end));
                    setDay(weekday.format(start) + " - " + weekday.format(end));
                } else {
                    setDate(sdf.format(start));
                    setDay(weekday.format(start));


                }
            }
        }

        if(getDay()==null || getDay().equals("")){
            setDay(day);
        }

    }


    public int getSportId() {
        return sportId;
    }


    public void setSportId(int sportId) {
        this.sportId = sportId;
    }


    public String getStartDate() {
        return startDate;
    }


    public String getEndDate() {
        return endDate;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }


    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public int getClubId() {
        return clubId;
    }


    public String getSeries() {
        return series;
    }


    public void setClubId(int clubId) {
        this.clubId = clubId;
    }


    public void setSeries(String series) {


        if (series == null || series.equals("")) {
            this.series = "";
        } else {
            this.series = series;
        }
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {

        if (location == null || location.equals("")) {
            this.location = "";
        } else {
            this.location = location;
        }
    }

    public int get_id() {
        return _id;
    }

    public String getDate() {
        return date;
    }

    public String getDay() {
        return day;
    }

    public String getDescription() {
        return description;
    }

    public int getClub() {
        return clubId;
    }

    public String getForm() {
        return form;
    }

    public String getStarters() {
        return starters;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public void setDate(String date) {

        if (date == null || date.equals("")) {
            this.date = "";
        } else {
            this.date = date;
        }


    }

    public void setDay(String day) {

        if (day == null || day.equals("")) {
            this.day = "";
        } else {
            this.day = day;
        }

    }

    public void setDescription(String description) {

        if (description == null || description.equals("")) {
            this.description = "";
        } else {
            this.description = description;
        }

    }

    public void setClub(int club) {
        this.clubId = club;
    }

    public void setForm(String form) {

        if (form == null || form.equals("")) {
            this.form = "";
        } else {
            this.form = form;
        }

    }

    public void setStarters(String starters) {

        if (starters == null || starters.equals("")) {
            this.starters = "";
        } else {
            this.starters = starters;
        }

    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


}
