package kr.hs.dgsw.ahnt3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import kr.hs.dgsw.ahnt3.CustomListView.ListViewAdapter;
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
    private OnFragmentInteractionListener mListener;
    private Button applicationButton;
    private ListView mListView = null;
    private ListViewAdapter mListViewAdapter = null;


    public LeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        dbHelper = new DBHelper(getActivity());
        applicationButton = (Button)getView().findViewById(R.id.applicationButton);
        Token = dbHelper.getDbToken();
        InitApplicationButon();
        ListView listview = getView().findViewById(R.id.Leavelistview);
    }

    public void InitLeaveListView(){
        Cursor res = dbHelper.getLeaveList();
        if(res.moveToNext()){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void InitApplicationButon(){
        applicationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                View mView = getLayoutInflater().inflate(R.layout.dialog_application, null);
                final EditText startTime = (EditText)mView.findViewById(R.id.startDate);
                final EditText endTime = (EditText)mView.findViewById(R.id.endDate);
                final EditText ReasonText = (EditText)mView.findViewById(R.id.reason);
                Button Confirm = (Button) mView.findViewById(R.id.appliConfirm);

                Confirm.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if(!startTime.getText().toString().isEmpty() && !endTime.getText().toString().isEmpty() && !ReasonText.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(), "신청하였습니다.", Toast.LENGTH_SHORT).show();
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
                                        //TODO: 신청 성공 시, DB저장 및 refresh?
                                        Toast.makeText(getActivity(), "외출/외박이 신청되엇습니다.", Toast.LENGTH_SHORT).show();
                                        Log.i("response",resultJson.toString());
                                    }else{
                                        Toast.makeText(getActivity(), "뭔가..뭔가 잘못됨..", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }).execute(uri+"out/go", json.toString(), Token);
                        }else{
                            Toast.makeText(getActivity(), "날짜와 이유를 반드시 기입해 주십시오.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leave, container, false);

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
            mListener = (OnFragmentInteractionListener) context;
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
    private class VIewHolder{
        public ImageView mIcon;
        public TextView mText;
        public TextView mDate;
    }
}

