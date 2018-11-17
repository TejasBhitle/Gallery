package com.tejasbhitle.gallery.enums;

public enum Sort {
    DATE_ASCEND("DATE_ASCEND"),
    DATE_DESCEND("DATE_DESCEND"),
    NAME_ASCEND("NAME_ASCEND"),
    NAME_DESCEND("NAME_DESCEND");

    private String sort;
    Sort(String sort){
        this.sort = sort;
    }

    @Override
    public String toString() {
        return sort;
    }

    public static Sort getSort(String sort){
        if(sort.equals(DATE_ASCEND.toString()))
            return DATE_ASCEND;
        if(sort.equals(DATE_DESCEND.toString()))
            return DATE_DESCEND;
        if(sort.equals(NAME_ASCEND.toString()))
            return NAME_ASCEND;
        if(sort.equals(NAME_DESCEND.toString()))
            return NAME_DESCEND;
        return NAME_ASCEND;
    }
}
