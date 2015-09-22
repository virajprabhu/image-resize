package com.adobe.slave;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WorkerThread implements Runnable{

	private String iDir;
	private String iName;
	private String oDir;
	public WorkerThread(String iDir, String oDir, String iName){
		this.iDir = iDir;
		this.iName = iName;
		this.oDir = oDir;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
		BufferedImage img = ImageIO.read(new File(iDir+iName));
		BufferedImage newImg = resizeImage(img, img.getType(), 900, 600);
		ImageIO.write(newImg, "jpg", new File(oDir+iName));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public static BufferedImage resizeImage(BufferedImage img, int type, int width, int height){
		BufferedImage newImg = new BufferedImage(width, height, type);
		Graphics2D g = newImg.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g.drawImage(img, 0, 0, width, height, null);
		g.dispose();
		return newImg;
	}

}
