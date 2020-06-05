package protect.FinanceLord.BudgetUtils;

import android.content.Context;
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
import protect.FinanceLord.Database.BudgetsValue;
import protect.FinanceLord.R;

public class BudgetListAdapter extends ArrayAdapter<BudgetsValue> {

    private Context context;
    private List<BudgetsType> budgetsTypes;

    public BudgetListAdapter(@NonNull Context context, List<BudgetsValue> budgetsValues, List<BudgetsType> budgetsTypes) {
        super(context, 0, budgetsValues);
        this.context = context;
        this.budgetsTypes = budgetsTypes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BudgetsValue budgetsValue = getItem(position);
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

        budgetStartDate.setText(dateFormat.format(new Date(budgetsValue.getDateStart())));
        budgetEndDate.setText(dateFormat.format(new Date(budgetsValue.getDateEnd())));
        budgetTotal.setText(String.valueOf(budgetsValue.getBudgetsValue()));
        for (BudgetsType budgetsType : budgetsTypes) {
            if (budgetsType.getBudgetsCategoryId() == budgetsValue.getBudgetsCategoryId()) {
                budgetName.setText(budgetsType.getBudgetsName());
            }
        }

        return convertView;
    }
}
