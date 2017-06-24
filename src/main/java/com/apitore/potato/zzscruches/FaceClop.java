package com.apitore.potato.zzscruches;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.indexer.UByteBufferIndexer;
import org.bytedeco.javacpp.indexer.UByteRawIndexer;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_core.*;

public class FaceClop {

  public static void main(String[] args) {

    Mat src1 = imread("./images/image1.jpg", IMREAD_COLOR);

    imshow("src",src1);
    Mat roi = new Mat(src1,new Rect(393,846,1132,1132));
    imshow("roi",roi);

    UByteRawIndexer sI = roi.createIndexer();
    int a = sI.get(0, 0, 0);
    System.out.println(a);

  }

  private static void imshow(String txt, Mat img) {
    CanvasFrame canvasFrame = new CanvasFrame(txt);
    canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    canvasFrame.setCanvasSize(img.cols(), img.rows());
    canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
  }

}
