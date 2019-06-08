import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.UIManager;


public class Tomasulo {
	static List<String> StrInstList = new ArrayList<>();	//读入的指令集序列
	static Simulator tomasuloSimulator = new Simulator();			//TomasuloSimulator
	
	//UI
	JFrame frame;
	JPanel LoadBuffer;

	public static void setUIFont()
	{
		Font f = new Font("Comic Sans MS",Font.PLAIN,25);
		String   names[]={ "Label", "CheckBox", "PopupMenu","MenuItem", "CheckBoxMenuItem",
				"JRadioButtonMenuItem","ComboBox", "Button", "Tree", "ScrollPane",
				"TabbedPane", "EditorPane", "TitledBorder", "Menu", "TextArea",
				"OptionPane", "MenuBar", "ToolBar", "ToggleButton", "ToolTip",
				"ProgressBar", "TableHeader", "Panel", "List", "ColorChooser",
				"PasswordField","TextField", "Table", "Label", "Viewport",
				"RadioButtonMenuItem","RadioButton", "DesktopPane", "InternalFrame"
		}; 
		for (String item : names) {
			 UIManager.put(item+ ".font",f); 
		}
	}
	
	public static void readFile() {	//读取测试文件指令集
        String pathname = "test/test2.nel"; 
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
        	//逐行读取指令
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
            	StrInstList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static void main(String []args){
    	setUIFont();
    	Tomasulo tomasuloUI = new Tomasulo();
    	
    	//总界面
    	tomasuloUI.frame = new JFrame("Tomasulo Simulator");
    	tomasuloUI.frame.setSize(1500 , 1000);
    	tomasuloUI.frame.getContentPane().setBackground(new java.awt.Color(240,248,255));
    	tomasuloUI.frame.setVisible(true);
    	
    	//各部件顶点位置
    	Point LBpoint = new Point(100,100); //LoadBuffer
    	
    	//各部件背景颜色
    	Color LBcolor =  new java.awt.Color(230,230,250);
    	
    	//边框颜色
    	Color LBBoderColor = new java.awt.Color(70,130,180);
    	
    	//各部件边框
    	Border LBb_line = BorderFactory.createMatteBorder(2, 2, 5, 5, LBBoderColor);
    	Border LBborder = BorderFactory.createTitledBorder(LBb_line, "LoadBuffer");  
    	
    		
    	//LoadBuffer
    	tomasuloUI.LoadBuffer = new LoadBufferUI();
    	tomasuloUI.LoadBuffer.setSize(500,300);
    	tomasuloUI.LoadBuffer.setLocation(LBpoint);
    	tomasuloUI.LoadBuffer.setBackground(LBcolor);
    	tomasuloUI.LoadBuffer.setBorder(LBborder);       
    	tomasuloUI.frame.add(tomasuloUI.LoadBuffer);
		tomasuloUI.frame.setLayout(null);

		
		
    	
    	readFile();
    	//输出待执行的指令集序列
    	System.out.println("-----------"+"Instructions"+"-----------");
    	for(int i = 0; i < StrInstList.size(); i++) {
    		System.out.println(i+" "+StrInstList.get(i));
    	}
    	//输出语法定义CFG
    	Instruction[] inst = new Instruction[StrInstList.size()];
    	for(int i = 0; i < StrInstList.size();i ++) {
    		String[] instArray = StrInstList.get(i).split(",");
    		//System.out.println("instArray"+" "+instArray[0]);
    		switch(instArray[0]) {
	    		case "LD":
	    			inst[i] = new LoadInstruction(instArray);
	    			break;
	    		case "ADD":
	    		case "SUB":
	    		case "MUL":
	    		case "DIV":
	    			inst[i] = new CalInstruction(instArray);
	    			break;
	    		case "JUMP":
	    			inst[i] = new JumpInstruction(instArray);
	    			break;
    			default:
    				System.out.println("Input Error:Unknown Operation Type!");
    		}
    	}
	
    	tomasuloSimulator = new Simulator();
    	tomasuloSimulator.runSimulator(inst);
    	
    	//为了防止编译运行不是新更新的版本,打上时间戳
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        System.out.println("current system time : " +df.format(new Date()));// new Date()为获取当前系统时间
    }
}


