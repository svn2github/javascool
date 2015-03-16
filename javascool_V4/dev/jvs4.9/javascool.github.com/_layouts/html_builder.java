import org.javascool.tools.FileManager;
import org.json.JSONObject;
import org.javascool.core.Proglet2Jar;
import java.util.Arrays;

/** This script encapsulates the wiki syndicated, proglet list and html pages compilation. */
public class html_builder { private html_builder() {}
  private static final String base = ".";
  
  /** Script wrapper. */ 
  public static void main(String usage[]) {
    //syndicateWiki(); // @todo pour alléger la mise au point
    listProglets();
    encapsulateDocs();
  }

  //
  // Wiki https://wiki.inria.fr/sciencinfolycee syndication mechanism
  //

  // Translates all registered wiki pages. 
  private static void syndicateWiki() {
    for(String name : new String[] {
	  // https://wiki.inria.fr/sciencinfolycee/Catégorie:JavaScool
	  "JavaScool:About",
	  "JavaScool:Accueil",
	  "JavaScool:Crédits",
	  "JavaScool:Faq",
	  "JavaScool:Manifeste",
	  "JavaScool:Proglet",
	  // https://wiki.inria.fr/sciencinfolycee/Catégorie:JavaScoolFaq
	  "JavaScool:Ailleurs",
	  "JavaScool:AutresInitiatives",
	  "JavaScool:Cadrage",
	  "JavaScool:InfoAuLycee",
	  // https://wiki.inria.fr/sciencinfolycee/Catégorie:JavaScoolRes
	  "JavaScool:Activites",
	  "JavaScool:ProgletsProcessing",
	  "JavaScool:Ressources",
	  // https://wiki.inria.fr/sciencinfolycee/Catégorie:TPE
	  "JavaScool:TPE-Accueil",
	  "JavaScool:TPE-Demos",
	  "JavaScool:TPE-Exemples",
	  "JavaScool:TPE-Interventions",
	  "JavaScool:TPE-Methode",
	  // https://wiki.inria.fr/sciencinfolycee/Catégorie:JavaScoolDev
	  "JavaScool:ProgletProcessing",
	  "JavaScool:ProcessingLinux",
	  "JavaScool:SyndicationWiki"
	})
      syndicateWiki(name);
  }
  private static void syndicateWiki(String name) {
    System.out.println("> do /wpages/"+name+".html");
    FileManager.save(base + "/wpages/"+name+".html", encapsulates(translateWiki(name)));
  }    
  // Translates a wiki contents into this site context and return the translated page. 
  private static String translateWiki(String name) {
    // Gets the raw page contents from the wiki
    String text = FileManager.load("https://wiki.inria.fr/sciencinfolycee/"+name+"?printable=yes&action=render");
    // Replaces wiki links by this site links
    text = text.replaceAll("href=\"http[s]?://wiki.inria.fr/sciencinfolycee/([^\"]*)\"", "class=\"internal\" href=\"http://javascool.github.com/wpages/$1.html\"");
    // Cancel meta-data table
    text  = text.replaceAll("<table class=\"wikitable\">[.\\s]*</table>", "");
    // Reduces the titles with the page.
    text  = text.replaceAll("<([/]?)h3>", "<$1h4>").replaceAll("<([/]?)h2>", "<$1h3>").replaceAll("<([/]?)h1>", "<$1h2>");
    // Detects page in error
    if (text.matches("<title>(Erreur|Connexion nécessaire)"))
      text = "Erreur: la page wiki "+name+" est en erreur.\n";
    return text;
  }

  //
  // Proglets page generation
  // 
  private static void listProglets() {
    String files[] = FileManager.list(base + "/wproglets");
    Arrays.sort(files);
    int WIDTH = 6, N = files.length, n = 0;
    String body = "<H1>En cours de développement : NE PAS UTILISER</H1><table align='center'><tr>\n"; // @todo en attendant la validation
    for(String file : files)
      if (file.matches("^.*/wproglets/javascool-proglet-[^-]*-html")) {
	body += "<td align='center'>" + listProglet(file) + "</td>\n";
	n++;
	if (n < N && n % WIDTH == 0)
	  body += "</tr><tr>\n";
      }
    body += "</tr></table>\n";
    FileManager.save(base + "/wpages/proglets.html", encapsulates(body));
  }
  private static String listProglet(String progletDir) {
    JSONObject params = Proglet2Jar.getProgletParameters(progletDir);
    String base = progletDir.replaceFirst("^.*/wproglets", "../wproglets");
    System.out.println("> do "+base);
    return "<a href='"+base+"/index.html' title='"+params.optString("title").replaceAll("&", "&amp;").replaceAll("'", "&aquot")+"'><img src='"+base+"/"+params.optString("icon")+"' alt='proglet-"+params.optString("name")+"'/></a><br>"+params.optString("name");
  }

  //
  // Wiki pages layout encapsulation
  //
  private static void encapsulateDocs() {
    for(String file : FileManager.list(base, 4))
      if (file.endsWith(".hm"))
	encapsulateDoc(file.replaceFirst("^.*/doc/(.*)\\.hm$", "$1"));
  }
  private static void encapsulateDoc(String name) {
    System.out.println("> do /wpages/"+name+".html");
    FileManager.save(base + "/wpages/"+name+".html", encapsulates(FileManager.load(base + "/doc/"+name+".hm")));
  }
  // Encapsulates a page body into this site layout.
  private static String encapsulates(String text) {
    String layout = FileManager.load(base + "/_layouts/default.html").replaceAll("[{][{] site.baseurl\\s*[}][}]", "http://javascool.github.com/");
    int i0 = layout.indexOf("{{content}}");
    return layout.substring(0, i0) + text + layout.substring(i0 + "{{content}}".length());
  }

}
