package protect.FinanceLord.NetWorthSwipeDashboard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import protect.FinanceLord.R;

/**
 * The pager adapter to deliver the data of net worth item to the UI dashboard cards.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public class NetWorthCardsAdapter extends PagerAdapter {

    private List<NetWorthCardsDataModel> dataModels;
    private Context context;

    public NetWorthCardsAdapter(List<NetWorthCardsDataModel> dataModels, Context context){
        this.dataModels = dataModels;
        this.context = context;
    }

    /**
     * Get the number of items.
     */
    @Override
    public int getCount() {
        Log.d("NetWorthCardsAdapter", "getCount()" + dataModels.size());
        return dataModels.size();
    }

    /**
     * Determine whether the view is from the object.
     *
     * @param view view of the item.
     * @param object The same object that was returned by instantiateItem(View, int).
     */
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    /**
     * Instantiate and load all the data to the item.
     * All the UI widgets are initialized first, and data is loaded onto each widget.
     *
     * @param container the container of the item
     * @param position the current position of the item
     */
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.net_worth_cards, container, false);

        ImageView imageView;
        TextView title, details;

        imageView = view.findViewById(R.id.net_worth_card_graphics);
        title = view.findViewById(R.id.cards_title);
        details = view.findViewById(R.id.details);

        imageView.setImageResource(dataModels.get(position).getImageId());
        title.setText(dataModels.get(position).getTitle());
        details.setText(dataModels.get(position).getDetails());

        container.addView(view, 0);

        Log.d("NetWorthCardsAdapter", "getView: " + view);
        return view;
    }

    /**
     * Remove a page for the given position.
     *
     * @param container the container of the item
     * @param position the current position of the item
     * @param object The same object that was returned by instantiateItem(View, int).
     */
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
