//********************************************************************
//  CircularMotion.java       
//  Physics lab simulation for uniform circular motion.
//  Useful for teaching using the modeling physics paradigm.
//  Author@ Jiaxin Dai
//********************************************************************

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class UniformCircularMotion extends JFrame implements ActionListener, ComponentListener, ChangeListener, WindowListener {
   	static int width = 800, height = 600; // both width and height can be resized
   	static int cWidth = 150, lWidth = width - cWidth;
   	
   	private int x, y;
   	private JPanel contentPane, control, lab, demo;
   	private double fc, delay;
   	private JButton startBtn, stopBtn; // start and stop buttons for animation
   	private JLabel mass, radius, velocity, force, frames_per_sec;
   	private JSlider mSlider, rSlider, vSlider, fpsSlider;
   	private JTextField fcText;
   	
   	private Motion motion = new Motion(0, 0, 0, 0);
   	
   	private Timer timer;
   	private boolean restart = false;
   	private DecimalFormat form = new DecimalFormat("#0.00");
   	private Font lblFont = new Font("Calibri", Font.PLAIN, 22);
   	private Font fpsFont = new Font("Calibri", Font.PLAIN, 12);
   	private Font btnFont = new Font("Calibri", Font.BOLD, 16);

	public static void main (String[] args) {      
		UniformCircularMotion ucm = new UniformCircularMotion("Uniform Circular Motion");
   	}
   	
   	public UniformCircularMotion(String title)	{
   		super(title);
   		
   		// set attributes for the jlabel
        mass = new JLabel("Mass (kg)", JLabel.CENTER); 
        mass.setFont(lblFont);
        mass.setAlignmentX(Component.CENTER_ALIGNMENT);
        radius = new JLabel("Radius (m)", JLabel.CENTER);
        radius.setFont(lblFont);
        radius.setAlignmentX(Component.CENTER_ALIGNMENT);
        velocity = new JLabel("Velocity (m/s)", JLabel.CENTER);
        velocity.setFont(lblFont);
        velocity.setAlignmentX(Component.CENTER_ALIGNMENT);
        frames_per_sec = new JLabel("Frames Per Second", JLabel.CENTER);
        frames_per_sec.setAlignmentY(Component.CENTER_ALIGNMENT);
        force = new JLabel("Force (N)", JLabel.CENTER); 
        force.setFont(lblFont);
       
        // set attributes for mass slider
   		mSlider = new JSlider(JSlider.VERTICAL, 1, 10, 5);
   		mSlider.setMajorTickSpacing(9);
   		mSlider.setMinorTickSpacing(1);
   		mSlider.setFont(lblFont);
        mSlider.setPaintTicks(true);
        mSlider.setPaintLabels(true);
        mSlider.setPreferredSize(new Dimension(80, height/4));
        mSlider.addChangeListener(this);
   		
        // set attributes for radius slider
   		rSlider = new JSlider(JSlider.VERTICAL, 1, 10, 5);
   		rSlider.setMajorTickSpacing(9);
        rSlider.setMinorTickSpacing(1);
        rSlider.setFont(lblFont);
        rSlider.setPaintTicks(true);
        rSlider.setPaintLabels(true);
        rSlider.setPreferredSize(new Dimension(80, height/4));
        rSlider.addChangeListener(this);
        
        // set attributes for velocity slider
        vSlider = new JSlider(JSlider.VERTICAL, 1, 10, 5);
        vSlider.setMajorTickSpacing(9);
        vSlider.setMinorTickSpacing(1);
        vSlider.setFont(lblFont);
        vSlider.setPaintTicks(true);
        vSlider.setPaintLabels(true);
        vSlider.setPreferredSize(new Dimension(80, height/4));
        vSlider.addChangeListener(this);
        
        // set attributes for framesPerSecond slider
        fpsSlider = new JSlider(JSlider.HORIZONTAL, 10, 60, 24);
        fpsSlider.setFont(fpsFont);
   		fpsSlider.setMajorTickSpacing(10);
        fpsSlider.setMinorTickSpacing(1);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintLabels(true);
        fpsSlider.addChangeListener(this);
        
        startBtn = new JButton("Start animation");
	    startBtn.setFont(btnFont);
	    startBtn.addActionListener(this);
	    stopBtn = new JButton("Stop animation");
	    stopBtn.setFont(btnFont);
	    stopBtn.addActionListener(this);
	    
	    fcText = new JTextField(" ---- ", 6);
	    fcText.setFont(fpsFont);
   		
	    control = new JPanel();
	    control.setPreferredSize(new Dimension(cWidth,height));
	    control.setBorder(BorderFactory.createLineBorder(Color.black));
	    control.add(mass);
		control.add(mSlider);
		control.add(radius);
		control.add(rSlider);
		control.add(velocity);
	    control.add(vSlider);
	    
	    demo = new JPanel();
	    demo.setPreferredSize(new Dimension(lWidth,100));
	    demo.add(startBtn);
		demo.add(stopBtn);
		demo.add(frames_per_sec);
		demo.add(fpsSlider);
		demo.add(force);
	    demo.add(fcText);
	    demo.setOpaque(false);
	    
	    lab = new Draw();
	    lab.add(demo);
		
      	contentPane = new JPanel();
      	contentPane.setPreferredSize(new Dimension(width,height));
      	contentPane.setLayout(new BorderLayout());
      	contentPane.add(control, BorderLayout.WEST);
      	contentPane.add(lab, BorderLayout.CENTER);
      	
      	updateMotion(); // loads the values from the sliders
      	timer = new Timer((int)delay, this);
      	lab.repaint();
      	  		
   		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
   		getContentPane().add(contentPane);
      	contentPane.addComponentListener(this); // to handle resizing
      	addWindowListener(this); // to pause upon minimizing to save memory
      	pack();
      	setVisible(true);
   	}

   	// reads  values from the sliders.
   	public void updateMotion() {
		motion.setMotion(mSlider.getValue(), rSlider.getValue(), vSlider.getValue());
		delay = 1000/fpsSlider.getValue();
		
		// compute centripetal force: Fc = m*v^2/r
		fc = (double)(motion.getMass() * motion.getVelocity() * motion.getVelocity() / motion.getRadius()); 
		fcText.setText(form.format(fc));
   	}
    
  	public void animate() {   	
		updateMotion();
		
		// Update position
		motion.setMotion((motion.getAngle() + (int)(delay * motion.getVelocity() / motion.getRadius())) % 360);
   		
   		lab.repaint();
   	}

   	// Listener for sliders
  	public void stateChanged(ChangeEvent e) {
    	updateMotion();
		timer.setDelay((int)delay);
		
		// update the demo for uniform circuclar motion
		lab.repaint();
	}
   
  	// listener for buttons and timer
   	public void actionPerformed (ActionEvent event)	{
     	Object src = event.getSource();
     	
     	if (src == timer) 
     		animate();
     	else if (src == startBtn)
     		timer.start();
     	else if (src == stopBtn)
     		timer.stop();
  	}

  	// listener for resizing
    public void componentResized(ComponentEvent e) {
    	Dimension newSize = this.getContentPane().getSize();
    	width = newSize.width;
    	height = newSize.height;
    	lWidth = width - cWidth;
    }
    
    public void componentShown(ComponentEvent e) {
    	// do nothing
    }
    
    public void componentMoved(ComponentEvent e) {
    	// do nothing
    }
    
    public void componentHidden(ComponentEvent e) {
    	// do nothing
    }
    
    
    // listener for minimizing (to save memory)
    public void	windowDeactivated(WindowEvent e) {// Invoked when the Window is no longer active
        if (timer.isRunning()) {	
        	timer.stop();
        	restart = true;
        }
 	}
    
    public void	windowIconified(WindowEvent e) { // Invoked when the window state is changed from normal to minimized
 		windowDeactivated(e);
 	}	
    
    public void windowActivated(WindowEvent e) { // Invoked when the Window is set to be active
	 	if (restart)
    		timer.start();
	} 
    
	public void	windowDeiconified(WindowEvent e) { // Invoked when the window state is changed from minimized to normal
 		windowActivated(e);
 	}
 	
 	public void	windowClosed(WindowEvent e) {  // Invoked when a window has been closed as the result of calling dispose on the window.
 		
 	}
 	public void	windowClosing(WindowEvent e) { // Invoked when the user attempts to close the window from the window's system menu.
 		
 	} 
 	public void	windowOpened(WindowEvent e) { // Invoked the first time a window is made visible.
 		
 	} 
	
 	//drawing space
 		private class Draw extends JPanel {
 			// drawing constants
 			double ecc = 0.33; //eccentricity of ellipse (0.5 or 0.33 is isometric view?)
 			int scale = 50; // to make the radius show up better on the screen
 			int weight = 4; // to make the orbiting mass show up better
 			int hangerWeight = 20; // to make the hanging weights show up better
 			int hangerWidth = 36; 
 			int tubeHeight = 200; 
 			int tubeWidth = 15;
 			int postWidth = 4;
 			int hook = 55;
 			int hookSpace = 35;
 			int realHook = hook - hookSpace;
 			int washerHeight = 7;
 			int tinyHeight = 3;
 			int medHeight = 5; 
 			
 			Color c1 = new Color(5,120,220);		
 			Color c2 = new Color(55,55,5);
 			Color c3 = new Color(215,130,25);
 			Color c4 = new Color(160,45,45);
 			Color c5 = new Color(125,35,35);
 			
 			// drawing/coordinate variables
 			int orbitX, orbitY, tubeL, tubeR, tubeBtm;
 			int postL, postHt;
 			int hangHt, hangBtm;
 			int lX, lY;
 			int smallNum, medNum, bigNum;
 			int smallRoom, medRoom, bigRoom, roomToHang;
 			int medStart, smallStart, smallFinish;
 			double radX, radY, sine, cosine;
 			
 		    @Override
 		    public void paintComponent(Graphics g) {
 		        super.paintComponent(g);
 		        this.setBackground(c1);
 		        
 		        // update size variables
 		        bigNum = (int)(fc / 100); // for each 100 N add big weight
 		        medNum = (int)((fc % 100) / 10); // for each 10 N left over, add medium weight
 		        smallNum = (int)((fc % 10) / 1); // for each 1 N left over, add tiny weight
 		        bigRoom = bigNum * (washerHeight-2);
 		        medRoom = medNum * medHeight;
 		        smallRoom = smallNum * tinyHeight;
 		        roomToHang = bigRoom + medRoom + smallRoom;
 		        orbitY = height/3; 
 				orbitX = lWidth/2;
 			 	tubeL = orbitX-tubeWidth/2;
 			 	tubeR = orbitX+tubeWidth/2;
 			 	tubeBtm = orbitY+tubeHeight;		 			 	
 		        hangHt = 120;
 				hangBtm = tubeBtm+hangHt;
 				medStart = hangBtm-bigRoom;
 				smallStart = medStart-medRoom;
 				smallFinish = smallStart-smallRoom;
 				postL = orbitX-postWidth/2;
 				postHt = hangHt - hook;

 				// calculate position of orbiting mass
 				sine = Math.sin(motion.getAngle()*Math.PI/180.0);
 				cosine = Math.cos(motion.getAngle()*Math.PI/180.0);
 				x = orbitX + (int)(motion.getRadius()*scale*cosine); 
 				y = orbitY - (int)(motion.getRadius()*scale*sine*ecc); // java y is reversed
 				
 				// for drawing thick lines
 		        Graphics2D g2 = (Graphics2D) g; 
 		        
 				// draw mass hanger and masses
 				// hanger
 				drawThickWasher(orbitX,hangBtm+washerHeight/2,hangerWidth,washerHeight,c4,c3,c4,g);// hanger bottom
 				// hook
 				g.setColor(c4);			
 	        	g2.setStroke(new BasicStroke(3));
 				g2.drawArc(tubeL-1,tubeBtm+hookSpace,tubeWidth,realHook,-90,250); //hook body (thick Arc)
 				g2.setStroke(new BasicStroke(1));
 				g.setColor(c3);
 				g.drawArc(tubeL-3,tubeBtm+hookSpace-2,tubeWidth+3,realHook+3,-80,250); // outside outline
 				g.setColor(c5);
 				g.drawArc(tubeL-1,tubeBtm+hookSpace+1,tubeWidth-2,realHook-3,-110,270); // inside outline
 				// string
 				g.setColor(Color.black);
 				g.drawLine(orbitX,tubeBtm,orbitX,tubeBtm+hookSpace);
 				// masses
 				for (int count = 0; count < bigNum; count++)
 					drawThickWasher(orbitX,hangBtm-count*washerHeight,(int)(hangerWeight*1.5)-1,washerHeight,Color.lightGray,c2,c4,g);
 				for (int count = 0; count < medNum; count++)
 					drawThickWasher(orbitX,medStart-count*medHeight,hangerWeight-1,medHeight,Color.lightGray,c2,c4,g);
 				for (int count = 0; count < smallNum; count++)
 					drawThickWasher(orbitX,smallStart-count*tinyHeight,(int)(hangerWeight*0.5)-1,tinyHeight,Color.lightGray,c2,c4,g);
 				// hanger post
 				g.setColor(c4); // post body
 				g.fillRect(postL,tubeBtm+hook,postWidth,postHt-roomToHang+4);
 				g.fillOval(postL,smallFinish-(int)(postWidth*ecc/2),postWidth,(int)(postWidth*ecc+1));
 				g.setColor(c3);
 				g.drawLine(postL-1,tubeBtm+hook,postL-1,smallFinish+2);
 				g.drawLine(postL+postWidth,tubeBtm+hook,postL+postWidth,smallFinish+2);
 				
 				// draw white tube: first bottom circle, then rectangle, then outlines
 				g.setColor(Color.white);
 				g.fillOval(tubeL,tubeBtm-(int)(tubeWidth*ecc/2),tubeWidth,(int)(tubeWidth*ecc));
 				g.fillRect(tubeL,orbitY,tubeWidth,tubeHeight);
 				g.setColor(Color.black);
 				g.drawArc(tubeL-1,tubeBtm-(int)(tubeWidth*ecc/2),tubeWidth,(int)(tubeWidth*ecc),180,180);	
 				g.drawLine(tubeL-1,orbitY,orbitX-tubeWidth/2-1,tubeBtm);
 				g.drawLine(tubeR,orbitY,tubeR,tubeBtm);
 							
 				// draw orbiting mass
 				drawThickWasher(x,y,motion.getMass()*weight,washerHeight,Color.lightGray,c2,c1,g);
 				
 				// string to middle 
 				g.drawLine(x,y,orbitX,orbitY);	
 				g.setColor(Color.lightGray);
 				g.fillOval(tubeL,orbitY-(int)(tubeWidth*ecc/2),tubeWidth,(int)(tubeWidth*ecc));
 				g.setColor(Color.black);
 				g.drawOval(tubeL-1,orbitY-(int)(tubeWidth*ecc/2),tubeWidth,(int)(tubeWidth*ecc));
 				lX = orbitX + (int)(tubeWidth*cosine);
 				lY = orbitY - (int)(tubeWidth*sine*ecc);
 				g.drawLine(lX,lY,orbitX,orbitY+(int)(tubeWidth/2*ecc)); // where it intersects the arc to the middle
 		    }
 		    
 		    private void drawThickWasher (int centerX, int centerY, int width, int thickness, Color color, Color outlineColor, Color holeColor, Graphics g) {
 		    	radX = width;
 				radY = radX*ecc;
 				for (int i=thickness/2+1;i>=0;i--) {
 					g.setColor(color);
 					g.fillOval((int)(centerX-radX),(int)(centerY-radY+i),(int)(radX*2),(int)(radY*2)); // washer body
 					g.setColor(outlineColor);
 					g.drawOval((int)(centerX-radX),(int)(centerY-radY+i),(int)(radX*2),(int)(radY*2)); // washer outline
 				}
 				g.setColor(holeColor);
 				g.fillOval((int)(centerX-radX/6),(int)(centerY-radY/6),(int)(radX/3),(int)(radY/3)); // donut hole
 				g.setColor(outlineColor);
 				g.drawOval((int)(centerX-radX/6),(int)(centerY-radY/6),(int)(radX/3),(int)(radY/3)); // donut hole outline
 		    	
 		    }
 		}
 	}