package com.chat.fx.application.chatapplication.utils;

import com.chat.fx.application.chatapplication.config.BearerContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import static com.chat.fx.application.chatapplication.config.FileConfig.storeDirectory;

@Component
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static HttpClient createHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (TrustStrategy) (arg0, arg1) -> true).build();
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslConnectionSocketFactory)
                .build();

        PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);

        return HttpClientBuilder.create().setSSLContext(sslContext).setConnectionManager(connMgr).build();
    }

    public ResponseEntity<?> callHttpPost(String apiUrl, Object object)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpClient httpClient = createHttpClient();
        HttpPost httpPost = new HttpPost(apiUrl);

        //set authen token
        if (BearerContext.getBearerToken() != null) {
            httpPost.addHeader("Authorization", BearerContext.getBearerToken());
        }

        if (object != null) {
            StringEntity requestBody = new StringEntity(OBJECT_MAPPER.writeValueAsString(object));
            requestBody.setContentType("application/json");
            httpPost.setEntity(requestBody);
        }

        HttpResponse httpResponse = httpClient.execute(httpPost);
        return checkResponse(httpResponse);
    }

    public void callHttpPostWithFileUpload(String apiUrl, File file) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpPost httpPost = new HttpPost(apiUrl);

        //set authen token
        if (BearerContext.getBearerToken() != null) {
            httpPost.addHeader("Authorization", BearerContext.getBearerToken());
        }

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addBinaryBody("file", file, ContentType.IMAGE_JPEG, file.getName());
        httpPost.setEntity(builder.build());
        HttpClient httpClient = createHttpClient();
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() != 200) {
            logger.debug("Upload file failed!");
        }
    }

    public ResponseEntity<?> callHttpGet(String apiUrl)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpGet httpGet = new HttpGet(apiUrl);

        //set authen token
        if (BearerContext.getBearerToken() != null) {
            httpGet.addHeader("Authorization", BearerContext.getBearerToken());
        }

        HttpClient httpClient = createHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpGet);

        return checkResponse(httpResponse);
    }

    public boolean callHttpGetDownloadFile(String apiUrl)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        HttpGet httpGet = new HttpGet(apiUrl);

        //set authen token
        if (BearerContext.getBearerToken() != null) {
            httpGet.addHeader("Authorization", BearerContext.getBearerToken());
        }

        HttpClient httpClient = createHttpClient();
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            BufferedInputStream bis = new BufferedInputStream(entity.getContent());
            String filePath = storeDirectory + File.separator + "SC.jpg";
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
            int inByte;
            while((inByte = bis.read()) != -1) bos.write(inByte);
            bis.close();
            bos.close();
            return true;
        }
        return false;
    }

    private static ResponseEntity<?> checkResponse(HttpResponse httpResponse) throws IOException{
        String line;
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.NO_CONTENT.value()) {
            return new ResponseEntity<>("No content", HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()))){
            while ((line = rd.readLine()) != null) {
                stringBuilder.append(line);
            }
        }

        return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.valueOf(httpResponse.getStatusLine().getStatusCode()));
    }
}
