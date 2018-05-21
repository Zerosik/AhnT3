package kr.hs.dgsw.ahnt3.CustomListView;

import android.graphics.drawable.Drawable;

import java.text.Collator;
import java.util.Comparator;

class ListData {
    public Drawable mIcon;
    public String mTItle;
    public String mDate;
    public static final Comparator<ListData> ALPHA_COMPARATOR = new Comparator<ListData>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(ListData o1, ListData o2) {
            return sCollator.compare(o1.mTItle, o2.mTItle);
        }
    };
}
