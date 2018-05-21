package kr.hs.dgsw.ahnt3.CustomListView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class ListViewAdapter  extends BaseAdapter{
    private Context mContent = null;
    private ArrayList<ListData>  mListData = new ArrayList<ListData>();
    public ListViewAdapter(Context mContent){
        super();
        this.mContent = mContent;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        return null;
    }
}
