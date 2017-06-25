package com.apitore.potato;


import java.io.IOException;

import com.apitore.potato.zzscruches.AlphaBlend;
import com.apitore.potato.zzscruches.MsFace;
import com.apitore.potato.zzscruches.MsImageSearch;
import com.apitore.potato.zzscruches.Word2Vec;


public class StrangeFaceMaker {

  public static void main(String[] args) throws IOException {
    String input = "./images/Image5.JPG";
    if (args.length > 0 && args[0]!=null)
      input = args[0];

    int[] face   = new int[4];
    double[] facevector = new double[200];
    if (MsFace.run(face, facevector, input))
      return;

    String neta = Word2Vec.run(facevector);
    String netafile = MsImageSearch.run(neta);

    AlphaBlend.run(input,face,netafile);
  }

}
