import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Tomasulo {
	static List<String> instList = new ArrayList<>();	//读入的指令集序列
	public static void readFile() {	//读取测试文件指令集
        String pathname = "test/test2.nel"; 
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader)
        ) {
        	//逐行读取指令
            String line;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
            	instList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    public static void main(String []args){
    	readFile();
    	//输出待执行的指令集序列
    	for(int i = 0; i < instList.size(); i++) {
    		System.out.println(i+" "+instList.get(i));
    	}
    	//输出语法定义CFG
    	for(int i = 0; i < instList.size(); i++) {
	    	String[] testOpr = instList.get(i).split(",");
	    	//System.out.println(testOpr[0]);
	    	Instruction inst = new Instruction(testOpr[0]);
	    	inst.PrintInst();
    	}
    }
}
