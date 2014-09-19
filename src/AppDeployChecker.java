import java.io.IOException;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Developer: Max Marusetchenko
 * Date: 19.09.14
 * Time: 14:18
 */
public class AppDeployChecker {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                                        private Builder builder;
    private AppDeployChecker(Builder builder) {
        this.builder = builder;
    }

    public void check() {
        final Runnable runnable = new Runnable() {
            public void run() {
                if (checkAppIsDeployed()) {
                    System.out.println("App " + builder.appName + " is deployed");
                } else {
                    System.out.print(".");
                }
            }
        };
        final ScheduledFuture<?> checkerHandle = scheduler.scheduleAtFixedRate(runnable, 0, builder.checkRate, TimeUnit.SECONDS);

        scheduler.schedule(new Runnable() {
            public void run() {
                checkerHandle.cancel(true);
            }
        }, builder.timeToWait, TimeUnit.SECONDS);
    }

    private boolean checkAppIsDeployed() {
        boolean result = false;

        try {
            URL u = new URL(builder.url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestMethod("GET");
            huc.connect();
            int code = huc.getResponseCode();

            result = code == HttpURLConnection.HTTP_OK;
        } catch (MalformedURLException ex) {

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static class Builder {
        private String url;
        private int timeToWait, checkRate;
        private String appName;

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setTimeToWait(int timeInSeconds) {
            this.timeToWait = timeInSeconds;
            return this;
        }

        public Builder setCheckRate(int timeInSeconds) {
            this.checkRate = timeInSeconds;
            return this;
        }

        public Builder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public AppDeployChecker build() {
            if (this.url == null || this.url.isEmpty()) {
                throw new IllegalArgumentException("URL can not be empty");
            }

            if (this.timeToWait <= 0) {
                throw new IllegalArgumentException("Time to wait must be > 0");
            }

            if (this.checkRate <= 0) {
                throw new IllegalArgumentException("Check rate must be > 0");
            }

            if (this.appName == null || this.appName.isEmpty()) {
                throw new IllegalArgumentException("Enter application name");
            }

            return new AppDeployChecker(this);
        }
    }
}
