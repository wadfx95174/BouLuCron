import java.awt.Container;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class DrawPicture extends JButton{
	private int btnX;//fish的X軸的位置
	private int btnY;//fish的Y軸的位置
	private int focusX = 0;  
    private int focusY = 0;
	
	public DrawPicture(String picName,int x,int y) {
		this.btnX = x;
		this.btnY = y;
		ImageIcon pic = new ImageIcon(getClass().getResource("img/"+picName+".gif"));
		pic.setImage(pic.getImage().getScaledInstance(80,80,Image.SCALE_DEFAULT));
		setIcon(pic);
		setBorder(null);//不繪製按鈕的邊
		setContentAreaFilled(false);//不會自行繪製按鈕背景
		setBounds(btnX,btnY,80,80);
		setFocusable(false);
		this.addMouseListener(new MouseAdapter() {
        	@Override
            public void mousePressed(MouseEvent event) {
        		focusX = event.getX();  
                focusY = event.getY(); 
            }
        });
		this.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent event) {
				Container container = getParent();  
	            int w = container.getWidth();
	            int h = container.getHeight();  
	  
	            btnX = getX() + event.getX() - focusX;  
	            btnY = getY() + event.getY() - focusY;  
	  
	            if (btnX +getWidth() > w) {  
	            	btnX = w - getWidth();  
	            }  
	            if (btnY + getHeight() > h) {  
	            	btnY = h - getHeight();  
	            }  
	            if (btnX < 0) {  
	            	btnX = 0;  
	            }  
	            if (btnY < 0) {  
	            	btnY = 0;  
	            }  
	  
	            setLocation((int)btnX, (int)btnY);  

	            container.repaint();  
	            container = null;  
			}
		}
	);
	}
}
