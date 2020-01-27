package ISOFT.APITesting.Utilities;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
public class TestParameters {
  public static String env;

    @Parameters({"selected_env"})
    @Test
    public static void Enviornment(String selected_env) {

        env = selected_env;
    }

}
