package com.example.a20220731_rohitkadam_nycschools;

public enum Constants {
    BASE_URL("https://data.cityofnewyork.us/Education/DOE-High-SchoolDirectory-2017/s3k6-pzi2"),
    WEBVIEW_URL("webViewUrl");

    public final String value;

    Constants(String value) {
        this.value = value;
    }
}
