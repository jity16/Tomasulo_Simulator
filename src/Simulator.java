import static java.lang.System.in;
import static java.lang.System.load;

public class Simulator {
	//参数设置
	final int CAddNum = 3, CMultNum = 2, CLoadNum = 2;
	final int AddRsNum = 6, MultRsNum = 3, LoadRsNum = 3;
	final int RegisterNum = 32;
	final int LDTime = 3, ADDTime = 3, MULTime = 12, DIVTime = 40, JUMPTime = 1;
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
		for (int i = 0; i < CAddNum; i++) {
        	cadds[i] = new Calculator();
        }
        
        for (int i = 0; i < CMultNum; i++) {
        	cmults[i] = new Calculator();
        }
        
        for (int i = 0; i < CLoadNum; i++) {
            cloads[i] = new Calculator();
        }
		//实例化保留站
		this.addRs = new ReserveStation[AddRsNum];
		this.multRs = new ReserveStation[MultRsNum];
		this.loadBuffers = new LoadBuffer[LoadRsNum];
        for (int i = 0; i < AddRsNum; i++) {
            addRs[i] = new ReserveStation();
        }
        for (int i = 0; i < MultRsNum; i++) {
            multRs[i] = new ReserveStation();
        }
        for (int i = 0; i < LoadRsNum; i++) {
            loadBuffers[i] = new LoadBuffer();
        }
		//实例化寄存器状态表
		this.registers = new RegisterStatus[RegisterNum];
		for (int i = 0; i < RegisterNum; i++) {
            registers[i] = new RegisterStatus();
        }
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
            	IssueLoad(nextInstruction);
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
	
	/* Issue Load
     * 1.遍历保留站,寻找空闲保留站,流出指令
     * 2.更新保留站信息
     * 3.更新指令信息(issue)
     * 4.更新状态寄存器信息
     * 5.更新nextInstIndex
     */
	public void IssueLoad(Instruction instruction){
        int loadBufferIndex = -1;
        for(int i = 0; i < LoadRsNum;i ++) {
        	if(!loadBuffers[i].isBusy) {	//有空闲LoadBuffer
        		loadBufferIndex = i;		//占用该空闲保留站
        		break;
        	}
        }
        if(loadBufferIndex == -1) return;	//无空闲LoadBuffer
        //更新LoadBuffer
        loadBuffers[loadBufferIndex].instruction = instruction;
        loadBuffers[loadBufferIndex].isBusy = true;
        loadBuffers[loadBufferIndex].issueTime = clock; 
        //更新指令信息(第一次发射的时间)
        if(instruction.issue == -1) { //指令从未发射过
        	instruction.issue = clock;
        }
        LoadInstruction loadInst = (LoadInstruction)instruction;
        System.out.println("Issue Load: "+ loadInst.OprType +" F"+loadInst.registerNo+" "+loadInst.loadAddr);
        //更新状态寄存器
        registers[loadInst.registerNo].stateFunc = "loadBuffer"+Integer.toString(loadBufferIndex);
        registers[loadInst.registerNo].isWaiting = true;
        //更新nextInstIndex
        nextInstIndex ++;
        return;
    }
	
	public void exec() {
		/*Exec Load 
		 * 1. 更新已被占用的运算器资源信息
		 * 2.如果有剩余运算器资源
		 *  2.1取最先就绪的指令
		 */
		int cLoadAvailable = CLoadNum;
		for(int i = 0; i < CLoadNum; i ++) {
			if(cloads[i].isBusy) {	//被占用的运算器
				cloads[i].remainRunTime --;
				if(cloads[i].remainRunTime == 1 && cloads[i].instruction.exec == -1)//在本周期第一次被执行完毕
                    cloads[i].instruction.exec = clock;
                cLoadAvailable--;
			}
		}
		while(cLoadAvailable != 0) { //有剩余运算器资源
            boolean LoadReady = false;
            int execLoadIndex = -1;
            int earlytime = Integer.MAX_VALUE;
            for (int i = 0; i < LoadRsNum; i++) { 	//取最先就绪的指令
                if (loadBuffers[i].isBusy && !loadBuffers[i].isExec && loadBuffers[i].issueTime < earlytime && loadBuffers[i].issueTime != clock){
                	LoadReady = true;				//存在就绪的Load
                	execLoadIndex = i;
                    earlytime = loadBuffers[i].issueTime;
                }
            }
            if(LoadReady){					//指令序列中有就绪的Load
            	cLoadAvailable--;			//占用运算器资源
                for (int i = 0; i < CLoadNum; i++) {
                    if(!cloads[i].isBusy){	//占用空闲运算器i
                    	//更新运算器i的信息
                        cloads[i].remainRunTime = LDTime;
                        cloads[i].isBusy = true;
                        cloads[i].instruction = loadBuffers[execLoadIndex].instruction;
                        cloads[i].result = ((LoadInstruction)cloads[i].instruction).loadAddr;
                        System.out.println("Start Exec Load: loadBuffer "+execLoadIndex+" cLoad "+i+" loadAddr = "+cloads[i].result);
                        //更新LoadBuffer信息
                        loadBuffers[execLoadIndex].isExec = true;
                        //关联cload 和LoadBuffer
                        cloads[i].cloadBuffer = loadBuffers[execLoadIndex];
                        break;
                    }
                }
            }
            else
                break;
        }
		
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
    LoadBuffer cloadBuffer;	 //若为Load指令所占用的LoadBuffer	
    	
    Calculator(){
    	remainRunTime = 41;
        isBusy = false;
        result = 0;
        cloadBuffer = null;
    }
}

/* 算数指令保留站类 ReserveStation
 * 6个加减法保留站
 * 3个乘除法保留站
 */
class ReserveStation{
	Instruction instruction;	//指令信息
	
	boolean isBusy;				//当前保留站是否被占用
	boolean isExec;				//当前保留站的指令是否被运行
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
        this.isExec = false;
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
	boolean isExec;				//当前保留站的指令是否被运行
	int issueTime;				//指令发射时间
//	int address;				//写入的寄存器地址
	
	LoadBuffer(){
		this.isBusy = false;
		this.isExec = false;
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



