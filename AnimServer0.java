import java.io.*;
import java.net.*;

public class AnimServer0{
  public static void main(String[] args) {
    try {
      byte buf[] = new byte[160*120];
      int i,pixel;
      BufferedInputStream biStream;
      int port = 8000;
      //サーバソケットの作成
      ServerSocket ss = new ServerSocket(port);
      System.out.println("Running...");

      while (true) {
        Socket s = ss.accept();
        biStream=new BufferedInputStream(new FileInputStream("bane.raw"));
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        /*
        for (i = 0; i<=200; i++) {
          biStream.read(buf,0,160*120);
          dos.write(buf,0,160*120);
          //System.out.println(biStream);
        }
        s.close();
        */
        for (int y = 0; y<120; y++) {
          for (int x = 0; x<160; x++) {
            pixel = (int)buf[y*160+x]*2;
            //System.out.println(pixel);
            if (pixel < 0) {
              continue;
            }
            else{
              biStream.read(buf,0,160*120);
              dos.write(buf,0,160*120);
            }
          }
        }
        System.out.println("Done.");
        s.close();
      }
    } catch(Exception e) {
      System.out.println("Exception:"+e);
    }
  }
}
