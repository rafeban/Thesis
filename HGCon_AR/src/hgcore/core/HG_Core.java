package hgcore.core;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class HG_Core extends Thread{
	private static final long serialVersionUID = 1L;
	
	private JLabel scene = new JLabel("Hello world!");
	
	BufferedImage image; 
	

	public HG_Core()	{
		super("Core");
		
		
	}//construct
	
	
	double ro, go, bo;
	double[] rgbo;
	
	double[] srcPx;
	double[] mskPx;
	
	//temp tresholding
	private double tresh = 50;
	
	//main must be replaced with a run function after it becomes a Thread
	public void run()	{
		Mat webcam_image = new Mat();
		VideoCapture capture = new VideoCapture(0);
				
		
		
		Mat model = new Mat();
		
		boolean sing = true;
		while(true)	{
			capture.read(webcam_image);
			System.out.println("Frame Captured: Width " + 
		    webcam_image.width() + " Height " + webcam_image.height());
			Core.flip(webcam_image, webcam_image, 1); // flip image
			
			//rgb to grayscale
			Mat nMz = webcam_image.clone();
		    Imgproc.cvtColor(webcam_image, nMz, Imgproc.COLOR_BGR2GRAY);
		     //rgb to grayscale
			//image = matToBufferedImage(nMz); // grayScale value
			
		    
		    //capturing a model image (one time only)
		    if(sing)	{
		    	capture.read(model);
		    	Core.flip(model, model, 1); // flip image
		    	sing = false;
		    }
		    
		    
		    
/****************************************************************************************************
*                TWO FRAME DIFFERENCING MOTION DETECTION ALGORITHM
*                                 START
****************************************************************************************************/
			for (int i = 0; i < webcam_image.rows(); i++)
				for (int j = 0; j < webcam_image.cols(); j++)	{
					srcPx = webcam_image.get(i,j);
					mskPx = model.get(i,j);
					double bp = mskPx[0];
					double gp = mskPx[1];
					double rp = mskPx[2];
					double bc = srcPx[0];
					double gc = srcPx[1];
					double rc = srcPx[2];
					if(bc < bp + tresh && bc > bp - tresh)
						webcam_image.put(i, j, new double[]{0 , 0, 0});
					else
						webcam_image.put(i, j, new double[]{255 , 255, 255});
					
					
				}//for
			//end for nested
			
			capture.read(model);
	    	Core.flip(model, model, 1); // flip image
/****************************************************************************************************
 *                TWO FRAME DIFFERENCING MOTION DETECTION ALGORITHM
 *                                 END
 ****************************************************************************************************/
			
	    	
	    	
			image = matToBufferedImage(webcam_image); // normal BGR Output
			
		}//while
	}//main
	
	public void setThresh(double value)	{
		tresh = value;
	}//setThresh
	
	public double getThresh()	{
		return tresh;
	}//get thresh
	
	public BufferedImage getImage()	{
		return image;
	}//getImage()
	
	public boolean isfetching()	{
		return true;
	}
	
	public static BufferedImage matToBufferedImage(Mat m) { 
		int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);  
        return image;

	}//metToBufferedImage
}//class