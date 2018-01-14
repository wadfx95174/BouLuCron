import java.awt.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FullScreenCapture extends JFrame implements ActionListener{
	public static int draw_panel_width = 1100,draw_panel_height=700;
			JButton button = new JButton("Capture!");
			JPanel jp1;
			DrawPanel drawPanel;
			BufferedImage screenFullImage;
			Graphics2D graphics;
			boolean IsPic = false;//確定擷到圖
			public FullScreenCapture(DrawPanel drawPanel) {
				super("Capture Screen");
				jp1 = new JPanel();
				this.add(jp1,BorderLayout.NORTH);  
				setLayout(new FlowLayout());
				button.setName("button");
				button.addActionListener(this);
				jp1.add(button);
				this.setSize(300, 100);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				this.setLocationRelativeTo(null);
				this.drawPanel = drawPanel;
			}
			public void capture() throws AWTException {
		        Robot robot = new Robot();
		        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
		        screenFullImage = robot.createScreenCapture(screenRect);
		        screenFullImage = resize(screenFullImage,draw_panel_width,  draw_panel_height);
		        
				Graphics2D g2d_bufImg = (Graphics2D) drawPanel.bufImg.getGraphics();
				g2d_bufImg.drawImage(screenFullImage,0,0,drawPanel);
				drawPanel.repaint();

		        System.out.println("A full screenshot saved!");
		        IsPic = true;     
			}
			public static BufferedImage resize(BufferedImage img, int newW, int newH) {  
			    int w = img.getWidth();  
			    int h = img.getHeight();  
			    BufferedImage dimg = new BufferedImage(newW, newH, img.getType());  
			    Graphics2D g = dimg.createGraphics();  
			    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
			    RenderingHints.VALUE_INTERPOLATION_BILINEAR);  
			    g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);  
			    g.dispose();  
			    return dimg;  
			} 
			public DrawPanel getDrawPic(){
				return drawPanel;
			}
			public boolean testT() {
				return IsPic;
			}
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton clickedButton = (JButton) event.getSource();
				// capture the screen
				try {
					capture();
				} catch (AWTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			/*public void static main(String[] args) {
				SwingUtilities.invokeLater(new Runnable() //用thread跑
					{
					@Override
					public void run() {
						new FullScreenCapture().setVisible(true);
						}
					}
				);
			}*/
		}