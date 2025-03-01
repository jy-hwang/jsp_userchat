package chat;

public class ChatDTO {
  private int chatNo;

  private String fromId;

  private String toId;

  private String chatContent;

  private String createdDate;

  public int getChatNo() {
    return chatNo;
  }

  public void setChatNo(int chatNo) {
    this.chatNo = chatNo;
  }

  public String getFromId() {
    return fromId;
  }

  public void setFromId(String fromId) {
    this.fromId = fromId;
  }

  public String getToId() {
    return toId;
  }

  public void setToId(String toId) {
    this.toId = toId;
  }

  public String getChatContent() {
    return chatContent;
  }

  public void setChatContent(String chatContent) {
    this.chatContent = chatContent;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

}
