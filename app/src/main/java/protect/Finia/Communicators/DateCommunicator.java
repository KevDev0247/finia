package protect.Finia.Communicators;

import java.util.Date;

/**
 * The communicator to communicate date between activity and fragments.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the fragment that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/04/28
 */
public interface DateCommunicator {
    void message(Date date);
}
