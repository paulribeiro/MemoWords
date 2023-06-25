package com.paulribe.memowords.common.restclient;

import org.jsoup.Jsoup;

public class HtmlHelper {

    private HtmlHelper() {

    }

    public static String html2text(String htmlText) {
        return Jsoup.parse(htmlText).text().trim();
    }

}
