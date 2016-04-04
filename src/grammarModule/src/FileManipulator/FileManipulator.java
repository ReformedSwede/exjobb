import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManipulator {

    private File file;
    private LinkedHashMap<String, ArrayList<String>> fileContent;
    private int index;

    private static final String[] sections = {"flags", "cat", "fun", "lincat", "lin", "params", };

    /**
     * Creates a new instance of FileManipulator.
     * @param path Path of the file to manipulate.
     */
    public FileManipulator(String path){
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
                matcher = regex("\\s+" + sections[i] + "\\s", content);
                if(!matcher.find())
                    continue;
                fileContent.put(sections[i], new ArrayList<>());
                content = content.substring(matcher.end(), content.length()).trim();

                //Set variable so that while loop knows when to stop
                int contentLengthAtNextSection;
                int j, lowest = Integer.MAX_VALUE;
                for(j = 0; j < sections.length; j++){
                    matcher = regex("\\s+" + sections[j] + "\\s", content);
                    if(matcher.find()){
                        if(matcher.start() < lowest) {
                            lowest = matcher.start();
                            break;
                        }
                    }
                }
                if(lowest == Integer.MAX_VALUE)
                    contentLengthAtNextSection = 2;
                else{
                    matcher = regex("\\s+" + sections[j] + "\\s", content);
                    matcher.find();
                    contentLengthAtNextSection = content.length() - matcher.start();
                }

                //Put each line in this section into map
                while (content.length() >= contentLengthAtNextSection) {
                    matcher = regex(";", content);
                    matcher.find();
                    fileContent.get(sections[i]).add(content.substring(0, matcher.start()).trim());
                    content = content.substring(matcher.end(), content.length()).trim();
                }
            }


        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void printFile(){
        for(Map.Entry<String, ArrayList<String>> element : fileContent.entrySet()){
            if(element.getKey().equals("title")){
                System.out.println(element.getValue().get(0) + " {");
            }else{
                System.out.println("\n\t" + element.getKey());
                for(String row : element.getValue())
                    System.out.println("\t\t" + row + " ;");
            }
        }
        System.out.println("\n}\n");
    }

    /**
     * Searches the file provided in the constructor, for the provided regular expression.
     * @param regex The regular expression to search for.
     * @param after If true, the method will insert the text after the found math, o/w before.
     * @return True, if a match was found, false o/w
     */
    /*public boolean findPlace(String regex, boolean after){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);

        boolean res = matcher.find();
        if(res)
            index = after ? matcher.end() : matcher.start();
        return res;
    }*/

    /**
     * Inserts some text at the match found with findPlace(), in the provided file.
     * @param text The text to insert.
     * @param newLine If true, a newLine will be inserted before the text.
     */
    /*public void insert(String text, boolean newLine){
        StringBuilder string = new StringBuilder(fileContent);
        string.insert(index, (newLine ? "\n" : "") + text);

        try{
            file.delete();
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bw.write(string.toString());
            bw.flush();
            bw.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }*/

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
}
