package grammar;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is under construction, lots of code is still missing!
 */
public class GfCloudProxy {

    private static final String dir = "/tmp/gfse.3295756966818776490";
    private InputStream is;

    /**
     * This generates a new working directory on the server, e.g. /tmp/gfse.123456.
     * Most of the cloud service commands require that a working directory is specified in the dir parameter.
     * The working directory is persistent, so clients are expected to remember and reuse it.
     * Access to previously uploaded files requires that the same working directory is used.
     * @return
     * @throws Exception
     */
    public boolean newDir() throws Exception{
        //TODO!
        return false;
    }

    /**
     * This command can be used to check GF source code for syntax errors.
     * It also converts GF source code to the JSON representation used in GFSE (the cloud-based GF grammar editor).
     * @param path
     * @return
     * @throws Exception
     */
    public boolean parse(File path) throws Exception{
        int response = sendGet("/parse?path=" + path);
        return response == 200;
    }

    /**
     * Upload files to be stored in the cloud.
     * The response code is 204 if the upload was successful.
     * @param files
     * @return
     * @throws Exception
     */
    public boolean upload(File... files) throws Exception{
        String command = "";
        for (int i = 0; i < files.length; i++)
            command = command + "path" + i+1 + "=" + files[i].getAbsolutePath();
        int response = sendGet("/cloud?dir=" + dir + "&command=upload&" + command);
        return response == 204;
    }

    /**
     * Upload grammar files and compile them into a PGF file. Example response:
     * {
     *    "errorcode":"OK", // "OK" or "Error"
     *    "command":"gf -s -make FoodsEng.gf FoodsSwe.gf FoodsChi.gf",
     *    "output":"\n\n" // Warnings and errors from GF
     * }
     * @param files
     * @return
     * @throws Exception
     */
    public boolean make(String... files) throws Exception{
        String command = "";
        for (int i = 0; i < files.length; i++)
            command = command + "path" + i+1 + "=" + files[i];
        int response = sendGet("/cloud?dir=" + dir + "&command=make&" + command);
        return response == 200;
    }

    /**
     * Like command=make, except you can leave the sources parts empty to reuse previously uploaded files.
     * @param files
     * @return
     * @throws Exception
     */
    public boolean remake(String... files) throws Exception{
        String command = "";
        for (int i = 0; i < files.length; i++)
            command = command + "path" + i+1 + "=" + files[i];
        int response = sendGet("/cloud?dir=" + dir + "&command=make&" + command);
        return response == 200;
    }

    /**
     * Download the specified file.
     * @param path
     * @return
     * @throws Exception
     */
    public boolean download(String path) throws Exception{
        int response = sendGet("/cloud?dir=" + dir + "&command=download&file=" + path);
        return response == 200;
    }

    /**
     * List files with the specified extension, e.g.
     * ["Foods.pgf","Letter.pgf"].
     * @param extension
     * @return
     * @throws Exception
     */
    public List<String> ls(String extension) throws Exception{
        int response = sendGet("/cloud?dir=" + dir + "&command=ls&ext=" + extension);

        if(response != 200)
            throw new HTTPException(response);

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine;
        List<String> list = new ArrayList<>();
        while ((inputLine = br.readLine()) != null)
            list.add(inputLine);
        br.close();

        return list;
    }

    /**
     * Remove the specified file.
     * @param path
     * @return
     * @throws Exception
     */
    public boolean remove(String path) throws Exception{
        int response = sendGet("/cloud?dir=" + dir + "&command=rm&file=" + path);
        return response == 200;
    }

    /**
     * Combine server directories. This is used by GFSE to share grammars between multiple devices.
     * @param newDir
     * @return
     * @throws Exception
     */
    public boolean linkDirectories(String newDir) throws  Exception{
        int response = sendGet("/cloud?dir=" + dir + "&command=link_directories&newdir=" + newDir);
        return response == 200;
    }

    /**
     * TODO Fix this !
     * @param command
     * @return
     * @throws Exception
     */
    private int sendGet(String command) throws Exception {
        URL url = new URL("http://cloud.grammaticalframework.org/" + command);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        is = con.getInputStream();
        return con.getResponseCode();
    }

    /**     *
     * TODO Fix this!
     * @param params
     * @return
     * @throws Exception
     */
    private int sendPost(String params) throws Exception {

        URL url = new URL("http://cloud.grammaticalframework.org/");
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        is = con.getInputStream();
        return con.getResponseCode();
    }

}
