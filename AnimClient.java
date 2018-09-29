import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

class AnimClient{
  public static void main(String[] args) {
    if (args.length == 0) {
      System.out.println("コマンドラインで相手を入力してください");
      System.exit(1);
    }
    AppFrame2 f = new AppFrame2(args[0]);
    f.setSize(640,480);
    f.addWindowListener(new WindowAdapter(){
      @Override public void windowClosing(WindowEvent e){
        System.exit(0);
      }
    });
    f.setVisible(true);
  }
}

class AppFrame2 extends Frame{
  ImageSocket imgsock;
  //int num = 0;
  AppFrame2(String hostname){
    imgsock = new ImageSocket(hostname);
  }
  @Override public void update(Graphics g){paint(g);}
  @Override public void paint(Graphics g){
    Image img = imgsock.loadNextFrame();
    if (img != null) {
      g.drawImage(img,10,100,480,360/2,this);
      repaint(1);
      //num++;
    }
    else {
      //System.out.println(num);
      //System.exit(0);//流し終わったら終了
    }
  }
}

class ImageSocket{
  private BufferedInputStream biStream;
  private BufferedImage bImage;
  byte buf[];
  int port = 8000;
  ImageSocket(String hostname){
    buf = new byte[160*120];
    bImage = new BufferedImage(160,120,BufferedImage.TYPE_BYTE_GRAY);
    try {
      Socket s = new Socket(hostname,port);
      InputStream is = s.getInputStream();
      biStream = new BufferedInputStream(is);
    }/*
    catch(ArrayIndexOutOfBoundsException e) {
      System.out.println("コマンドラインに相手が指定されていません");
      System.out.println("java AnimClient 'IP address'or'hostname' ");
    }*/
    catch(Exception e) {
      System.out.println("Exception:→"+e);
    }
  }
  Image loadNextFrame(){
    try {
      int b = 0;
      while (b < 160*120) {
        b += biStream.read(buf,b,160*120 - b);
      }
      int x,y,pixel;
      for (y=0; y<120; y++) {
        for (x=0; x<160; x++) {
          pixel = (int)buf[y*160+x]*2;
          if (pixel < 0) {
            biStream.close();
            System.out.println("Done.");
            return null;
          }
          pixel = new Color(pixel,pixel,pixel).getRGB();
          bImage.setRGB(x,y,pixel);
        }
      }
    }
    catch(Exception e) {
      System.err.println("Exception:"+e);
    }
    return bImage;
  }
}
