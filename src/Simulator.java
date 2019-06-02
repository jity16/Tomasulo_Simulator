
public class Simulator {
	//参数设置
	final int CAddNum = 3;
	final int CMultNum = 2;
	final int CLoadNum = 2;
	//运算器部件
	Calculator[] cadds;
	Calculator[] cmults;
	Calculator[] cloads;
	Simulator(){
		//初始化运算器部件
		this.cadds = new Calculator[CAddNum];
		this.cmults = new Calculator[CMultNum];
		this.cloads = new Calculator[CLoadNum];
	}
	
	
}

/*运算器类 Calculator
 * 3个加减法运算器 Add1,Add2,Add3
 * 2个乘除法运算器 Mult1,Mult2
 * 2个Load 部件 Load1,Load2
 */
class Calculator{
    Instruction instruction;
    int remainRunTime;	//剩余运行时间
    boolean isBusy;		//当前运算器是否被占用
    int result;			//指令运行结果
    Calculator(){
    	remainRunTime = 41;
        isBusy = false;
        result = 0;
    }
}
