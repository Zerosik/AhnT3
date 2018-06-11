package kr.hs.dgsw.ahnt3.Networks;

import android.os.AsyncTask;


import org.hyunjun.school.School;
import org.hyunjun.school.SchoolException;
import org.hyunjun.school.SchoolMenu;

import java.util.List;


public class MealAsyncTask extends AsyncTask<Integer, Void, SchoolMenu> {

    School api = new School(School.Type.HIGH, School.Region.DAEGU, "D100000282");
    public AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinish(SchoolMenu result);
    }

    public MealAsyncTask(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected SchoolMenu doInBackground(Integer... data) {
        SchoolMenu menu = null;
        try{
            List<SchoolMenu> menuList = api.getMonthlyMenu(data[0], data[1]);
            menu = menuList.get(data[2]);
        }catch(SchoolException e){
            e.printStackTrace();
        }
        return menu;
    }

    @Override
    protected void onPostExecute(SchoolMenu postResult) {
        delegate.processFinish(postResult);
    }
}
