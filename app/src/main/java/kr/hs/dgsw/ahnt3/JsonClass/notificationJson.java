package kr.hs.dgsw.ahnt3.JsonClass;

public class notificationJson{
    int status;
    String message;
    notiData data;

}
class notiData{
    int idx;
    String content;
    String writer;
    String writeDate;
    String modifyDate;
    NoticeFile noticeFile;
}
class NoticeFile{
    int idx;
    String uploadName;
    String uploadDir;
    int noticeIdx;

}