/*
 *  Copyright 2010 Vodafone Group Services Ltd.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *    
 */
package org.onesocialweb.gwt.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListModel<T> implements Iterable<T> {

	private final List<T> elements = new ArrayList<T>();

	private final List<ListEventHandler<T>> handlers = new ArrayList<ListEventHandler<T>>();

	public void add(T element) {
		if (elements.add(element)) {
			fireElementAddedEvent(element);
		}
	}

	public void remove(T element) {
		if (elements.remove(element)) {
			fireElementRemovedEvent(element);
		}
	}

	public void clear() {
		elements.clear();
		fireListClearedEvent();
	}

	public int size() {
		return elements.size();
	}

	public T get(int i) {
		return elements.get(i);
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}

	public void registerEventHandler(ListEventHandler<T> handler) {
		handlers.add(handler);
	}

	public void unregisterEventHandler(ListEventHandler<T> handler) {
		handlers.remove(handler);
	}

	protected void fireElementAddedEvent(T element) {
		for (ListEventHandler<T> handler : handlers) {
			handler.elementAdded(element);
		}
	}

	protected void fireElementRemovedEvent(T element) {
		for (ListEventHandler<T> handler : handlers) {
			handler.elementRemoved(element);
		}
	}

	protected void fireListClearedEvent() {
		for (ListEventHandler<T> handler : handlers) {
			handler.listCleared();
		}
	}

}
