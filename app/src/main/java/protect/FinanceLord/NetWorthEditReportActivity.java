package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import protect.FinanceLord.NetWorthEditReportsUtils.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.Edit_LiabilitiesFragment;
import protect.FinanceLord.NetWorthEditReportsUtils.SectionsPagerAdapter;

public class NetWorthEditReportActivity extends AppCompatActivity {

    Date currentTime;
    Button btnCalendar;
    public ParentActivityCommunicator parentActivityCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_report);
        this.btnCalendar = findViewById(R.id.btnCalendar);

        Calendar calendar = new GregorianCalendar();
        /* 我们不需要吧时间清零啊，我们只需要获取今天的日期就行了，可能需要获取今天开始的一刻时间，那应该吧时分秒清零，而不是年月日
        calendar.set(Calendar.YEAR, 0);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 0);
        */
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, "yyyy-MM-dd");
        btnCalendar.setText(stringDate);
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        resetView(search);
    }

    private void resetView(String search){
        TabLayout tabLayout = findViewById(R.id.edit_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        final Edit_AssetsFragment assetsFragment = new Edit_AssetsFragment("Assets", currentTime);
        final Edit_LiabilitiesFragment liabilitiesFragment = new Edit_LiabilitiesFragment("Liabilities");
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        this.btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getSupportFragmentManager();
                Log.d("EditReportPassing", "time is " + currentTime);
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    CalendarDialogCommunicator calendarDialogCommunicator = new CalendarDialogCommunicator() {
        @Override
        public void onDialogMessage(Date date) {
            Log.d("EditReportCommunicator", "time is " + currentTime);
            String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, "yyyy-MM-dd");
            btnCalendar.setText(stringDate);
            parentActivityCommunicator.onActivityMessage(date);
        }
    };

    interface CalendarDialogCommunicator {
        void onDialogMessage(Date date);
    }
}