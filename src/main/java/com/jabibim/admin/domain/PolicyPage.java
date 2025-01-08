package com.jabibim.admin.domain;

public class PolicyPage {
    private int maxpage;
    private int startpage;
    private int endpage;

    public PolicyPage(int page, int limit, int listcount ) {

        //총 페이지수
        int maxpage = (listcount + limit -1) /limit;

        // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등 ...)
        int startpage = ((page-1) /10) * 10 +1;

        // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등 ...)
        int endpage = startpage + 10 -1;

        if( endpage > maxpage)
            endpage = maxpage;

        this.maxpage = maxpage;
        this.endpage = endpage;
        this.startpage = startpage;
    }

    public int getMaxpage() {
        return maxpage;
    }

    public int getStartpage() {
        return startpage;
    }

    public int getEndpage() {
        return endpage;
    }

}
