package kr.hs.dgsw.ahnt3;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.hyunjun.school.SchoolMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hs.dgsw.ahnt3.Networks.MealAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GupsikFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GupsikFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GupsikFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public GupsikFragment() {
        // Required empty public constructor
    }

    TextView nextMeal;
    TextView nextMealKind;
    TextView getDate;

    Calendar calendar;

    SchoolMenu menu;

    int year;
    int month;
    int day;

    DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    public void onStart() {
        super.onStart();


        getDate = getView().findViewById(R.id.DatePick);
        nextMealKind = getView().findViewById(R.id.nextMealTV);
        nextMeal = getView().findViewById(R.id.nextMeal);
        initOnClickListener();
        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE) - 1;
        this.getDate.setText(year + "년 " + month + "월 " + (day+1) + "일");
        updateDateMeal(year, month, day);



    }

    private void initOnClickListener(){
        Button morningButton = getView().findViewById(R.id.setMorning);
        Button lunchButton = getView().findViewById(R.id.setLunch);
        Button dinnerButton = getView().findViewById(R.id.setDinner);
        morningButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMeal("아침", menu.breakfast);
                    }
                }
        );
        lunchButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        setMeal("점심", menu.lunch);
                    }
                }
        );
        dinnerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setMeal("저녁", menu.dinner);
                    }
                }
        );

        getDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDate();
                    }
                }
        );

        //날짜 버튼 누를시 실행되는 메서드
        mDateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                updateDateMeal(year, month+1, dayOfMonth-1);
            }
        };
    }
    private void getDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        DatePickerDialog dialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mDateSetListener,
                year,month,day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    //어플 실행시 자동으로 시간에 맞는 급식 띄워주는 메서드
    public void setAutoMeal()
    {
        try {
            SimpleDateFormat parser = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            SimpleDateFormat parser2 = new SimpleDateFormat("yyyy/MM/dd");

            Date currentDate = Calendar.getInstance().getTime();
            String currentString = parser.format(currentDate);
            String currentString2 = parser2.format(currentDate);

            Date morningDate = parser.parse(currentString2 + " 07:20");
            Date lunchDate = parser.parse(currentString2 + " 12:40");
            Date dinnerDate = parser.parse(currentString2 + " 18:40");
            Date userDate = parser.parse(currentString);
            if (userDate.before(morningDate)) {
                setMeal("아침", menu.breakfast);
            } else if (userDate.after(morningDate) && userDate.before(lunchDate)){
                setMeal("점심", menu.lunch);
            } else if (userDate.after(lunchDate) && userDate.before(dinnerDate)){
                setMeal("저녁", menu.dinner);
            } else if (userDate.after(dinnerDate)){
                setMeal("아침", menu.breakfast);
            }
        } catch (java.text.ParseException e) {
            // Invalid date was entered
        }
    }

    //이 날짜에 해당하는 급식을 전역변수에 저장
    public void updateDateMeal(int year, int month, int day)
    {
        this.getDate.setText(year + "년 " + month + "월 " + (day+1) + "일");
        new MealAsyncTask(new MealAsyncTask.AsyncResponse(){
            @Override
            public void processFinish(SchoolMenu result) {
                menu = result;
                setAutoMeal();
                //Toast.makeText(getContext(), "업데이트 되었습니다!", Toast.LENGTH_SHORT).show();
            }
        }).execute(year, month, day);
    }

    public void setMeal(String tag, String meal){
        nextMealKind.setText(tag);
        nextMeal.setText(meal);
    }



    // TODO: Rename and change types and number of parameters
    public static GupsikFragment newInstance(String param1, String param2) {
        GupsikFragment fragment = new GupsikFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

// endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gupsik, container, false);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "Gupsik Fragment Attached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
