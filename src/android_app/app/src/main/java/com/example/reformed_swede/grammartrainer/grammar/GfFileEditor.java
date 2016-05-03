package com.example.reformed_swede.grammartrainer.grammar;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for manipulating GF-files. Can parse a .gf file and insert new data, or remove existing data.
 * Also contains static methods for initializing abstract or concrete files.
 * Use: Specify a file to edit in the constructor. Then call insert() or delete() as desired.
 * After all changes have been made, call saveToFile()
 */
class GfFileEditor {

    private File file;
    private LinkedHashMap<String, ArrayList<String>> fileContent;

    private static final String[] sections = {"open", "flags", "cat", "fun", "lincat", "lin", "params", "oper"};

    /**
     * Creates a new instance of GfFileEditor. The specified file will be parsed for use.
     * @param path Path of the file to manipulate.
     */
    GfFileEditor(String path){
        file = new File(path);

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF8"))){
            //Read file and store in String
            char[] buff = new char[(int)file.length()];
            br.read(buff);
            String content = new String(buff);

            //Put first line into map
            fileContent = new LinkedHashMap<>();
            Matcher matcher = matchRegex("\\{", content);
            matcher.find();
            fileContent.put("title", new ArrayList<String>());
            fileContent.get("title").add(content.substring(0, matcher.start()).trim());
            content = content.substring(matcher.start()+1, content.length());

            //Put each section into map
            for(int i = 0; i < sections.length; i++) {
                //Check if section exists
                matcher = matchRegex("\\s+" + sections[i] + "\\s", content);
                if(!matcher.find())
                    continue;
                //Create new entry in map
                fileContent.put(sections[i], new ArrayList<String>());
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
                    }else
                        break;
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Initialized an abstract gf file. I.E. creates a new file and fills it with data.
     * A freshly initialized file will have the following content:
            abstract Words =  {
            cat Noun;
            Verb;
            Adjective;
            fun }
     * @param path Path indicating where the file should be created, including filename.
     */
    static void initAbstractFile(String path){
        File file = new File(path);

        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            file.createNewFile();
            br.write("abstract Words =  { ");
            br.newLine();
            br.write("cat ");
            for(String cat : Utils.getGfCats()) {
                br.write(cat + ";");
                br.newLine();
            }
            br.write("fun }");
            br.newLine();
            br.flush();
            br.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Initialized a concrete gf file. I.E. creates a new file and fills it with data.
     * A freshly initialized file for the English language will have the following content:
             concrete WordsEng of Words = open CatEng, ParadigmsEng in {
             lincat Noun = N;
             Verb = V;
             Adjective = A;
             lin }
     * @param path Path indicating where the file should be created, including filename.
     * @param language The language of the concrete gf file.
     */
    static void initConcreteFile(String path, String language){
        File file = new File(path);
        String capitalizedLangCode = language.substring(0,1).toUpperCase() + language.substring(1, 3).toLowerCase();

        try(BufferedWriter br = new BufferedWriter(new FileWriter(file))){
            file.createNewFile();
            br.write("concrete " + Utils.nameToGf(language) + " of Words = open Cat" + capitalizedLangCode +
                    ", Paradigms" + capitalizedLangCode + " in { ");
            br.newLine();
            br.write("lincat ");
            for(String cat : Utils.getGfCats()) {
                br.write(cat + " = " + Utils.getGfCatByName(cat) + ";");
                br.newLine();
            }
            br.write("lin }");
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
     * Checks whether the text already exists in the file or not.
     * @param text The text for which to look
     * @param atSection The section at which the text should be inserted
     * @return The duplicate line, if any. Otherwise null
     */
    String isDuplicateInsertion(String text, String atSection){
        for(String line : fileContent.get(atSection)){
            if(line.startsWith(text))
                return line;
        }
        return null;
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
     * Saves the data to to file specified in the constructor.
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

    /**
     * Pretty prints a parsed file
     */
    void printFile(){
        System.out.println("Printing " + file.getAbsolutePath());
        for(Map.Entry<String, ArrayList<String>> element : fileContent.entrySet()){
            if(element.getKey().equals("title")){
                System.out.println(element.getValue().get(0) + " {");
            }else{
                System.out.println("\n\t" + element.getKey());
                for(String row : element.getValue())
                    System.out.println("\t\t" + row + " ;");
            }
        }
        System.out.println("}\n");
    }
}
