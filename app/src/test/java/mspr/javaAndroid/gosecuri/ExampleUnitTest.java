package mspr.javaAndroid.gosecuri;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void InformationCNIFr_isCorrect(){
        String name;
        String firstName;
        String CardId;

        String testingParse [][]={
                {"IDFRASELENOUILLE<<<<<<<<<<<<<<<855410","SELENOUILLE"},
                {"9409923102854CORRINNE<<<<<<<6512068F4","CORRINNE","9409923102854"},
                {"IDFRADUPONT<<<<<<<<<<<<<<<<<<<<951034","DUPONT"},
                {"1234567855483JEAN<<<<<<<<<<<0543548M7","JEAN","1234567855483"},
                {"IDFRACHINAMAN<<<<<<<<<<<<<<<<<<411734","CHINAMAN"},
                {"9834589455483JOHN<<<<<<<<<<<0543556P7","JOHN","9834589455483"},
                {"IDFRASAQCUET<<<<<<<<<<<<<<<<<<<5624O4","SAQCUET"},
                {"1234545215483BILBON<<<<<<<<<0543548U7","BILBON","1234545215483"}
        };

        /*for (String[] test: testingParse) {
            mspr.javaAndroid.gosecuri.controller.Detection.textRecognition.TextDetection

        }*/


    }
}