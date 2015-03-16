/*
 * @licstart
 * This program is meant to write a log file in the element with "log" id
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

var Logger = new function () {
  var me = this ;
  var muted = false ;
  
  me.clear_log = function() {
    var logger = document.getElementById("log") ;
    logger.innerHTML = "" ;
  }

  me.mute = function() {
    muted = true ;
  }

  me.unmute = function() {
    muted = false ;
  }

  me.log = function(txt) {
    if(!muted) {
      var logger = document.getElementById("log") ;
      if(logger.innerHTML) {
        logger.innerHTML += "<br />" ;
      }
      logger.innerHTML += txt ;
    }
  }
}
