package com.paulribe.memowords.common.model.pons;

import com.google.gson.annotations.SerializedName;

public class Translation {

    @SerializedName("source")
    private String source;
    @SerializedName("target")
    private String target;

    public Translation(String source, String target) {
        this.source = source;
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
