package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.widget.ExpandableListView;

import protect.FinanceLord.MainActivity;

public class SecondLevelExpandableListView extends ExpandableListView {
    public SecondLevelExpandableListView(Context context){
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
