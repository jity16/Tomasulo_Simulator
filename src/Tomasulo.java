import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.awt.*;
import javax.swing.JFrame;

public class Tomasulo {
	static List<String> StrInstList = new ArrayList<>();	//读入的指令集序列
	static Simulator tomasuloSimulator = new Simulator();			//TomasuloSimulator
	
	//UI
	JFrame jframe = new JFrame();

	
	
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
    	
    	JFrame frame = new JFrame("我的第一个Frame");
		frame.setSize(4000 , 1000);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setVisible(true);
    	
    	
    	
    	
    	
    	
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


