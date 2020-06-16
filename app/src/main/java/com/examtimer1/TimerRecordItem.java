package com.examtimer1;

public class TimerRecordItem {
    private Long id;
//    private int code;
    private String title;
    private String date;
    private String time;

    public TimerRecordItem(){}

    public TimerRecordItem(Long id, String title, String time, String date){
        this.id=id;
//        this.code=code;
        this.title=title;
        this.date=date;
        this.time=time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
