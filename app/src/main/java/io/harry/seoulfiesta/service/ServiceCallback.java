package io.harry.seoulfiesta.service;

public interface ServiceCallback<T> {
    void onSuccess(T object);
    void onFailure(String errorMessage);
}
