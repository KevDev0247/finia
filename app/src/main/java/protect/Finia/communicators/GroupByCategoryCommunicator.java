package protect.Finia.communicators;

/**
 * The communicator to communicate the filter the user clicked.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the adapter that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/05/29
 */
public interface GroupByCategoryCommunicator {
    void message(String categoryLabel);
}
