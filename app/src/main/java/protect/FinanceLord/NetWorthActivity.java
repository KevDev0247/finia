package protect.FinanceLord;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import protect.FinanceLord.ui.main.PastReportsAdapter;

public class NetWorthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth);

        PastReportsAdapter pastReportsAdapter = new PastReportsAdapter(this);

        Button completeReportButton = findViewById(R.id.CompleteReport);
        completeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(NetWorthActivity.this, NetWorthEditReportsActivity.class);
                startActivity(intent);
            }
        });
    }
}
