import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;

public class SetPanel extends JPanel implements ItemListener, ChangeListener, ActionListener{
		private	JPanel jPanel_set1=new JPanel();
		private	JPanel jPanel_set2=new JPanel();
		private	JPanel temp0=new JPanel(new GridLayout(4,1))
				, temp1=new JPanel(new FlowLayout(FlowLayout.LEFT))
				, temp2=new JPanel(new FlowLayout(FlowLayout.LEFT))
				, temp3=new JPanel(new FlowLayout(FlowLayout.LEFT))
				, temp4=new JPanel(new FlowLayout(FlowLayout.LEFT))
				, temp5=new JPanel(new FlowLayout(FlowLayout.LEFT))
				, temp6=new JPanel(new GridLayout(3,1));

		public JCheckBox jCheckBox = new JCheckBox();
		private BufferedImage bufImg = new BufferedImage(50 ,50,BufferedImage.TYPE_3BYTE_BGR);
		private JLabel jlbImg=new JLabel();
		float data[]={20};
		JLabel pie[]=new JLabel[3];
		public int number=5;
		JSpinner lineWidthSelect = new JSpinner();
		JRadioButton style[] = new JRadioButton[ 3 ];
		ButtonGroup styleGroup = new ButtonGroup() ,pieGroup = new ButtonGroup();
		
		private Variable variable;
		private Painter painter;
        
		public SetPanel(Variable var,Painter paint){//產生版面//
			this.variable = var;
			this.painter = paint;
			
			this.setLayout(null);
			this.add(jPanel_set1);

			jlbImg.setIcon(new ImageIcon(bufImg));
			jPanel_set1.setLayout(new FlowLayout());
			jPanel_set1.setBounds(new Rectangle(0, 0, 100, 160));
			jPanel_set1.setBorder( new TitledBorder(null, "邊框",TitledBorder.LEFT, TitledBorder.TOP) );
			lineWidthSelect.setValue(new Integer(5));

      
			temp1.add(new JLabel("大小:"));
			temp1.add(lineWidthSelect);
			
			temp2.add(new JLabel("虛線:"));
			temp2.add(jCheckBox);
			
			temp0.add(temp1);
			temp0.add(temp2);

			
			jPanel_set1.add(temp0);
			lineWidthSelect.addChangeListener( this );
			jCheckBox.addItemListener( this );
			
			jPanel_set2.setBounds(new Rectangle(0, 170, 100, 130));
			jPanel_set2.setBorder( new TitledBorder(null, "扇型設定",TitledBorder.LEFT, TitledBorder.TOP) );
			
			for(int i=0;i<=2;i++){
				style[i] = new JRadioButton();
				pieGroup.add(style[i]);
				style[i].addActionListener(this);
			}
			style[2].setSelected( true );
			
			pie[0] = new JLabel("弦狀:");
			temp3.add(pie[0]);
			temp3.add(style[0]);
			
			pie[1] = new JLabel("開放:");
			temp4.add(pie[1]);
			temp4.add(style[1]);
			
			pie[2] = new JLabel("派狀:");
			temp5.add(pie[2]);
			temp5.add(style[2]);
			
			temp6.add(temp3);
			temp6.add(temp4);
			temp6.add(temp5);
			
			temp6.setPreferredSize(new Dimension( 71 , 95 ));

			jPanel_set2.add(temp6);
			this.add(jPanel_set2);

			pie_remove_ctrl();
			variable.stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		}
		//邊框設定，隱藏
		public void pencil_add_ctrl(){
			jCheckBox.setSelected(false);
			jCheckBox.setEnabled(false);
			BasicStroke stroke2 = (BasicStroke) variable.stroke;
			variable.stroke = new BasicStroke(stroke2.getLineWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
		}
		//邊框設定，顯示
		public void pencil_remove_ctrl(){
			jCheckBox.setEnabled(true);
		}
		//扇形設定，顯示
		public void pie_add_ctrl(){
			pie[0].setEnabled(true);
			pie[1].setEnabled(true);
			pie[2].setEnabled(true);
			style[0].setEnabled(true);
			style[1].setEnabled(true);
			style[2].setEnabled(true);
		}
		//扇形設定，隱藏
		public void pie_remove_ctrl(){
			pie[0].setEnabled(false);
			pie[1].setEnabled(false);
			pie[2].setEnabled(false);
			style[0].setEnabled(false);
			style[1].setEnabled(false);
			style[2].setEnabled(false);
		}
		
		public void actionPerformed( ActionEvent e ){
			if ( e.getSource() == style[0] )
				painter.drawPanel.pie_shape=Arc2D.CHORD;
			else if ( e.getSource() == style[1] )
				painter.drawPanel.pie_shape=Arc2D.OPEN;
			else if ( e.getSource() == style[2] )
				painter.drawPanel.pie_shape=Arc2D.PIE;
		}
		
		public void stateChanged(ChangeEvent e){
			number = Integer.parseInt(lineWidthSelect.getValue().toString());
			if(number <= 0) {
				lineWidthSelect.setValue(new Integer(1));
				number = 1;
			}
        	BasicStroke stroke2 = (BasicStroke) variable.stroke;
        	variable.stroke = new BasicStroke( number, stroke2.getEndCap(), stroke2.getLineJoin()
        			, stroke2.getMiterLimit(), stroke2.getDashArray(), stroke2.getDashPhase() );
		}
		
		public void itemStateChanged( ItemEvent e ){
			BasicStroke stroke2 = (BasicStroke) variable.stroke;
			if ( e.getSource() == jCheckBox ){
				if ( e.getStateChange() == ItemEvent.SELECTED )
					variable.stroke = new BasicStroke( stroke2.getLineWidth(), stroke2.getEndCap()
							, stroke2.getLineJoin(), 10, data, 0 );
				else
					variable.stroke = new BasicStroke( stroke2.getLineWidth(), stroke2.getEndCap()
							, stroke2.getLineJoin());
			}
		}
		
		public Dimension getPreferredSize(){
			return new Dimension( 100, 300 );
		}


	}