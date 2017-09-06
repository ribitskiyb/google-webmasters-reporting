package etl.cloud.google.webmasters;

import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.SearchAnalyticsQueryRequest;
import etl.cloud.google.webmasters.internal.WebmastersReportingFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReportDownloader {

    public ReportDownloader(String pathToClientSecretsFile) throws IOException {
        this.service = WebmastersReportingFactory.getInstance(pathToClientSecretsFile);
    }

    public void download(String siteUrl, String startDate, String endDate, List<String> dimensions, String outputFile)
            throws IOException {
        checkForWriteAccess(outputFile);

        SearchAnalyticsQueryRequest content = new SearchAnalyticsQueryRequest()
                .setDimensions(dimensions)
                .setStartDate(startDate)
                .setEndDate(endDate);

        service.searchanalytics()
                .query(siteUrl, content)
                .executeAndDownloadTo(new FileOutputStream(new File(outputFile)));
    }

    private Webmasters service;

    private static void checkForWriteAccess(String path) {
        File sample = new File(path);
        try {
            sample.createNewFile();
            sample.delete();
        } catch (IOException e) {
            System.out.println("Can't write to the output directory: " + e.getMessage());
            System.exit(1);
        }
    }

}