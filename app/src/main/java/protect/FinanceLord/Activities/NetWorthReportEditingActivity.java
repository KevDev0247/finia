package protect.FinanceLord.Activities;

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
import protect.FinanceLord.NetWorthReportEditing.EditPagerAdapter;
import protect.FinanceLord.NetWorthReportEditing.Edit_AssetsFragment;
import protect.FinanceLord.NetWorthReportEditing.Edit_LiabilitiesFragment;
import protect.FinanceLord.R;
import protect.FinanceLord.TimeUtils.CalendarDialog;
import protect.FinanceLord.TimeUtils.TimeProcessor;

/**
 * The activity to edit or add a new report of net worth.
 * It contains two fragments for assets and liabilities input sheets.
 * The user will be able to submit two sheets separately.
 * The data of assets and liabilities are saved separately in different entities in the database.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NetWorthReportEditingActivity extends AppCompatActivity {

    Date currentTime;
    LinearLayout calendarButton;
    TextView timeDisplay;
    public DateCommunicator toEditAssetsFragmentCommunicator;
    public DateCommunicator toEditLiabilitiesFragmentCommunicator;

    /**
     * Create and initialize the activity.
     * This method was called when the activity was created.
     * The method will first set the view of the content by finding the corresponding layout file through id.
     * Then, it will initialize the date to the current time and set up the calendar picker button and the return button.
     * Lastly, it will call the methods to set up the two tabs for assets and liabilities input sheets.
     *
     * @param savedInstanceState A mapping from String keys to various Parcelable values.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_worth_edit_report);
        this.calendarButton = findViewById(R.id.calendar_button);
        this.timeDisplay = findViewById(R.id.time_selected);

        ImageButton returnButton = findViewById(R.id.edit_report_return_button);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        currentTime = calendar.getTime();

        String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
        this.timeDisplay.setText(stringDate);

        setUpTabs();
    }

    /**
     * Set up the tabs for assets and liabilities input sheets
     * First initialize the TabLayout, each fragment, as well the ViewPager for the the layout.
     * Then, set up ViewPager by adding an adapter sectionsPagerAdapter as well as TabLayout by adding the ViewPager
     * Lastly, set onClickListener for the calendar button to show the datePicker dialog when the button is clicked.
     */
    private void setUpTabs() {
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
    }

    /**
     * Communicator between this activity and CalendarDialog.
     * Method message is implemented by the activity and the object is passed to CalendarDialog.
     * CalendarDialog will call message method to transfer the data to this activity.
     * Then, the time picked by the user will be displayed on the calendar button and sent to each fragment
     * through two other communicators.
     */
    CalendarDateBroadcast calendarDialogCommunicator = new CalendarDateBroadcast() {
        @Override
        public void message(Date date) {
            currentTime = date;
            Log.d("EditReportCommunicator", "time is " + currentTime);
            String stringDate = TimeProcessor.getStringFromDate(currentTime, getString(R.string.date_format));
            timeDisplay.setText(stringDate);

            toEditAssetsFragmentCommunicator.message(currentTime);
            toEditLiabilitiesFragmentCommunicator.message(currentTime);
        }
    };
}
