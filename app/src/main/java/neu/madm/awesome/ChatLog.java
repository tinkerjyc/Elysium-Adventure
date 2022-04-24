package neu.madm.awesome;

public class ChatLog {

    private String content;

    public ChatLog(){
        this.content = "new content";
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return content;
    }

    @Override
    public String toString() {
        return String.format("Content: %s", content);
    }

}
