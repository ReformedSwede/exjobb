package grammar;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is for manipulating GF-files.
 *
 * It can parse an .gf file and read its data, insert new data, or change existing data.
 */
class GfFileEditor {

    private File file;
    private LinkedHashMap<String, ArrayList<String>> fileContent;

    private static final String[] sections = {"open", "flags", "cat", "fun", "lincat", "lin", "params", "oper"};

    /**
     * Creates a new instance of GfFileEditor.
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
            Matcher matcher = regex("\\{", content);
            matcher.find();
            fileContent.put("title", new ArrayList<>());
            fileContent.get("title").add(content.substring(0, matcher.start()).trim());
            content = content.substring(matcher.start()+1, content.length());

            //Put each section into map
            for(int i = 0; i < sections.length; i++) {
                //Check if section exists
                matcher = regex("\\s+" + sections[i] + "\\s", content);
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
                    matcher = regex("\\s+" + sections[j] + "\\s", content);
                    if(matcher.find()){
                        if(matcher.start() < lowest) {
                            lowest = matcher.start();
                            contentLengthAtNextSection = content.length() - matcher.start();
                        }
                    }
                }

                //Put each line in this section into map
                while (content.length() > contentLengthAtNextSection) {
                    matcher = regex(";", content);
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
     * Pretty prints a parsed file
     */
    void printFile(){
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

    /**
     * Inserts a line of text at a specified section
     * @param text The text to insert
     * @param atSection The section to insert the text
     */
    void insert(String text, String atSection){
        fileContent.get(atSection).add(text);
    }

    /**
     * Removes an entry from the file
     * @param args
     */
    void delete(String... args){
        //TODO: remove
    }

    /**
     * Checks if source contains match for regex
     * @param regex Regular expression to search for
     * @param source The source in which to check for matches
     * @return A Matcher object
     */
    private Matcher regex(String regex, String source){
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(source);
    }

    /**
     * Saves the data to disk
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
