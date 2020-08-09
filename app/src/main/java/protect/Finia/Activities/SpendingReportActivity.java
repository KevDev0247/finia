package protect.Finia.Activities;

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

import protect.Finia.Database.BudgetsType;
import protect.Finia.R;
import protect.Finia.SpendingModule.GroupedSpending;

/**
 * The activity that allow the user to view the spending report.
 * Each report contains a pie chart that display the distribution of spending in the current month.
 * The user will be able to view the value of the category and its percentage in the total spending.
 * The activity use a AnyChart library to implement the pie chart.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class SpendingReportActivity extends AppCompatActivity {

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize return button and set the page title to the date the user selected.
     * Lastly, the method will call the method to set up the pie chart.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
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

        setUpPieChart(allBudgetsTypes, groupedSpendingList, getIntent().getExtras().getString(getString(R.string.spending_report_month_key)));
    }

    /**
     * Set up the pie chart with the AnyChart library.
     * The DataEntry class is used to store and format the data to generate the chart.
     * First, the groupedSpendingList is traversed and the current month spending is retrieved.
     * Then, the data stored in the current month spending is added into the data entries.
     * Lastly, the data entry is added into the spendingChart to generate the chart
     *
     * @param allBudgetsTypes all budget types stored in the database.
     * @param groupedSpendingList the class that store the more detailed data used to generate the report.
     * @param month the month that the user selected.
     */
    private void setUpPieChart(List<BudgetsType> allBudgetsTypes, List<GroupedSpending> groupedSpendingList, String month) {
        AnyChartView spendingChartView = findViewById(R.id.spending_chart);
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
        spendingChart.legend().position("top");
        spendingChartView.setChart(spendingChart);
    }
}
