/*
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */


function initRss(portletns, selectedRssItemTitle) {
    
  var ppid = "#p_p_id"+portletns;

  // Excerpt all items, show excerpt and hide content/links
  jQuery(ppid+" .news-excerpt").excerpt({lines: '${portletPreferencesValues.numberOfExcerptRows[0]}', end: '...'});
  jQuery(ppid+" .news-excerpt").show;
  jQuery(ppid+" .news-content").hide;

  // Define function handling sort/group by souce
  jQuery(ppid+" #group-by-source").click(function() {
    updateSorting("${groupBySourceResource}");
    return false;
  });

  // Define function handling sort by date
  jQuery(ppid+" #sort-by-date").click(function() {
    updateSorting("${sortByDateResource}");
    return false;
  });

  // Define function handling click on title and "read more"/"read less"
  jQuery(ppid+" .read-more, "+ppid+" .read-less, "+ppid+" .news-title").click(function() {
    var li = jQuery(this).parents("li");
    li.find(".news-excerpt").toggle("medium");
    li.find(".news-content").toggle("medium");

    jQuery(this).parents("li").toggleClass("active");
  
    return false;
  });

  // Lastly on document ready: 
  if (selectedRssItemTitle != null && selectedRssItemTitle != "") {
    // If we got a pre-selected item title, try to expand that news item
    // We also have an anchor for the list item, so it should be scrolled automatically
    var titleHref = jQuery(ppid+" .news-title[@href="+selectedRssItemTitle+"]");
    titleHref.click();
  }
}
  
function updateSorting(sortingUrl, portletns) {

  var ppid = "#p_p_id"+portletns; 
    
  //Display "loading" block until unblocked, but max 10 seconds
  jQuery(ppid + " #blockMe").block({ 
    message: jQuery(ppid + " #blockDisplayMessage"),
    centerY: 0,
    centerX: 0,
    overlayCSS: {backgroundColor: "#EFEFEF" },
    fadeIn: 500, 
    fadeOut: 500, 
    timeout: 10000
  });
      
  jQuery.ajax({
    url: sortingUrl,
    success: function(data) {
      jQuery(ppid+" #blockMe #rss-item-container").html(data);
      //Hide "loading" block
      jQuery(ppid + " #blockMe").unblock();
     }
  });
}
