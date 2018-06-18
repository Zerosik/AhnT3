package kr.hs.dgsw.ahnt3.CustomView;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.hs.dgsw.ahnt3.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<DataList> mDataset;
    public RecyclerAdapter(ArrayList<DataList> dataLists){
        mDataset = dataLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_listview, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mReason.setText(mDataset.get(position).Reason);
        holder.mDate.setText(mDataset.get(position).Date);
        holder.mDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mDate;
        private TextView mReason;

        public ViewHolder(View view) {
            super(view);
            mDate = view.findViewById(R.id.LeaveDate);
            mReason = view.findViewById(R.id.LeaveReason);
        }
    }
}
