import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;

public class Scoreboard {
    ArrayList<String> strings = new ArrayList<>();
    ArrayList<MyPoint> points = new ArrayList<>();
    AffineTransform tx;
    AffineTransformOp op;
    BufferedImage imageBackgroung;

    public Scoreboard(double size, double wight, double hight, String adressFile) throws IOException {

        FileReader reader = new FileReader(adressFile);
        Scanner scanner=new Scanner(reader);
        for (int i = 0; i < 10; i++){
            Integer iIn = i+1;
            strings.add(scanner.nextLine());
        }
        Collections.sort(strings, Collections.reverseOrder ());

        this.imageBackgroung = ImageIO.read(new File("data\\Buttom.png"));
        double k = (size) / (double) imageBackgroung.getHeight();
        tx = AffineTransform.getScaleInstance(k, k);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        imageBackgroung = op.filter(imageBackgroung, null);
        double locationX = imageBackgroung.getWidth() / 2;
        double locationY = imageBackgroung.getHeight() / 2;
        tx = AffineTransform.getRotateInstance(0.0, locationX, locationY);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        double x = wight/2 - imageBackgroung.getWidth() - imageBackgroung.getHeight()/8.0+ Constants.R / 2;
        double y = hight / 2 - 5/4*imageBackgroung.getHeight();
        for (int i = 0; i < 5; i++){
            points.add(new MyPoint(x, y + i * 1.15*imageBackgroung.getHeight()));
        }
        x = wight/2 + imageBackgroung.getHeight()/8.0 + Constants.R / 2;
        for (int i = 0; i < 5; i++){
            points.add(new MyPoint(x, y + i * 1.15*imageBackgroung.getHeight()));
        }
    }

    public void paint(Graphics g){
        for (int i = 0; i < 10; ++i){
            g.drawImage(imageBackgroung, (int)points.get(i).x, (int)points.get(i).y, null);
            Integer integer = i+1;
            g.setFont(new Font("TimesRoman",Font.BOLD + Font.ITALIC,40));
            if (i==9){
                g.drawString(integer.toString() + "." + strings.get(i), (int)points.get(i).x, (int)points.get(i).y + imageBackgroung.getHeight()/4*3);
            }else{
                g.drawString(integer.toString() + ". " + strings.get(i), (int)points.get(i).x + imageBackgroung.getWidth()/20, (int)points.get(i).y + imageBackgroung.getHeight()/4*3);
            }
        }
    }

}
