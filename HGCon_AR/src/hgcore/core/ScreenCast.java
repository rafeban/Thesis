package hgcore.core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;

public class ScreenCast extends JFrame implements ChangeListener{
	private static final long serialVersionUID = 1L;
	
	HG_Core core = new HG_Core();
	
	JPanel screen;
	JPanel windowPane = new JPanel();
	
	JSlider threshSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
	JLabel tVal = new JLabel("Current Threshold: ");
	public ScreenCast()	{
		super("Screen Casting");
		core.start();
		
		screen = new JPanel()	{
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                BufferedImage dimg = core.getImage();
                g.drawImage(dimg, 0, 0, null);
                repaint();
            }
        };//pane
        
        setLayout(new BorderLayout());
        add(windowPane, BorderLayout.CENTER); windowPane.setLayout(new BorderLayout());
        windowPane.add(screen); screen.setBackground(Color.gray);
        
        threshSlider.setMajorTickSpacing(5);
        threshSlider.setPaintTicks(true);
        add(threshSlider, BorderLayout.SOUTH);
        add(tVal, BorderLayout.NORTH);
        threshSlider.addChangeListener(this);
        
        
        
	}//construct
	
	public void stateChanged(ChangeEvent e)	{
		int value = threshSlider.getValue();
		core.setThresh((double)value);
		
		tVal.setText("Current Threshold: " + value);
	}//event
	
	public static void main(String []args)	{
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		ScreenCast frame = new ScreenCast();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500,500);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}//main
	
}//class
