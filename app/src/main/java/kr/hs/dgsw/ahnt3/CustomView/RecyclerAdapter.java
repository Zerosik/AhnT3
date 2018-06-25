package kr.hs.dgsw.ahnt3.CustomView;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.hs.dgsw.ahnt3.R;

import static kr.hs.dgsw.ahnt3.R.drawable.checkmark_xxl;
import static kr.hs.dgsw.ahnt3.R.drawable.x_mark_xxl;

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
        String type;
        if(mDataset.get(position).type == "out")
            type = "외출";
        else
            type = "외박";
        holder.mReason.setText(mDataset.get(position).Reason);
        holder.mDate.setText(mDataset.get(position).Date + " " + type);
        if(mDataset.get(position).status == false){
            holder.mStatus.setImageResource(x_mark_xxl);
        }else{
            holder.mStatus.setImageResource(checkmark_xxl);
        }
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
        private ImageView mStatus;

        public ViewHolder(View view) {
            super(view);
            mDate = view.findViewById(R.id.LeaveDate);
            mReason = view.findViewById(R.id.LeaveReason);
            mStatus = view.findViewById(R.id.LeaveStatueImage);
        }
    }
}
