package protect.FinanceLord.SpendingUtils;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupedSpending implements Parcelable {
    public int categoryId;
    public float categoryTotal;
    public String month;

    public GroupedSpending() { }

    private GroupedSpending(Parcel in) {
        categoryId = in.readInt();
        categoryTotal = in.readFloat();
        month = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(categoryId);
        dest.writeFloat(categoryTotal);
        dest.writeString(month);
    }

    public static final Creator<GroupedSpending> CREATOR = new Creator<GroupedSpending>() {
        @Override
        public GroupedSpending createFromParcel(Parcel in) {
            return new GroupedSpending(in);
        }

        @Override
        public GroupedSpending[] newArray(int size) {
            return new GroupedSpending[size];
        }
    };
}
