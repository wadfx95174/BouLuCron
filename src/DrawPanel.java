
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

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, ItemListener, ActionListener, ChangeListener{//中央畫布
		public BufferedImage bufImg;//記錄最新畫面，並在此上作畫
		private BufferedImage bufImg_data[];//記錄所有畫出圖面，索引值越大越新，最大為最新
		private BufferedImage bufImg_cut;
		private ImageIcon img;
		private JLabel jlbImg;
		private int x1=-1,y1=-1,x2,y2,count,redo_lim,press,temp_x1,temp_y1,temp_x2,temp_y2
				,temp_x3,temp_y3,step,step_chk,step_arc,step_chk_arc,chk,first,click,cut;
		private Arc2D.Double arc2D = new Arc2D.Double();//扇型
		private Line2D.Double line2D = new Line2D.Double();//直線
		private Ellipse2D.Double ellipse2D = new Ellipse2D.Double();//橢圓
		private Rectangle2D.Double rectangle2D = new Rectangle2D.Double();//矩型
		private CubicCurve2D.Double cubicCurve2D = new CubicCurve2D.Double();//貝氏曲線
		private RoundRectangle2D.Double roundRectangle2D = new RoundRectangle2D.Double();//圓角矩型
		private Polygon polygon;//多邊型
		private float data[]={5};
		private Rectangle2D.Double rectangle2D_select = new Rectangle2D.Double();//矩型
		private Ellipse2D.Double ellipse2D_pan = new Ellipse2D.Double();
		private BasicStroke basicStroke_pen = new BasicStroke(1, BasicStroke.CAP_ROUND
				, BasicStroke.JOIN_MITER);
		private BasicStroke basicStroke_select = new BasicStroke(1, BasicStroke.CAP_ROUND
				, BasicStroke.JOIN_MITER,10, data, 0);
		private double center_point_x;
		private double center_point_y;
		private double start;
		private double end;
		public String filename;
		private JTextField textField_font = new JTextField("Fixedsys",30)
				, textField_word = new JTextField("JAVA",16);
		private int size=100;
		private JSpinner fontsize = new JSpinner();
		private JDialog jDialog;
		private JCheckBox bold, italic;
		private JButton ok, cancel;
		public int pie_shape=Arc2D.PIE;
		private int valBold = Font.BOLD;
		private int valItalic = Font.ITALIC;
		private int select_x,select_y,select_w,select_h;
		
		private Variable variable;
		private Painter painter;
		
		private ImageIcon icon;
		
		private DrawPicture pic;
		private ArrayList<DrawPicture> pics = new ArrayList<DrawPicture>();
		
		public DrawPanel(Variable var,Painter paint) {
			this.setOpaque(false);
			this.variable = var;
			this.painter = paint;
			
			bufImg_data = new BufferedImage[1000];
			bufImg = new BufferedImage(Variable.draw_panel_width
					, Variable.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用來繪圖

			this.setLayout(null);
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Variable.draw_panel_width
					, Variable.draw_panel_height));
			
			variable.jMenuItem[1][0].setEnabled(false);
			variable.jMenuItem[1][1].setEnabled(false);
			variable.jMenuItem[1][2].setEnabled(false);
			variable.jMenuItem[1][3].setEnabled(false);
			variable.jMenuItem[1][4].setEnabled(false);
    		
    		//畫出空白//
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.WHITE);
			g2d_bufImg.fill(new Rectangle2D.Double(0,0,Variable.draw_panel_width
					,Variable.draw_panel_height));
			
    		bufImg_data[count] = new BufferedImage(Variable.draw_panel_width
    				, Variable.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
    		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count]
    				.getGraphics();
    		g2d_bufImg_data.drawImage(bufImg,0,0,this);
			
			//Font//
			jDialog = new JDialog(painter, "請選擇文字、字型、大小與屬性", true);
			fontsize.setValue(new Integer(100));
			bold = new JCheckBox( "粗體" ,true);
			italic = new JCheckBox( "斜體" ,true);
			ok = new JButton("確定");
			cancel = new JButton("取消");
			JPanel temp_0 = new JPanel(new GridLayout(3,2,2,2));
			JPanel temp_1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel temp_4 = new JPanel(new FlowLayout());
			JPanel temp_6 = new JPanel(new FlowLayout());
			JPanel temp_5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			Container jDialog_c = jDialog.getContentPane();
			
        	jDialog_c.setLayout(new FlowLayout());
        	jDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE );
        	jDialog.setSize(300, 250);
        	temp_5.add(new JLabel("文字:"));
			temp_5.add(textField_word);
			temp_1.add(new JLabel("字體:"));
			temp_1.add(textField_font);
			temp_2.add(new JLabel("大小:"));
			temp_2.add(fontsize);
			temp_3.add(new JLabel("屬性:"));
			temp_3.add(bold);
			temp_3.add(italic);
			temp_4.add(ok);
			temp_6.add(cancel);
			temp_0.add(temp_5);
			temp_0.add(temp_1);
        	temp_0.add(temp_2);
        	temp_0.add(temp_3);
        	temp_0.add(temp_4);
        	temp_0.add(temp_6);
        	jDialog_c.add(temp_0);
        	
        	bold.addItemListener( this );
        	italic.addItemListener( this );
        	fontsize.addChangeListener( this );
        	ok.addActionListener(this);
        	//cancel.addActionListener(this);
        	temp_0.setPreferredSize(new Dimension( 180 , 150 ));
        	
			repaint();
    		addMouseListener(this);
    		addMouseMotionListener(this);
		}
		public void resize(){//改變大小
			bufImg = new BufferedImage(Variable.draw_panel_width
					, Variable.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用來繪圖
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Variable.draw_panel_width
					, Variable.draw_panel_height));
			
    		//畫出原本圖形//
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Variable.draw_panel_width
    				,Variable.draw_panel_height));
    		g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			//記錄可重做最大次數，並讓重做不可按//
			redo_lim=count++;
			variable.jMenuItem[1][1].setEnabled(false);
			
   			//新增一張BufferedImage型態至bufImg_data[count]，並將bufImg繪製至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Variable.draw_panel_width
   					, Variable.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count]
   					.getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);
   			
			//判斷座標為新起點//
			press=0;
			
			//讓復原MenuItem可以點選//
   			if(count>0)
   				variable.jMenuItem[1][0].setEnabled(true);
		}
		public void stateChanged(ChangeEvent e){
			size = Integer.parseInt(fontsize.getValue().toString());
			if(size <= 0) {
				fontsize.setValue(new Integer(1));
				size = 1;
			}
		}
		
		public void actionPerformed( ActionEvent e ){
			jDialog.dispose();
		}
		public void itemStateChanged( ItemEvent e ){
			if ( e.getSource() == bold )
				if ( e.getStateChange() == ItemEvent.SELECTED )
					valBold = Font.BOLD;
				else
					valBold = Font.PLAIN;
			if ( e.getSource() == italic )
				if ( e.getStateChange() == ItemEvent.SELECTED )
					valItalic = Font.ITALIC;
				else
					valItalic = Font.PLAIN;
		}
		
		public Dimension getPreferredSize(){
			return new Dimension( Variable.draw_panel_width
					, Variable.draw_panel_height );
		}
		
		public void openfile(String filename){//開啟舊檔
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
			ImageIcon img = new ImageIcon(filename);
			g2d_bufImg.drawImage(img.getImage(),0,0,this);
			
			count++;
    		bufImg_data[count] = new BufferedImage(Variable.draw_panel_width
    				, Variable.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
    		Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count]
    				.getGraphics();
    		g2d_bufImg_data.drawImage(bufImg,0,0,this);
			
			repaint();
		}
		
		public void undo(){//上一步
   			count--;
			
   			Variable.draw_panel_width=bufImg_data[count].getWidth();
   			Variable.draw_panel_height=bufImg_data[count].getHeight();
   			setSize(Variable.draw_panel_width,Variable.draw_panel_height);

			bufImg = new BufferedImage(Variable.draw_panel_width
					, Variable.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用來繪圖
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Variable.draw_panel_width
					, Variable.draw_panel_height));
			
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Variable.draw_panel_width
    				,Variable.draw_panel_height));
			g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			painter.underDrawPanel.ctrl_area.setLocation(Variable.draw_panel_width+3
					,Variable.draw_panel_height+3);
			painter.underDrawPanel.ctrl_area2.setLocation(Variable.draw_panel_width+3
					,Variable.draw_panel_height/2+3);
			painter.underDrawPanel.ctrl_area3.setLocation(Variable.draw_panel_width/2+3
					,Variable.draw_panel_height+3);
			
			painter.underDrawPanel.x=Variable.draw_panel_width;
			painter.underDrawPanel.y=Variable.draw_panel_height;
			
	   		if(count<=0)
	   			variable.jMenuItem[1][0].setEnabled(false);
	   		variable.jMenuItem[1][1].setEnabled(true);
	    	cut=3;
   			repaint();
   		}

		public void redo(){//下一步
			count++;
			
			Variable.draw_panel_width=bufImg_data[count].getWidth();
			Variable.draw_panel_height=bufImg_data[count].getHeight();
   			setSize(Variable.draw_panel_width,Variable.draw_panel_height);

			bufImg = new BufferedImage(Variable.draw_panel_width
					, Variable.draw_panel_height,BufferedImage.TYPE_3BYTE_BGR);
			jlbImg = new JLabel(new ImageIcon(bufImg));//在JLabel上放置bufImg，用來繪圖
			this.removeAll();
			this.add(jlbImg);
			jlbImg.setBounds(new Rectangle(0, 0, Variable.draw_panel_width
					, Variable.draw_panel_height));
			
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.white);
    		g2d_bufImg.fill(new Rectangle2D.Double(0,0,Variable.draw_panel_width
    				,Variable.draw_panel_height));
			g2d_bufImg.drawImage(bufImg_data[count],0,0,this);

			painter.underDrawPanel.ctrl_area.setLocation(Variable.draw_panel_width+3
					,Variable.draw_panel_height+3);
			painter.underDrawPanel.ctrl_area2.setLocation(Variable.draw_panel_width+3
					,Variable.draw_panel_height/2+3);
			painter.underDrawPanel.ctrl_area3.setLocation(Variable.draw_panel_width/2+3
					,Variable.draw_panel_height+3);
			
			painter.underDrawPanel.x=Variable.draw_panel_width;
			painter.underDrawPanel.y=Variable.draw_panel_height;
			
			if(redo_lim<count)
				variable.jMenuItem[1][1].setEnabled(false);
			variable.jMenuItem[1][0].setEnabled(true);
			cut=3;
   			repaint();
   		}
		
		public void cut(){//剪下
			bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth()
					, (int)rectangle2D_select.getHeight()
					, BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX()
					,(int)rectangle2D_select.getY(),(int)rectangle2D_select
					.getWidth(),(int)rectangle2D_select.getHeight());
			Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
			g2d_bufImg_cut.drawImage(copy,0,0,this);
			
    		Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    		g2d_bufImg.setPaint(Color.WHITE);
			g2d_bufImg.fill(new Rectangle2D.Double((int)rectangle2D_select
					.getX(),(int)rectangle2D_select.getY(),(int)rectangle2D_select
					.getWidth(),(int)rectangle2D_select.getHeight()));
			
			redo_lim=count++;
			variable.jMenuItem[1][1].setEnabled(false);
			
   			//新增一張BufferedImage型態至bufImg_data[count]，並將bufImg繪製至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Variable.draw_panel_width
   					, Variable.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count]
   					.getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);

			//判斷座標為新起點//
			press=0;
			
			//讓復原MenuItem可以點選//
   			if(count>0)
   				variable.jMenuItem[1][0].setEnabled(true);
   			variable.jMenuItem[1][2].setEnabled(false);
   			variable.jMenuItem[1][3].setEnabled(false);
   			variable.jMenuItem[1][4].setEnabled(true);
			cut=3;
			repaint();
		}
		public void copy(){//複製
			bufImg_cut = new BufferedImage((int)rectangle2D_select.getWidth()
					, (int)rectangle2D_select.getHeight()
					, BufferedImage.TYPE_3BYTE_BGR);
			BufferedImage copy = bufImg.getSubimage((int)rectangle2D_select.getX()
					,(int)rectangle2D_select.getY(),(int)rectangle2D_select
					.getWidth(),(int)rectangle2D_select.getHeight());
			Graphics2D g2d_bufImg_cut = (Graphics2D) bufImg_cut.createGraphics();
			g2d_bufImg_cut.drawImage(copy,0,0,this);
			variable.jMenuItem[1][4].setEnabled(true);
			cut=1;
			repaint();
		}
		public void paste(){//貼上
			cut=2;
			repaint();
		}
    	public void mousePressed(MouseEvent e) {
    		x1=e.getX();
    		y1=e.getY();
    		if(first==0){
    			polygon = new Polygon();
				polygon.addPoint(x1, y1);
				first=1;
			}
			//判斷座標為新起點//
			press=1;
			chk=0;
			if(cut!=2) cut=0;
			
		}

    	public void mouseReleased(MouseEvent e) {
    		x2=e.getX();
    		y2=e.getY();
    		
    		if(step_chk==0)//控制貝氏曲線用
    			step=1;
    		else if(step_chk==1)
    			step=2;
    		
    		if(step_chk_arc==0)//控制扇型用
    			chk=step_arc=1;
    		else if(step_chk_arc==1)
    			chk=step_arc=2;
			
			if(variable.drawMethod==6 && click!=1){
				polygon.addPoint(x2, y2);
				repaint();
			}
			if(variable.drawMethod==10){
				if(cut!=2) cut=1;
				select_x=(int)rectangle2D_select.getX();
				select_y=(int)rectangle2D_select.getY();
				select_w=(int)rectangle2D_select.getWidth();
				select_h=(int)rectangle2D_select.getHeight();
				variable.jMenuItem[1][2].setEnabled(true);
				variable.jMenuItem[1][3].setEnabled(true);
			}

    		if((step_chk==2 && step==2) || (step_chk_arc==2 && step_arc==2) 
    				|| variable.drawMethod==0 || variable.drawMethod==1 
    				|| variable.drawMethod==2 || variable.drawMethod==3 
    				|| variable.drawMethod==7 || variable.drawMethod==8 
    				|| variable.drawMethod==9 || cut==2||checkDrawMethod()){//當不是畫貝氏曲線或是已經完成貝氏曲線時畫
				toDraw();
    		}
    		
		}
		public void clear(){
			cut=select_x=select_y=select_w=select_h=step_chk_arc=step_arc
					=first=step_chk=step=0;
			x1=x2=y1=y2=-1;
		}
		
		public void toDraw(){
			if(x1<0 || y1<0) return;//防止誤按
			chk=3;
			draw(x1,y1,x2,y2);
			
			//畫出圖形至bufImg//
			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
			if(cut!=2){
				if(checkDrawMethod()) {
				g2d_bufImg.drawImage(icon.getImage(),x2-40,y2-40,this);

				}
				if(variable.color_inside!=null && variable.drawMethod!=8&&!checkDrawMethod()){
					g2d_bufImg.setPaint(variable.color_inside);
					g2d_bufImg.fill(variable.shape);
				}
				if(variable.color_border!=null && variable.drawMethod!=8&&!checkDrawMethod()){
					g2d_bufImg.setPaint(variable.color_border);
					g2d_bufImg.setStroke(variable.stroke);
					g2d_bufImg.draw(variable.shape);
				}
			}
			else{
   				g2d_bufImg.drawImage(bufImg_cut,x2,y2,this);
			}
			repaint();
			clear();
			//記錄可重做最大次數，並讓重做不可按//
			redo_lim=count++;
			variable.jMenuItem[1][1].setEnabled(false);
			
   			//新增一張BufferedImage型態至bufImg_data[count]，並將bufImg繪製至bufImg_data[count]//
   			bufImg_data[count] = new BufferedImage(Variable.draw_panel_width
   					, Variable.draw_panel_height, BufferedImage.TYPE_3BYTE_BGR);
   			Graphics2D g2d_bufImg_data = (Graphics2D) bufImg_data[count]
   					.getGraphics();
   			g2d_bufImg_data.drawImage(bufImg,0,0,this);
   			
			//判斷座標為新起點//
			press=0;
			
			//讓復原MenuItem可以點選//
   			if(count>0)
   				variable.jMenuItem[1][0].setEnabled(true);
		}
		
    	public void mouseEntered(MouseEvent e){}
    	public void mouseExited(MouseEvent e){}
    	public void mouseClicked(MouseEvent e){
//    		if(click==1){//連點兩下時
//    			toDraw();
//    		}
//    		click=1;
//    		System.out.println(pic);
//    		if(variable.drawMethod==11){//大樓
//				pic = new DrawPicture("大樓",x1,y1);
//				System.out.println(pic);
//				painter.underDrawPanel.add(pic);
//				pics.add(pic);
//			}
//    		for(DrawPicture p:pics) {
//    			add(p);
//    		}
//			else if(variable.drawMethod==12){//平交道
//				pic = new DrawPicture("大樓",x1,y1);
//				pics.add(pic);
//			}
//			else if(variable.drawMethod==13){//紅綠燈
//				variable.shape=roundRectangle2D;
//				roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2)
//						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
//						,Math.abs(input_y1-input_y2),10.0f,10.0f);
//			}
//			else if(variable.drawMethod==14){//停車場
//				variable.shape=roundRectangle2D;
//				roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2)
//						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
//						,Math.abs(input_y1-input_y2),10.0f,10.0f);
//			}
//			else if(variable.drawMethod==15){//救護站
//				variable.shape=roundRectangle2D;
//				roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2)
//						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
//						,Math.abs(input_y1-input_y2),10.0f,10.0f);
//			}
    	}
    	
    	public void mouseDragged(MouseEvent e){
    		x2=e.getX();
    		y2=e.getY();
    		if(variable.drawMethod==7 || variable.drawMethod==8){
				draw(x1,y1,x2,y2);
				x1=e.getX();
				y1=e.getY();
			}
			if(variable.drawMethod!=9)
    			repaint();
    	}

    	public void mouseMoved(MouseEvent e) {
    		variable.show_x=x2=e.getX();
    		variable.show_y=y2=e.getY();
    		
    		variable.jLabel.setText(variable.show_x+","+variable.show_y);
			click=0;
			if(variable.drawMethod==7 || variable.drawMethod==8 || cut==2||checkDrawMethod())
				repaint();
    	}
		
		public void draw(int input_x1,int input_y1,int input_x2,int input_y2){
			if(variable.drawMethod==0){//直線時，讓shape為Line2D
				variable.shape=line2D;
				line2D.setLine(input_x1,input_y1,input_x2,input_y2);
			}
			else if(variable.drawMethod==1){//矩型時，讓shape為Rectangle2D
				variable.shape=rectangle2D;
				rectangle2D.setRect(Math.min(input_x1,input_x2)
						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
						,Math.abs(input_y1-input_y2));
			}
			else if(variable.drawMethod==2){//橢圓時
				variable.shape=ellipse2D;
				ellipse2D.setFrame(Math.min(input_x1,input_x2)
						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
						,Math.abs(input_y1-input_y2));
			}
			else if(variable.drawMethod==3){//圓角矩型
				variable.shape=roundRectangle2D;
				roundRectangle2D.setRoundRect(Math.min(input_x1,input_x2)
						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
						,Math.abs(input_y1-input_y2),10.0f,10.0f);
			}
			
			else if(variable.drawMethod==4){//貝氏曲線
				variable.shape=cubicCurve2D;
				if(step==0){
					cubicCurve2D.setCurve(input_x1,input_y1,input_x1,input_y1
							,input_x2,input_y2,input_x2,input_y2);
					temp_x1=input_x1;
					temp_y1=input_y1;
					temp_x2=input_x2;
					temp_y2=input_y2;
					step_chk=0;
				}
				else if(step==1){
					cubicCurve2D.setCurve(temp_x1,temp_y1,input_x2,input_y2
							,input_x2,input_y2,temp_x2,temp_y2);
					temp_x3=input_x2;
					temp_y3=input_y2;
					step_chk=1;
				}
				else if(step==2){
					cubicCurve2D.setCurve(temp_x1,temp_y1,temp_x3,temp_y3
							,input_x2,input_y2,temp_x2,temp_y2);
					step_chk=2;
				}
			}
			else if(variable.drawMethod==5){//扇型，chk用來防止意外的repaint//
				if(step_arc==0 || chk==1){//步驟控制
					variable.shape=ellipse2D;
					ellipse2D.setFrame(Math.min(input_x1,input_x2)
							,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
							,Math.abs(input_y1-input_y2));
					temp_x1=input_x1;
					temp_y1=input_y1;
					temp_x2=input_x2;
					temp_y2=input_y2;
					step_chk_arc=0;
				}
				else if(step_arc==1 || chk==2){//步驟控制
					variable.shape=arc2D;

					center_point_x = Math.min(temp_x1,temp_x2)
							+Math.abs(temp_x1-temp_x2)/2;
					center_point_y = Math.min(temp_y1,temp_y2)
							+Math.abs(temp_y1-temp_y2)/2;
					
					double a = Math.pow(Math.pow(input_x2-center_point_x,2)
							+Math.pow(input_y2-center_point_y,2),0.5);
					double b = input_x2-center_point_x;
					if(input_y2>center_point_y)
						start=360+Math.acos(b/a)/Math.PI*-180;
					else
						start=Math.acos(b/a)/Math.PI*180;
					
					arc2D.setArc(Math.min(temp_x1,temp_x2),Math.min(temp_y1,temp_y2)
							,Math.abs(temp_x1-temp_x2),Math.abs(temp_y1-temp_y2)
							,start,0,pie_shape);
					step_chk_arc=1;
				}
				else if(step_arc==2 || chk==3){//步驟控制
					variable.shape=arc2D;
					
					double a = Math.pow(Math.pow(input_x2-center_point_x,2)
							+Math.pow(input_y2-center_point_y,2),0.5);
					double b = input_x2-center_point_x;
					if(input_y2>center_point_y)
						end=360+Math.acos(b/a)/Math.PI*-180-start;
					else
						end=Math.acos(b/a)/Math.PI*180-start;
					if(end<0){end=360-Math.abs(end);}
					
					arc2D.setArc(Math.min(temp_x1,temp_x2),Math.min(temp_y1,temp_y2)
							,Math.abs(temp_x1-temp_x2),Math.abs(temp_y1-temp_y2)
							,start,end,pie_shape);
					step_chk_arc=2;
				}
			}
			else if(variable.drawMethod==6){//多邊型
				variable.shape=polygon;
				repaint();
			}
			else if(variable.drawMethod==7 || variable.drawMethod==8){//任意線＆橡皮擦
    			Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
    			
    			variable.shape=line2D;
				line2D.setLine(input_x1,input_y1,input_x2,input_y2);
				if(variable.drawMethod==7)
					g2d_bufImg.setPaint(variable.color_border);
				else
					g2d_bufImg.setPaint(Color.white);
				g2d_bufImg.setStroke(variable.stroke);
				g2d_bufImg.draw(variable.shape);
			}
			
			else if(variable.drawMethod==9){//文字
				Graphics2D g2d_bufImg = (Graphics2D) bufImg.getGraphics();
        		FontRenderContext frc = g2d_bufImg.getFontRenderContext();
        		jDialog.show();
        		
        		Font f = new Font(textField_font.getText(),valBold + valItalic,size);
        		TextLayout tl = new TextLayout(textField_word.getText(), f, frc);
        		double sw = tl.getBounds().getWidth();
        		double sh = tl.getBounds().getHeight();

        		AffineTransform Tx = AffineTransform.getScaleInstance(1, 1);
        		Tx.translate(input_x2,input_y2+sh);
        		variable.shape = tl.getOutline(Tx);
			}
			else if(variable.drawMethod==10){//選取工具
				variable.shape=rectangle2D;
				rectangle2D.setRect(Math.min(input_x1,input_x2)
						,Math.min(input_y1,input_y2),Math.abs(input_x1-input_x2)
						,Math.abs(input_y1-input_y2));
			}
			else if(checkDrawMethod()) {//大樓
				icon = new ImageIcon(getClass().getResource(variable.toolname[variable.drawMethod]));
			}
			
			if(variable.color_border instanceof GradientPaint){//使用漸層填色讀取拖拉座標
				variable.color_border = new GradientPaint( input_x1,input_y1
						, (Color)((GradientPaint)variable.color_border).getColor1()
						, input_x2,input_y2, (Color)((GradientPaint)variable
								.color_border).getColor2(), true );
			}
			if(variable.color_inside instanceof GradientPaint){
				variable.color_inside = new GradientPaint( input_x1,input_y1
						, (Color)((GradientPaint)variable.color_inside).getColor1()
						, input_x2,input_y2, (Color)((GradientPaint)variable
								.color_inside).getColor2(), true );
			}
		}
		
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			super.paint(g2d);//重繪底層JPanel以及上面所有元件

			if(press==1 && variable.drawMethod!=10 && !(x1<0 || y1<0)) {//繪圖在最上面的JLabel上，並判斷是不是起點才畫
				draw(x1,y1,x2,y2);
				
				if(variable.drawMethod==8) return;
				if(checkDrawMethod()) {
					g2d.drawImage(icon.getImage(),x2-40,y2-40,this);
				}
				if(variable.color_inside!=null&&!checkDrawMethod()){
					g2d.setPaint(variable.color_inside);
					g2d.fill(variable.shape);
				}
				if(variable.color_border!=null&&!checkDrawMethod()){
					g2d.setPaint(variable.color_border);
					g2d.setStroke(variable.stroke);
					g2d.draw(variable.shape);
				}
			}

			if(variable.drawMethod==10 && cut==0){//選取控制、判斷是否選取、剪下、或貼上
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_select);
				rectangle2D_select.setRect(Math.min(x1,x2),Math.min(y1,y2)
						,Math.abs(x1-x2),Math.abs(y1-y2));
				g2d.draw(rectangle2D_select);
			}
			if(cut==1){
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_select);
				rectangle2D_select.setRect(select_x,select_y,select_w,select_h);
				g2d.draw(rectangle2D_select);
			}
			if(cut==2){
   				g2d.drawImage(bufImg_cut,x2,y2,this);
   			}

			//跟隨游標的圓形//
			if(variable.drawMethod==7 ||variable.drawMethod==8){
				g2d.setPaint(Color.black);
				g2d.setStroke(basicStroke_pen);
				ellipse2D_pan.setFrame(x2-painter.setPanel.number/2
						,y2-painter.setPanel.number/2,painter.setPanel.number
						,painter.setPanel.number);
				g2d.draw(ellipse2D_pan);
			}
		}
		public boolean checkDrawMethod() {
			if(variable.drawMethod>=11)return true;
			return false;
		}
	}