package protect.FinanceLord.NetWorthReportViewing;

import android.content.Context;
import android.widget.ListView;

public class NetWorthListView extends ListView {
    public NetWorthListView(Context context) {
        super(context);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
