import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class pacman {
    private Image img;
    private AffineTransform tx;
    private double scaleX;
    private double scaleY;
    private double x;
    private double y;
    private int vx;
    private int vy;
    public boolean debugging = true;

    // New spin variables
    private boolean spinning = false;
    private double angle = 0;
    private long spinStartTime = 0;

    public pacman() {
        img = getImage("/imgs/pacman.gif");
        tx = AffineTransform.getTranslateInstance(0, 0);
        scaleX = 0.6;
        scaleY = 0.6;
        x = 370;
        y = 650;
        vx = 6;
        init(x, y);
    }

    public pacman(int x, int y, int scaleX, int scaleY) {
        this();
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        init(x, y);
    }

    public pacman(int x, int y, int scaleX, int scaleY, int vx, int vy) {
        this();
        this.x = x;
        this.y = y;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.vx = vx;
        this.vy = vy;
        init(x, y);
    }

    public void setVelocityVariables(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    public void changePicture(String imageFileName) {
        img = getImage("/imgs/" + imageFileName);
        init(x, y);
    }

    public void update() {
        x += vx;

        if (x >= 600) {
            vx *= -1;
        }

        if (x <= 140) {
            vx *= -1;

            if (vx == 0 && vy > 10) {
                if (y >= 750) {
                }
                vy = -(int) (Math.random() * 8 + 3);
                vx = (int) (Math.random() * 8 + 3);

                if (Math.random() < 0.5) {
                    vx *= -1;
                }
            }
        }

        if (y >= 750 && vx != 0) vy *= -1;
    }

    // NEW: method to trigger the spin
    public void spin() {
        spinning = true;
        spinStartTime = System.currentTimeMillis();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        update();

        // Handle spinning animation
        if (spinning) {
            long elapsed = System.currentTimeMillis() - spinStartTime;
            angle += 0.3; // spin speed

            // Stop spinning after 1 second
            if (elapsed > 1000) {
                spinning = false;
                angle = 0;
            }
        }

        // Reset transform and apply translation, scale, and optional rotation
        tx.setToIdentity();
        tx.translate(x, y);
        tx.scale(scaleX, scaleY);

        if (spinning) {
            tx.rotate(angle, img.getWidth(null) / 2.0, img.getHeight(null) / 2.0);
        }

        g2.drawImage(img, tx, null);

        // if(debugging) {
        // g.setColor(Color.green);
        // g.drawRect((int) x, (int) y, 100, 100);
        // }
    }

    private void init(double a, double b) {
        tx.setToTranslation(a, b);
        tx.scale(scaleX, scaleY);
    }

    private Image getImage(String path) {
        Image tempImage = null;
        try {
            URL imageURL = pacman.class.getResource(path);
            tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempImage;
    }

    public void setScale(double sx, double sy) {
        scaleX = sx;
        scaleY = sy;
        init(x, y);
    }

    public void setLocation(double newX, double newY) {
        x = newX;
        y = newY;
        init(x, y);
    }

    public boolean checkCollision(int mX, int mY) {
        Rectangle mouse = new Rectangle(mX, mY, 50, 50);
        Rectangle thisObject = new Rectangle((int) x, (int) y, 100, 100);

        if (mouse.intersects(thisObject)) {
            vx = 0;
            vy = 9;
            return true;
        } else {
            return false;
        }
    }
}
