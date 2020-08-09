package protect.Finia.Communicators;

/**
 * The communicator to communicate the message to save data.
 * This interface must be implemented by the activity receiving the message
 * and the method message can only be called in the database helper that
 * belongs to this activity.
 *
 * @author Owner  Kevin Zhijun Wang
 * @version 2020.0609
 */
public interface SaveDataCommunicator {
    void message();
}
