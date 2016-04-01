import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManipulator {

    private File file;
    private String fileContent;
    private int index;

    /**
     * Creates a new instance of FileManipulator.
     * @param path Path of the file to manipulate.
     */
    public FileManipulator(String path){
        file = new File(path);

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF8"))){
            char[] buff = new char[(int)file.length()];
            br.read(buff);
            fileContent = new String(buff);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Searches the file provided in the constructor, for the provided regular expression.
     * @param regex The regular expression to search for.
     * @param after If true, the method will insert the text after the found math, o/w before.
     * @return True, if a match was found, false o/w
     */
    public boolean findPlace(String regex, boolean after){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileContent);

        boolean res = matcher.find();
        if(res)
            index = after ? matcher.end() : matcher.start();
        return res;
    }

    /**
     * Inserts some text at the match found with findPlace(), in the provided file.
     * @param text The text to insert.
     * @param newLine If true, a newLine will be inserted before the text.
     */
    public void insert(String text, boolean newLine){
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
    }
}
