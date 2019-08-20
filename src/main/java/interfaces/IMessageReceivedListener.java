package interfaces;

import connectivity.Message;

public interface IMessageReceivedListener {
    void onReceiveMessage(Message message);
}
