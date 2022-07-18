package com.example.android.newsapp.domain.util;

public class ResultWrapper<T> {

    private final T value;
    private final String errorMessage;
    private final boolean errorPresent;

    public ResultWrapper(T value, boolean errorPresent, String errorMessage){
        this.value = value;
        this.errorPresent = errorPresent;
        this.errorMessage = errorMessage;
    }

    public T getValue() {
        return value;
    }

    public boolean getErrorPresent() {
        return errorPresent;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
