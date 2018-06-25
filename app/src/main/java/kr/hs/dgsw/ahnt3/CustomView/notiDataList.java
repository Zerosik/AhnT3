package kr.hs.dgsw.ahnt3.CustomView;

import org.json.JSONArray;

public class notiDataList {
    int idx;
    String content;
    String writer;
    String writeDate;
    String modifyDate;
    JSONArray NoticeFile;

    public notiDataList(int idx, String content, String writer, String writeDate, String modifyDate, JSONArray noticeFile) {
        this.idx = idx;
        this.content = content;
        this.writer = writer;
        this.writeDate = writeDate;
        this.modifyDate = modifyDate;
        NoticeFile = noticeFile;
    }
}
