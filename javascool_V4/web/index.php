<?php
include('includes/sal.class.php');
include('includes/v3_redirections.php');
include('includes/url_normalize.php');
include('includes/cache_get_contents.php');
include('includes/wiki_get_contents.php');
include('includes/html_get_normalized_url.php');
include('includes/html_get_contents.php');
if (isset($_GET['page']) && $_GET['page'] == "phpinfo.php") {
  include('pages/phpinfo.php');
  exit;
 }
if (isset($_GET['page'])) {
  if (v3_redirections($_GET['page']))
    exit;
}
if (isset($_GET['weberroreport'])) {
  include('pages/developers/weberroreport.php');
  exit;
 }
?>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8" />
        <title>Java's Cool</title>
        <style type="text/css"><?php include('pages/style.css');?></style>

        <script type="text/javascript">
            function getElementsByClass( searchClass, domNode, tagName) { 
                if (domNode == null) domNode = document;
                if (tagName == null) tagName = '*';
                var el = new Array();
                var tags = domNode.getElementsByTagName(tagName);
                var tcl = " "+searchClass+" ";
                for(i=0,j=0; i<tags.length; i++) { 
                    var test = " " + tags[i].className + " ";
                    if (test.indexOf(tcl) != -1) 
                        el[j++] = tags[i];
                } 
                return el;
            } 
            
            function loaded() {
                applyLabelStyles();
                addListeners();
                updateAnimation(5);
            }
            function unloaded() {
                updateAnimation(5);
            }
            function applyLabelStyles() {
                var labels=getElementsByClass('label');
                for (i=0; i<labels.length; i++) {
                    labels[i].innerHTML='<table class="label"><tr><td class="label-left"></td><td class="label-center">'+labels[i].innerHTML+'</td><td class="label-right"></td></tr></table>';
                }
                var labels=getElementsByClass('labelclickable');
                for (i=0; i<labels.length; i++) {
                    labels[i].innerHTML='<table class="labelclickable"><tr><td class="label-left"></td><td class="label-center">'+labels[i].innerHTML+'</td><td class="label-right"></td></tr></table>';
                }
            }
            function addListeners() {
                var lefts=getElementsByClass('menubegin');
                for (i=0; i<lefts.length; i++) {
                    lefts[i].addEventListener("mouseover", mouseOverMenuSide, false);
                    lefts[i].addEventListener("mouseout", mouseOutMenuSide, false);
                    lefts[i].addEventListener("click", mouseClickMenuSide, false);
                }
                var rights=getElementsByClass('menuend');
                for (i=0; i<rights.length; i++) {
                    rights[i].addEventListener("mouseover", mouseOverMenuSide, false);
                    rights[i].addEventListener("mouseout", mouseOutMenuSide, false);
                    rights[i].addEventListener("click", mouseClickMenuSide, false);
                }
                var middles=getElementsByClass('menuitem');
                for (i=0; i<middles.length; i++) {
                    middles[i].addEventListener("mouseover", mouseOverMenu, false);
                    middles[i].addEventListener("mouseout", mouseOutMenu, false);
                }
                window.addEventListener("resize", windowResized, false);
            }
            function windowResized() {
                if (animationrunning) return;
                framePosition=getPos(document.getElementById("header"));
                var w=getSize(document.getElementById("header")).width;
                document.getElementById("plugright").style.left=(framePosition.left+w+400-document.getElementById("plugright").offsetWidth)-(animationend-30)*3+"px";
                document.getElementById("plugleft").style.left=(framePosition.left-400)+(frame-30)*3+"px";
            }
            function mouseOverMenuSide(event) {
                var menuItemName=this.id.substr(0,this.id.length-2);
                document.getElementById(menuItemName).className="menuitemselected";
                document.getElementById(menuItemName+"_l").className="menuselectedbegin";
                document.getElementById(menuItemName+"_r").className="menuselectedend";
            }
            function mouseOutMenuSide(event) {
                var menuItemName=this.id.substr(0,this.id.length-2);
                document.getElementById(menuItemName).className="menuitem";
                document.getElementById(menuItemName+"_l").className="menubegin";
                document.getElementById(menuItemName+"_r").className="menuend";
            }
            function mouseOverMenu(event) {
                var menuItemName=this.id;
                document.getElementById(menuItemName).className="menuitemselected";
                document.getElementById(menuItemName+"_l").className="menuselectedbegin";
                document.getElementById(menuItemName+"_r").className="menuselectedend";
            }
            function mouseOutMenu(event) {
                var menuItemName=this.id;
                document.getElementById(menuItemName).className="menuitem";
                document.getElementById(menuItemName+"_l").className="menubegin";
                document.getElementById(menuItemName+"_r").className="menuend";
            }
            function mouseClickMenuSide(event) {
                var menuItemName=this.id.substr(0,this.id.length-2);
                var elm=document.getElementById(menuItemName);
                elm.onclick();
            }
            function gotolocnow(loc) {
                document.location=loc;
            }
            function gotoloc(loc) {
                updateAnimation(-100,loc);
            }
            function followLink(e) {
                var elem=this;
                window.alert(elem.alt);
                updateAnimation(-4,elem.alt);
                window.event.cancelBubble = true;
                e.stopPropagation();
                return false;
            }
            
            var w=300;
            var h=200;
            var frame=1;
            
            function showButtons() {
                var middles=getElementsByClass('menuitem');
                for (i=0; i<middles.length; i++) {
                    middles[i].width=w+"px";
                }
                w--;
                if (w>0)
                    setTimeout("showButtons()",1);
            }
            function plugContent() {
                document.getElementById("mainPanel").style.marginTop=h+"px";
                
                h--;
                if (h>0) 
                    setTimeout("plugContent()",1);
            }
            function getPos(obj) {
                var output = new Object();
                var mytop=0, myleft=0;
                while( obj) {
                    mytop+= obj.offsetTop;
                    myleft+= obj.offsetLeft;
                    obj= obj.offsetParent;
                }
                output.left = myleft;
                output.top = mytop;
                return output;
            }
            function getSize(obj) {
                var output=new Object();
                output.width=obj.offsetWidth;
                output.height=obj.offsetHeight;
                return output;
            }
            
            var animationend=85;
            var animationrunning=false;
            
            function updateAnimation(increment) {
                return updateAnimation(increment,"");
            }
            
            function updateAnimation(increment,redirect) {
                var plugleft=document.getElementById("plugleft");
                var plugright=document.getElementById("plugright");
                var mainPanel=document.getElementById("mainPanel");
                if (!animationrunning) {
                    animationrunning=true;
                    plugleft.style.display="block";
                    plugright.style.display="block";
                }
                framePosition=getPos(document.getElementById("header"));
                var w=getSize(document.getElementById("header")).width;
                
                if (frame<30) {
                    plugleft.style.top=120-frame*3+"px";
                    plugleft.style.left=(framePosition.left-400)+"px";
                    plugright.style.top=120-frame*3+"px";
                    plugright.style.left=(framePosition.left+w+400-document.getElementById("plugright").offsetWidth)+"px";
          //          mainPanel.style.top=(29*3-frame*3)+"px";
                }
                if (frame>30) {
                    plugleft.style.left=(framePosition.left-400)+(frame-30)*3+"px";
                    plugleft.style.top="30px";
                    plugright.style.left=(framePosition.left+w+400-document.getElementById("plugright").offsetWidth)-(frame-30)*3+"px";
                    plugright.style.top="30px";
                }
                
                if (frame>80 && increment>0) {
                    var lights=getElementsByClass("pluglight");
                    for (i=0; i<lights.length; i++) {
                        lights[i].src="images/ledgreen.png";
                    }
                }
                else if (frame<80 && increment<0) {
                    var lights=getElementsByClass("pluglight");
                    for (i=0; i<lights.length; i++) {
                        lights[i].src="images/ledred.png";
                    }
                }
                
                frame+=increment;
                if (frame>=0 && frame<animationend)
                    setTimeout("updateAnimation("+increment+",\""+redirect+"\")",10);
                else {
                    animationrunning=false;
                    
                    if (redirect!="" && redirect!="undefined" && redirect) {gotolocnow(redirect);}
                }
            }
            
            function mouseOverLink(event) {
            }

            document.onload = function() {
                document.onselectstart = function() {return false;} // ie
                document.onmousedown = function() {return false;} // mozilla
            }

            document.onload = function() {
                var elements = getElementsByClass('noselect');
                for (i=0; i<elements.length; i++) {
                    elements[i].onselectstart = function () { return false; } // ie
                    elements[i].onmousedown = function () { return false; } // mozilla
                }
            }
            
            var mouseX=0;
            var mouseY=0;
            
            window.onmousemove=getMouseCoordinates;
            function getMouseCoordinates(event)
            {
                ev = event || window.event;
                mouseX=ev.pageX;
                mouseY=ev.pageY;
            }

            var cX = 0; var cY = 0; var rX = 0; var rY = 0;
            function UpdateCursorPosition(e){ cX = e.pageX; cY = e.pageY;}
            function UpdateCursorPositionDocAll(e){ cX = event.clientX; cY = event.clientY;}
            if(document.all) { document.onmousemove = UpdateCursorPositionDocAll; }
            else { document.onmousemove = UpdateCursorPosition; }
            function AssignPosition(d,h) {
                if(self.pageYOffset) {
                    rX = self.pageXOffset;
                    rY = self.pageYOffset;
                }
                else if(document.documentElement && document.documentElement.scrollTop) {
                    rX = document.documentElement.scrollLeft;
                    rY = document.documentElement.scrollTop;
                }
                else if(document.body) {
                    rX = document.body.scrollLeft;
                    rY = document.body.scrollTop;
                }
                if(document.all) {
                    cX += rX; 
                    cY += rY;
                }
                d.style.left = (getAbsolutePosition(h).x) + "px";
                d.style.top = (getAbsolutePosition(h).y-80) + "px";
            }
            function HideContent(d) {
                if(d.length < 1) { return; }
                document.getElementById(d).style.display = "none";
            }
            function ShowContent(d,h) {
                if(d.length < 1) { return; }
                var dd = document.getElementById(d);
                var hh=document.getElementById(h);
                AssignPosition(dd,hh);
                dd.style.display = "block";
            }
            function ReverseContentDisplay(d,h) {
                if(d.length < 1) { return; }
                var dd = document.getElementById(d);
                var hh=document.getElementById(h);
                AssignPosition(dd,hh);
                if(dd.style.display == "none") { dd.style.display = "block"; }
                else { dd.style.display = "none"; }
            }
            function getAbsolutePosition(element) {
                var r = { x: element.offsetLeft, y: element.offsetTop };
                if (element.offsetParent) {
                    var tmp = getAbsolutePosition(element.offsetParent);
                    r.x += tmp.x;
                    r.y += tmp.y;
                }
                return r;
            }
        </script>

    </head>

