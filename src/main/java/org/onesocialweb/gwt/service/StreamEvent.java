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
package org.onesocialweb.gwt.service;

import java.util.List;

import org.onesocialweb.gwt.util.Event;

public abstract class StreamEvent<T> implements Event {

	public enum Type {
		added, removed, cleared, refreshed, replied
	};

	private final List<T> items;

	private final Type type;

	public StreamEvent(Type type, List<T> items) {
		this.type = type;
		this.items = items;
	}
	

	public List<T> getItems() {
		return this.items;
	}

	public Type getType() {
		return type;
	}
}
