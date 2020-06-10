package protect.FinanceLord.BudgetUtils;

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

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.R;

public class BudgetListAdapter extends ArrayAdapter<BudgetInfo> {

    private Context context;
    private List<BudgetsType> budgetsTypes;

    private String TAG = "BudgetListAdapter";

    public BudgetListAdapter(@NonNull Context context, List<BudgetInfo> financialRecords, List<BudgetsType> budgetsTypes) {
        super(context, 0, financialRecords);
        this.context = context;
        this.budgetsTypes = budgetsTypes;
    }

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

    public void refreshBudgetTypes(List<BudgetsType> newBudgetsTypes) {
        this.budgetsTypes = newBudgetsTypes;
    }
}
