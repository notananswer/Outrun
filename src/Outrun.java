import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import java.text.Normalizer;

class Outrun {

//	public static int width = 1024;
//	public static int height = 768;
	public static int width = 800;
	public static int height = 600;
	public static int roadW = 2000;
	public static int segL = 200; //segment length
	public static double camD = 0.84; //camera depth

	private static Color grass1  = new Color(16,200,16); //    Color grass  = (n/3)%2?Color(16,200,16):Color(0,154,0);
	private static Color grass2  = new Color(0,154,0); //    Color grass  = (n/3)%2?Color(16,200,16):Color(0,154,0);
	private static Color rumble1 = new Color(255,255,255);//    Color rumble = (n/3)%2?Color(255,255,255):Color(0,0,0);
	private static Color rumble2 = new Color(0,0,0);//    Color rumble = (n/3)%2?Color(255,255,255):Color(0,0,0);
//	private static Color road1   = new Color(107,107,107);//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);
//	private static Color road2   = new Color(100,100,100);//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);

	private static Color bgColor = new Color(105,205,4);//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);

//	private static String nonnormal = "Tohle je nìjakej èùráckej textíèek..";
//	private static String normal = Normalizer.normalize(nonnormal, Normalizer.Form.NFD);

    private static MyFrameOutrun frame;
    BufferedImage img, buffer;
    Graphics2D g, g2;
    BufferedImage road1tex, road2tex;

