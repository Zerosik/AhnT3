package kr.hs.dgsw.ahnt3.CustomView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
