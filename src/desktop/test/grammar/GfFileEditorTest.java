package grammar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class GfFileEditorTest {

    private GfFileEditor editor;

    @Before
    public void setUp(){
        GfFileEditor.initConcreteFile("Test.gf", "English");
        editor = new GfFileEditor("Test.gf");
    }

    @After
    public void breakDown(){
        editor.delete("Stuff", "lin");
        new File("Test.gf").delete();
    }
}
