package kr.hs.dgsw.ahnt3.JsonClass;
import java.io.Serializable;
import java.util.Date;

public class ResponseOutJson implements Serializable {
    int status;
    String message;
    Data data;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Data getData() {
        return data;
    }

    public String getStartDate(){ return data.go_out.start_time;}
    public String getEndDate(){ return data.go_out.end_time;}
    public String getReason(){ return data.go_out.reason;}



}
class Data{
    go_out go_out;
    sleep_out sleep_out;

    public go_out getGo_out() {
        return go_out;
    }

    public sleep_out getSleep_out() {
        return sleep_out;
    }
}
class go_out{
    int accept;
    int idx;
    String start_time;
    String end_time;
    String reason;
    int class_idx;
    String student_email;

    public int getAccept() {
        return accept;
    }

    public int getIdx() {
        return idx;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getReason() {
        return reason;
    }

    public int getClass_idx() {
        return class_idx;
    }

    public String getStudent_email() {
        return student_email;
    }
}
class sleep_out{
    int accept;
    int idx;
    String start_time;
    String end_time;
    String reason;
    int class_idx;
    String student_email;

    public int getAccept() {
        return accept;
    }

    public int getIdx() {
        return idx;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getReason() {
        return reason;
    }

    public int getClass_idx() {
        return class_idx;
    }

    public String getStudent_email() {
        return student_email;
    }
}