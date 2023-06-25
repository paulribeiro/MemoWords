package com.paulribe.memowords.common.model.mymemory;

import com.google.gson.annotations.SerializedName;

public class Match {

    @SerializedName("id")
    private Integer id;

    @SerializedName("segment")
    private String segment;

    @SerializedName("translation")
    private String translation;

    @SerializedName("source")
    private String source;

    @SerializedName("target")
    private String target;

    @SerializedName("quality")
    private Integer quality;

    @SerializedName("reference")
    private String reference;

    @SerializedName("usage-count")
    private Integer usageCount;

    @SerializedName("model")
    private String model;

    public Match() {
    }

    public Match(Integer id, String segment, String translation, String source, String target, Integer quality, Integer usageCount) {
        this.id = id;
        this.segment = segment;
        this.translation = translation;
        this.source = source;
        this.target = target;
        this.quality = quality;
        this.usageCount = usageCount;
        this.model = "";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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

    public Integer getQuality() {
        return quality;
    }

    public void setQuality(Integer quality) {
        this.quality = quality;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
