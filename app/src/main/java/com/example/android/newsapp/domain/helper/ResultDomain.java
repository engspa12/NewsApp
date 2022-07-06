package com.example.android.newsapp.domain.helper;

public class ResultDomain<T> {

    private final T value;
    private final String errorMessage;
    private final boolean errorPresent;

    public ResultDomain(T value, boolean errorPresent, String errorMessage){
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
