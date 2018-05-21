package kr.hs.dgsw.ahnt3.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternChecker {
    public boolean EmailChecker(String email){
        Pattern p = Pattern.compile("^[a-zA-Z0-9]+@dgsw.hs.kr+$");
        Matcher m = p.matcher(email);

        return m.find();
    }
    public boolean PassworkChecker(String pw){
        Pattern p = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#$%^&*()]).{8,16}$");
        Matcher m = p.matcher(pw);
        return m.find();
    }
}
