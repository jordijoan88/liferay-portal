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

package com.liferay.taglib.portletext;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletContainerUtil;
import com.liferay.portal.kernel.portlet.RestrictPortletServletRequest;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.PipingServletResponse;
import com.liferay.portal.kernel.servlet.taglib.portletext.RuntimePortletIDs;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.PortletPreferencesFactoryUtil;

import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Brian Wing Shun Chan
 */
public class RuntimeTag extends TagSupport {

	public static void doTag(
			String portletName, PageContext pageContext,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		doTag(portletName, null, pageContext, request, response);
	}

	public static void doTag(
			String portletName, String queryString, PageContext pageContext,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		doTag(portletName, queryString, null, pageContext, request, response);
	}

	public static void doTag(
			String portletName, String queryString, String defaultPreferences,
			PageContext pageContext, HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {

		if (pageContext != null) {
			response = new PipingServletResponse(
				response, pageContext.getOut());
		}

		String portletId = portletName;

		RestrictPortletServletRequest restrictPortletServletRequest =
			new RestrictPortletServletRequest(request);

		request = DynamicServletRequest.addQueryString(
			restrictPortletServletRequest, queryString);

		try {
			request.setAttribute(WebKeys.RENDER_PORTLET_RESOURCE, Boolean.TRUE);

			if (Validator.isNotNull(defaultPreferences)) {
				PortletPreferencesFactoryUtil.getPortletSetup(
					request, portletId, defaultPreferences);
			}

			ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
				WebKeys.THEME_DISPLAY);

			Portlet portlet = getPortlet(
				themeDisplay.getCompanyId(), portletId);

			PortletContainerUtil.render(request, response, portlet);

			RuntimePortletIDs runtimePortletIDs =
				(RuntimePortletIDs)request.getAttribute(
					WebKeys.RUNTIME_PORTLET_IDS);

			if (runtimePortletIDs == null) {
				runtimePortletIDs = new RuntimePortletIDs();

				request.setAttribute(
					WebKeys.RUNTIME_PORTLET_IDS, runtimePortletIDs);
			}

			runtimePortletIDs.addRuntimePortletID(portletName);
		}
		finally {
			restrictPortletServletRequest.mergeSharedAttributes();
		}
	}

	public static Set<String> getRuntimePortletIDs(
		ServletRequest servletRequest) {

		RuntimePortletIDs runtimePortletIDs =
			(RuntimePortletIDs)servletRequest.getAttribute(
				WebKeys.RUNTIME_PORTLET_IDS);

		if (runtimePortletIDs == null) {
			return null;
		}

		return runtimePortletIDs.getRuntimePortletIDs();
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			HttpServletRequest request =
				(HttpServletRequest)pageContext.getRequest();

			Layout layout = (Layout)request.getAttribute(WebKeys.LAYOUT);

			if (layout == null) {
				return EVAL_PAGE;
			}

			HttpServletResponse response =
				(HttpServletResponse)pageContext.getResponse();

			doTag(
				_portletName, _queryString, _defaultPreferences, pageContext,
				request, response);

			return EVAL_PAGE;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new JspException(e);
		}
	}

	public void setDefaultPreferences(String defaultPreferences) {
		_defaultPreferences = defaultPreferences;
	}

	public void setPortletName(String portletName) {
		_portletName = portletName;
	}

	public void setQueryString(String queryString) {
		_queryString = queryString;
	}

	/**
	 * @see com.liferay.portal.model.impl.LayoutTypePortletImpl#getStaticPortlets(
	 *      String)
	 */
	protected static Portlet getPortlet(long companyId, String portletId)
		throws Exception {

		Portlet portlet = PortletLocalServiceUtil.getPortletById(
			companyId, portletId);

		// See LayoutTypePortletImpl#getStaticPortlets for why we only clone
		// non-instanceable portlets

		if (!portlet.isInstanceable()) {
			portlet = (Portlet)portlet.clone();
		}

		portlet.setStatic(true);

		return portlet;
	}

	private static Log _log = LogFactoryUtil.getLog(RuntimeTag.class);

	private String _defaultPreferences;
	private String _portletName;
	private String _queryString;

}