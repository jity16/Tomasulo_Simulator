
public class Simulator {
	//参数设置
	final int CAddNum = 3, CMultNum = 2, CLoadNum = 2;
	final int AddRsNum = 6, MultRsNum = 3, LoadRsNum = 3;
	final int RegisterNum = 32;
	//运算器部件
	Calculator[] cadds;
	Calculator[] cmults;
	Calculator[] cloads;
	//保留站
	ReserveStation[] addRs;
	ReserveStation[] multRs;
	LoadBuffer[] loadBuffers;
	//寄存器状态表
	RegisterStatus[] registers;
	
	
	Instruction[] inst;	//指令集序列
	boolean hasJump;			
	//int curInstIndex;	//当前指令的序号
    int nextInstIndex;	//下一条待发射指令的序号
    int clock;			//时钟
    
	Simulator(){
		//实例化运算器部件
		this.cadds = new Calculator[CAddNum];
		this.cmults = new Calculator[CMultNum];
		this.cloads = new Calculator[CLoadNum];
		//实例化保留站
		this.addRs = new ReserveStation[AddRsNum];
		this.multRs = new ReserveStation[MultRsNum];
		this.loadBuffers = new LoadBuffer[LoadRsNum];
		//实例化寄存器状态表
		this.registers = new RegisterStatus[RegisterNum];
		//初始化其余成员变量
		inst = null;
		hasJump = false;
//		curInstIndex = 0;
		nextInstIndex = 0;
		clock = 0;
	}
	
	public void runSimulator(Instruction[] inst) {
		this.inst = inst;
		//System.out.println(inst[0].OprType);
		while(true){
			//计时器
			clock ++;
			System.out.println("-----------"+"Clock: "+clock+"-----------");
			issue();
			exec();
			write();
			if(isFinished()) break;
		}
	}
	
	public void issue() {
//		curInstIndex ++;
//		nextInstIndex ++;
//		Instruction curInstruction = inst[nextInstIndex];
        Instruction nextInstruction = inst[nextInstIndex];
        System.out.println("Issue new instruction: Index = "+nextInstIndex+" Type = "+nextInstruction.OprType);
        switch (nextInstruction.OprType){
            case LD:
            	if(IssueLoadBuffer(nextInstruction)){//success
                    nextInstIndex++;
                }
//            	IssueLoadBuffer(nextInstruction);
                break;
            case ADD:
            case SUB:
            	nextInstIndex++;
                break;
            case MUL:
            case DIV:
                nextInstIndex++;
                break;
            case JUMP:
                nextInstIndex++;
                break;
        }
	}
	
	public boolean IssueLoadBuffer(Instruction instruction){
        return true;
    }
	
	public void exec() {
		
	}
	public void write() {
		
	}
	public boolean isFinished() {
		if(nextInstIndex > inst.length - 1)
			return true;
		return false;
	}

}

/*运算器类 Calculator
 * 3个加减法运算器 Add1,Add2,Add3
 * 2个乘除法运算器 Mult1,Mult2
 * 2个Load 部件 Load1,Load2
 */
class Calculator{
    Instruction instruction; //指令信息
    int remainRunTime;		 //剩余运行时间
    boolean isBusy;			 //当前运算器是否被占用
    int result;				 //指令运行结果
    Calculator(){
    	remainRunTime = 41;
        isBusy = false;
        result = 0;
    }
}

/* 算数指令保留站类 ReserveStation
 * 6个加减法保留站
 * 3个乘除法保留站
 */
class ReserveStation{
	Instruction instruction;	//指令信息
	
	boolean isBusy;				//当前保留站是否被占用
	OperationType operation;	//当前操作指令类型
    String Qj, Qk;				//保留站字段
    int Vj, Vk;					//保留站字段
    
	boolean isReady;	//当前操作数是否均就绪
	int issueTime;		//指令流出时间
    int readyTime;		//操作数就绪时间
    
    
    
    ReserveStation(){
        this.issueTime = -1;
        this.readyTime = 0;
        this.isBusy = false;
        this.isReady = false;
        this.Qj =this.Qk = null;
        this.Vj = this.Vk = 0;
    }
}

/* Load保留站类：LoadBuffer
 * 3个LoadBuffer
 */
class LoadBuffer{
	Instruction instruction;	//指令信息
	
	boolean isBusy;				//当前保留站是否被占用
	int issueTime;				//指令发射时间
//	int address;				//写入的寄存器地址
	
	LoadBuffer(){
		this.isBusy = false;
		this.issueTime = -1;
	}
}


/*寄存器状态类：RegisterStatus 
 * 32个寄存器（F0-F31）
 */
class RegisterStatus{
    String stateFunc;	//寄存器状态	
    int value;			//寄存器数值
    
    boolean isWaiting;
    
    RegisterStatus(){
        stateFunc = null;
        value = 0;
        isWaiting = false;
    }
}



