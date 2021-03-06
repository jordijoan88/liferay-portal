/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portalweb.portal.dbupgrade.sampledata6010.bookmarks.bookmarksentry;

import com.liferay.portalweb.portal.BaseTestCase;
import com.liferay.portalweb.portal.util.RuntimeVariables;

/**
 * @author Brian Wing Shun Chan
 */
public class ViewBookmarksFolderEntryTest extends BaseTestCase {
	public void testViewBookmarksFolderEntry() throws Exception {
		selenium.open("/web/guest/home/");

		for (int second = 0;; second++) {
			if (second >= 90) {
				fail("timeout");
			}

			try {
				if (selenium.isElementPresent("link=Control Panel")) {
					break;
				}
			}
			catch (Exception e) {
			}

			Thread.sleep(1000);
		}

		selenium.clickAt("link=Control Panel",
			RuntimeVariables.replace("Control Panel"));
		selenium.waitForPageToLoad("30000");
		selenium.clickAt("link=Communities",
			RuntimeVariables.replace("Communities"));
		selenium.waitForPageToLoad("30000");
		selenium.type("_134_name",
			RuntimeVariables.replace("Bookmarks Entry Community"));
		selenium.clickAt("//input[@value='Search']",
			RuntimeVariables.replace("Search"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Open"),
			selenium.getText("//td[2]/a"));
		selenium.clickAt("//td[2]/a", RuntimeVariables.replace("Open"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Bookmarks Folder Name"),
			selenium.getText("//a/strong"));
		selenium.clickAt("//a/strong",
			RuntimeVariables.replace("Bookmarks Folder Name"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("Bookmarks Entry Name"),
			selenium.getText("//td[1]/a"));
		assertEquals(RuntimeVariables.replace("http://www.liferay.com"),
			selenium.getText("//td[2]/a"));
		selenium.clickAt("//td[1]/a",
			RuntimeVariables.replace("Bookmarks Entry Name"));
		selenium.waitForPageToLoad("30000");
		assertEquals(RuntimeVariables.replace("http://www.liferay.com"),
			selenium.getText("//div[@class='lfr-asset-url']/a"));
		selenium.clickAt("//div[@class='lfr-asset-url']/a",
			RuntimeVariables.replace("http://www.liferay.com"));
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isElementPresent("//img[@alt='Liferay']"));
	}
}