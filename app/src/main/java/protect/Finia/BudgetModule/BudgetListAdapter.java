package protect.Finia.BudgetModule;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import protect.Finia.Database.BudgetsType;
import protect.Finia.R;

/**
 * The list adapter to deliver the data of each budget to the UI.
 * The BudgetListAdapter are used by both expenses and revenues.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class BudgetListAdapter extends ArrayAdapter<BudgetInfo> {

    private Context context;
    private List<BudgetsType> budgetsTypes;

    public BudgetListAdapter(@NonNull Context context, List<BudgetInfo> financialRecords, List<BudgetsType> budgetsTypes) {
        super(context, 0, financialRecords);
        this.context = context;
        this.budgetsTypes = budgetsTypes;
    }

    /**
     * Create and return the view for each item in the list.
     * The method first retrieve the data source of the current item, which contains the information to be displayed.
     * Then the method set the content of the view if it is not initialized.
     * Next, all the widgets on the UI are initialized.
     * Lastly, the data is displayed on to the widgets.
     *
     * @param position the position of the current item in the list
     * @param convertView the view class of this item
     * @param parent the ViewGroup of this list.
     * @return the view of the current transaction item.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BudgetInfo budgetInfo = getItem(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.date_format), Locale.CANADA);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.budgets_item, null, false);
        }

        TextView budgetName = convertView.findViewById(R.id.budget_item_name);
        TextView budgetStartDate = convertView.findViewById(R.id.budget_start_date);
        TextView budgetEndDate = convertView.findViewById(R.id.budget_end_date);
        TextView budgetUsage = convertView.findViewById(R.id.budget_item_usage);
        TextView budgetTotal = convertView.findViewById(R.id.budget_item_total);
        ProgressBar budgetProgress = convertView.findViewById(R.id.budget_progress_bar);

        budgetProgress.setMax(100);
        budgetProgress.setProgress(-Math.round(budgetInfo.totalUsage/ budgetInfo.budgetTotal * 100));
        if (-budgetInfo.totalUsage > budgetInfo.budgetTotal) {
            budgetProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));
        }

        budgetTotal.setText(String.valueOf(budgetInfo.budgetTotal));
        budgetStartDate.setText(dateFormat.format(new Date(budgetInfo.dateStart)));
        budgetEndDate.setText(dateFormat.format(new Date(budgetInfo.dateEnd)));
        if (!String.valueOf(budgetInfo.totalUsage).isEmpty()) {
            budgetUsage.setText(String.valueOf(-budgetInfo.totalUsage));
        } else {
            budgetUsage.setText(-(0));
        }
        for (BudgetsType budgetsType : budgetsTypes) {
            if (budgetsType.getBudgetsCategoryId() == budgetInfo.budgetCategoryId) {
                budgetName.setText(budgetsType.getBudgetsName());
            }
        }

        return convertView;
    }

    /**
     * Refresh the budget types from the other entity.
     */
    public void refreshBudgetTypes(List<BudgetsType> newBudgetsTypes) {
        this.budgetsTypes = newBudgetsTypes;
    }
}
