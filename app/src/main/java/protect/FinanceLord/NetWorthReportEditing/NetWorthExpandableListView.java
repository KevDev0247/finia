package protect.FinanceLord.NetWorthReportEditing;

import android.content.Context;
import android.widget.ExpandableListView;


public class NetWorthExpandableListView extends ExpandableListView {
    public NetWorthExpandableListView(Context context){
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
