package com.deep.englishdictionarymerged;

public class History {

    private String en_word;
    private String en_def;

    public History(String enword,String endef){
        this.en_word=enword;
        this.en_def=endef;
    }

    public String getEn_word(){
        return en_word;
    }

    public  String getEn_def(){
        return en_def;
    }
}
