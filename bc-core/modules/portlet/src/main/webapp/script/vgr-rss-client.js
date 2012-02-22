AUI().add('vgr-rss-client',function(A) {
	var Lang = A.Lang,
		isArray = Lang.isArray,
		isFunction = Lang.isFunction,
		isNull = Lang.isNull,
		isObject = Lang.isObject,
		isString = Lang.isString,
		isUndefined = Lang.isUndefined,
		getClassName = A.ClassNameManager.getClassName,
		concat = function() {
			return Array.prototype.slice.call(arguments).join(SPACE);
		},
		
		CONTENT_BOX = 'contentBox',
		CONTENT_NODE = 'contentNode',
		
		NAME = 'vgr-rss-client',
		NS = 'vgr-rss-client',
		
		TABS_BOUNDING_BOX = 'tabsBoundingBox',
		TABS_LIST_NODE = 'tabsListNode',
		TABS_CONTENT_NODE = 'tabsContentNode'		
	;

	var VgrRssClient = A.Component.create(
			{
				ATTRS: {
					tabsBoundingBox: {
						setter: A.one
					},
					tabsListNode: {
						setter: A.one
					},
					tabsContentNode: {
						setter: A.one
					},
				},
				EXTENDS: A.Component,
				NAME: NAME,
				NS: NS,
				prototype: {
					tabs: null,
					initializer: function(config) {
						var instance = this;
						
						//instance.initConsole();
					},
					
					renderUI: function() {
						var instance = this;
						
						instance.initRssTabs();
					},
	
					bindUI: function() {
						var instance = this;
					},
					
					syncUI: function() {
						var instance = this;
					},

					initConsole: function() {
						var instance = this;
						
					    new A.Console({
					        height: '250px',
					        newestOnTop: true,
					        style: 'block',
					        visible: true,
					        width: '600px'
					    }).render();			
					},
					
					initRssTabs: function() {
						var instance = this;
						
						instance.tabs = new A.TabView({
							boundingBox: instance.get(TABS_BOUNDING_BOX),
							listNode: instance.get(TABS_LIST_NODE),
							contentNode: instance.get(TABS_CONTENT_NODE),
							cssClass: 'vgr-tabs'
						});
						
						instance.tabs.after('activeTabChange', instance._afterActiveTabChange, instance);

						instance.tabs.render();
						
						// Plug loading mask to bounding box
						instance.get(TABS_BOUNDING_BOX).plug(A.LoadingMask, { background: '#000' });
					},
					
					_afterActiveTabChange: function(e) {
						var instance = this;
						
						var tab = instance.tabs.get('activeTab');
						var tabIndex = instance.tabs.getTabIndex(tab);
						
						var tabContentNode = tab.get(CONTENT_NODE);
						var tabContentHtml = tabContentNode.html();
						
						var isEmptyContent = (tabContentHtml == '');
						
						// Do not continue if content is already loaded
						if(!isEmptyContent) {
							return;
						}
						
						var tabContentBox = tab.get(CONTENT_BOX);
						var tabLinkNode = tabContentBox.one('a');
						var tabUrl = tabLinkNode.getAttribute('href');
						
						tabUrl = tabUrl.replace('p_p_state=normal', 'p_p_state=exclusive');
						
						var tabIO = A.io.request(tabUrl, {
							autoLoad : false,
							method : 'GET'
						});
						
						tabIO.on('success', instance._onUpdateSuccess, instance, {tabContentNode: tabContentNode});
						
						instance.get(TABS_BOUNDING_BOX).loadingmask.show();
						
						tabIO.start();
					},
					
					_onUpdateSuccess: function(e, id, xhr, attr) {
						var instance = this;
						
						var tabContentNode = attr.tabContentNode;
						
						var data = xhr.responseText;

						var tempNode = A.Node.create(data);
						
						var contentNode = instance.get(TABS_CONTENT_NODE);
						var contentNodeId = contentNode.getAttribute('id');
						
						var content = tempNode.one('#' + contentNodeId).html();
						tempNode.remove();
						
						tabContentNode.html(content);
						
						instance.get(TABS_BOUNDING_BOX).loadingmask.hide();
					}
					
					
				}
			}
	);

	A.VgrRssClient = VgrRssClient;
		
	},1, {
		requires: [
			'aui-base',
			'aui-io-request',
			'aui-loading-mask',
			'aui-tabs',
			'console',
			'substitute'
      ]
	}
);