package se.vgregion.portal.rss.client.intra.displaytemplates;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import se.vgregion.portal.rss.client.beans.FeedEntryBean;

import com.liferay.portal.kernel.template.TemplateVariableGroup;
import com.liferay.portal.kernel.portletdisplaytemplate.BasePortletDisplayTemplateHandler;
import com.liferay.portlet.display.template.PortletDisplayTemplateConstants;

public class RssPortletDisplayTemplateHandler extends BasePortletDisplayTemplateHandler {

	@Override
	public String getClassName() {
		return FeedEntryBean.class.getName();
	}

	@Override
	public String getName(Locale arg0) {
		return "VGR Intra Rss Client";
	}

	@Override
	public String getResourceName() {
		return "IntraRssClient_WAR_rssclientportlet";
	}

	@Override
	public Map<String, TemplateVariableGroup> getTemplateVariableGroups(
			long classPK, String language, Locale locale)
		throws Exception {

		Map<String, TemplateVariableGroup> templateVariableGroups =
			super.getTemplateVariableGroups(classPK, language, locale);

		TemplateVariableGroup templateVariableGroup =
			templateVariableGroups.get("fields");

		templateVariableGroup.empty();

		templateVariableGroup.addCollectionVariable(
			"entries", List.class, PortletDisplayTemplateConstants.ENTRIES,
			"entry", FeedEntryBean.class, "curEntry", "title");

		templateVariableGroup.addVariable("feedTitle", String.class, "feedTitle");

		return templateVariableGroups;
	}


}
