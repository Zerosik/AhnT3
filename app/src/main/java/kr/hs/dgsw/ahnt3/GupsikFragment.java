package kr.hs.dgsw.ahnt3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import kr.hs.dgsw.ahnt3.MealLibrary.BapTool;
import kr.hs.dgsw.ahnt3.MealLibrary.ProcessTask;
import kr.hs.dgsw.ahnt3.MealLibrary.Tools;


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
    TextView mDisplayDate;
    DatePickerDialog.OnDateSetListener mDataSetListener;
    BapTool.restoreBapDateClass mData;
    //ProcessTask를 상속한 BapDownloadTask class
    BapDownloadTask mProcessTask;

    //진행 상황을 표시하기 위한 Dialog
    ProgressDialog mDialog;

    //오늘 날짜를 알아오기 위한 Calendar
    Calendar mCalendar;

    //번이상 BapDownloadTask를 실행하지 않도록 도와주는 boolean
    boolean isUpdating = false;

    @Override
    public void onStart() {
        super.onStart();

        //지금 날짜를 가져오기 위한Calendar 생성
        mCalendar = Calendar.getInstance();

        //급식 리스트를 얻습니다.
        //isUpdate=true로 설정하여 급식이 없을경우 BapDownloadTask를 실행합니다.
        getBapList(true);

        initButtonListener();//버튼 리스터 활성화

        initDatePicker();//DATEPICKER 활성화
        CheckBapTime();
    }
// region 급식
    private void initDatePicker(){
        mDisplayDate = (TextView)getView().findViewById(R.id.DatePick);
        mDisplayDate.setText(mCalendar.get(Calendar.YEAR) + "/" + (mCalendar.get(Calendar.MONTH)+1) + "/" + mCalendar.get(Calendar.DAY_OF_MONTH));
        mDisplayDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDataSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                mCalendar.set(year, month, day);
                month += 1;
                Log.d("DatePicker ", "onDataSet : mm/dd/yyy" + year + "/" + month + "/" + year);
                String date = year + "/" + month + "/" + day;
                mDisplayDate.setText(date);
                getBapDay();
                CheckBapTime();
            }//Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH
        };
    }
    private void CheckBapTime(){
        int time = mCalendar.get(Calendar.HOUR_OF_DAY);
        Log.e("현재 시간 ", Integer.toString(time)) ;
        if(time<8){
            nextMeal.setText(mData.Morning);
        }else if(8<=time && time<13){
            nextMeal.setText(mData.Lunch);
        }else if(13<=time && time<20){
            nextMeal.setText(mData.Dinner);
        }else{
            mData =  BapTool.restoreBapData(getActivity().getApplicationContext(), Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH+1);
            getBapList(true);
            nextMeal.setText(mData.Morning);
        }
    }

    private void initButtonListener(){
        Button morning = getView().findViewById(R.id.setMorning);
        morning.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextMeal.setText(mData.Morning);
                        if(mData.Morning == ""){
                            nextMeal.setText("해당 날에는 아침급식이 없습니다.");
                        }
                    }
                }
        );
        Button lunch = getView().findViewById(R.id.setLunch);
        lunch.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextMeal.setText(mData.Lunch);
                        if(mData.Lunch == ""){
                            nextMeal.setText("해당 날에는 점심급식이 없습니다.");
                        }
                    }
                }
        );
        Button dinner = getView().findViewById(R.id.setDinner);
        dinner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        nextMeal.setText(mData.Dinner);
                        if(mData.Dinner == ""){
                            nextMeal.setText("해당 날에는 저녁급식이 없습니다.");
                        }
                    }
                }
        );
    }
    private void getBapDay(){
        mData =  BapTool.restoreBapData(getActivity().getApplicationContext(), Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        getBapList(true);
    }
    private void getBapList(boolean isUpdate) {


        nextMeal = getView().findViewById(R.id.nextMeal);

        if (mCalendar == null)
            mCalendar = Calendar.getInstance();//calendar 초기화 여부 확인

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        //BapTool을 이용해서 저장된 급식 데이터를 가져옵니다.
        mData =  BapTool.restoreBapData(getActivity().getApplicationContext(), year, month, day);

        //isBlankDay가 true이면 급식 데이터가 저장되지 않은 날입니다.
        if (mData.isBlankDay) {
            if (Tools.isNetwork(getActivity().getApplicationContext())) {
                /**
                 * 네트워크가 켜져있으면
                 */
                if (!isUpdating && isUpdate) {
                    /**
                     * mProcessTask가 실행중이지 않고 : !isUpdating
                     * 업데이트를 실행하라고 하면 : isUpdate
                     *
                     * TODO 작업중 표시를 커스텀하려면 이곳을 수정하세요
                     */
                    mDialog = new ProgressDialog(getActivity());
                    mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mDialog.setMax(100);
                    mDialog.setTitle("로딩중");
                    mDialog.setCancelable(false);
                    mDialog.show();
                    /**
                     * 급식을 업데이트 합니다.
                     */
                    mProcessTask = new BapDownloadTask(getActivity().getApplicationContext());
                    mProcessTask.execute(year, month, day);
                }
            } else {
                /**
                 * 네트워크가 꺼져있으면 오류 메세지를 표시합니다.
                 * TODO 원하는 오류 처리방식으로 수정하세요
                 */
                Context context = getActivity();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                alertDialogBuilder.setTitle("오류!");

                alertDialogBuilder
                        .setMessage("인터넷이 연결되어 있지 않습니다!")
                        .setCancelable(false)
                        .setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton("재시도",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        getBapList(true);
                                    }
                                });

                // 다이얼로그 생성
                AlertDialog alertDialog = alertDialogBuilder.create();

                // 다이얼로그 보여주기
                alertDialog.show();


            }
            return;
        }
        //mAdapter.addItem(mData.Calender, mData.DayOfTheWeek, mData.Morning, mData.Lunch, mData.Dinner);

        /**
         * TODO for문이 실행되고 나면 mCalendar의 날짜가 이번주 금요일을 설정되므로 mCalendar의 날짜를 다시 설정해주어야 합니다.
         */
    }
    /**
     * ProcessTask를 상속받아 만든 BapDownloadTask
     */
    public class BapDownloadTask extends ProcessTask {
        public BapDownloadTask(Context mContext) {
            super(mContext);
        }

        @Override
        public void onPreDownload() {
            isUpdating = true;
        }

        @Override
        public void onUpdate(int progress) {
            /**
             * TODO 작업 현황을 표시하는 방법을 커스텀 하세요
             */
            mDialog.setProgress(progress);
        }

        @Override
        public void onFinish(long result) {
            if (mDialog != null)
                mDialog.dismiss();

            isUpdating = false;

            if (result == -1) {
                /**
                 * TODO 에러가 발생하면 어떻게 처리할건지 이곳에 작성하세요
                 */
                return;
            }

            /**
             * 무한 반복 업데이트를 막기 위해 isUpdate=false로 getBapList()을 호출합니다.
             */
            getBapList(false);
            CheckBapTime();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        /**
         * 앱을 일시중지 할경우 Dialog를 닫습니다.
         */
        if (mDialog != null)
            mDialog.dismiss();

        mCalendar = null;
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
