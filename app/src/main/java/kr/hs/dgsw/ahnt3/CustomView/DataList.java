package kr.hs.dgsw.ahnt3.CustomView;


public class DataList {

    public String id;
    public String Date;
    public String Reason;
    public boolean status;
    public String type;

    public DataList(String id, String date, String reason, boolean status, String type){
        this.id = id;
        this.Date = date;
        this.Reason = reason;
        this.status = status;
        this.type = type;
    }
}
