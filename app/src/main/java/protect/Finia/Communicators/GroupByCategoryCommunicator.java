package protect.Finia.Communicators;

/**
 * The communicator to communicate the filter the user clicked.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the adapter that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public interface GroupByCategoryCommunicator {
    void message(String categoryLabel);
}
