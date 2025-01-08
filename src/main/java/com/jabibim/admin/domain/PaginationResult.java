package com.jabibim.admin.domain;

public class PaginationResult {
    private int maxpage;
    private int startpage;
    private int endpage;

    public int getMaxpage() {
        return maxpage;
    }

    public void setMaxpage(int maxpage) {
        this.maxpage = maxpage;
    }

    public int getStartpage() {
        return startpage;
    }

    public void setStartpage(int startpage) {
        this.startpage = startpage;
    }

    public int getEndpage() {
        return endpage;
    }

    public void setEndpage(int endpage) {
        this.endpage = endpage;
    }

    public PaginationResult(int page, int limit, int listcout) {

        // 총 페이지 수
        int maxpage = (listcout + limit - 1) / limit;

        // 현재 페이지에 보여줄 시작 페이지 수(1, 11, 21 등...)
        int startpage = ((page - 1) / 10) * 10 + 1;

        // 현재 페이지에 보여줄 마지막 페이지 수(10, 20, 30 등...)
        int endpage = startpage + 10 - 1;

        if(endpage > maxpage)
            endpage = maxpage;

        this.maxpage = maxpage;
        this.startpage = startpage;
        this.endpage = endpage;
    }

}
