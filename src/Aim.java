import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Aim {
    BaseBot bot;
    BufferedImage image;

    Boolean flagPrint = false;

    public Aim(){}

    public Aim(BaseBot bot) throws IOException {
        this.bot = bot;
        this.image = ImageIO.read(new File("data\\aim.png"));
        flagPrint = true;
    }

    public void print(Graphics g){
        if (flagPrint){
            g.drawImage(image, (int)(bot.x - Constants.R/2), (int)(bot.y - Constants.R/2), Constants.R, Constants.R, null);
        }
    }
}
