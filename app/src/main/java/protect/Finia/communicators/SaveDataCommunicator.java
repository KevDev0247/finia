package protect.Finia.communicators;

/**
 * The communicator to communicate the message to save data.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the database helper that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * created on 2020/05/20
 */
public interface SaveDataCommunicator {
    void message();
}