    public static final String[] spritesTilesNames={"images/1.png","images/2.png","images/3.png","images/4.png","images/5.png","images/6.png","images/7.png" };
    public static Image object[];

void drawQuad(Graphics2D g, Color c, int x1,int y1,int w1,int x2,int y2,int w2)
{
	g.setColor(c);
    int[] x = new int[]{x1-w1,x2-w2,x2+w2,x1+w1};
    int[] y = new int[]{y1,y2,y2,y1};
    g.fillPolygon (x, y, x.length);
}

void drawQuadRoad(Graphics2D g, int c, int x1,int y1,int w1,int x2,int y2,int w2)
{
//	//g.setColor(c);
//    int[] x = new int[]{x1-w1,x2-w2,x2+w2,x1+w1};
//    int[] y = new int[]{y1,y2,y2,y1};
//    if (c>0) {
//    g.drawImage(road1tex, x1-w1, y1, x1+w1, y1+1, 0, 0, 550, 1, null); }
//    //app.drawImage(s, (int) (destX-destW), (int) (destY), (int)(destX),(int)(destY+destH),0,0,w,h,null);
//    else {
//        g.drawImage(road2tex, x1-w1, y1, x1+w1, y1+1, 0, 0, 550, 1, null); }
//    //g.fillPolygon (x, y, x.length);
	double x1s = x1 - w1;
	double x1e = x1 + w1;
	double x2s = x2 - w2;
	double x2e = x2 + w2;

	double smernice1s = (x1s - x2s) / (y1 - y2);
	double smernice1e = (x1e - x2e) / (y1 - y2);

	double iterXs = x1s;
	double iterXe = x1e;

	for(int y=y2;y<y1;y++) {
//	    int[] x = new int[]{x1-w1,x2-w2,x2+w2,x1+w1};
//	    int[] y = new int[]{y1,y2,y2,y1};

	    if (c>0) {
	        	g.drawImage(road1tex, (int)iterXs, y, (int)iterXe, y+1, 0, 0, 550, 1, null); }
	        //app.drawImage(s, (int) (destX-destW), (int) (destY), (int)(destX),(int)(destY+destH),0,0,w,h,null);
	        else {
	            g.drawImage(road2tex, (int)iterXs, y, (int)iterXe, y+1, 0, 0, 550, 1, null); }
	    iterXs +=smernice1s;
	    iterXe +=smernice1e;
/*		if (c>0) {
	        g.drawImage(road1tex, x1s, y, x1e, y+1, 0, 0, 550, 1, null); }
	        //app.drawImage(s, (int) (destX-destW), (int) (destY), (int)(destX),(int)(destY+destH),0,0,w,h,null);
	        else {
	            g.drawImage(road2tex, x1s, y, x1e, y+1, 0, 0, 550, 1, null); }
*/

//	    if (c>0) {
//	        g.drawImage(road1tex, x1-w1, y1, x1+w1, y1+1, 0, 0, 550, 1, null); }
//	        //app.drawImage(s, (int) (destX-destW), (int) (destY), (int)(destX),(int)(destY+destH),0,0,w,h,null);
//	        else {
//	            g.drawImage(road2tex, x1-w1, y1, x1+w1, y1, 0, 0, 550, 1, null); }
	}

}

public static void pauza(int ms) {try {    Thread.sleep(ms);    } catch (InterruptedException e) { e.printStackTrace(); }}

public static void main(String[] args) throws IOException {
	//System.out.println(normal);
	Outrun outrun = new Outrun();
	try {
		outrun.run();
	} catch (Exception e) {

		e.printStackTrace();
	}
    System.exit(0);
}

private void run() throws IOException  {
    BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();

    BufferedImage buffer = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = buffer.createGraphics();

    frame = new MyFrameOutrun("Outrun!");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.getContentPane().setLayout(new FlowLayout());
    frame.getContentPane().add(new JLabel(new ImageIcon(buffer)));
    frame.setResizable(false);
    System.out.println("Started");

    long estimatedTime;
	long startTime = 0;



    KeyListener keys = new KeyListener() {
        public void keyPressed(KeyEvent e) {       	//System.out.println(""+e.getKeyCode()+", ");
        			if(e.getKeyCode()==16) { frame.shift=true; }
        	        if(e.getKeyCode()==17) { frame.ctrl=true; }
        	        //if(e.getKeyCode()==18) { frame.alt=true; }
        	        if(e.getKeyCode()==27) { frame.pressedEsc=true; }
        	        if(e.getKeyCode()==37) { frame.cursorLeft=true; }
        	        if(e.getKeyCode()==38) { frame.cursorUp=true; }
        	        if(e.getKeyCode()==39) { frame.cursorRight=true; }
        	        if(e.getKeyCode()==40) { frame.cursorDown=true; }
        	        if(('A'==e.getKeyChar())||('a'==e.getKeyChar())) {frame.pressedA=true; }
        	        if(('D'==e.getKeyChar())||('d'==e.getKeyChar())) {frame.pressedD=true; }
        	        if(('W'==e.getKeyChar())||('w'==e.getKeyChar())) {frame.pressedW=true; }
        	        if(('S'==e.getKeyChar())||('s'==e.getKeyChar())) {frame.pressedS=true; }
        	        if(e.getKeyCode() == 32 ) { frame.space=true; }
        	        //if(s1==true && s2==true) { System.out.println("EXIT NOW"); System.exit(0); }
        }
        public void keyReleased(KeyEvent e) {  //System.out.println("re"+e.getKeyChar());
        						if(e.getKeyCode()==17) { frame.ctrl=false; }
//        						if(e.getKeyCode()==18) { frame.alt=false; }
        						if(e.getKeyCode()==16) { frame.shift=false; }
        						if(e.getKeyCode()==32 ) { frame.space=false; }
        						if(e.getKeyCode()==27) { frame.pressedEsc=true; }
        	        	        if(e.getKeyCode()==37) { frame.cursorLeft=false; }
        	        	        if(e.getKeyCode()==38) { frame.cursorUp=false; }
        	        	        if(e.getKeyCode()==39) { frame.cursorRight=false; }
        	        	        if(e.getKeyCode()==40) { frame.cursorDown=false; }

        	        	        if(('A'==e.getKeyChar())||('a'==e.getKeyChar())) {frame.pressedA=false; }
        	        	        if(('D'==e.getKeyChar())||('d'==e.getKeyChar())) {frame.pressedD=false; }
        	        	        if(('W'==e.getKeyChar())||('w'==e.getKeyChar())) {frame.pressedW=false; }
        	        	        if(('S'==e.getKeyChar())||('s'==e.getKeyChar())) {frame.pressedS=false; }
        }
        public void keyTyped(KeyEvent e) {   }
    };
    frame.addKeyListener(keys);

    frame.pack();frame.setVisible(true);
    frame.revalidate();frame.repaint();
    System.out.println("Window started");

    object = new Image[spritesTilesNames.length];
    for(int i=0;i<object.length;i++)
    { //pis("."+i);
    	object[i]=ImageIO.read(new File(spritesTilesNames[i]));
    		  } //pisl();

    road1tex = ImageIO.read(new File("images/road4.png"));
    //road2tex = ImageIO.read(new File("images/road2.png"));
    road2tex = ImageIO.read(new File("images/road5.png"));
    Image carFrame = ImageIO.read(new File("images/Q/CAB1SPR.SPR_8.png"));

    Image bgTex;     //Texture bg;
    bgTex=ImageIO.read(new File("images/bg.png")); //bg.loadFromFile("images/bg.png");

    BufferedImage bg = new BufferedImage(5000,411,BufferedImage.TYPE_INT_RGB);
    Graphics2D bgG = bg.createGraphics();
    for(int i=0;i<7;i++) {
    bgG.drawImage(bgTex, i*768, 0, null); }

    //std::vector<Line> lines;
    List <Line> lines = new ArrayList<Line>();

    for(int i=0;i<1600;i++)
     {
       Line line = new Line();
       line.z = i*segL;

       if (i>300 && i<700) line.curve=0.5;
       if (i>1100) line.curve=-0.7;

       line.sprite = object.length;
       if (i<300 && i%20==0) {line.spriteX=-2.5; line.sprite=4;}
       if (i%17==0)          {line.spriteX=2.5; line.sprite=5;}
       if (i>300 && i%20==0) {line.spriteX=-1.3; line.sprite=3;}
       if (i>800 && i%20==0) {line.spriteX=-1.2; line.sprite=0;}
       if (i==400)           {line.spriteX=-1.2; line.sprite=6;}
       if (i>750) line.y = Math.sin(i/30.0)*1500;

       lines.add(line);//lines.push_back(line);
     }

   int N = lines.size();
   float playerX = 0;
   int pos = 0;
   int H = 1500;

   boolean isRunning = true;
   while (isRunning) //while (app.isOpen())
    {
  int speed=0;
	if(frame.pressedEsc) {  isRunning=false; } // end app
	if(frame.cursorRight){ playerX+=0.1;} // turn right
	if(frame.cursorLeft){ playerX-=0.1;} // turn left
	if(frame.cursorDown){ speed=-200;} // brake
	if(frame.cursorUp){ speed=200;} //accelerate
	if(frame.pressedW)   {H+=100;  } //ctrl = ??? time breaker?
	if(frame.pressedS) { H-=100; } //shift = cast spell
//	if(frame.alt) { frame.alt=false;player.hurt=true;player.frame=10;}  //alt= hurt
//	if(frame.space)   {   } //space = handbrake..
//	if(frame.ctrl)   {  } //ctrl = ??? time breaker?
//	if(frame.shift) {  } //shift = cast spell

  pos+=speed;
  while (pos >= N*segL) pos-=N*segL;
  while (pos < 0) pos += N*segL;

  g.setColor(bgColor);g.fillRect(0, 0, width, height);//app.clear(Color(105,205,4));
  g.drawImage(bg, 0, 0, null);//app.draw(sBackground);
  int startPos = pos/segL;
  int camH = (int) (lines.get(startPos).y + H);  //int camH = lines[startPos].y + H;
  if (speed>0) g.drawImage(bg, (int) (-lines.get(startPos).curve*2), 0, null); //sBackground.move(-lines[startPos].curve*2,0);  //if (speed>0) sBackground.move(-lines[startPos].curve*2,0);
  if (speed<0) g.drawImage(bg, (int) (lines.get(startPos).curve*2), 0, null);  //if (speed<0) sBackground.move( lines[startPos].curve*2,0);

  double maxy = height;//int maxy = height;
  double x=0,dx=0;

  ///////draw road////////
  for(int n = startPos; n<startPos+300; n++)
   {
    Line l = lines.get(n%N);//Line &l = lines[n%N];
    l.project((int) (playerX*roadW-x), camH, startPos*segL - (n>=N?N*segL:0));
    x+=dx;
    dx+=l.curve;

    l.clip=maxy;
    if (l.Y>=maxy) continue;
    maxy = l.Y;

    Color grass  = (n/3)%2==0?grass1:grass2; //    Color grass  = (n/3)%2?Color(16,200,16):Color(0,154,0);
    Color rumble = (n/3)%2==0?rumble1:rumble2;//    Color rumble = (n/3)%2?Color(255,255,255):Color(0,0,0);
    //Color road   = (n/3)%2==0?road1:road2;//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);

    //int roadTex   = (n/3)%2==0?1:0;//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);
    int roadTex   = (n/3)%2;//    Color road   = (n/3)%2?Color(107,107,107):Color(105,105,105);

    Line p = lines.get((n-1)%N);    //Line p = lines[(n-1)%N]; //previous line


    drawQuad(g, grass, 0,        (int)p.Y, width,        0,         (int)l.Y, width);
    drawQuad(g, rumble,(int)p.X, (int)p.Y, (int)(p.W*1.2), (int)l.X, (int)l.Y, (int)(l.W*1.2));

    //drawQuad(g, road,  (int)p.X, (int)p.Y, (int)p.W,     (int)l.X, (int)l.Y, (int)l.W); //normal color road

    drawQuadRoad(g, roadTex,  (int)p.X, (int)p.Y, (int)p.W,     (int)l.X, (int)l.Y, (int)l.W); // textured road

    //lines.get(n%N).drawSprite(g);
   }

    //////draw objects////////
//    for(int n=startPos+300; n>startPos; n--)
//    for(int n=startPos+100; n>startPos; n--) {
   for(int n=startPos+300; n>startPos; n--) {
    	lines.get(n%N).drawSprite(g); //lines[n%N].drawSprite(app);
    }

   
//   g.drawImage(carFrame, (width-carFrame.getWidth(null))/2, height/2, carFrame.getWidth(null), height/2, 0, 0, carFrame.getWidth(null), carFrame.getHeight(null), null);
   g.drawImage(carFrame, (width-carFrame.getWidth(null))/2, height/2, null);
   
   
   
	g2.drawImage(img, 0, 0, null);

	estimatedTime = System.currentTimeMillis() - startTime;
    //System.out.println(estimatedTime);
	startTime = System.currentTimeMillis();

    //if (estimatedTime<20) pauza(5); /**/
	//if (estimatedTime<10) pauza(5);
    //System.out.println(estimatedTime);
	
	
	frame.revalidate();frame.repaint();
    }
}

}

