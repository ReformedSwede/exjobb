package main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    //TODO: Refactor for better efficiency. Maybe singleton with pre-parsed xml document objects?

    private static final File langXML = new File(System.getProperty("user.dir") + "/src/resources/xml/language.xml");
    private static final File gfXML = new File(System.getProperty("user.dir") + "/src/resources/xml/gf-resources.xml");

    /**
     * Converts a short language code to its real name (e.g. eng -> English)
     * @param language A three letter language code
     * @return The name of the language, or an empty string if none was found
     */
    public static String codeToName(String language){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("short").equals(language)){
                        return eElement.getAttribute("name");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Converts a short language code to the name of the corresponding .gf file (e.g. eng -> WordsEng)
     * @param language A three letter language code
     * @return The name of the gf language, or an empty string if none was found
     */
    public static String codeToGF(String language){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("short").equals(language)){
                        return eElement.getAttribute("gf");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Converts a name of a gf language to its real name(e.g. WordsEng -> English)
     * @param language A name of a gf language
     * @return The name of the language, or an empty string if none was found
     */
    public static String gfToName(String language){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("gf").equals(language)){
                        return eElement.getAttribute("name");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Converts a name of a gf language to its short code(e.g. WordsEng -> eng)
     * @param language A name of a gf language
     * @return The code for the language, or an empty string if none was found
     */
    public static String gfToCode(String language){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("gf").equals(language)){
                        return eElement.getAttribute("short");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Converts a name of a language to the name of the corresponding .gf file (e.g. English -> WordsEng)
     * @param language A name of a language
     * @return The code for the gf language, or an empty string if none was found
     */
    public static String nameToGf(String language){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(langXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("name").equals(language)){
                        return eElement.getAttribute("gf");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Gives a list of all languages that are specified in the gf-resources xml resource file.
     * These are the only languages that can be used
     * @return All languages
     */
    public static List<String> getGfLanguages(){
        List<String> list = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    list.add(eElement.getAttribute("lang"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns the names of the categories specified in the xml resource that are parts of speech.
     * E.G. Noun, Verb, but not VerbForm, Word, Float
     * @return all part of speech names
     */
    public static List<String> getPartOfSpeechCats(){
        List<String> list = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cat");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    list.add(eElement.getAttribute("name"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns the category, given its name. EX:
     * Verb -> VF
     * Noun -> NF
     * @param catName The real name of a category
     * @return The corresponding gf category name
     */
    public static String getPartOfSpeechLinCatByName(String catName){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cat");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("name").equals(catName))
                        return eElement.getAttribute("cat");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns a list containing the real names of the inflection forms.
     * @param catName The name of the part of speech (e.g. Noun or Verb)
     * @return A list of all inflection form names
     */
    public static List<String> getInflectionRealNamesByCat(String catName){
        List<String> list = new ArrayList<>();
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cat");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("name").equals(catName)) {
                        NodeList inflList = ((Element) nNode).getElementsByTagName("inflection");
                        for (int j = 0; j < inflList.getLength(); j++)
                            list.add(((Element) inflList.item(j)).getAttribute("name"));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Returns a list containing the categories from the GF library that correspond to the inflection forms.
     * @param catName The name of the part of speech (e.g. Noun or Verb)
     * @param inflName inflection name
     * @param lang language code
     * @return A list of all inflection gf names
     */
    public static String getInflectionGfNamesByCat(String lang, String catName, String inflName){
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList langList = doc.getElementsByTagName("language");

            for (int i = 0; i < langList.getLength(); i++) {
                Element langElem = (Element) langList.item(i);
                if(langElem.getAttribute("lang").equals(lang)) {
                    NodeList catList = langElem.getElementsByTagName("category");
                    for (int j = 0; j < catList.getLength(); j++){

                        Element catElem = ((Element)catList.item(j));
                        if(catElem.getAttribute("name").equals(catName)) {
                            NodeList inflList = catElem.getElementsByTagName("inflection-form");
                            for (int k = 0; k < inflList.getLength(); k++) {

                                Element inflElem = (Element) inflList.item(k);
                                if (inflElem.getAttribute("name").equals(inflName))
                                    return inflElem.getAttribute("form");
                            }
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the category of the paradigm
     * @param catName The name of a category
     * @return The corresponding gf paradigm category
     */
    public static String getParadigmCatByName(String catName){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("cat");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("name").equals(catName))
                        return eElement.getAttribute("paradigm-cat");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns a comma separated list of all necessary files
     * @param lang code of the language
     * @return a string with files listed
     */
    public static String getLangFilesByLang(String lang){
        String langcode = lang.substring(0, 3).toLowerCase();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("lang").equals(langcode))
                        return eElement.getAttribute("files");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the name of the form record
     * @param lang code of the language
     * @param partOfSpeech part of speech
     * @return the form record name
     */
    public static String getLincatRecord(String lang, String partOfSpeech){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("lang").equals(lang)) {
                        NodeList catlist = eElement.getElementsByTagName("category");
                        for(int j = 0; j < catlist.getLength(); j++) {
                            Element catElem = (Element) catlist.item(j);
                            if (catElem.getAttribute("name").equals(partOfSpeech))
                                return catElem.getAttribute("lincat-record");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Returns the lin cat form fun select clause
     * @param lang code of the language
     * @param partOfSpeech part of speech
     * @return the lin cat form fun select clause
     */
    public static String getLinSelect(String lang, String partOfSpeech){
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(gfXML);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("language");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(eElement.getAttribute("lang").equals(lang)) {
                        NodeList catlist = eElement.getElementsByTagName("category");
                        for(int j = 0; j < catlist.getLength(); j++) {
                            Element catElem = (Element) catlist.item(j);
                            if (catElem.getAttribute("name").equals(partOfSpeech))
                                return catElem.getAttribute("lin-select");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
