package protect.FinanceLord.ui.NetWorthEditReports;

import android.content.Context;
import android.widget.ExpandableListView;


public class AssetsExpandableListView extends ExpandableListView {
    public AssetsExpandableListView(Context context){
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
