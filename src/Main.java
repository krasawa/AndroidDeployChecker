/**
 * Created with IntelliJ IDEA.
 * Developer: Max Marusetchenko
 * Date: 19.09.14
 * Time: 14:00
 */
public class Main {

    private final static String URL_TO_CHECK = "https://play.google.com/store/apps/details?id=com.greetzy";
    private final static String APP_NAME = "Greetzy";
    private final static int TIME_TO_WAIT = 2 * 60 * 60;   //in seconds (2 hours)
    private final static int CHECK_RATE = 60 * 10;    //in seconds (every 10 min)

    public static void main(String[] args) {
        AppDeployChecker appDeployChecker = new AppDeployChecker.Builder()
                .setAppName(APP_NAME)
                .setCheckRate(CHECK_RATE)
                .setTimeToWait(TIME_TO_WAIT)
                .setUrl(URL_TO_CHECK)
                .build();

        appDeployChecker.check();
    }
}