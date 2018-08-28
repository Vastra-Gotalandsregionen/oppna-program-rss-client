package se.vgregion.portal.rss.client.intra.action;

import java.util.Map;

import com.liferay.portal.kernel.model.PortletPreferencesWrapper;
import com.liferay.portal.kernel.portlet.DefaultConfigurationAction;
import com.liferay.portal.kernel.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portal.kernel.template.TemplateHandler;
import com.liferay.portal.kernel.template.TemplateHandlerRegistryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.SessionClicks;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;

/**
 * @author Erik Andersson
 */
public class ConfigurationActionImpl extends DefaultConfigurationAction {

	// public void include(
	// 		PortletConfig portletConfig, HttpServletRequest request,
	// 		HttpServletResponse response)
	// 	throws Exception;

	@Override
	public void include(
			PortletConfig portletConfig, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {

//		httpServletRequest.setAttribute(
//				ExampleConfiguration.class.getName(),
//				_exampleConfiguration);


		long scopeGroupId = PortalUtil.getScopeGroupId(httpServletRequest);

		PortletPreferences portletPreferences = getPreferences(httpServletRequest);

		String displayStyle = GetterUtil.getString(portletPreferences.getValue("displayStyle", ""));
		long displayStyleGroupId = GetterUtil.getLong(portletPreferences.getValue("displayStyleGroupId", null), scopeGroupId);

		String rssFeedtitle1 = GetterUtil.getString(portletPreferences.getValue("rssFeedtitle1", ""));
		String rssFeedLink1 = GetterUtil.getString(portletPreferences.getValue("rssFeedLink1", ""));
		int numberOfItems1 = GetterUtil.getInteger(portletPreferences.getValue("numberOfItems1", "5"));

		TemplateHandler templateHandler = TemplateHandlerRegistryUtil.getTemplateHandler(FeedEntryBean.class.getName());
		long templateHandlerClassNameId = PortalUtil.getClassNameId(templateHandler.getClassName());

		String refreshUrl = PortalUtil.getCurrentURL(httpServletRequest);

		httpServletRequest.setAttribute("displayStyle", displayStyle);
		httpServletRequest.setAttribute("displayStyleGroupId", displayStyleGroupId);
		httpServletRequest.setAttribute("refreshUrl", refreshUrl);
		httpServletRequest.setAttribute("numberOfItems1", numberOfItems1);
		httpServletRequest.setAttribute("rssFeedtitle1", rssFeedtitle1);
		httpServletRequest.setAttribute("rssFeedLink1", rssFeedLink1);
		httpServletRequest.setAttribute("templateHandlerClassName", templateHandler.getClassName());



		super.include(portletConfig, httpServletRequest, httpServletResponse);
	}


//	@Override
//	public String include(
//			PortletConfig portletConfig, RenderRequest renderRequest,
//			RenderResponse renderResponse)
//		throws Exception {
//
//		System.out.println("Custom Configuration Action - ConfigurationActionImpl - render");
//
//		ThemeDisplay themeDisplay = (ThemeDisplay)renderRequest.getAttribute(
//				WebKeys.THEME_DISPLAY);
//
//		long scopeGroupId = themeDisplay.getScopeGroupId();
//
//		PortletPreferences portletPreferences = renderRequest.getPreferences();
//
//		String displayStyle = GetterUtil.getString(portletPreferences.getValue("displayStyle", ""));
//		long displayStyleGroupId = GetterUtil.getLong(portletPreferences.getValue("displayStyleGroupId", null), scopeGroupId);
//
//		String rssFeedtitle1 = GetterUtil.getString(portletPreferences.getValue("rssFeedtitle1", ""));
//		String rssFeedLink1 = GetterUtil.getString(portletPreferences.getValue("rssFeedLink1", ""));
//		int numberOfItems1 = GetterUtil.getInteger(portletPreferences.getValue("numberOfItems1", "5"));
//
//		TemplateHandler templateHandler = TemplateHandlerRegistryUtil.getTemplateHandler(FeedEntryBean.class.getName());
//		long templateHandlerClassNameId = PortalUtil.getClassNameId(templateHandler.getClassName());
//
//		String refreshUrl = PortalUtil.getCurrentURL(renderRequest);
//
//		renderRequest.setAttribute("displayStyle", displayStyle);
//		renderRequest.setAttribute("displayStyleGroupId", displayStyleGroupId);
//		renderRequest.setAttribute("refreshUrl", refreshUrl);
//		renderRequest.setAttribute("numberOfItems1", numberOfItems1);
//		renderRequest.setAttribute("rssFeedtitle1", rssFeedtitle1);
//		renderRequest.setAttribute("rssFeedLink1", rssFeedLink1);
//		renderRequest.setAttribute("templateHandlerClassNameId", templateHandlerClassNameId);
//
//		return super.render(portletConfig, renderRequest, renderResponse);
//	}

	@Override
	public void processAction(
			PortletConfig portletConfig, ActionRequest actionRequest,
			ActionResponse actionResponse)
		throws Exception {

		updateRssSettings(actionRequest, actionResponse);

		super.processAction(portletConfig, actionRequest, actionResponse);
	}

	protected void updateRssSettings(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		PortletPreferences portletPreferences = actionRequest.getPreferences();

		System.out.println("Updated RSS settings");

		//String displayStyle = ParamUtil.getString(actionRequest, "preferences--displayStyle--");
		//portletPreferences.setValue("displayStyle", displayStyle);
		//portletPreferences.store();

		/*
		int defaultDuration = ParamUtil.getInteger(
			actionRequest, "defaultDuration");
		boolean isoTimeFormat = ParamUtil.getBoolean(
			actionRequest, "isoTimeFormat");
		String timeZoneId = ParamUtil.getString(actionRequest, "timeZoneId");
		boolean usePortalTimeZone = ParamUtil.getBoolean(
			actionRequest, "usePortalTimeZone");
		int weekStartsOn = ParamUtil.getInteger(actionRequest, "weekStartsOn");

		portletPreferences.setValue(
			"defaultDuration", String.valueOf(defaultDuration));
		portletPreferences.setValue("defaultView", defaultView);
		portletPreferences.setValue(
			"isoTimeFormat", String.valueOf(isoTimeFormat));
		portletPreferences.setValue("timeZoneId", timeZoneId);
		portletPreferences.setValue(
			"usePortalTimeZone", String.valueOf(usePortalTimeZone));
		portletPreferences.setValue(
			"weekStartsOn", String.valueOf(weekStartsOn));

		HttpServletRequest httpServletRequest =
			PortalUtil.getHttpServletRequest(actionRequest);

		SessionClicks.put(
			httpServletRequest, "calendar-portlet-default-view", defaultView);

		portletPreferences.store();
		*/
	}
	
	private PortletPreferences getPreferences(HttpServletRequest request) {
        PortletRequest portletRequest = (PortletRequest)request.getAttribute(
            JavaConstants.JAVAX_PORTLET_REQUEST);

        PortletPreferences portletPreferences = null;

        if (portletRequest != null) {
            portletPreferences = portletRequest.getPreferences();
        }

        return portletPreferences;
}

}
