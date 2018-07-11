package io.coldfish.lettv.model;

import java.util.List;

public class Response {
    private int page;
    private List<TVShow> results;
    private int total_results;
    private int total_pages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<TVShow> getResults() {
        return results;
    }

    public void setResults(List<TVShow> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("page=").append(page);
        sb.append(", results=").append(results);
        sb.append(", total_results=").append(total_results);
        sb.append(", total_pages=").append(total_pages);
        sb.append('}');
        return sb.toString();
    }
}
