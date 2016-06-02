package mk.mlabs.najdicimer.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mile on 10/05/2016.
 */
public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    public void addFormField(String name, String value) {
        writer.append("--").
                append(boundary).
                append(LINE_FEED).
                append("Content-Disposition: form-data; name=\"").
                append(name).append("\"").
                append(LINE_FEED).
                append("Content-Type: text/plain; charset=").
                append(charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    public void addFilePart(String fieldName, byte[] fileBytes)
            throws IOException {
        writer.append("--")
                .append(boundary)
                .append(LINE_FEED)
                .append("Content-Disposition: form-data; name=\"")
                .append(fieldName)
                .append("\"; filename=\"")
                .append("file.png")
                .append("\"")
                .append(LINE_FEED)
                .append("Content-Type: image/png")
                .append(LINE_FEED)
                .append("Content-Transfer-Encoding: binary")
                .append(LINE_FEED)
                .append(LINE_FEED);
        writer.flush();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    public String finish() throws IOException {
        writer.append(LINE_FEED).flush();
        writer.append("--")
                .append(boundary)
                .append("--")
                .append(LINE_FEED)
                .close();
        String data = "";
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                data = data + line;
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }
        return data.isEmpty() ? null : data;
    }
}
