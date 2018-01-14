import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

public class Painter extends JFrame implements ActionListener {
	private	Container c = getContentPane();
	
	public SetPanel setPanel;///屬性欄Panel
	public DrawPanel drawPanel;
	public UnderDrawPanel underDrawPanel;
	public ColorPanel colorPanel;//色塊Panel
	public Variable variable;
	
	private DrawPicture pic;
	
	public Painter(Variable var){
		this.variable = var;
		//設定JMenuBar，並產生JMenuItem、並設置快捷鍵
		JMenuBar bar = new JMenuBar();
		variable.jMenu = new JMenu[variable.menuBar.length];
		for(int i=0;i<variable.menuBar.length;i++){
			variable.jMenu[i] = new JMenu(variable.menuBar[i]);
			//設置快捷鍵(Alt)(setMnemonic)
			variable.jMenu[i].setMnemonic(variable.menuBar[i].split("\\(")[1].charAt(0));
			bar.add(variable.jMenu[i]);
		}
		
		for(int i=0;i<variable.menuItem.length;i++){
			for(int j=0;j<variable.menuItem[i].length;j++){
				if(i==0 && j==4 || i==0 && j==7 || i==1 && j==2 ) variable.jMenu[i].addSeparator();//加入分隔線
				if(i!=2){
					variable.jMenuItem[i][j] = new JMenuItem(variable.menuItem[i][j].split("\\|")[0]);
					//設置Ctrl快捷鍵(setAccelerator)
					if(variable.menuItem[i][j].split("\\|").length!=1)
						variable.jMenuItem[i][j].setAccelerator(KeyStroke.getKeyStroke(
								Integer.parseInt(variable.menuItem[i][j].split("\\|")[1]), ActionEvent.CTRL_MASK) );
					variable.jMenuItem[i][j].addActionListener(this);
					//設置快捷鍵(Alt)(setMnemonic)
					variable.jMenuItem[i][j].setMnemonic(variable.menuItem[i][j].split("\\(")[1].charAt(0));

					variable.jMenu[i].add(variable.jMenuItem[i][j]);
				}
				else{
					variable.jCheckBoxMenuItem[j] = new JCheckBoxMenuItem(variable.menuItem[i][j].split("\\|")[0]);
					//設置Ctrl快捷鍵(setAccelerator)
					if(variable.menuItem[i][j].split("\\|").length!=1)
						variable.jCheckBoxMenuItem[j].setAccelerator(KeyStroke.getKeyStroke(Integer.parseInt(variable.menuItem[i][j].split("\\|")[1]), ActionEvent.CTRL_MASK) );
					variable.jCheckBoxMenuItem[j].addActionListener(this);
					//設置快捷鍵(Alt)(setMnemonic)
					variable.jCheckBoxMenuItem[j].setMnemonic(variable.menuItem[i][j].split("\\(")[1].charAt(0));
					variable.jCheckBoxMenuItem[j].setSelected( true );
					variable.jMenu[i].add(variable.jCheckBoxMenuItem[j]);
				}
			}
		}
		this.setJMenuBar( bar );
		c.setLayout( new BorderLayout() );
		for(int i=0;i<4;i++)
			variable.jPanel[i]=new JPanel();
		
		variable.buttonGroup = new ButtonGroup();
		//將JToolBar設為垂直擺放，預設為水平擺放
		JToolBar jToolBar=new JToolBar("工具箱",JToolBar.VERTICAL);
		
		variable.jToggleButton=new JToggleButton[variable.ButtonName.length];
		for(int i=0;i<variable.ButtonName.length;i++){
			variable.toolIcon[i] = new ImageIcon(getClass()
					.getResource(variable.toolname[i]));
			variable.jToggleButton[i] = new JToggleButton(variable.toolIcon[i]);
			variable.jToggleButton[i].addActionListener( this );
			variable.jToggleButton[i].setFocusable( false );
			variable.buttonGroup.add(variable.jToggleButton[i]);
		}
		jToolBar.add(variable.jToggleButton[7]);
		jToolBar.add(variable.jToggleButton[8]);
		jToolBar.add(variable.jToggleButton[0]);
		jToolBar.add(variable.jToggleButton[4]);
		jToolBar.add(variable.jToggleButton[1]);
		jToolBar.add(variable.jToggleButton[3]);
		jToolBar.add(variable.jToggleButton[2]);
		jToolBar.add(variable.jToggleButton[5]);
		jToolBar.add(variable.jToggleButton[9]);
		jToolBar.add(variable.jToggleButton[10]);
		jToolBar.add(variable.jToggleButton[11]);
		jToolBar.add(variable.jToggleButton[12]);
		jToolBar.add(variable.jToggleButton[13]);
		jToolBar.add(variable.jToggleButton[14]);
		jToolBar.add(variable.jToggleButton[15]);
		jToolBar.add(variable.jToggleButton[16]);
		jToolBar.add(variable.jToggleButton[17]);
		jToolBar.add(variable.jToggleButton[18]);
		jToolBar.add(variable.jToggleButton[19]);
		jToolBar.add(variable.jToggleButton[20]);
		jToolBar.add(variable.jToggleButton[21]);
		jToolBar.add(variable.jToggleButton[22]);
		variable.jToggleButton[7].setSelected(true);
		jToolBar.setLayout( new GridLayout( 8, 3, 2, 2 ) );
		variable.jPanel[1].add(jToolBar);
		
		//jToolBar.setFloatable(false);//無法移動
		
		colorPanel=new ColorPanel(variable,this);
		variable.jPanel[2].setLayout(new FlowLayout(FlowLayout.LEFT));
		variable.jPanel[2].add(colorPanel);
		
		drawPanel=new DrawPanel(variable,this);
		underDrawPanel=new UnderDrawPanel(variable,this);
		underDrawPanel.setLayout(null);
		underDrawPanel.add(drawPanel);
		drawPanel.setBounds(new Rectangle(2, 2, variable.draw_panel_width
				, variable.draw_panel_height));
		
		setPanel=new SetPanel(variable,this);
		variable.jPanel[3].add(setPanel);
		
		variable.jPanel[0].setLayout( new BorderLayout() );
		variable.jPanel[0].add(underDrawPanel,BorderLayout.CENTER);
		variable.jPanel[0].add(variable.jPanel[1],BorderLayout.WEST);
		variable.jPanel[0].add(variable.jPanel[2],BorderLayout.SOUTH);
		variable.jPanel[0].add(variable.jPanel[3],BorderLayout.EAST);
		
		variable.jLabel.setBorder(BorderFactory.createBevelBorder(
				BevelBorder.LOWERED));
		underDrawPanel.setBorder(BorderFactory.createBevelBorder(
				BevelBorder.LOWERED));
		underDrawPanel.setBackground(new Color(128,128,128));
		variable.jPanel[2].setBorder(BorderFactory.createMatteBorder(
				1,0,0,0,new Color(172,168,153)));
		
		c.add(variable.jPanel[0],BorderLayout.CENTER);
		c.add(variable.jLabel,BorderLayout.SOUTH);
		
		setSize(variable.draw_panel_width,variable.draw_panel_height);
		setTitle("Java期末專案");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		show();
	}
	//檔案儲存
	public void save(){
		FileDialog fileDialog = new FileDialog( new Frame() , "請指定一個檔名"
				, FileDialog.SAVE );
		fileDialog.show();
		if(fileDialog.getFile()==null) return;
		drawPanel.filename = fileDialog.getDirectory()+fileDialog.getFile();
	}
	//工具箱中的按鈕事件
	public void actionPerformed( ActionEvent e ){
		for(int i=0;i<variable.ButtonName.length;i++){
			if(e.getSource()==variable.jToggleButton[i]){
				variable.drawMethod=i;
//				if(i>=11) {
//					
//				}
				if(variable.drawMethod==5)
					setPanel.pie_add_ctrl();
				else
					setPanel.pie_remove_ctrl();
				if(variable.drawMethod==7 || variable.drawMethod==8)
					setPanel.pencil_add_ctrl();
				else
					setPanel.pencil_remove_ctrl();
				drawPanel.clear();
				drawPanel.repaint();
				variable.jMenuItem[1][2].setEnabled(false);
				variable.jMenuItem[1][3].setEnabled(false);
			}
		}
		
		if(e.getSource()==variable.jMenuItem[1][0]){
			drawPanel.undo();//上一步
		}
		else if(e.getSource()==variable.jMenuItem[1][1]){
			drawPanel.redo();//下一步
		}
		else if(e.getSource()==variable.jMenuItem[1][2]){
			drawPanel.cut();//剪下
		}
		else if(e.getSource()==variable.jMenuItem[1][3]){
			drawPanel.copy();//複製
		}
		else if(e.getSource()==variable.jMenuItem[1][4]){
			drawPanel.paste();//貼上
		}
		else if(e.getSource()==variable.jMenuItem[0][0]){//開新檔案
			underDrawPanel.remove(drawPanel);
			drawPanel=null;
			drawPanel=new DrawPanel(variable,this);
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, variable.draw_panel_width
					, variable.draw_panel_height));
			underDrawPanel.ctrl_area.setLocation(variable.draw_panel_width+3
					,variable.draw_panel_height+3);
			underDrawPanel.ctrl_area2.setLocation(variable.draw_panel_width+3
					,variable.draw_panel_height/2+3);
			underDrawPanel.ctrl_area3.setLocation(variable.draw_panel_width/2+3
					,variable.draw_panel_height+3);
			repaint();
		}
		else if(e.getSource()==variable.jMenuItem[0][1]){//開啟舊檔
			FileDialog fileDialog = new FileDialog( new Frame() 
					, "選擇一個圖檔", FileDialog.LOAD );
			fileDialog.show();
			if(fileDialog.getFile()==null) return;
			
			underDrawPanel.removeAll();
			drawPanel=null;
			drawPanel=new DrawPanel(variable,this);
			underDrawPanel.add(drawPanel);
			drawPanel.setBounds(new Rectangle(2, 2, variable.draw_panel_width
					, variable.draw_panel_height));
			
			drawPanel.openfile(fileDialog.getDirectory()+fileDialog.getFile());
		}
		else if(e.getSource()==variable.jMenuItem[0][2]){//儲存檔案
			if(drawPanel.filename==null){
				save();
			}
			else{
				try{
					int dotpos = drawPanel.filename.lastIndexOf('.');
					ImageIO.write(drawPanel.bufImg, drawPanel.filename
							.substring(dotpos + 1), new File(drawPanel.filename));
				}
				catch(IOException even) {
					JOptionPane.showMessageDialog(null, even.toString()
							,"無法儲存圖檔", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
		else if(e.getSource()==variable.jMenuItem[0][3]){//另存新檔
			save();
			try{
				int dotpos = drawPanel.filename.lastIndexOf('.');
				ImageIO.write(drawPanel.bufImg, drawPanel.filename
						.substring(dotpos + 1), new File(drawPanel.filename));
			}
			catch(IOException event) {
				JOptionPane.showMessageDialog(null, event.toString()
						,"無法儲存圖檔", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource()==variable.jMenuItem[0][4]){//錄音
			MyRecord record = new MyRecord();
		}
		else if(e.getSource()==variable.jMenuItem[0][5]){//Google map
			if (Desktop.isDesktopSupported()) {
			    try {
			    	URL base = this.getClass().getResource("GoogleAPI.html");
					Desktop.getDesktop().browse(base.toURI());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if(e.getSource()==variable.jMenuItem[0][6]){//截圖
			FullScreenCapture capture = new FullScreenCapture(drawPanel);
			capture.setVisible(true);
			if(capture.testT() == true) {
				drawPanel = capture.getDrawPic();
			}
		}
		else if(e.getSource()==variable.jMenuItem[0][7]){//離開
			System.exit(0);
		}
		else if(e.getSource()==variable.jMenuItem[3][0]){//關於
			JOptionPane.showMessageDialog(null, "程式名稱：報路狂\n系統概述：本應用程式是利用2D繪圖的方式繪製平面圖，如展場導覽圖、\r\n" + 
					"               室內平面圖等。使用者可以選擇畫筆來繪製，亦可以使用程式裡提\r\n" + 
					"               供的多種圖示來表達地圖上已知的一些地標來指路，也結合Google\r\n" + 
					"               Map，方便使用者繪製戶外的地圖。程式也提供一些輔助功能，如錄\r\n" + 
					"               音錄影，可以讓使用者更清楚地表達及報路。最後利用儲存及匯出\r\n" + 
					"               圖檔的功能，讓使用者可以提供給欲前往該目的地的（有緣）人。\n"
					+ "可加入圖示 : \n                  1.大樓         2.公園      3.加油站    4.平交道    5.紅綠燈      6.停車場\n"
					+ "                  7.救護站     8.銀行       9.廟          10.學校      11.醫院       12.警察局\n ", "關於 報路狂", 1, new ImageIcon(getClass().getResource("報路狂.jpg")));
		}
		for(int i=0;i<2;i++){
			if(variable.jCheckBoxMenuItem[i].isSelected())
				variable.jPanel[i+1].setVisible( true );
           	else
           		variable.jPanel[i+1].setVisible( false );
       	}
       	if(variable.jCheckBoxMenuItem[3].isSelected()){
       		setPanel.setVisible( true );
       		variable.jPanel[3].setVisible( true );
       	}
       	else{
       		setPanel.setVisible( false );
       		variable.jPanel[3].setVisible( false );
       	}
		if(variable.jCheckBoxMenuItem[2].isSelected())
			variable.jLabel.setVisible( true );
       	else
       		variable.jLabel.setVisible( false );
	}
	

	public static void main( String args[] ){
		try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}
		catch(Exception e){e.printStackTrace();}
		Variable variable = new Variable();

		Painter app = new Painter(variable);
		app.setVisible(true);
		app.setExtendedState(Frame.MAXIMIZED_BOTH);
	}
}
