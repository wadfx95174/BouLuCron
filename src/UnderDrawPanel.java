import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class UnderDrawPanel extends JPanel implements MouseListener, MouseMotionListener{
	public int x,y;
	float data[]={2};
	public JPanel ctrl_area=new JPanel(),ctrl_area2=new JPanel()
			,ctrl_area3=new JPanel();
	
	private Variable variable;
	private Painter painter;
	
	public UnderDrawPanel(Variable var,Painter paint){
		this.setOpaque(false);
		this.variable = var;
		this.painter = paint;
		
		this.setLayout(null);
		this.add(ctrl_area);
		this.add(ctrl_area2);
		this.add(ctrl_area3);
		
		ctrl_area.setBounds(new Rectangle(variable.draw_panel_width+3
				, variable.draw_panel_height+3, 5, 5));
		ctrl_area.setBackground(new Color(0,0,0));
		ctrl_area2.setBounds(new Rectangle(variable.draw_panel_width+3
				, variable.draw_panel_height/2, 5, 5));
		ctrl_area2.setBackground(new Color(0,0,0));
		ctrl_area3.setBounds(new Rectangle(variable.draw_panel_width/2
				, variable.draw_panel_height+3, 5, 5));
		ctrl_area3.setBackground(new Color(0,0,0));
		ctrl_area.addMouseListener(this);
		ctrl_area.addMouseMotionListener(this);
		ctrl_area2.addMouseListener(this);
		ctrl_area2.addMouseMotionListener(this);
		ctrl_area3.addMouseListener(this);
		ctrl_area3.addMouseMotionListener(this);
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){
		variable.draw_panel_width=x;
		variable.draw_panel_height=y;
		
		ctrl_area.setLocation(variable.draw_panel_width+3,variable.draw_panel_height+3);
		ctrl_area2.setLocation(variable.draw_panel_width+3,variable.draw_panel_height/2+3);
		ctrl_area3.setLocation(variable.draw_panel_width/2+3,variable.draw_panel_height+3);
		painter.drawPanel.setSize(x,y);
		painter.drawPanel.resize();
		repaint();
	}
	
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	
	public void mouseDragged(MouseEvent e){
		if(e.getSource()==ctrl_area2){
			x = e.getX()+variable.draw_panel_width;
			y = variable.draw_panel_height;
		}
		else if(e.getSource()==ctrl_area3){
			x = variable.draw_panel_width;
			y = e.getY()+variable.draw_panel_height;
		}
		else{
			x = e.getX()+variable.draw_panel_width;
			y = e.getY()+variable.draw_panel_height;
		}
		repaint();
		variable.jLabel.setText(x+","+y);
	}
	@Override
	public void mouseMoved(MouseEvent e) {}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		super.paint(g2d);
		
		g2d.setPaint( new Color(128,128,128) );
		g2d.setStroke( new BasicStroke( 1,  BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER, 10, data, 0 ) );
		g2d.draw( new Rectangle2D.Double( -1, -1, x+3, y+3 ) );
	}
}