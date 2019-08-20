package connectivity;

import java.io.OutputStream;

public class Message {
    private String message;
    private OutputStream out;

    public Message(String message, OutputStream out){
        this.message = message;
        this.out = out;
    }

    public String getMessage() {
        return message;
    }

    public OutputStream getOut() {
        return out;
    }
}
