package kr.hs.dgsw.ahnt3;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFormatException;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hs.dgsw.ahnt3.CustomView.DataList;
import kr.hs.dgsw.ahnt3.CustomView.RecyclerAdapter;
import kr.hs.dgsw.ahnt3.JsonClass.JsonConverter;
import kr.hs.dgsw.ahnt3.JsonClass.ResponseOutJson;
import kr.hs.dgsw.ahnt3.Networks.AsyncResponse;
import kr.hs.dgsw.ahnt3.Networks.HttpAsyncTask;
import kr.hs.dgsw.ahnt3.Util.DBHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LeaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LeaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaveFragment extends Fragment {
    DBHelper dbHelper;
    String uri = "http://flow.cafe24app.com/";
    ResponseOutJson resultJson;
    String Token;
    private LeaveFragment.OnFragmentInteractionListener mListener;
    private Button applicationButton;

    private RecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<DataList> mData;

    public LeaveFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leave, container, false);


        mRecyclerView = (RecyclerView)view.findViewById(R.id.Leavelistview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new RecyclerAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        applicationButton = (Button)getView().findViewById(R.id.applicationButton);
        Token = dbHelper.getDbToken();

        InitApplicationButon();
    }
    public void InitLeaveListView(){
        mData = new ArrayList<>();
        Cursor res = dbHelper.getLeaveList();
        while(res.moveToNext()){
            String startTime = res.getString(1);
            String endTime = res.getString(2);
            String reason = res.getString(3);
            int status = res.getInt(4);
            String type = res.getString(5);
            String date = startTime + " ~ " + endTime;
            boolean bStatus;
            if(status == 0){ bStatus = false; }else{ bStatus = true; }
            mData.add(new DataList("", date, reason, bStatus, type));
        }
    }
    public void AddListData(ResponseOutJson data, String type){

        if(type == "out/go")
            type = "out";
        else
            type = "sleep";
        if(dbHelper.insertLeaveData(data, type)) {
            String startdate = data.getStartDate().replace("T", " ").substring(0, 16);
            String enddate = data.getEndDate().replace("T", " ").substring(0, 16);
            mData.add(new DataList("", startdate + " ~ " + enddate, data.getReason(), false, type));
            mAdapter.notifyItemInserted(mData.size() - 1);
        }else{
            Log.i("msg","insert failed");
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        InitLeaveListView();
    }


    public void InitApplicationButon(){

        applicationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog dialog;
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                final View mView = getLayoutInflater().inflate(R.layout.dialog_application, null);
                final EditText startTime = (EditText)mView.findViewById(R.id.startDate);
                final EditText endTime = (EditText)mView.findViewById(R.id.endDate);
                final EditText ReasonText = (EditText)mView.findViewById(R.id.reason);
                Button Confirm = (Button) mView.findViewById(R.id.appliConfirm);

                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();

// region InitDate
                String fMonth;
                String fDay;
                Calendar currentTime = Calendar.getInstance();
                if((currentTime.get(Calendar.MONTH)+1)<10)
                    fMonth = "0"+(currentTime.get(Calendar.MONTH)+1);
                else
                    fMonth = ""+(currentTime.get(Calendar.MONTH)+1);

                if(currentTime.get(Calendar.DATE)<10)
                    fDay = "0"+currentTime.get(Calendar.DATE);
                else
                    fDay = ""+currentTime.get(Calendar.DATE);
                String datestr = currentTime.get(Calendar.YEAR)+"-"+fMonth+"-"+fDay+" ";
                long time = System.currentTimeMillis();
                SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm");
                String timestr = dayTime.format(new Date(time));

                startTime.setText(datestr + timestr);
                endTime.setText(datestr + timestr);
// endregion
                Confirm.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(!startTime.getText().toString().isEmpty() && !endTime.getText().toString().isEmpty() && !ReasonText.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "신청하였습니다.", Toast.LENGTH_SHORT).show();
                            RadioGroup radioGroup = (RadioGroup)mView.findViewById(R.id.LeaveTypeRadio);
                            int radioButtonID = radioGroup.getCheckedRadioButtonId();
                            View radioButton = radioGroup.findViewById(radioButtonID);
                            int idx = radioGroup.indexOfChild(radioButton);
                            final String leaveType;
                            if(idx == 0)
                                leaveType = "out/go";
                            else
                                leaveType = "out/sleep";
                            JSONObject json = new JSONObject();
                            try {
                                json.put("start_time", startTime.getText());
                                json.put("end_time", endTime.getText());
                                json.put("reason", ReasonText.getText());
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            new HttpAsyncTask(new AsyncResponse(){
                                @Override
                                public void processFinish(String output) {
                                    JsonConverter converter = new JsonConverter();
                                    resultJson = (ResponseOutJson)converter.ConvertObjectToJson(output, 2);
                                    if(resultJson.getStatus() == 400)
                                        Toast.makeText(getActivity(), "입력 양식이 부적절합니다.", Toast.LENGTH_SHORT).show();
                                    else if(resultJson.getStatus() == 500)
                                        Toast.makeText(getActivity(), "서버 문제로 신청 할 수 없습니다.\n 잠시 후 다시 시도하십시오", Toast.LENGTH_SHORT).show();
                                    else if(resultJson.getStatus() == 412)
                                        Toast.makeText(getActivity(), "날짜가 유효하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    else if(resultJson.getStatus() == 409)
                                        Toast.makeText(getActivity(), "이미 신청하엿습니다.", Toast.LENGTH_SHORT).show();
                                    else if(resultJson.getStatus() == 200){
                                        Toast.makeText(getActivity(), "외출/외박이 신청되엇습니다.", Toast.LENGTH_SHORT).show();
                                        Log.i("response",resultJson.toString());
                                        AddListData(resultJson, leaveType);
                                        dialog.dismiss();
                                    }else{
                                        Toast.makeText(getActivity(), "뭔가..뭔가 잘못됨..", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).execute(uri+leaveType, json.toString(), Token);
                        }else{
                            Toast.makeText(getActivity(), "날짜와 이유를 반드시 기입해 주십시오.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


// region overrideMethods
    // TODO: Rename and change types and number of parameters
    public static LeaveFragment newInstance(String param1, String param2) {
        LeaveFragment fragment = new LeaveFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (LeaveFragment.OnFragmentInteractionListener) context;
        } else {

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
// endregion

}

