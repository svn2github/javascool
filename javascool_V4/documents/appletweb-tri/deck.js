/*
 * @licstart
 * This program handles a deck of indexed cards used in combination with
 * memorybricks.js to introduce kids to sorting algorithms.
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

var deck_handler = new function() {
  var me = this ;
  var nbcomp = 0 ;
  var size = 5 ;
  var deck = new Array() ;
  var cards_visible = false ;
  var selected_cards = [] ;

  function updateCounters() {
    var counter = document.getElementById('nbcomp') ;
    counter.innerHTML = nbcomp ;
  }

  function init_deck() {
    deck = new Array(size) ;
    for(var i=0; i<size; i++) {
      var rnum = rand(Math.max(size,100)) ;
      while(in_deck(rnum)) {
	rnum = rand(Math.max(size,100)) ;
      }
      deck[i] = rnum ;
    }
    shuffle() ;
    Logger.clear_log() ;
    Logger.log("Initialisation du problème avec "+size+" cartes.") ;
  }

  function reset_page(n) {
    size = n ;
  }

  function rand(n) {
    return Math.floor(Math.random()*n) ;
  }

  function in_deck(k) {
    for(var i=0; i<size; i++) {
      if(deck[i] == k) {
	return true ;
      }
    }
    return false ;
  }

  function swap(i,j) {
    tmp = deck[i] ;
    deck[i] = deck[j] ;
    deck[j] = tmp ;
  }

  function shuffle() {
    for(var i=0; i<deck.length; i++) {
      var j = rand(deck.length-i) ;
      swap(i,i+j) ;
    }
  }

  function card_value(card) {
    return deck[card_index(card)] ;
  }

  function card_index(card) {
    return parseInt(card.id.replace("card",""))-1 ;
  }

  function select_card() {
    if (selected_cards.length >= 2) {
      var c1 = document.getElementById(selected_cards[0]) ;
      var c2 = document.getElementById(selected_cards[1]) ;
      if(c1) {
        c1.className = 'card' ;
      }
      if(c2) {
        c2.className = 'card' ;
      }
      selected_cards = [] ;
    }
    if (this.className == 'card') {
      this.className = 'card_selected' ;
      selected_cards.push(this.id) ;
    } else {
      this.className = 'card' ;
      selected_cards.pop() ;
    }
    if (selected_cards.length >= 2) {
      var c1 = document.getElementById(selected_cards[0]) ;
      var c2 = document.getElementById(selected_cards[1]) ;
      var v1 = card_value(c1) ;
      var v2 = card_value(c2) ;
      var addr1 = MemoryBricks.get_card_adress(c1) ;
      var addr2 = MemoryBricks.get_card_adress(c2) ;
      var test_string = "" ;
      if(v1<v2) {
        c1.className = 'card_min' ;
        c2.className = 'card_max' ;
	test_string = "plus petite que" ;
        Logger.log("La "+addr1+" est plus petite que la "+addr2+".") ;
      } else {
        c1.className = 'card_max' ;
        c2.className = 'card_min' ;
	test_string = "plus grande que" ;
        Logger.log("La "+addr1+" est plus grande que la "+addr2+".") ;
      }
      nbcomp++ ;
      updateCounters() ;
    }
  }

  me.init = function() {
    MemoryBricks.init_layout() ;
    var size_element = document.getElementById('deck_size');
    if(size_element.value) {
      size = size_element.value ;
    } else {
      size_element.value = 5 ;
    }
    cards_visible = false ;
    selected_cards = [] ;
    nbcomp = 0 ;
    updateCounters() ;
    init_deck() ;
    Logger.mute() ;
    var arr = document.getElementById('array1:0') ;
    var adder = arr.childNodes[0] ;
    for(var i=0; i<deck.length; i++) {
      var box = MemoryBricks.add_box_to_tab(arr) ;
      var card = MemoryBricks.createCard() ;
      card.innerHTML = '?' ;
      card.id = 'card'+(i+1) ;
      box.appendChild(card) ;
      EventHelpers.addEvent(card,'click',select_card) ;
    }
    Logger.unmute() ;
    MemoryBricks.init_events() ;

    var showbtn = document.getElementById('toggle_cards') ;
    EventHelpers.addEvent(showbtn,'click',toggle_card_visibility) ;

    var restartbtn = document.getElementById('restart') ;
    EventHelpers.addEvent(restartbtn,'click',me.init) ;

    var helpbtn = document.getElementById('toggle_help') ;
    EventHelpers.addEvent(helpbtn,'click',toggle_help) ;
    
    var aboutbtn = document.getElementById('toggle_about') ;
    EventHelpers.addEvent(aboutbtn,'click',toggle_about) ;
    
    var aboutbtn = document.getElementById('toggle_log') ;
    EventHelpers.addEvent(aboutbtn,'click',toggle_log) ;
  }

  function toggle_pane(name) {
    var pane = document.getElementById(name) ;
    var panetool = document.getElementById('toggle_'+name) ;
    if(!pane.style.display || pane.style.display == 'none') {
      pane.style.display = 'block' ;
      panetool.className = 'tool_selected' ;
    } else {
      pane.style.display = 'none' ;
      panetool.className = 'tool' ;
    }
  }

  function toggle_help() {
    toggle_pane('help') ;
  }

  function toggle_about() {
    toggle_pane('about') ;
  }

  function toggle_log() {
    toggle_pane('log') ;
  }
  
  function get_all_cards() {
    var cards = document.getElementsByClassName('card') ;
    var res = new Array(cards.length) ;
    for(var i=0; i<cards.length; i++) {
      res[i] = cards[i] ;
    }
    for(var i=0; i<selected_cards.length; i++) {
      var node = document.getElementById(selected_cards[i]) ;
      if(node) {
        res.push(node) ;
      }
    }
    var trashed_cards = document.getElementsByClassName('card_trashed') ;
    for(var i=0; i<trashed_cards.length; i++) {
      res.push(trashed_cards[i]) ;
    }
    return res ;
  }

  function toggle_card_visibility() {
    var cards = get_all_cards() ;
    if(cards_visible) {
      for(var i=0; i<cards.length; i++) {
        cards[i].innerHTML = '?' ;
      }
      Logger.log("Dissimulation des cartes.") ;
    } else {
      for(var i=0; i<cards.length; i++) {
        cards[i].innerHTML = card_value(cards[i]) ;
      }
      Logger.log("Découverte des cartes.") ;
    }
    cards_visible = !cards_visible ;
  }
}

EventHelpers.addPageLoadEvent('deck_handler.init');
