package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {

	public static void main(String[] args) {
		try {
			getContentHtml("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
			getContentHtml("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Cette fonction cr�e un fichier un CSV contenant les donn�es des tableaux d'une page wikip�dia
	 * @param url l'adresse de la page que nous voulons exploiter
	 * @throws IOException
	 */
	public static void getContentHtml(String url) throws IOException{//trouver un meilleur nom pour cette fonction
		//r�cup�ration des donn�es d'une page wikip�dia dans un Document
		Document doc=getHtmlJsoup(url);

		System.out.println("D�but de l'extraction :");

		//Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
		Elements titre=doc.select("h1");
		FileWriter fileWriter = new FileWriter("fichierCSV\\"+titre.first().text()+".csv");
		//******************************************************************************

		String ligneDunTableau="";
		String nouvelleLigne="\n";//permettra de passer une ligne

		//parcours des tableaux de la page
		//attention peut-�tre un probl�me?? si on est sur une page wikip�dia contenant le mot "table"
		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) { 
				ligneDunTableau="";
				for(Element td : row.select("td")) {
					ligneDunTableau=ligneDunTableau+td.text()+";";
				}
				fileWriter.append(ligneDunTableau);
				fileWriter.append(nouvelleLigne);
			}
		}
		fileWriter.close();
		System.out.println("Fin de l'extraction");
	}
	public static Document getHtmlJsoup(String url) {
		Document doc=null;
		try {
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
}
