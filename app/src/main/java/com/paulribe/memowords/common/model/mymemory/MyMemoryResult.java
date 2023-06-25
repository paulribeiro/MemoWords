package com.paulribe.memowords.common.model.mymemory;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyMemoryResult {

    @SerializedName("responseData")
    private ResponseData lang;

    @SerializedName("quotaFinished")
    private Boolean quotaFinished;

    @SerializedName("mtLangSupported")
    private String mtLangSupported;

    @SerializedName("responseDetails")
    private String responseDetails;

    @SerializedName("responseStatus")
    private String responseStatus;

    @SerializedName("responderId")
    private String responderId;

    @SerializedName("exception_code")
    private String exceptionCode;

    @SerializedName("matches")
    private List<Match> matches;

    public MyMemoryResult() {
        // default constructor.
    }

    public ResponseData getLang() {
        return lang;
    }

    public void setLang(ResponseData lang) {
        this.lang = lang;
    }

    public Boolean getQuotaFinished() {
        return quotaFinished;
    }

    public void setQuotaFinished(Boolean quotaFinished) {
        this.quotaFinished = quotaFinished;
    }

    public String getMtLangSupported() {
        return mtLangSupported;
    }

    public void setMtLangSupported(String mtLangSupported) {
        this.mtLangSupported = mtLangSupported;
    }

    public String getResponseDetails() {
        return responseDetails;
    }

    public void setResponseDetails(String responseDetails) {
        this.responseDetails = responseDetails;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }

    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
