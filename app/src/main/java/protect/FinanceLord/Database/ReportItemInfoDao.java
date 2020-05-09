package protect.FinanceLord.Database;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ReportItemInfoDao {

    @Query("SELECT \n" +
            "  assetsValue.assetsId AS totalAssetsId, \n" +
            "  assetsValue.assetsValue AS totalAssetsValue, \n" +
            "  strftime('%Y-%m-%d', datetime(assetsValue.date/1000, 'unixepoch', 'localtime')) AS totalAssetsDate, \n" +
            "  liabilitiesValue.liabilitiesId AS totalLiabilitiesId, \n" +
            "  liabilitiesValue.liabilitiesValue AS totalLiabilitiesValue, \n" +
            "  strftime('%Y-%m-%d', datetime(liabilitiesValue.date/1000, 'unixepoch', 'localtime')) AS totalLiabilitiesDate, \n" +
            "  assetsValue.assetsValue - liabilitiesValue.liabilitiesValue AS netWorthValue \n " +
            "FROM assetsValue INNER JOIN liabilitiesValue \n" +
            "ON assetsValue.date = liabilitiesValue.date \n" +
            "WHERE assetsValue.assetsId = 35 AND liabilitiesValue.liabilitiesId = 14 \n" +
            "ORDER BY assetsValue.date DESC")
    List<ReportItemInfo> queryReportItemsInfo();
}
