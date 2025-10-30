import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Duck {

    private Image img;
    private Image normal;
    private Image dead;
    private long time = 1220;
    public boolean isDead = false;

    private AffineTransform tx;
    private double scaleX;
    private double scaleY;
    private double x;
    private double y;

    private int vx;
    private int vy;
    private int vxBase = 30;
    private int vyBase = 30;

    public Duck() {
        normal = getImage("/imgs/BlueGhost.gif");
        dead = getImage("/imgs/BlueExplode.gif");
        img = normal;

        tx = AffineTransform.getTranslateInstance(0, 0);

        scaleX = 0.65;
        scaleY = 0.65;
        x = 600;
        y = 300;

        vx = vxBase;
        vy = vyBase;
        init(x, y);
    }

    public void setVelocityVariables(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public int getVXBase() { return vxBase; }
    public int getVYBase() { return vyBase; }

    public void update() {
        if(this.isDead) {
            time -= 16;
            if(time <= 0) {
                isDead = false;
                time = 1220;
                vx = vxBase + (int)(Math.random()*2);
                vy = vyBase + (int)(Math.random()*2);
                img = normal;
            }
        }

        x += vx;
        y += vy;

        if(x >= 800) vx *= -1;
        if(x <= 0) vx *= -1;
        if(y <= 30) vy *= -1;
        if(y >= 720 && vy > 1 && vy < 40) vy *= -1;

        if(vx == 0 && vy > 1) {
            vy = -(int)(Math.random()*8+3);
            vx = (int)(Math.random()*8+3);
            if(Math.random()<0.5) vx *= -1;
        }

        if(y >= 550 && vx != 0) vy*= -1;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(img, tx, null);
        update();
        init(x,y);
    }

    private void init(double a, double b) {
        tx.setToTranslation(a, b);
        tx.scale(scaleX, scaleY);
    }

    private Image getImage(String path) {
        Image tempImage = null;
        try {
            URL imageURL = Duck.class.getResource(path);
            tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    public boolean checkCollision(int mX, int mY) {
        Rectangle mouse = new Rectangle(mX, mY, 50, 50);
        Rectangle thisObject = new Rectangle((int)x, (int)y, 100, 100);

        if(mouse.intersects(thisObject)) {
            vx = 0;
            vy = 0;
            img = dead;
            isDead = true;
            return true;
        } else return false;
    }
}
