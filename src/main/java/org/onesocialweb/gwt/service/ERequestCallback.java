package org.onesocialweb.gwt.service;

public interface ERequestCallback<T> {

	public void onSuccess(T result);

	public void onFailure(T result);
}
