package grammar;

import main.ResourceManager;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for manipulating GF-files. Can parse a .gf file and insert new data, or remove existing data.
 * Also contains static methods for initializing abstract or concrete .gf files.
 * How to use: Specify a file to edit in the constructor. Then call insert() or delete() as desired.
 * After all changes have been made, call saveToFile()
 */
class GfFileEditor {
    //Static list of Gf keywords. Used when parsing .gf files
    private static final String[] sections = {"open", "flags", "cat", "fun", "lincat", "lin", "params", "oper"};

    private File file; //The file to edit
    private LinkedHashMap<String, ArrayList<String>> fileContent; //The parsed contents of the file

    /**
     * Creates a new instance of GfFileEditor. The specified file will be parsed for use.
     * @param path Path of the file to manipulate.
     */
    GfFileEditor(String path){
        file = new File(path);

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF8"))){
            //Read file and store contents in String
            char[] buff = new char[(int)file.length()];
            br.read(buff);
            String content = new String(buff);

            //Put first line into map
            fileContent = new LinkedHashMap<>();
            Matcher matcher = matchRegex("\\{", content);
            matcher.find();
            fileContent.put("title", new ArrayList<>());
            fileContent.get("title").add(content.substring(0, matcher.start()).trim());
            content = content.substring(matcher.start()+1, content.length());

            //Put each remaining section into map
            for(int i = 0; i < sections.length; i++) {
                //Check if section exists
                matcher = matchRegex("\\s+" + sections[i] + "\\s", content);
                if(!matcher.find())
                    continue;
                //Create new entry in map
                fileContent.put(sections[i], new ArrayList<>());
                //Shorten content every time new data is added from content to map
                content = content.substring(matcher.end(), content.length()).trim();

                //Set variable so that the while loop knows when to stop
                int contentLengthAtNextSection = 2;
                int j, lowest = Integer.MAX_VALUE;
                for(j = i; j < sections.length; j++){
                    matcher = matchRegex("\\s+" + sections[j] + "\\s", content);
                    if(matcher.find()){
                        if(matcher.start() < lowest) {
                            lowest = matcher.start();
                            contentLengthAtNextSection = content.length() - matcher.start();
                        }
                    }
                }

                //Put each line in this section into map
                while (content.length() > contentLengthAtNextSection) {
                    matcher = matchRegex(";", content);
                    if (matcher.find()) {
                        fileContent.get(sections[i]).add(content.substring(0, matcher.start()).trim());
                        content = content.substring(matcher.end(), content.length());
                    } else
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            System.out.println(file.getName());
            throw e;
        }
    }

    /**
     * Initializes an abstract gf file. I.E. creates a new file and fills it with data.
     * @param path Path indicating where the file should be created, including filename.
     */
    static void initAbstractFile(String path){
        File file = new File(path);

        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            file.createNewFile();

            //Start filling file with contents
            br.write("abstract Words =  { ");
            br.newLine();
            br.write("flags startcat = Word ;");
            br.newLine();
            br.write("cat ");
            br.write("Word ;");
            br.newLine();
            for(String cat : ResourceManager.getPartOfSpeechCats()) {
                br.write(cat + " ;" + cat + "Form ;");
                br.newLine();
            }
            br.newLine();
            br.write("fun");
            br.newLine();
            for(String cat : ResourceManager.getPartOfSpeechCats()){
                br.write(cat.substring(0, 1) + "FormFun :" + cat + " -> " + cat + "Form -> Word ;");
                br.newLine();
                for(String inflection : ResourceManager.getInflectionRealNamesByCat(cat)) {
                    br.write(inflection + " : " + cat + "Form ;");
                    br.newLine();
                }
                br.newLine();
            }
            br.write("}");
            br.newLine();
            br.flush();
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Initializes a concrete gf file. I.E. creates a new file and fills it with data.
     * @param path Path indicating where the file should be created, including filename.
     * @param language The name of the language to init.
     */
    static void initConcreteFile(String path, String language){
        File file = new File(path);
        String lowercaseLangCode = language.substring(0,3).toLowerCase();

        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            file.createNewFile();

            //Start filling file with content
            br.write("concrete " + ResourceManager.nameToGf(language) + " of Words = open " +
                    ResourceManager.getLangFilesByLang(lowercaseLangCode) + " in { ");
            br.newLine();
            br.write("lincat ");
            br.write("Word = Str ;");
            for(String cat : ResourceManager.getPartOfSpeechCats()) {
                br.write(cat + " = " + ResourceManager.getParadigmCatByName(cat) + " ;");
                br.newLine();
                br.write(cat + "Form = {" + cat.substring(0, 1).toLowerCase() + "f:" +
                        ResourceManager.getLincatRecord(lowercaseLangCode, cat) + ";s:Str};");
                br.newLine();
            }
            br.write("lin");
            br.newLine();
            for(String cat : ResourceManager.getPartOfSpeechCats()) {
                String catFirstLetter = cat.substring(0, 1).toLowerCase();
                br.write(ResourceManager.getPartOfSpeechLinCatByName(cat) + " " + catFirstLetter + " f = f.s++" +
                    catFirstLetter + ".s ! f." + catFirstLetter + "f " +
                        ResourceManager.getLinSelect(lowercaseLangCode, cat) + " ;");
                br.newLine();
                for(String inflection : ResourceManager.getInflectionRealNamesByCat(cat)){
                    br.write(inflection + " = {" + catFirstLetter + "f=" +
                            ResourceManager.getInflectionGfNamesByCat(lowercaseLangCode, cat, inflection)
                            + " ; s=\"\"} ;");
                    br.newLine();
                }
            }
            br.write("}");
            br.newLine();
            br.flush();
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Inserts a line of text at a specified section
     * @param text The text to insert
     * @param atSection The section to insert the text into
     */
    void insert(String text, String atSection){
        fileContent.get(atSection).add(text);
    }

    /**
     * Removes an entry from the file
     * @param fun The function to remove
     * @param atSection The section from where to remove
     */
    void delete(String fun, String atSection){
        int i;
        for(i = 0; i < fileContent.get(atSection).size(); i++)
            if(fileContent.get(atSection).get(i).startsWith(fun))
                break;
        fileContent.get(atSection).remove(i);
    }

    /**
     * Calculates the number of the next function to be written into the abstract syntax file.
     * Remember! Only use on abstract syntax!
     */
    int getNextFunctionNumber(){
        List<String> funcs = fileContent.get("fun");
        String last = funcs.get(funcs.size()-1);
        Matcher matcher = matchRegex(":", last);
        matcher.find();
        int number;
        try {
            System.out.println(last.substring(3, matcher.start() - 1));
            number = Integer.parseInt(last.substring(3, matcher.start() - 1)) + 1;
        }catch(NumberFormatException e){
            number = 0;
        }
        System.out.println(number);
        return number;
    }

    /**
     * Checks if source contains match for matchRegex
     * @param regex Regular expression to search for
     * @param source The source in which to check for matches
     * @return A Matcher object
     */
    private Matcher matchRegex(String regex, String source){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(source);
    }

    /**
     * Saves the parsed (and edited) contents of the map into a real file, formatted as a .gf file should be.
     */
    void saveToFile(){
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            for(Map.Entry<String, ArrayList<String>> element : fileContent.entrySet()){
                if(element.getKey().equals("title")){
                    bw.write(element.getValue().get(0) + " {\n");
                }else{
                    bw.write("\n\t" + element.getKey() + "\n");
                    for(String row : element.getValue())
                        bw.write("\t\t" + row + " ;\n");
                }
            }
            bw.write("}\n");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
