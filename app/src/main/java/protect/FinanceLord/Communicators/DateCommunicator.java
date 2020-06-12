package protect.FinanceLord.Communicators;

import java.util.Date;

/**
 * The communicator to communicate date between activity and fragments.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the fragment that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public interface DateCommunicator {
    void message(Date date);
}
