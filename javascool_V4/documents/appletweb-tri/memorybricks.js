/*
 * @licstart
 * This program provides GUI elements to teach memory management to kids
 *
 * Copyright (C) 2011 Vincent Nivoliers (http://alice.loria.fr/~nivoliev)
 *
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see http://www.gnu.org/licenses/gpl.html.
 * @licend
*/

var MemoryBricks = new function () {
  var me = this ;
  var nbmove = 0 ;
  var nbbox = 0 ;
  var nbarrays = 0 ;

  var draggedObject = "" ;

  me.createBox = function() {
    var elem = document.createElement('div') ;
    elem.className = 'box' ;
    return elem ;
  }
  
  me.createArray = function () {
    var elem = document.createElement('div') ;
    elem.className = 'array' ;
    var adder = document.createElement('div') ;
    adder.className = 'add_box' ;
    adder.innerHTML = '+' ;
    elem.appendChild(adder) ;
    nbarrays ++ ;
    return elem ;
  }

  me.createCard = function() {
    var card = document.createElement('div') ;
    card.className = 'card' ;
    card.draggable = 'true' ;
    return card ;
  }

  me.init_layout = function() {
    nbbox = 0 ;
    nbmove = 0 ;
    nbarrays = 0 ;
    var memory = document.getElementById('memory') ;
    memory.innerHTML = "" ;
    var arr = me.createArray() ;
    arr.id = "array"+nbarrays+":0" ;
    memory.appendChild(arr) ;
    var adder = document.createElement('div') ;
    adder.className = 'add_array' ;
    adder.innerHTML = '+' ;
    memory.appendChild(adder) ;
    var trash = document.createElement('div') ;
    trash.className = 'trash' ;
    trash.id = 'trash' ;
    memory.appendChild(trash) ;
  }

  me.init_events = function() {
    var elems = document.getElementsByClassName('add_box') ;
    for( var i=0; i<elems.length; i++) {
      EventHelpers.addEvent(elems[i],'click',addBox) ;
    }
    elems = document.getElementsByClassName('add_array') ;
    for( var i=0; i<elems.length; i++) {
      EventHelpers.addEvent(elems[i],'click',addArray) ;
    }
    elems = document.getElementsByClassName('box') ;
    for( var i=0; i<elems.length; i++) {
      EventHelpers.addEvent(elems[i],'drop',dropCard) ;
      EventHelpers.addEvent(elems[i],'dragover',cardOver) ;
    }
    elems = document.getElementsByClassName('card') ;
    for( var i=0; i<elems.length; i++) {
      EventHelpers.addEvent(elems[i],'dragstart',dragCard) ;
    }
    updateCounters() ;
    EventHelpers.addEvent(document,'dragover',autoScroll) ;
    EventHelpers.addEvent(document,'keyup',toggle_trash) ;
  }

  function toggle_trash(e) {
    if(e.keyCode == 80) {
      var trash = document.getElementById('trash') ;
      if(!trash.style.display || trash.style.display == 'none') {
	trash.style.display = 'table' ;
      } else {
	trash.style.display = 'none' ;
      }
    }
  }

  function updateCounters() {
    var counter = document.getElementById('nbmove') ;
    counter.innerHTML = nbmove ;
    counter = document.getElementById('nbbox') ;
    counter.innerHTML = nbbox ;
  }

  function getWindowSize() {
    var size = new Object() ;
    size.x = window.innerWidth;
    size.y = window.innerHeight;
    return size ;
  }

  function getScrollState() {
    var off = new Object() ;
    off.x = window.pageXOffset ;
    off.y = window.pageYOffset ;
    return off ;
  }

  function getEventPos(e) {
    var pos = new Object() ;
    pos.x = e.layerX ;
    pos.y = e.layerY ;
    return pos
  }

  function autoScroll(e) {
    var wsize = getWindowSize() ;
    var pos = getEventPos(e)  ;
    var off = getScrollState() ;
    if(pos.x - off.x<30) {
      window.scrollBy(-10,0) ;
    }
    if(pos.y - off.y<30) {
      window.scrollBy(0,-10) ;
    }
    if(wsize.x-pos.x<30) {
      window.scrollBy(10,0) ;
    }
    if(wsize.y-pos.y<30) {
      window.scrollBy(0,10) ;
    }
  }

  function dragCard(e) {
    draggedObject = this.id ;
    e.dataTransfer.setData('Text',this.id) ;
  }

  function dropCard(e) {
    //var id = e.dataTransfer.getData('Text') ;
    var id = draggedObject ;
    var draggedNode = document.getElementById(id) ;
    if(draggedNode.className.indexOf('card') > -1) {
      var elems = this.childNodes ;
      for(var i=0; i<elems.length; i++) {
	if(elems[i].className) {
	  if(elems[i].className.indexOf('card') > -1) {
	    var trash = document.getElementById('trash') ;
	    elems[i].className = 'card_trashed' ;
	    trash.appendChild(elems[i]) ;
          }
	}
      }
      Logger.log("La "+me.get_box_adress(this)+" reçoit la "+me.get_card_adress(draggedNode)+".") ;
      this.appendChild(draggedNode) ;
      nbmove++ ;
      updateCounters() ;
      EventHelpers.preventDefault(e) ;
    }
  }

  function cardOver(e) {
    var id = e.dataTransfer.getData('Text') ;
    var id = draggedObject ;
    var draggedNode = document.getElementById(id) ;
    if(draggedNode) {
      if(draggedNode.className && draggedNode.className.indexOf('card') > -1) {
	EventHelpers.preventDefault(e) ;
      }
    }
  }

  function array_index(tab) {
    if(tab.className.indexOf("array") > -1) {
      var metadata = tab.id.replace("array","") ;
      var reg = new RegExp(":.*","") ;
      return parseInt(metadata.replace(reg,"")) ;
    } else {
      return -1 ;
    }
  }
  
  function array_size(tab) {
    if(tab.className.indexOf("array") > -1) {
      var metadata = tab.id.replace("array","") ;
      var reg = new RegExp("^.*:","") ;
      return parseInt(metadata.replace(reg,"")) ;
    } else {
      return -1 ;
    }
  }

  function incr_array_size(tab) {
    var size = array_size(tab) ;
    var index = array_index(tab) ;
    size += 1 ;
    tab.id = "array"+index+":"+size ;
  }
  
  function box_array(box) {
    if(box.className.indexOf("box") > -1) {
      var metadata = box.id.replace("box","") ;
      var reg = new RegExp(":.*","") ;
      return parseInt(metadata.replace(reg,"")) ;
    } else {
      return -1 ;
    }
  }
  
  function box_index(box) {
    if(box.className.indexOf("box") > -1) {
      var metadata = box.id.replace("box","") ;
      var reg = new RegExp("^.*:","") ;
      return parseInt(metadata.replace(reg,"")) ;
    } else {
      return -1 ;
    }
  }

  me.add_box_to_tab = function(tab) {
    box = me.createBox() ;
    nbbox ++ ;
    var size = array_size(tab) ;
    var index = array_index(tab) ;
    box.id = "box"+index+":"+(size+1) ;
    box.innerHTML = size+1 ;
    EventHelpers.addEvent(box,'drop',dropCard) ;
    EventHelpers.addEvent(box,'dragover',cardOver) ;
    var adder = tab.childNodes[size] ;
    tab.insertBefore(box,adder) ;
    updateCounters() ;
    incr_array_size(tab) ;
    Logger.log("Création de la "+me.get_box_adress(box)+".") ;
    return box ;
  }
  
  function addBox() {
    var tab = this.parentNode ;
    me.add_box_to_tab(tab) ;
  }

  function addArray() {
    var mem = this.parentNode ;
    var arr = me.createArray() ;
    arr.id = "array"+nbarrays+":0" ;
    Logger.log("Création du tableau "+nbarrays+".") ;
    EventHelpers.addEvent(arr.childNodes[0],'click',addBox) ;
    mem.insertBefore(arr,this) ;
  }

  me.get_box_adress = function(box) {
    var tabindex = box_array(box) ;
    var boxindex = box_index(box) ;
    if(boxindex > 0 && tabindex > 0) {
      return "case "+boxindex+" du tableau "+box_array(box) ;
    } else {
      return "poubelle" ;
    }
  }
  
  me.get_card_adress = function(card) {
    var box = card.parentNode ;
    return "carte de la "+me.get_box_adress(box) ;
  }
}
