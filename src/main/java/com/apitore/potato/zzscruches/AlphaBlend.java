package com.apitore.potato.zzscruches;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

public class AlphaBlend {

  public static void main(String[] args) {
    double alpha = 0.5;
    double beta;

    Mat src1 = imread("./images/background.png", IMREAD_COLOR);
    Mat src2 = imread("./images/sunflower03.png", IMREAD_COLOR);

    imshow("before",src1);

    Mat roi = new Mat(src1,new Rect(0,0,src2.cols(),src2.rows()));
    beta = (1.0 - alpha);
    addWeighted(roi,alpha,src2,beta,0.0,roi);

    imshow("after",src1);


  }

  private static void imshow(String txt, Mat img) {
    CanvasFrame canvasFrame = new CanvasFrame(txt);
    canvasFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
    canvasFrame.setCanvasSize(img.cols(), img.rows());
    canvasFrame.showImage(new OpenCVFrameConverter.ToMat().convert(img));
  }

  public static void run(String input, int[] face, String netafile) {
    double alpha = 0.2;
    double beta;

    Mat src1 = imread(netafile, IMREAD_COLOR);
    Mat src2 = imread(input, IMREAD_COLOR);
    //imshow("src1",src1);
    //imshow("src2",src2);

    Mat fm = new Mat(src2, new Rect(face[0],face[1],face[2],face[3]));
    int sx = fm.cols(); int sy = fm.rows();
    if (300<sx || 300<sy) {
      double bx = 300.0/sx; double by = 300.0/sy;
      double bi = (bx>by) ? by:bx;
      Mat dst = new Mat((int)(fm.rows()*bi), (int)(fm.cols()*bi), fm.type());

      resize(fm,dst,dst.size());
      dst.copyTo(fm);
      //imshow("check",fm);
    }

    sx = fm.cols(); sy = fm.rows();
    int tx = src1.cols(); int ty = src1.rows();
    if (tx<sx || ty<sy) {
      int bx = sx/tx + 1; int by = sy/ty + 1;
      int bi = (bx>by) ? bx:by;
      Mat dst = new Mat(src1.rows()*bi, src1.cols()*bi, src1.type());

      resize(src1,dst,dst.size());
      dst.copyTo(src1);
      //imshow("check",src1);
    }

    Mat sm = new Mat(src1, new Rect((src1.cols()-sx)/2,(src1.rows()-sy)/2,sx,sy));
    beta = (1.0 - alpha);
    addWeighted(sm,alpha,fm,beta,0.0,sm);

    imshow("after",src1);
  }

}
