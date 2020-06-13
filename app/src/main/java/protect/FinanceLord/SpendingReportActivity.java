package protect.FinanceLord;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

import protect.FinanceLord.Database.BudgetsType;
import protect.FinanceLord.SpendingUtils.GroupedSpending;

public class SpendingReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_report);
        List<GroupedSpending> groupedSpendingList = getIntent().getParcelableArrayListExtra(getString(R.string.spending_monthly_list_key));
        List<BudgetsType> allBudgetsTypes = getIntent().getParcelableArrayListExtra(getString(R.string.spending_categories));

        ImageButton returnButton = findViewById(R.id.spending_report_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView pageTitle = findViewById(R.id.spending_report_page_title);
        pageTitle.setText(getIntent().getExtras().getString(getString(R.string.spending_report_month_key)));

        AnyChartView spendingChartView = findViewById(R.id.spending_chart);

        setUpPieChart(spendingChartView, allBudgetsTypes, groupedSpendingList, getIntent().getExtras().getString(getString(R.string.spending_report_month_key)));
    }

    private void setUpPieChart(AnyChartView spendingChartView, List<BudgetsType> allBudgetsTypes, List<GroupedSpending> groupedSpendingList, String month) {
        Pie spendingChart = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        List<GroupedSpending> currentMonthSpending = new ArrayList<>();

        for (GroupedSpending item : groupedSpendingList) {
            if (item.month.equals(month)) {
                currentMonthSpending.add(item);
            }
        }

        for (GroupedSpending item : currentMonthSpending) {
            for (BudgetsType budgetsType : allBudgetsTypes) {
                if (budgetsType.getBudgetsCategoryId() == item.categoryId) {
                    dataEntries.add(new ValueDataEntry(budgetsType.getBudgetsName(), item.categoryTotal));
                }
            }
        }

        spendingChart.data(dataEntries);
        spendingChartView.setChart(spendingChart);
    }
}
