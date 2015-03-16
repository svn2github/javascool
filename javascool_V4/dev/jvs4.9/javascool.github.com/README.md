
Java'sCool 4.9 before 5 : [Java'sCool](http://javascool.github.com)

## Repository architecture

- `index.md`

    Home page

This git repository has the following top-level folders : 

- `/doc`
  
    Contains md, xcf and png files (sources of the doc). All the static pages of the website are here.

- `_layouts`

    Contains Jekyll layouts to encapsulate the web site pages,
     and our java mechanism to generate pages from wiki or proglet files.

- `/wpages` 

    Stores the web pages syndicated from https://wiki.inria.fr/sciencinfolycee/Catégorie:JavaScool

- `/wproglets` 

    Stores the web pages produced by http://code.google.com/p/javascool

- `/assets`
  
   The Web style pages (mainly twitter bootstrap files).

## The key points 

* Use `./makefile` to manage the site updates

* Edit the site pages in `/doc` as .md file or .hm file (i.e. as HTML file with any header, etc..)

* Changes the navbar menu in `_layouts/default.html#navbar ...`

* Adjust the site look and feel using `/assets/pygments.css` and `_layouts/default.html`

* Patch the wiki and html pages generation mechanism in `_layouts/html-builder.sh` and `_layouts/HtmlBuilder.java`

* Do not use or edit the cache directories `/wpages` and `/wproglets` 

* Do not change the `/assets/bootstrap` which contains only the bootstrap distribution files

