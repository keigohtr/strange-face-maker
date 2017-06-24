package com.apitore.potato.zzscruches;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MsComputerVision {

  static String ENDPOINT     = "https://westcentralus.api.cognitive.microsoft.com/vision/v1.0/analyze";
  static String ACCESS_TOKEN = "YOUR-ACCESS-TOKEN";

  public static void main(String[] args) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

    String url = String.format("%s?visualFeatures=Tags", ENDPOINT);

    Path path = Paths.get("./images/image3.JPG");
    byte[] data = Files.readAllBytes(path);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.set("Ocp-Apim-Subscription-Key", ACCESS_TOKEN);
    HttpEntity<byte[]> entity = new HttpEntity<byte[]>(data, headers);

    ResponseEntity<Map> response = restTemplate
        .exchange(url, HttpMethod.POST, entity, Map.class);
    Map res = response.getBody();
    System.out.println(res);
    List<Map> list = (List<Map>) res.get("tags");
    for (Map mp: list)
      System.out.println(mp.get("name"));
  }

}
