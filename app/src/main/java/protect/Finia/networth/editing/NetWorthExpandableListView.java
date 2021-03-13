package protect.Finia.networth.editing;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * A custom expandable list that is compatible with a multi-expandable lists.
 * The onMeasure method will measure the height of the expandable list when category is clicked.
 * This custom expandable list will enable an expandable list to be nested in a expandable list.
 * The length of the expandable list will be subject to the items to be displayed
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/03/21
 */
public class NetWorthExpandableListView extends ExpandableListView {

    public NetWorthExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
