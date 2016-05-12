package main;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for querying XML resource files.
 */
public class ResourceManager {
    private static final InputSource langXML =
            new InputSource(System.getProperty("user.dir") + "/src/resources/xml/language.xml");
    private static final InputSource gfXML =
            new InputSource(System.getProperty("user.dir") + "/src/resources/xml/gf-resources.xml");

    /**
     * Converts a short language code to its real name (e.g. eng -> English)
     * @param language A three letter language code
     * @return The name of the language, or an empty string if none was found
     */
    public static String codeToName(String language){
        return (String)createXPath(langXML, "//language[@short = '" + language + "']/@name", XPathConstants.STRING);
    }

    /**
     * Converts a short language code to the name of the corresponding .gf file (e.g. eng -> WordsEng)
     * @param language A three letter language code
     * @return The name of the gf language, or an empty string if none was found
     */
    public static String codeToGF(String language){
        return (String)createXPath(langXML, "//language[@short = '" + language + "']/@gf", XPathConstants.STRING);
    }

    /**
     * Converts a name of a gf language to its real name(e.g. WordsEng -> English)
     * @param language A name of a gf language
     * @return The name of the language, or an empty string if none was found
     */
    public static String gfToName(String language){
        return (String)createXPath(langXML, "//language[@gf = '" + language + "']/@name", XPathConstants.STRING);
    }

    /**
     * Converts a name of a gf language to its short code(e.g. WordsEng -> eng)
     * @param language A name of a gf language
     * @return The code for the language, or an empty string if none was found
     */
    public static String gfToCode(String language){
        return (String)createXPath(langXML, "//language[@gf = '" + language + "']/@short", XPathConstants.STRING);
    }

    /**
     * Converts a name of a language to the name of the corresponding .gf file (e.g. English -> WordsEng)
     * @param language A name of a language
     * @return The code for the gf language, or an empty string if none was found
     */
    public static String nameToGf(String language){
        return (String)createXPath(langXML, "//language[@name = '" + language + "']/@gf", XPathConstants.STRING);
    }

    /**
     * Gives a list language codes for all specified languages in the gf-resources xml resource file.
     * These are the only languages that can be used
     * @return All languages
     */
    public static List<String> getGfLanguages() {
        NodeList nodes = (NodeList) createXPath(gfXML, "//language", XPathConstants.NODESET);
        List<String> langs = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++)
            langs.add(((Element) nodes.item(i)).getAttribute("lang"));
        return langs;
    }

    /**
     * Returns the names of the categories specified in the xml resource that are parts of speech.
     * E.G. Noun, Verb, but not VerbForm, Word, Float
     * @return all part of speech names
     */
    public static List<String> getPartOfSpeechCats(){
        NodeList nodes = (NodeList)createXPath(gfXML, "//cat", XPathConstants.NODESET);
        List<String> partsOspeech = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i ++)
            partsOspeech.add(((Element)nodes.item(i)).getAttribute("name"));
        return partsOspeech;
    }

    /**
     * Returns the category, given its name. EX:
     * Verb -> VF
     * Noun -> NF
     * @param catName The real name of a category
     * @return The corresponding gf category name
     */
    public static String getPartOfSpeechLinCatByName(String catName){
        return (String)createXPath(gfXML, "//cat[@name = '" + catName + "']/@cat", XPathConstants.STRING);
    }

    /**
     * Returns the category of the paradigm
     * @param catName The name of a category
     * @return The corresponding gf paradigm category
     */
    public static String getParadigmCatByName(String catName){
        return (String)createXPath(gfXML, "//cat[@name='" + catName + "']/@paradigm-cat", XPathConstants.STRING);
    }

    /**
     * Returns the name of the operation for this category
     * @param catName The name of a category
     * @return The corresponding gf operation
     */
    public static String getOperationByCatName(String catName){
        return (String)createXPath(gfXML, "//cat[@name='" + catName + "']/@oper", XPathConstants.STRING);
    }

    /**
     * Returns a list containing the names of the inflection forms.
     * @param catName The name of the part of speech (e.g. Noun or Verb)
     * @return A list of all inflection form names
     */
    public static List<String> getInflectionRealNamesByCat(String catName){
        NodeList nodes = (NodeList)
                createXPath(gfXML, "//cat[@name = '" + catName + "']/inflection", XPathConstants.NODESET);
        List<String> inflNames = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i ++)
            inflNames.add(((Element)nodes.item(i)).getAttribute("name"));
        return inflNames;
    }

    /**
     * Returns the name of a GF inflection name
     * @param catName The name of the part of speech (e.g. Noun or Verb)
     * @param inflName inflection name
     * @param lang language code
     * @return A GF inflection name
     */
    public static String getInflectionGfNamesByCat(String lang, String catName, String inflName){
        return (String)createXPath(gfXML, "//language[@lang ='" + lang + "']/category[@name = '"
                + catName + "']/inflection-form[@real-name='" + inflName + "']/@gf-name", XPathConstants.STRING);
    }

    /**
     * Returns a comma separated list of all necessary files
     * @param lang code of the language
     * @return a string with files listed
     */
    public static String getLangFilesByLang(String lang){
        return (String)createXPath(gfXML, "//language[@lang = '" + lang + "']/@files", XPathConstants.STRING);
    }

    /**
     * Returns the name of the form record
     * @param lang code of the language
     * @param partOfSpeech part of speech
     * @return the form record name
     */
    public static String getLincatRecord(String lang, String partOfSpeech){
        return (String)createXPath(gfXML, "//language[@lang='" + lang + "']/category[@name='"
                + partOfSpeech + "']/@lincat-record", XPathConstants.STRING);
    }

    /**
     * Returns the lin cat form fun select clause
     * @param lang code of the language
     * @param partOfSpeech part of speech
     * @return the lin cat form fun select clause
     */
    public static String getLinSelect(String lang, String partOfSpeech){
        return (String)createXPath(gfXML, "//language[@lang='" + lang + "']/category[@name='"
                + partOfSpeech + "']/@lin-select", XPathConstants.STRING);
    }

    /**
     * Evaluates an xpath expression in the context of the specified file and returns the result as the specified type
     * @param xmlFile The file to query
     * @param expression The expression to evaluate
     * @param constant The type to return
     * @return An object that encapsulated the result from the evaluation
     */
    private static Object createXPath(InputSource xmlFile, String expression, QName constant){
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            return xpath.evaluate(expression, xmlFile, constant);
        }catch(XPathExpressionException e){
            e.printStackTrace();
        }
        return null;
    }
}
