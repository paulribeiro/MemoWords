package com.paulribe.memowords.common.model.pons;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchWordResultList {

    @SerializedName("type")
    private String type;
    @SerializedName("opendict")
    private Boolean opendict;
    @SerializedName("roms")
    private List<SearchWordResult> roms;

    public SearchWordResultList(String type, List<SearchWordResult> roms) {
        this.type = type;
        this.roms = roms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SearchWordResult> getRoms() {
        return roms;
    }

    public void setRoms(List<SearchWordResult> roms) {
        this.roms = roms;
    }

    public Boolean getOpendict() {
        return opendict;
    }

    public void setOpendict(Boolean opendict) {
        this.opendict = opendict;
    }
}
