package com.examtimer1;

public class TimerRecordItem_tt {
    private Long id;
    //    private int code;
    private String title;
    private String date;
    private String time_Korean, time_math, time_English, time_history, time_science1, time_science2, time_foreignLanguage;

    public TimerRecordItem_tt(){}

    public TimerRecordItem_tt(Long id, String title, String date, String time_Korean, String time_math, String time_English, String time_history, String time_science1, String time_science2, String time_foreignLanguage) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.time_Korean = time_Korean;
        this.time_math = time_math;
        this.time_English = time_English;
        this.time_history = time_history;
        this.time_science1 = time_science1;
        this.time_science2 = time_science2;
        this.time_foreignLanguage = time_foreignLanguage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
//
//    public String getTime_Korean() {
//        return time_Korean;
//    }
//
//    public void setTime_Korean(String time_Korean) {
//        this.time_Korean = time_Korean;
//    }
//
//    public String getTime_math() {
//        return time_math;
//    }
//
//    public void setTime_math(String time_math) {
//        this.time_math = time_math;
//    }
//
//    public String getTime_English() {
//        return time_English;
//    }
//
//    public void setTime_English(String time_English) {
//        this.time_English = time_English;
//    }
//
//    public String getTime_history() {
//        return time_history;
//    }
//
//    public void setTime_history(String time_history) {
//        this.time_history = time_history;
//    }
//
//    public String getTime_science1() {
//        return time_science1;
//    }
//
//    public void setTime_science1(String time_science1) {
//        this.time_science1 = time_science1;
//    }
//
//    public String getTime_science2() {
//        return time_science2;
//    }
//
//    public void setTime_science2(String time_science2) {
//        this.time_science2 = time_science2;
//    }
//
//    public String getTime_foreignLanguage() {
//        return time_foreignLanguage;
//    }
//
//    public void setTime_foreignLanguage(String time_foreignLanguage) {
//        this.time_foreignLanguage = time_foreignLanguage;
//    }
}

