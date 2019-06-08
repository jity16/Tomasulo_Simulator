import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import javax.swing.UIManager;
import javax.swing.JOptionPane;


public class Tomasulo {
	static List<String> StrInstList = new ArrayList<>();	//读入的指令集序列
	static Simulator tomasuloSimulator;			//TomasuloSimulator
	static String pathname = "test/test0.nel"; 
	
	//UI
	JFrame frame;
	ClockUI controlPanel;
	LoadBufferUI LoadBufferPanel;
	RegisterUI   RegistersPanel;
	ReservationUI ReserveStationPanel;
	CalculatorUI CalculatorPanel;
	

	public static void setUIFont() //修改全局字体
	{
		Font f = new Font("Comic Sans MS",Font.PLAIN,18);
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
    	
    	//各部件顶点位置
    	Point conPoint = new Point(100,50); //Control
    	Point LBpoint = new Point(100,200); //LoadBuffer
    	Point RsPoint = new Point(1400,100); //RS
    	Point RegPoint = new Point(100,600); //Registers
    	Point CalPoint = new Point(550,200); //Calculator
    	
    	
    	
    	//各部件大小 per height = 50
    	Dimension windowSize = new Dimension(2000 , 1000);//frame
    	Dimension conSize = new Dimension(400,100);  //control
    	Dimension LBsize = new Dimension(400,200); //LoadBuffer
    	Dimension RegSize = new Dimension(1800,300);//Registers
    	Dimension RsSize = new Dimension(500,500); //RS
    	Dimension CalSize = new Dimension(800,400);//Calculator
    	
    	//各部件背景颜色
    	Color frameColor = new java.awt.Color(240,248,255);
    	Color conColor = new java.awt.Color(230,230,250);
    	Color LBcolor =  new java.awt.Color(230,230,250);
    	Color RegColor =  new java.awt.Color(230,230,250);
    	Color RsColor =  new java.awt.Color(230,230,250);
    	Color CalColor =  new java.awt.Color(230,230,250);
    	
    	//边框颜色和线性
    	Color BoderColor = new java.awt.Color(70,130,180);
    	Border borderline = BorderFactory.createMatteBorder(2, 2, 5, 5, BoderColor);
    	
    	//各部件边框
    	Border conBorder = BorderFactory.createTitledBorder(borderline, "Control");
    	Border LBborder = BorderFactory.createTitledBorder(borderline, "LoadBuffer");
    	Border RegBorder = BorderFactory.createTitledBorder(borderline, "Register Status");
    	Border RsBorder = BorderFactory.createTitledBorder(borderline, "Reservation Station");
    	Border CalBorder = BorderFactory.createTitledBorder(borderline, "Calculators");
    	
    	//总界面
    	tomasuloUI.frame = new JFrame("Tomasulo Simulator");
    	tomasuloUI.frame.setSize(windowSize);
    	tomasuloUI.frame.getContentPane().setBackground(frameColor);
    	tomasuloUI.frame.setVisible(true);
    	
    	//Control
    	tomasuloUI.controlPanel = new ClockUI();
    	tomasuloUI.controlPanel.setSize(conSize);
    	tomasuloUI.controlPanel.setLocation(conPoint);
    	tomasuloUI.controlPanel.setBackground(conColor);
    	tomasuloUI.controlPanel.setBorder(conBorder);       
    	tomasuloUI.frame.add(tomasuloUI.controlPanel);
		tomasuloUI.frame.setLayout(null);
    	
    	//LoadBuffer
    	tomasuloUI.LoadBufferPanel = new LoadBufferUI();
    	tomasuloUI.LoadBufferPanel.setSize(LBsize);
    	tomasuloUI.LoadBufferPanel.setLocation(LBpoint);
    	tomasuloUI.LoadBufferPanel.setBackground(LBcolor);
    	tomasuloUI.LoadBufferPanel.setBorder(LBborder);       
    	tomasuloUI.frame.add(tomasuloUI.LoadBufferPanel);
		tomasuloUI.frame.setLayout(null);
		
		//Registers
		tomasuloUI.RegistersPanel = new RegisterUI();
		tomasuloUI.RegistersPanel.setSize(RegSize);
		tomasuloUI.RegistersPanel.setLocation(RegPoint);
		tomasuloUI.RegistersPanel.setBackground(RegColor);
		tomasuloUI.RegistersPanel.setBorder(RegBorder); 
		tomasuloUI.frame.add(tomasuloUI.RegistersPanel);
		tomasuloUI.frame.setLayout(null);
		
    	//Reservation Station
		tomasuloUI.ReserveStationPanel = new ReservationUI();
		tomasuloUI.ReserveStationPanel.setSize(RsSize);
		tomasuloUI.ReserveStationPanel.setLocation(RsPoint);
		tomasuloUI.ReserveStationPanel.setBackground(RsColor);
		tomasuloUI.ReserveStationPanel.setBorder(RsBorder); 
		tomasuloUI.frame.add(tomasuloUI.ReserveStationPanel);
		tomasuloUI.frame.setLayout(null);
		
		//Calculator
		tomasuloUI.CalculatorPanel = new CalculatorUI();
		tomasuloUI.CalculatorPanel.setLocation(CalPoint);
		tomasuloUI.CalculatorPanel.setSize(CalSize);
		tomasuloUI.CalculatorPanel.setBackground(CalColor);
		tomasuloUI.CalculatorPanel.setBorder(CalBorder); 
		tomasuloUI.frame.add(tomasuloUI.CalculatorPanel);
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
	
    	tomasuloUI.tomasuloSimulator = new Simulator(tomasuloUI);
    	tomasuloUI.tomasuloSimulator.runSimulator(inst);
    	tomasuloUI.updateUI(tomasuloSimulator.finishAllInst);
    	tomasuloUI.controlPanel.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	tomasuloUI.tomasuloSimulator.runSimulator(inst);
            	tomasuloUI.controlPanel.clock.setText(Integer.toString(tomasuloUI.tomasuloSimulator.clock));
            }
        });

    	//为了防止编译运行不是新更新的版本,打上时间戳
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置时间格式
        System.out.println("current system time : " +df.format(new Date()));// new Date()为获取当前系统时间
    }
    
    public String instToString(OperationType opr) {
    	if(opr == null) return "";
        else return opr.toString();
    }
    public void updateLoadBuffer() {
    	//Update LoadBuffer
    	for(int i = 0 ; i < tomasuloSimulator.LoadRsNum;i++) {
    		String Busy = null;
    		if(tomasuloSimulator.loadBuffers[i].isBusy) {
    			Busy = "Yes";
    		}else {
    			Busy = "No";
    		}
            LoadBufferPanel.labels[3*(i+1)+1].setText(Busy);
            String loadAddr = null;
            if(tomasuloSimulator.loadBuffers[i].isBusy) {
            	loadAddr = Integer.toString(((LoadInstruction) tomasuloSimulator.loadBuffers[i].instruction).loadAddr);
                
            }else {
            	loadAddr = null;
            }
            LoadBufferPanel.labels[3 * (i + 1) + 2].setText(loadAddr);
    	}
    }
    
    public void updateRegisters() {
    	//Update Registers
    	for(int i = 0; i < tomasuloSimulator.RegisterNum/2; i ++) {
    		//set F1 - F16
    		String status1 = null;
    		String value1 = "0";
    		status1 = tomasuloSimulator.registers[i].stateFunc;
    		RegistersPanel.labels[1][i+1].setText(status1);
			value1 = Integer.toString(tomasuloSimulator.registers[i].value);
			RegistersPanel.labels[2][i+1].setText(value1);
    		
    		String status2 = null;
    		String value2 = "0";
    		status2 = tomasuloSimulator.registers[i+16].stateFunc;
    		RegistersPanel.labels[4][i+1].setText(status2);
			value2 = Integer.toString(tomasuloSimulator.registers[i+16].value);
			RegistersPanel.labels[5][i+1].setText(value2);
    	}
    }
    
    public void updateReservation() {
    	for (int i = 0; i < tomasuloSimulator.addRs.length; i++) {
            String text = tomasuloSimulator.addRs[i].isBusy == true ? "Yes" : "No";
            ReserveStationPanel.labels[7*(i+1)+1].setText(text);
            if(tomasuloSimulator.addRs[i].isBusy) {
            	ReserveStationPanel.labels[7 * (i + 1) + 2].setText(instToString(tomasuloSimulator.addRs[i].operation));
            	ReserveStationPanel.labels[7 * (i + 1) + 3].setText(Integer.toString(tomasuloSimulator.addRs[i].Vj));
            	ReserveStationPanel.labels[7 * (i + 1) + 4].setText(Integer.toString(tomasuloSimulator.addRs[i].Vk));
            	ReserveStationPanel.labels[7 * (i + 1) + 5].setText(tomasuloSimulator.addRs[i].Qj);
            	ReserveStationPanel.labels[7 * (i + 1) + 6].setText(tomasuloSimulator.addRs[i].Qk);
            }
            else{
            	ReserveStationPanel.labels[7 * (i + 1) + 2].setText(null);
            	ReserveStationPanel.labels[7 * (i + 1) + 3].setText(null);
            	ReserveStationPanel.labels[7 * (i + 1) + 4].setText(null);
            	ReserveStationPanel.labels[7 * (i + 1) + 5].setText(null);
            	ReserveStationPanel.labels[7 * (i + 1) + 6].setText(null);
            }
        }

        for (int i = 0; i < tomasuloSimulator.multRs.length; i++) {
            String text = tomasuloSimulator.multRs[i].isBusy == true ? "Yes" : "No";
            ReserveStationPanel.labels[7*(i+7)+1].setText(text);
            if(tomasuloSimulator.multRs[i].isBusy) {
            	ReserveStationPanel.labels[7 * (i + 7) + 2].setText(instToString(tomasuloSimulator.multRs[i].operation));
            	ReserveStationPanel.labels[7 * (i + 7) + 3].setText(Integer.toString(tomasuloSimulator.multRs[i].Vj));
            	ReserveStationPanel.labels[7 * (i + 7) + 4].setText(Integer.toString(tomasuloSimulator.multRs[i].Vk));
            	ReserveStationPanel.labels[7 * (i + 7) + 5].setText(tomasuloSimulator.multRs[i].Qj);
            	ReserveStationPanel.labels[7 * (i + 7) + 6].setText(tomasuloSimulator.multRs[i].Qk);
            }
            else {
            	ReserveStationPanel.labels[7 * (i + 7) + 2].setText(null);
            	ReserveStationPanel.labels[7 * (i + 7) + 3].setText(null);
            	ReserveStationPanel.labels[7 * (i + 7) + 4].setText(null);
            	ReserveStationPanel.labels[7 * (i + 7) + 5].setText(null);
            	ReserveStationPanel.labels[7 * (i + 7) + 6].setText(null);
            }
        }
    }
    
    public String getInst(String opr, int i ) {
    	String res = null;
    	switch(opr) {
    		case "LD":
    			res = opr + " F"+Integer.toString(((LoadInstruction)tomasuloSimulator.cloads[i].instruction).registerNo)+" "+Integer.toString(((LoadInstruction)tomasuloSimulator.cloads[i].instruction).loadAddr);
    			break;
    		case "ADD":
    		case "SUB":
    			res = opr + 
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cadds[i].instruction).registerD)+
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cadds[i].instruction).registerS1)+
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cadds[i].instruction).registerS2);
    			break;
    		case "MUL":
    		case "DIV":
    			res = opr + 
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cmults[i].instruction).registerD)+
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cmults[i].instruction).registerS1)+
					" F"+Integer.toString(((CalInstruction)tomasuloSimulator.cmults[i].instruction).registerS2);
				break;
    		case "JUMP":
    			res = opr + 
					" "+Integer.toString(((JumpInstruction)tomasuloSimulator.cadds[i].instruction).compare)+
					" F"+Integer.toString(((JumpInstruction)tomasuloSimulator.cadds[i].instruction).registerNo)+
					" "+Integer.toString(((JumpInstruction)tomasuloSimulator.cadds[i].instruction).jumpAddr);
    			break;
    		default:
    	}
    	return res;
    }
    public void updateCalculators() {
    	//更新加减法器部件
    	for(int i = 0; i < tomasuloSimulator.CAddNum; i ++) {
    		String insttext_a = null;
    		String lefttime_a = null;
    		if(tomasuloSimulator.cadds[i].isBusy) {
    			insttext_a = getInst(tomasuloSimulator.cadds[i].instruction.OprType.toString(),i);
    			lefttime_a = Integer.toString(tomasuloSimulator.cadds[i].remainRunTime);
    		}else {
    			insttext_a = null;
    			lefttime_a = null;
    		}
    		CalculatorPanel.labels[i+1][1].setText(insttext_a);
    		CalculatorPanel.labels[i+1][2].setText(lefttime_a);
    	}
    	//更新乘除法器部件
    	for(int i = 0; i < tomasuloSimulator.CMultNum; i ++) {
    		String insttext_m = null;
    		String lefttime_m = null;
    		if(tomasuloSimulator.cmults[i].isBusy) {
    			insttext_m = getInst(tomasuloSimulator.cmults[i].instruction.OprType.toString(),i);
    			lefttime_m = Integer.toString(tomasuloSimulator.cmults[i].remainRunTime);
    		}else {
    			insttext_m = null;
    			lefttime_m = null;
    		}
    		CalculatorPanel.labels[i+4][1].setText(insttext_m);
    		CalculatorPanel.labels[i+4][2].setText(lefttime_m);
    	}
    	//更新loader
    	for(int i = 0; i < tomasuloSimulator.CLoadNum; i ++) {
    		String insttext_l = null;
    		String lefttime_l = null;
    		if(tomasuloSimulator.cloads[i].isBusy) {
    			insttext_l = getInst(tomasuloSimulator.cloads[i].instruction.OprType.toString(),i);
    			lefttime_l = Integer.toString(tomasuloSimulator.cloads[i].remainRunTime);
    		}else {
    			insttext_l = null;
    			lefttime_l = null;
    		}
    		CalculatorPanel.labels[i+6][1].setText(insttext_l);
    		CalculatorPanel.labels[i+6][2].setText(lefttime_l);
    	}
    	
    }
    
    public void updateUI(boolean finished) {
    	updateLoadBuffer();
    	updateRegisters();
    	updateReservation();
    	updateCalculators();
//    	if(finished) {
//    		JOptionPane.showMessageDialog(null, "finished", "finished", JOptionPane.ERROR_MESSAGE);
//    	}
    }

}


