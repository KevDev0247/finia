package protect.FinanceLord;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.charts.Pie;

public class SpendingReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spending_report);

        AnyChartView spendingChartView = findViewById(R.id.spending_chart);

        setUpPieChart(spendingChartView);
    }

    private void setUpPieChart(AnyChartView spendingChartView) {
        Pie spendingChart = AnyChart.pie();
    }
}
