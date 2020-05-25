package protect.FinanceLord;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import protect.FinanceLord.Communicators.CalendarDateBroadcast;
import protect.FinanceLord.Communicators.DateCommunicator;
import protect.FinanceLord.NetWorthReportEditingUtils.EditPagerAdapter;
import protect.FinanceLord.NetWorthReportEditingUtils.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthReportEditingUtils.Edit_LiabilitiesFragment;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;

public class NetWorthReportEditingActivity extends AppCompatActivity {

    Date currentTime;
    LinearLayout calendarButton;
    TextView timeDisplay;
    public DateCommunicator toEditAssetsFragmentCommunicator;
    public DateCommunicator toEditLiabilitiesFragmentCommunicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_report);
        this.calendarButton = findViewById(R.id.calendar_button);
        this.timeDisplay = findViewById(R.id.time_selected);

        Calendar calendar = new GregorianCalendar();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
        this.timeDisplay.setText(stringDate);
        resetView();
    }

    private void resetView(){
        ImageButton returnButton = findViewById(R.id.edit_report_return_button);
        TabLayout tabLayout = findViewById(R.id.edit_tab_layout);
        final ViewPager viewPager = findViewById(R.id.edit_view_pager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        final Edit_AssetsFragment assetsFragment = new Edit_AssetsFragment(currentTime);
        final Edit_LiabilitiesFragment liabilitiesFragment = new Edit_LiabilitiesFragment(currentTime);
        fragments.add(assetsFragment);
        fragments.add(liabilitiesFragment);

        EditPagerAdapter sectionsPagerAdapter = new EditPagerAdapter(this, getSupportFragmentManager(), fragments);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarDialog calendarDialog = new CalendarDialog(calendarDialogCommunicator);
                FragmentManager fragmentManager = getSupportFragmentManager();
                Log.d("EditReportPassing", "time is " + currentTime);
                calendarDialog.show(fragmentManager, "DateTimePicker");
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void onDialogMessage(Date date) {
            currentTime = date;
            Log.d("EditReportCommunicator", "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            timeDisplay.setText(stringDate);

            toEditAssetsFragmentCommunicator.onActivityMessage(currentTime);
            toEditLiabilitiesFragmentCommunicator.onActivityMessage(currentTime);
        }
    };
}
