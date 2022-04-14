package ascii.version;

import ascii.version.Game;
import org.junit.Test;
import static org.junit.Assert.*;

public class QuickTests {
    //verifies that correct upper left corner of box indexes are given as a result
    @Test
    public void CheckBoxIndexesTest(){
        ascii.version.Game game = new Game();
        game.populateBoard("000400970000051600042000010030000000070508064000070000700030000300090000005864009");
        int[] answers = game.getBoxIndexes(7, 7);
        assertEquals(6, answers[0]);
        assertEquals(6, answers[1]);
    }
}
