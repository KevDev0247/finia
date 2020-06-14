package protect.FinanceLord.TransactionAdding;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

import protect.FinanceLord.R;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to
 * one of the tabs.
 */
public class EditPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.expenses_name, R.string.revenues_name};
    private final Context mContext;
    private ArrayList<Fragment> fragments;

    public EditPagerAdapter(Context context, FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        mContext = context;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
