import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.unitils.reflectionassert.ReflectionAssert;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ProjectTest {

    private static ByteArrayOutputStream outContent;
    private static PrintStream originalOut;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @ParameterizedTest
    @MethodSource("getDirectionTestParameters")
    public void getDirectionTest(String currentMove, int currentDirection, int expected) {
        int actual = Project.getDirection(currentDirection, currentMove);
        assertEquals(expected, actual);
    }

    private static Stream<Arguments> getDirectionTestParameters() {
        return Stream.of(
                Arguments.of("R", 1, 4),
                Arguments.of("R", 2, 3),
                Arguments.of("R", 3, 1),
                Arguments.of("R", 4, 2),
                Arguments.of("L", 1, 3),
                Arguments.of("L", 2, 4),
                Arguments.of("L", 3, 2),
                Arguments.of("L", 4, 1)
        );
    }

    @ParameterizedTest
    @MethodSource("getCoordinatesTestParameters")
    public void getCoordinatesTest(int[][] outputArray, int x, int y, String currentMove, int currentDirection, int penState, int[] expected) {
        int[] actual = Project.getCoordinates(outputArray, x, y, currentMove, currentDirection, penState);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
        if (currentMove.equalsIgnoreCase("D")) {
            assertEquals(1, outputArray[x][y]);
        }
        if (currentMove.length() > 1 && (currentMove.charAt(0) == 'M' || currentMove.charAt(0) == 'm') && penState == 1) {
            int numberOfSpaces = Integer.valueOf(currentMove.substring(2));
            int[][] expectedOutputArray = new int[5][5];
            Project.draw(expectedOutputArray, currentDirection, numberOfSpaces, x, y);
            Assert.assertArrayEquals(expectedOutputArray, outputArray);
        }
    }

    private static Stream<Arguments> getCoordinatesTestParameters() {
        int[] expected1 = new int[]{0, 0, 1, 0};
        int[] expected2 = new int[]{0, 0, 2, 1};
        int[] expected3 = new int[]{0, 0, 4, 0};
        int[] expected4 = new int[]{0, 0, 2, 1};
        int[] expected5 = new int[]{2, 0, 1, 0};
        int[] expected6 = new int[]{2, 0, 1, 1};
        int[] expected7 = new int[]{1, 2, 2, 0};
        int[] expected8 = new int[]{1, 2, 2, 1};
        int[] expected9 = new int[]{2, 0, 3, 0};
        int[] expected10 = new int[]{2, 0, 3, 1};
        int[] expected11 = new int[]{1, 4, 4, 0};
        int[] expected12 = new int[]{1, 4, 4, 1};

        return Stream.of(
                Arguments.of(new int[5][5], 0, 0, "U", 1, 0, expected1),
                Arguments.of(new int[5][5], 0, 0, "D", 2, 0, expected2),
                Arguments.of(new int[5][5], 0, 0, "R", 1, 0, expected3),
                Arguments.of(new int[5][5], 0, 0, "L", 3, 1, expected4),
                Arguments.of(new int[5][5], 0, 0, "M 2", 1, 0, expected5),
                Arguments.of(new int[5][5], 0, 0, "M 2", 1, 1, expected6),
                Arguments.of(new int[5][5], 2, 2, "M 1", 2, 0, expected7),
                Arguments.of(new int[5][5], 2, 2, "M 1", 2, 1, expected8),
                Arguments.of(new int[5][5], 2, 2, "M 2", 3, 0, expected9),
                Arguments.of(new int[5][5], 2, 2, "M 2", 3, 1, expected10),
                Arguments.of(new int[5][5], 1, 1, "M 3", 4, 0, expected11),
                Arguments.of(new int[5][5], 1, 1, "M 3", 4, 1, expected12)
        );
    }

    @ParameterizedTest
    @MethodSource("drawTestParameters")
    public void drawTest(int currentDirection, int numberOfSpaces, int x, int y, int[][] expected) {
        int[][] outputArray = new int[5][5];
        Project.draw(outputArray, currentDirection, numberOfSpaces, x, y);
        Assert.assertArrayEquals(expected, outputArray);
    }

    private static Stream<Arguments> drawTestParameters() {
        int[][] expected1 = new int[][]{{0, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        int[][] expected2 = new int[][]{{0, 0, 1, 0, 0}, {0, 0, 1, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        int[][] expected3 = new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 1, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        int[][] expected4 = new int[][]{{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 1, 1, 1}, {0, 0, 0, 0, 0}};
        return Stream.of(
                Arguments.of(1, 2, 0, 0, expected1),
                Arguments.of(2, 2, 2, 2, expected2),
                Arguments.of(3, 1, 2, 2, expected3),
                Arguments.of(4, 3, 3, 1, expected4)
        );
    }

    @Test
    public void printMyArrayTestOne() {
        int[][] outputArray = new int[][]{{0, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {1, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};
        String expected = "          \n" + "          \n" + "*         \n" + "*         \n" + "         ";
        Project.printMyArray(outputArray);
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void printMyArrayTestTwo() {
        int[][] outputArray = new int[][]{{0, 1, 1, 1, 0}, {1, 0, 0, 0, 1}, {1, 0, 1, 0, 0}, {0, 1, 0, 0, 0}, {1, 1, 0, 0, 0}};
        String expected = "* *       \n" + "  *       \n" + "*   *     \n" + "*       * \n" + "  * * *";
        Project.printMyArray(outputArray);
        assertEquals(expected, outContent.toString().trim());
    }

    @Test
    public void printMyArrayTestThree() {
        int[][] outputArray = new int[][]{{0, 0, 0, 0, 0}, {1, 0, 1, 0, 1}, {0, 0, 1, 0, 0}, {0, 1, 1, 0, 0}, {1, 0, 0, 0, 0}};
        String expected = "1 0 0 0 0 \n" + "0 1 1 0 0 \n" + "0 0 1 0 0 \n" + "1 0 1 0 1 \n" + "0 0 0 0 0";
        Project.printMyArray(outputArray);
        assertEquals(expected, outContent.toString().trim());
    }

    @Test
    public void recurseTestOne() {
        List<String> inputArray = new ArrayList<>(Arrays.asList("I 10", "C", "D", "C", "M 4", "C", "R", "C", "M 3", "C", "P", "Q"));
        String expected = "Position: 0, 0 - Pen: up - Facing: north\r\n" +
                "Position: 0, 0 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: east\r\n" +
                "Position: 3, 4 - Pen: down - Facing: east\r\n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "* * * *             \n" +
                "*                   \n" +
                "*                   \n" +
                "*                   \n" +
                "*                   " +
                "";
        Project.recurse(new int[0][0], 0, 0, 1, 0, inputArray, 0);
        
        for (int i = 0; i < expected.length(); i++)
        	assertEquals(expected.charAt(i), outContent.toString().charAt(i));
        


       System.out.print(expected.length());

       System.out.print(outContent.toString().length());
    }

    @Test
    public void recurseTestTwo() {
        List<String> inputArray = new ArrayList<>(Arrays.asList("I 10", "C", "D", "C", "M 4", "C", "R", "C", "M 3", "C", "P", "c", "r", "m 2", "p", "Q"));
        String expected = "Position: 0, 0 - Pen: up - Facing: north\r\n" +
                "Position: 0, 0 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: east\r\n" +
                "Position: 3, 4 - Pen: down - Facing: east\r\n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "1 1 1 1 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "\r\n" +
                "Position: 3, 4 - Pen: down - Facing: east\r\n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "1 1 1 1 0 0 0 0 0 0 \n" +
                "1 0 0 1 0 0 0 0 0 0 \n" +
                "1 0 0 1 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0";
        Project.recurse(new int[0][0], 0, 0, 1, 0, inputArray, 0);

        ReflectionAssert.assertReflectionEquals(expected, outContent.toString().trim());
       
    }

    @Test
    public void recurseTestThree() {
        List<String> inputArray = new ArrayList<>(Arrays.asList("I 10", "C", "D", "C", "M 4", "C", "R", "C", "M 3", "C", "P", "c", "r", "m 2", "p", "l", "m 3", "L", "m 5", "C", "p", "Q"));
        String expected = "Position: 0, 0 - Pen: up - Facing: north\r\n" +
                "Position: 0, 0 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: north\r\n" +
                "Position: 0, 4 - Pen: down - Facing: east\r\n" +
                "Position: 3, 4 - Pen: down - Facing: east\r\n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "1 1 1 1 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "\r\n" +
                "Position: 3, 4 - Pen: down - Facing: east\r\n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "1 1 1 1 0 0 0 0 0 0 \n" +
                "1 0 0 1 0 0 0 0 0 0 \n" +
                "1 0 0 1 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "\r\n" +
                "Position: 6, 7 - Pen: down - Facing: north\r\n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 0 0 0 0 \n" +
                "0 0 0 0 0 0 1 0 0 0 \n" +
                "0 0 0 0 0 0 1 0 0 0 \n" +
                "0 0 0 0 0 0 1 0 0 0 \n" +
                "1 1 1 1 0 0 1 0 0 0 \n" +
                "1 0 0 1 0 0 1 0 0 0 \n" +
                "1 0 0 1 1 1 1 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0 \n" +
                "1 0 0 0 0 0 0 0 0 0";
        Project.recurse(new int[0][0], 0, 0, 1, 0, inputArray, 0);
        assertEquals(expected, outContent.toString().trim());
    }
    
    @Test
    public void recurseTestfour() {
        List<String> inputArray = new ArrayList<>(Arrays.asList("I 10", "C", "C", "M 4", "C", "R", "C", "M 3", "C", "P", "Q"));
        String expected = "Position: 0, 0 - Pen: up - Facing: north\r\n" +
                "Position: 0, 0 - Pen: up - Facing: north\r\n" +
                "Position: 0, 4 - Pen: up - Facing: north\r\n" +
                "Position: 0, 4 - Pen: up - Facing: east\r\n" +
                "Position: 3, 4 - Pen: up - Facing: east\r\n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "                    \n" +
                "\r\n";
        Project.recurse(new int[0][0], 0, 0, 1, 0, inputArray, 0);
        
        assertEquals(expected, outContent.toString());

    }


}
