package org.joliverie.sio.slam.worldstat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;
/**
 * Lecteur de ressources XML distantes (liste de pays et caractéristiques d'un pays)  
 * @author kpu
 *
 */
public class UtilWorldXml {
  
  public UtilWorldXml() {
  }

  /**
   * Obtient une liste de pays à partir d'un flux XML via une ressource web (http)
   * @param urlXmlressource l'url à solliciter
   * @param defaultContent un contenu par défaut si chargement impossible
   * @param dest liste d'instance de Pays à valoriser (par pays, seuls l'id et le nom sont valorisés) 
   */
  
  /** test du parser avec un fichier xml en dur
   * static public void parseListePays(Context context, String defaultContent, ArrayList<Pays> dest) {
   * XmlPullParser parser = context.getResources().getXml(R.xml.countries);
   */
	  
  static public void parseListePays(String urlXmlressource, String defaultContent, ArrayList<Pays> dest) {
		  XmlPullParser parser = Xml.newPullParser();
	  
    
	  try {

      // auto-detect the encoding from the stream
      parser.setInput(getInputStream(urlXmlressource, defaultContent), null);

      int eventType = parser.getEventType();
      Pays currentPays = null;
      boolean done = false;
      while (eventType != XmlPullParser.END_DOCUMENT && !done) {
        String name = null;
        switch (eventType) {
        case XmlPullParser.START_TAG:
          name = parser.getName();
          if (name.equalsIgnoreCase("country")) {
            currentPays = new Pays();
          } else if (currentPays != null) {
            if (name.equalsIgnoreCase("id")) {
              currentPays.setId(parser.nextText());
            } else if (name.equalsIgnoreCase("name")) {
              currentPays.setName(parser.nextText());
            }
            // etc.
          }
          break;
        case XmlPullParser.END_TAG:
          name = parser.getName();
          // si fin de balise country, on ajoute l'objet dans la liste
          if (name.equalsIgnoreCase("country") && currentPays != null) {
            Log.i("ajout pays�: ", currentPays.toString());
            dest.add(currentPays);
          }
          // fin de countries ?
          else if (name.equalsIgnoreCase("countries")) {
            // oui, on signe l'arrêt de la boucle
            done = true;
          }
          break;
        }
        // avance dans le flux
        eventType = parser.next();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static private InputStream getInputStream(String strUrl, String defaulContent) {
    InputStream res = null;
    try {
      Log.i("url", strUrl);			// affichage dans la fen�tre LogCat
      URL url = new URL(strUrl);
      URLConnection urlConnection = url.openConnection();
      res = urlConnection.getInputStream();
//      res = url.openConnection().getInputStream();
    } catch (Exception e) {
      Log.d("Erreur de lecture", strUrl + ":"+ e.getMessage());
    }
    if (res == null)
      // retourne quelque chose tout de même...
      res = new ByteArrayInputStream(defaulContent.getBytes());
    return res;
  }


  
}