class MyFrameOutrun extends JFrame{
	private static final long serialVersionUID = 1L;
	boolean pressedA,pressedS, pressedW,pressedD, //pressedQ, pressedBigW,pressedE,pressedO,pressedP,pressedV,shooting,
		pressedEsc, cursorLeft,cursorRight,cursorUp,cursorDown, //pressedZ,pressedDel, pressedI,pressedU,pressedF,pressedG,
		space,ctrl, shift;
	MyFrameOutrun(String s) {
		super(s);
	} // end of constructor
} // end of myframe

//********************************* Line ************************************************
class Line
{
  double x,y,z; //3d center of line
  double X,Y,W; //screen coord
  double curve,spriteX,clip,scale;
  int sprite;

  public Line()
  {spriteX=curve=x=y=z=0;}

  void project(int camX,int camY,int camZ)
  {
    scale = Outrun.camD/(z-camZ);
    X = (1 + scale*(x - camX)) * Outrun.width/2;
    Y = (1 - scale*(y - camY)) * Outrun.height/2;
    W = scale * Outrun.roadW  * Outrun.width/2;
  }


  void drawSprite(Graphics2D app)
  {

    //SpriteOutrun s = sprite;
	if (sprite < Outrun.object.length) {
	Image s = Outrun.object[sprite];
	int w = s.getWidth(null);	//    int w = s.getTextureRect().width;
	int h = s.getHeight(null);//    int h = s.getTextureRect().height;

    double destX = (X + scale * spriteX * Outrun.width/2);
    double destY = (Y + 4);
    double destW  = (w * W / 266.0);
    double destH  = (h * W / 266.0); //final height

    destX += destW * spriteX; //offsetX
    destY += destH * (-1);    //offsetY

    //double clipH = destY+destH-clip; //to prevent drawing things through ground?
    double clipH = destY+destH-clip; //to prevent drawing things through ground?
    if (clipH<0) clipH=0;
    //if (clipH>=destH) return;
    if (clip<destY+destH-5) return;
    app.drawImage(s, (int) (destX-destW), (int) (destY), (int)(destX),(int)(destY+destH),0,0,w,h,null);
    }
  }
};
