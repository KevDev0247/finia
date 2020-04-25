package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.Date;

import protect.FinanceLord.NetWorthEditReportsUtils.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.Edit_LiabilitiesFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.SectionsPagerAdapter;

public class NetWorthEditReportActivity extends AppCompatActivity {

    Date currentTime = new Date();
    Button btnCalendar;

    Communicator communicator = new Communicator() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_report);

        String search = getIntent().getStringExtra(SearchManager.QUERY);
        resetView(search);
    }

    private void resetView(String search){
        TabLayout tabLayout = findViewById(R.id.edit_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_view_pager);

        this.btnCalendar = findViewById(R.id.btnCalendar);
        this.btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDialog calendarDialog = new CalendarDialog(communicator);
                calendarDialog.loadData();
            }
        });

        ArrayList<Fragment> fragments = new ArrayList<>();
        Edit_AssetsFragment assetsFragment = new Edit_AssetsFragment("Assets");
        Edit_LiabilitiesFragment liabilitiesFragment = new Edit_LiabilitiesFragment("Liabilities");
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    interface Communicator {
        void onDialogMessage(Date date);
    }
}