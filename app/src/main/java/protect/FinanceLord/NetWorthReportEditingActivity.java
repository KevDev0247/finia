package protect.FinanceLord;

import android.app.SearchManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import protect.FinanceLord.Communicators.ActivityToFragment;
import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.NetWorthReportEditingUtils.CalendarDialog;
import protect.FinanceLord.NetWorthReportEditingUtils.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthReportEditingUtils.Edit_LiabilitiesFragment;
import protect.FinanceLord.NetWorthReportEditingUtils.NetWorthTimeUtils;
import protect.FinanceLord.NetWorthReportEditingUtils.SectionsPagerAdapter;

public class NetWorthReportEditingActivity extends AppCompatActivity {

    Date currentTime;
    Button calendarButton;
    public ActivityToFragment toEditAssetsFragmentCommunicator;
    public ActivityToFragment toEditLiabilitiesFragmentCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_report);
        this.calendarButton = findViewById(R.id.btnCalendar);

        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, getString(R.string.date_format));
        String search = getIntent().getStringExtra(SearchManager.QUERY);
        this.calendarButton.setText(stringDate);
        resetView(search);
    }

    private void resetView(String search){
        TabLayout tabLayout = findViewById(R.id.edit_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        final Edit_AssetsFragment assetsFragment = new Edit_AssetsFragment(getString(R.string.assets_name), currentTime);
        final Edit_LiabilitiesFragment liabilitiesFragment = new Edit_LiabilitiesFragment(getString(R.string.liabilities_name), currentTime);
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        this.calendarButton.setOnClickListener(new View.OnClickListener() {
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

    CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d("EditReportCommunicator", "time is " + currentTime);
            String stringDate = NetWorthTimeUtils.getStringFromDate(currentTime, getString(R.string.date_format));
            calendarButton.setText(stringDate);

            toEditAssetsFragmentCommunicator.onActivityMessage(currentTime);
            toEditLiabilitiesFragmentCommunicator.onActivityMessage(currentTime);
        }
    };
}
