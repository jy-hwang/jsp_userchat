package board;

public class BoardDTO {

  private int boardNo;

  private String userId;

  private String boardTitle;

  private String boardContent;

  private int boardHit;

  private String boardFile;

  private String boardRealFile;

  private int boardGroup;

  private int boardSequence;

  private int boardLevel;

  private int boardAvailable;
  
  private String createdDate;

  private String updatedDate;

  public int getBoardNo() {
    return boardNo;
  }

  public void setBoardNo(int boardNo) {
    this.boardNo = boardNo;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getBoardTitle() {
    return boardTitle;
  }

  public void setBoardTitle(String boardTitle) {
    this.boardTitle = boardTitle;
  }

  public String getBoardContent() {
    return boardContent;
  }

  public void setBoardContent(String boardContent) {
    this.boardContent = boardContent;
  }

  public int getBoardHit() {
    return boardHit;
  }

  public void setBoardHit(int boardHit) {
    this.boardHit = boardHit;
  }

  public String getBoardFile() {
    return boardFile;
  }

  public void setBoardFile(String boardFile) {
    this.boardFile = boardFile;
  }

  public String getBoardRealFile() {
    return boardRealFile;
  }

  public void setBoardRealFile(String boardRealFile) {
    this.boardRealFile = boardRealFile;
  }

  public int getBoardGroup() {
    return boardGroup;
  }

  public void setBoardGroup(int boardGroup) {
    this.boardGroup = boardGroup;
  }

  public int getBoardSequence() {
    return boardSequence;
  }

  public void setBoardSequence(int boardSequence) {
    this.boardSequence = boardSequence;
  }

  public int getBoardLevel() {
    return boardLevel;
  }

  public void setBoardLevel(int boardLevel) {
    this.boardLevel = boardLevel;
  }

  public int getBoardAvailable() {
    return boardAvailable;
  }

  public void setBoardAvailable(int boardAvailable) {
    this.boardAvailable = boardAvailable;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

}
