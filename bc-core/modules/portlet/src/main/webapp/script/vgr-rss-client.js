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
		
		NAME = 'labs-search-results',
		NS = 'labs-search-results',
		
		NODE_ID_RESULTS_CONTAINER = 'nodeIdResultsContainer',
		NODE_ID_GROUP_BY_SOURCE = 'nodeIdGroupBySource',
		NODE_ID_SORT_BY_DATE = 'nodeIdSortByDate',
		
		SELECTED_RSS_ITEM_TITLE = 'selectedRssItemTitle',
		URL_GROUP_BY_SOURCE = 'urlGroupBySource',
		URL_SORT_BY_DATE = 'urlSortByDate',
		PORTLET_NAMESPACE = 'portletNamespace',
		PPID = 'ppid'
	;

	var VgrRssClient = A.Component.create(
			{
				ATTRS: {
					nodeIdResultsContainer: {
						value: 'rss-item-container'
					},
					nodeIdGroupBySource: {
						value: 'group-by-source'
					},
					nodeIdSortByDate: {
						value: 'sort-by-date'
					},
					portletNamespace: {
						value: ''
					},
					ppid: {
						value: ''
					},
					selectedRssItemTitle: {
						value: ''
					},
					urlGroupBySource: {
						value: ''
					},
					urlSortByDate: {
						value: ''
					}
				},
				EXTENDS: A.Component,
				NAME: NAME,
				NS: NS,
				prototype: {
					io: null,
					nodeGroupBySource: null,
					nodeSortByDate: null,
					nodeResults: null,
					initializer: function(config) {
						var instance = this;
						
						var ppid = '#p_p_id' + instance.get(PORTLET_NAMESPACE);
						instance.set(PPID, ppid);
					},
					
					renderUI: function() {
						var instance = this;
						
						instance._setupRssContainer();
					},
	
					bindUI: function() {
						var instance = this;
					},
					
					syncUI: function() {
						var instance = this;
						
						var updateURL = instance.get(URL_SORT_BY_DATE);
						instance._updateRssItems(updateURL);
					},
					
					_handleSortClick: function(e) {
						var instance = this;
						e.halt();
						
						var curTarget = e.currentTarget;
						var curId = curTarget.attr('id');
						
						var updateURL = '';
						
						if(curId == instance.get(NODE_ID_GROUP_BY_SOURCE)) {
							updateURL = instance.get(URL_GROUP_BY_SOURCE);
						}
						else if(curId == instance.get(NODE_ID_SORT_BY_DATE)) {
							updateURL = instance.get(URL_SORT_BY_DATE);
						}
						
						if(updateURL != '') {
							instance._updateRssItems(updateURL);
						}
					},
					
					_handleUpdateSuccess: function(e, id, xhr) {
						var instance = this;
						
						e.halt();
						
						var data = xhr.responseText;

						//instance.nodeResults.html(data);
						
						var nodeResultsParent = instance.nodeResults.ancestor();
						nodeResultsParent.html(data);
						
						instance._rebindRssContainer();
					},
					
					_handleReadToggleClick: function(e) {
						var instance = this;
						
						e.halt();
						
						var curTarget = e.currentTarget;
						
						var listItem = curTarget.ancestors('li.news-item').item(0);
						var newsContent = listItem.one('.news-content');
						var newsExcerpt = listItem.one('.news-excerpt');
						
						var newsContentFx = newsContent.get('fx');
						newsContentFx.detach('start');
						newsContentFx.detach('end');
						
						if(listItem.hasClass('active')) {
							newsContentFx.on('end', function(event) {
								listItem.removeClass('active');
								newsExcerpt.show();
							});
							
							newsContent.slideUp(0.5);
						}
						else {
							newsContentFx.on('start', function(event) {
								listItem.addClass('active');
								newsExcerpt.hide();
							});
						
							newsContent.slideDown(0.5);
						}
					},
					
					_rebindRssContainer: function() {
						var instance = this;
						
						var newsContentEls = instance.nodeResults.all('.news-content');
						newsContentEls.setStyles({
							display: 'block',
							height: 0,
							overflow: 'hidden'
						});
						
						var sortLinks = instance.nodeResults.all('.sort-box a');
						sortLinks.detach('click');
						sortLinks.on('click', instance._handleSortClick, instance);
						
						var readMoreLinks = instance.nodeResults.all('.news-title', '.read-more');
						readMoreLinks.detach('click');
						readMoreLinks.on('click', instance._handleReadToggleClick, instance);
						
						var readLessLinks = instance.nodeResults.all('.read-less');
						readLessLinks.detach('click');
						readLessLinks.on('click', instance._handleReadToggleClick, instance);
					},
					
					_setupRssContainer: function() {
						var instance = this;
						
						var ppid = instance.get(PPID);
						
						instance.nodeResults = A.one(ppid + ' #' + instance.get(NODE_ID_RESULTS_CONTAINER));

						instance.nodeGroupBySource = A.one(ppid + ' #' + instance.get(NODE_ID_GROUP_BY_SOURCE));
						instance.nodeSortByDate = A.one(ppid + ' #' + instance.get(NODE_ID_SORT_BY_DATE));
						
						var updateURL = instance.get(URL_SORT_BY_DATE);
						
						// Create new IO request
						instance.io = A.io.request(updateURL, {
							autoLoad : false,
							cache: false,
							data: {},
							dataType: 'html',
							method : 'GET'
						});

						// Attach success handler to io request
						instance.io.on('success', instance._handleUpdateSuccess, instance);
					},
					
					_updateRssItems: function(updateURL) {
						var instance = this;

						instance.io.set('uri', updateURL);
						
						// Stop ongoing io requests
						instance.io.stop();
						
						// Start new io request
			    		instance.io.start();
					}
					
				}
			}
	);

	A.VgrRssClient = VgrRssClient;
		
	},1, {
		requires: [
			'aui-base',
			'aui-io-request',
	    	'anim',
			'aui-component',
			'aui-node-fx'
      ]
	}
);