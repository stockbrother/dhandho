package cc.dhandho.jetty;

@Deprecated //use raw JSON
public class Message {

    private String id;

    private String handler;

    private long timestamp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public String toString() {
        return "id:" + id + ",timestamp:" + timestamp;
    }
}
