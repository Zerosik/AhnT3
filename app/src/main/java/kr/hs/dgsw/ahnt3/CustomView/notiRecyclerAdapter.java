package kr.hs.dgsw.ahnt3.CustomView;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import kr.hs.dgsw.ahnt3.R;

public class notiRecyclerAdapter extends RecyclerView.Adapter<notiRecyclerAdapter.ViewHolder>{
    private ArrayList<notiDataList> mDataset;
    public notiRecyclerAdapter(ArrayList<notiDataList> dataLists){
        mDataset = dataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.noti_listview, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final notiRecyclerAdapter.ViewHolder holder, final int position) {
        holder.mWriter.setText("작성자 : "+mDataset.get(position).writer);
        if(mDataset.get(position).content.length() >= 20) {
            holder.mTitle.setText(mDataset.get(position).content.replace("\\n", " ").substring(0, 20)+"...");
        }else{
            holder.mTitle.setText(mDataset.get(position).content);
        }
        if(mDataset.get(position).writeDate.equals(mDataset.get(position).modifyDate)){
            holder.mUptime.setText(mDataset.get(position).writeDate);
        }else{
            holder.mUptime.setText(mDataset.get(position).modifyDate+"(수정됨)");
        }
        holder.mView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                final AlertDialog dialog;
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(view.getRootView().getContext());
                LayoutInflater li = (LayoutInflater)view.getRootView().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = li.inflate(R.layout.dialog_notification, null);
                mBuilder.setView(mView);
                dialog = mBuilder.create();
                dialog.show();
                final TextView writer = mView.findViewById(R.id.notiDetailWriter);
                final TextView writeTime = mView.findViewById(R.id.notiDetailUptime);
                final TextView filename = mView.findViewById(R.id.notiDetailFileName);
                final TextView content = mView.findViewById(R.id.notiDetailTitle);
                JSONArray files = mDataset.get(position).NoticeFile;
                try{
                    writer.setText(mDataset.get(position).writer);
                    writeTime.setText(mDataset.get(position).writeDate);
                    content.setText(mDataset.get(position).content);
                    filename.setText(files.getJSONObject(0).getString("upload_name"));
                }catch (JSONException e){

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mWriter;
        private TextView mUptime;
        private LinearLayout mView;
        public ViewHolder(View view) {
            super(view);
            mTitle = view.findViewById(R.id.notiTitle);
            mWriter = view.findViewById(R.id.notiWriter);
            mView = view.findViewById(R.id.notiView);
            mUptime = view.findViewById(R.id.notiUptime);
        }
    }
}
