package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {

	public static void main(String[] args) {
			getContentHtml("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
			getContentHtml("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
			getContentHtml("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
	}
	
	/**
	 * Cette fonction cr�e un fichier un CSV contenant les donn�es des tableaux d'une page wikip�dia
	 * @param url l'adresse de la page que nous voulons exploiter
	 * @throws IOException
	 */
	public static void getContentHtml(String url) {//trouver un meilleur nom pour cette fonction
		System.out.println("D�but de l'extraction :");
		
		//r�cup�ration des donn�es d'une page wikip�dia dans un Document
		Document doc=getHtmlJsoup(url);	

		//Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
		FileWriter fileWriter = creationFichierCsv(doc);
		
		//parcours du code html et insertion dans le fichier csv des donn�es contenues dans des tableaux
		insertionDonnesTableauDansFichierCSV(doc,fileWriter);
		
	}
	public static Document getHtmlJsoup(String url) {
		Document doc=null;
		try {
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e) {
			System.out.println("erreur lors de la r�cup�ration du code html");
			e.printStackTrace();
		}
		return doc;
	}
	public static FileWriter creationFichierCsv(Document doc){ //peut �tre d�placer dans un autre package (createFileCSV)
		//Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
		Elements titre=doc.select("h1");
		FileWriter fileWriter=null;
		try {
			fileWriter = new FileWriter("fichierCSV\\"+titre.first().text()+".csv");
		} catch (IOException e) {
			System.out.println("erreur lors de la cr�ation du fichier .CSV");
			e.printStackTrace();
		}
		return fileWriter;
	}
	public static void insertionDonnesTableauDansFichierCSV(Document doc, FileWriter fileWriter) {
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
				try {
					fileWriter.append(ligneDunTableau);
					fileWriter.append(nouvelleLigne);
				} catch (IOException e) {
					System.out.println("erreur lors de l'ajout d'une ligne dans le fichier .CSV");
					e.printStackTrace();
				}				
			}
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("erreur lors de la fermeture du fichier .CSV");
			e.printStackTrace();
		}
	}
}