<!--?php echo "<h1> Désolé: un problème sur le site , javascool revient le lundi 30 avril . . </h1>"; exit() ?-->

    <!-- //TODO comment -->
    <body onload="loaded()" onunload="unloaded()"><a name=""/>
        <?php

        function showLink($link, $text, $type) {
            //TODO security
            //$text = addslashes($text);
            $id = sha1(uniqid(rand()));
            $c="Clic gauche pour suivre lien, clic milieu pour ouvrir le lien dans un nouvel onglet";
            if ($type == "proglet" || $type == "internal" || $type == "external" || type == "internal_newtab")
                echo('<a href="' . $link . '">' . $text . '</a>');
            else if ($type == "file" || $type = "wiki")
                echo('<a href="' . $link . '">' . $text . '</a>');
            else
                echo("INVALID LINK !!");
        }

        function showButton($doc) {
            echo ('<table class="labelMain"><tr><td>');
            if ($doc[1] != "") {
                $id = sha1(uniqid(rand()));
                echo('<span style="display:none" id="' . $id . '" class="labelclickable" onclick="gotoloc(\'' . $doc[1] . '\');">' . $doc[0] . '</span><script type="text/javascript">document.getElementById(\'' . $id . '\').style.display="inline";</script><noscript><a href="' . $doc[1] . '">' . $doc[0] . '</a></noscript>');
            }
            else
                echo('<span class="label">' . $doc[0] . '</span>');
            echo ('</td></tr></table><br />');
        }
        
        function showBrowser($parents, $brothers=array()) {
            echo ('<table class="labelMain"><tr><td>');
            $i = 0;
            foreach ($parents as $doc) {
                if ($i==count($parents)-1 && count($brothers)!=0) {
                    $doc[0].=" (";
                    $j=0;
                    foreach($brothers as $brother) {
                        $brother[0]=addslashes($brother[0]);
                        $doc[0].='<script type="text/javascript">document.write(\'<a href="#" onclick="gotoloc(\\\'' . $brother[1] . '\\\');">'.$brother[0].'</a>\');</script><noscript><a href="'.$brother[1].'">'.stripslashes($brother[0]).'</a></noscript>';
                        if ($j!=count($brothers)-1) $doc[0].=', ';
                        $j++;
                    }
                    $doc[0].=')';
                }
                if ($doc[1] != "") {
                    $id = sha1(uniqid(rand()));
                    echo('<span style="display:none" id="' . $id . '" class="labelclickable" onclick="gotoloc(\'' . $doc[1] . '\');">' . $doc[0] . '</span><script type="text/javascript">document.getElementById(\'' . $id . '\').style.display="inline";</script><noscript><a href="' . $doc[1] . '">' . $doc[0] . '</a></noscript>');
                }
                else
                    echo('<span class="label">' . $doc[0] . '</span>');
                if ($i != count($parents) - 1)
                    echo('<script type="text/javascript">document.write(\'<span class="label-arrow"></span>\');</script><noscript><img src="images/label-separator.png" alt=" -> " style="position:relative; top:7px; margin-left: 15px; margin-right: 15px;" /></noscript>');
                $i++;
            }
            echo ('</td></tr></table><br />');
        }

        $page = html_get_normalized_url(isset($_GET['page']) ? $_GET['page'] : 'home');
        Sal::validatePage($page);
        $action = (isset($_GET['action'])) ? $_GET['action'] : 'display';
        Sal::validateAction($action);
        $include = 'pages/' . $page . '/' . $action . '.php';
        if (!is_file($include)) {
            die("Page not found");
        }
        include('pages/header.php');
        ?>

        <div class="main2" id="mainPanel">
            <table class="main2"><tr class="top"><td class="topleft"></td><td class="top"></td><td class="topright"></td></tr>
                <tr class="center"><td class="left"></td><td class="center">
                        <table style="width: 100%">
                            <tr>
                                <td>
                                    <img src="images/ledred.png" style="width: 50px; height: 50px; display: none;" class="pluglight" id="led1"/>
                                    <script>document.getElementById("led1").style.display="inline";</script>
                                    <noscript>
                                    <a href="index.php?action=jsinfo"><img src="images/ledblue.png" style="border: 0px; width: 50px; height: 50px;" class="pluglight" id="led1"/></a>
                                    </noscript>
                                </td>
                                <td style="width: 100%"></td>
                                <td>
                                    <img src="images/ledred.png" style="width: 50px; height: 50px; display: none;" class="pluglight" id="led2"/>
                                    <script>document.getElementById("led2").style.display="inline";</script>
                                    <noscript>
                                    <a href="index.php?action=jsinfo"><img src="images/ledblue.png" style="border: 0px; width: 50px; height: 50px;" class="pluglight" id="led2"/></a>
                                    </noscript>
                                </td>
                            </tr>
                        </table><div id="mainContent">
                        <?php
                        include($include);
                        ?></div>
                    </td><td class="right"></td></tr>
                <tr class="bottom"><td class="bottomleft"></td><td class="bottom"></td><td class="bottomright"></td></tr></table>
        </div>
    </body>
</html>
