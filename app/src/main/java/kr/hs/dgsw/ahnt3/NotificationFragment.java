package kr.hs.dgsw.ahnt3;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import kr.hs.dgsw.ahnt3.CustomView.notiDataList;
import kr.hs.dgsw.ahnt3.CustomView.notiRecyclerAdapter;
import kr.hs.dgsw.ahnt3.Networks.AsyncResponse;
import kr.hs.dgsw.ahnt3.Networks.HttpAsyncTask;
import kr.hs.dgsw.ahnt3.Util.DBHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    String uri = "http://flow.cafe24app.com/";
    String Token;
    DBHelper dbHelper;

    private RecyclerView mRecyclerView;
    private notiRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<notiDataList> mData;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void InitNotiData(){

        Token = dbHelper.getDbToken();
        new HttpAsyncTask(new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.e("asdf", "asdf");
                    JSONObject json;
                    int code;
                    try{
                        json = new JSONObject(output);
                        code = json.getInt("status");
                        if(code == 200) {
                            JSONObject data = json.getJSONObject("data");
                            JSONArray array = data.getJSONArray("list");
                            for(int i = 0; i < array.length(); i++) {
                                String wDate = array.getJSONObject(i).getString("write_date").replace("T", " ").substring(0, 16);
                                String mDate = array.getJSONObject(i).getString("modify_date").replace("T", " ").substring(0, 16);
                                mData.add(new notiDataList(
                                        array.getJSONObject(i).getInt("idx"),
                                        array.getJSONObject(i).getString("content"),
                                        array.getJSONObject(i).getString("writer"),
                                        wDate,
                                        mDate,
                                        array.getJSONObject(i).getJSONArray("notice_files")
                                ));

                            }
                        }
                        mAdapter.notifyItemInserted(mData.size() - 1);
                    }catch(JSONException e){
                        Log.i("asd","망한 부분이고욘\n"+output);
                    }

                }
            }).execute(uri+"notice", null, Token);
        }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mData = new ArrayList<>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.notiListView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mAdapter = new notiRecyclerAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        InitNotiData();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //Toast.makeText(context, "Notification Fragment Attached", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


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
