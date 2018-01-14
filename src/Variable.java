import java.awt.*;
import javax.swing.*;

public class Variable {
	public static int draw_panel_width = 1100,draw_panel_height=700;
	public	String menuBar[] = {"檔案(F)","編輯(E)","檢視(V)","說明(H)"};
	public	String menuItem[][] = {
		{"開啟新檔(N)|78","開啟舊檔(O)|79","儲存檔案(S)|83","另存新檔(A)|65","錄音(R)|82","Google Maps(G)|71", "截圖(P)|80","結束(E)|69"},
		{"上一步(U)|85","下一步(Y)|89","剪下(B)|66","複製(C)|67","貼上(P)|80"},
		{"工具箱(T)|84","調色盤(D)|68","狀態列(X)|88","屬性欄(M)|77"},
		{"關於小畫家(Z)|90"}
	}; 
	public	JMenuItem jMenuItem[][] = new JMenuItem[4][8];
	public	JMenu jMenu[];
	public	JCheckBoxMenuItem jCheckBoxMenuItem[] = new JCheckBoxMenuItem[4];
	public	String ButtonName[] = {"直線","矩型","橢圓","圓角矩型","貝氏曲線","扇型"
			,"多邊型","鉛筆","橡皮擦","文字","選取","大樓","公園","加油站","平交道","紅綠燈"
			,"停車場","救護站","銀行","廟","學校","醫院","警察局",};
	public	JToggleButton jToggleButton[];
	public	ButtonGroup buttonGroup;
    public	JPanel jPanel[] = new JPanel[4];//0整個視窗的Panel,1工具箱,2色塊,3屬性欄
    public	JLabel jLabel = new JLabel();//狀態列
	
    public	String toolname[]={"img/tool1.gif","img/tool2.gif","img/tool3.gif","img/tool4.gif"
    		,"img/tool5.gif","img/tool8.gif","img/tool9.gif","img/tool7.gif","img/tool6.gif"
    		,"img/tool10.gif","img/tool11.gif","img/大樓.gif","img/公園.gif","img/加油站.gif"
    		,"img/平交道.gif","img/紅綠燈.gif","img/停車場.gif","img/救護站.gif","img/銀行.gif"
    		,"img/廟.gif","img/學校.gif","img/醫院.gif","img/警察局.gif"};
    public	Icon toolIcon[]=new ImageIcon[23];//放工具箱中圖案的陣列
    public	int show_x,show_y,drawMethod=7;
    public	Paint color_border,color_inside;
	public	Stroke stroke;
	public	Shape shape;
	public	String isFilled;
}
