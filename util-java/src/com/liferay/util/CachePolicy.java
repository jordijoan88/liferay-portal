/**
 * Copyright (c) 2000-2007 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.util;

import com.opensymphony.oscache.base.CacheEntry;
import com.opensymphony.oscache.base.EntryRefreshPolicy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="CachePolicy.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class CachePolicy implements EntryRefreshPolicy {

	public CachePolicy(long refresh) {
		_refresh = refresh;
	}

	public boolean needsRefresh(CacheEntry entry) {
		if (_refresh <= 0) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Cache is never stale for " + entry.getKey() +
						" because refresh less than or equal to 0");
			}

			return false;
		}

		long lastUpdate = entry.getLastUpdate();
		long now = System.currentTimeMillis();

		if (lastUpdate + _refresh < now) {
			if (_log.isDebugEnabled()) {
				_log.debug("Cache is stale for " + entry.getKey());
			}

			return true;
		}
		else {
			if (_log.isDebugEnabled()) {
				_log.debug("Cache is not stale for " + entry.getKey());
			}

			return false;
		}
	}

	private static Log _log = LogFactory.getLog(CachePolicy.class);

	private static long _refresh;

}