package com.apitore.potato.zzscruches;

import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_COLOR;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MsFace {

  static String ENDPOINT     = "https://westcentralus.api.cognitive.microsoft.com/face/v1.0/detect";
  static String ACCESS_TOKEN = "YOUR-ACCESS-TOKEN";
  static String[] LANDS = new String[]{
      "pupilLeft","pupilRight","noseTip",
      "mouthLeft","mouthRight","eyebrowLeftOuter",
      "eyebrowLeftInner","eyeLeftOuter","eyeLeftTop",
      "eyeLeftBottom","eyeLeftInner","eyebrowRightInner",
      "eyebrowRightOuter","eyeRightInner","eyeRightTop",
      "eyeRightBottom","eyeRightOuter","noseRootLeft",
      "noseRootRight","noseLeftAlarTop","noseRightAlarTop",
      "noseLeftAlarOutTip","noseRightAlarOutTip","upperLipTop",
      "upperLipBottom","underLipTop","underLipBottom",
      };

  @SuppressWarnings("rawtypes")
  public static void main(String[] args) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

    String url = String.format("%s?returnFaceLandmarks=true&returnFaceAttributes=emotion", ENDPOINT);

    Path path = Paths.get("./images/image1.JPG");
    byte[] data = Files.readAllBytes(path);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.set("Ocp-Apim-Subscription-Key", ACCESS_TOKEN);
    HttpEntity<byte[]> entity = new HttpEntity<byte[]>(data, headers);

    ResponseEntity<List> response = restTemplate
        .exchange(url, HttpMethod.POST, entity, List.class);
    List res = response.getBody();
    System.out.println(res);
  }

  @SuppressWarnings("rawtypes")
  public static boolean run(int[] face, double[] facevector, String input) throws IOException {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

    String url = String.format("%s?returnFaceLandmarks=true&returnFaceAttributes=emotion", ENDPOINT);
    Path path = Paths.get(input);
    byte[] data = Files.readAllBytes(path);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.set("Ocp-Apim-Subscription-Key", ACCESS_TOKEN);
    HttpEntity<byte[]> entity = new HttpEntity<byte[]>(data, headers);

    ResponseEntity<List> response = restTemplate
        .exchange(url, HttpMethod.POST, entity, List.class);
    List res = response.getBody();
    if (res.isEmpty())
      return true;
    Map mp = (Map) res.get(0);

    Map amp = (Map) mp.get("faceAttributes");
    Map emp = (Map) amp.get("emotion");
    if ((double) emp.get("anger") < 0.2 &&
        (double) emp.get("disgust") < 0.2 &&
        (double) emp.get("sadness") < 0.2)
      return true;

    Map fmp = (Map) mp.get("faceRectangle");
    face[0] = (int) fmp.get("left");
    face[1] = (int) fmp.get("top");
    face[2] = (int) fmp.get("width");
    face[3] = (int) fmp.get("height");

    Mat mat = imread(input, IMREAD_COLOR);
    UByteRawIndexer sI = mat.createIndexer();
    Map lmp = (Map) mp.get("faceLandmarks");
    for (int i=0,j=0; i<198; i+=3,j++) {
      int idx = j%27;
      Map tmp = (Map) lmp.get(LANDS[idx]);
      int x = (int)((double) tmp.get("x"));
      int y = (int)((double) tmp.get("y"));

      facevector[i]   = (sI.get(x, y, 0)-127)/128.0;
      facevector[i+1] = (sI.get(x, y, 1)-127)/128.0;
      facevector[i+2] = (sI.get(x, y, 2)-127)/128.0;
    }
    facevector[198] = 0;facevector[199] = 0;

    return false;
  }

}
