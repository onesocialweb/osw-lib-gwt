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
package org.onesocialweb.gwt.service.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryCache {

	private Map<String, List<QueryCallback>> queries = new HashMap<String, List<QueryCallback>>();

	public void addQuery(String key) {
		queries.put(key, new ArrayList<QueryCallback>());
	}

	public void removeQuery(String key) {
		queries.remove(key);
	}

	public boolean hasQuery(String key) {
		return queries.containsKey(key);
	}

	public void addCallback(String key, QueryCallback callback) {
		if (queries.containsKey(key)) {
			queries.get(key).add(callback);
		}
	}

	public void removeCallback(String key, QueryCallback callback) {
		if (queries.containsKey(key)) {
			queries.get(key).remove(callback);
		}
	}

	public void success(String key, Object value) {
		if (queries.containsKey(key)) {
			List<QueryCallback> callbacks = queries.get(key);
			for (QueryCallback cacheCallback : callbacks) {
				cacheCallback.onResult(value);
			}
			queries.remove(key);
		}
	}

	public void failure(String key) {
		if (queries.containsKey(key)) {
			List<QueryCallback> callbacks = queries.get(key);
			for (QueryCallback cacheCallback : callbacks) {
				cacheCallback.onFailure();
			}
			queries.remove(key);
		}
	}

	public void clear() {
		queries.clear();
	}

}
