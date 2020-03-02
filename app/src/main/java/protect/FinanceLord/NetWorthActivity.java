package protect.FinanceLord;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.os.Bundle;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabLayout;

import protect.FinanceLord.ui.main.PastReportsAdapter;

public class NetWorthActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth);

        PastReportsAdapter pastReportsAdapter = new PastReportsAdapter(this);

    }
}
