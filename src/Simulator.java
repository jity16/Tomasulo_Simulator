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
	
	public boolean isFinished() {
//		if(nextInstIndex > inst.length - 1)
//			return true;
//		return false;
		for(int i = 0; i < AddRsNum; i ++) {
			if(addRs[i].isBusy) {
				return false;
			}
		}
		for(int i = 0; i < LoadRsNum; i ++) {
			if(loadBuffers[i].isBusy) {
				return false;
			}
		}
		return true;
	}
	
	public void runSimulator(Instruction[] inst) {
		this.inst = inst;
		//System.out.println(inst[0].OprType);
		while(true){
			if(nextInstIndex >= inst.length && isFinished()) break;
			//计时器
			clock ++;
			System.out.println("-----------"+"Clock: "+clock+"-----------");
			write();
			exec();
			issue();	
			printRs();
		}
		printResult();
	}
	

	
	public void issue() {
//		curInstIndex ++;
//		nextInstIndex ++;
//		Instruction curInstruction = inst[nextInstIndex];
		if(nextInstIndex > inst.length -1) return;
        Instruction nextInstruction = inst[nextInstIndex];
//        System.out.println("Issue new instruction: Index = "+nextInstIndex+" Type = "+nextInstruction.OprType);
        switch (nextInstruction.OprType){
            case LD:
            	IssueLoad(nextInstruction);
                break;
            case ADD:
            case SUB:
            	IssueAdd(nextInstruction);
                break;
            case MUL:
            case DIV:
                IssueMUL(nextInstruction);
                break;
            case JUMP:
                IssueJUMP(nextInstruction);
            	//nextInstIndex ++;
                break;
            default:
            	System.out.println("Error in Issue: Unknown Instruction Type!");
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
	
	/* Issue ADD,SUB
     * 1.遍历保留站,寻找空闲保留站,流出指令
     * 2.更新保留站信息
     * 3.更新指令信息(issue)
     * 4.更新状态寄存器信息
     * 5.更新nextInstIndex
     */
	public void IssueAdd(Instruction instruction) {
		int addRsIndex = -1;
		//寻找空闲保留站
		for(int i = 0; i < AddRsNum; i ++) {
			if(!addRs[i].isBusy) {
				addRsIndex = i;
				break;
			}
		}
		if(addRsIndex == -1) return; //未找到空闲保留站
		
		//更新AddReservation
		addRs[addRsIndex].instruction = instruction;
		addRs[addRsIndex].isBusy = true;
		addRs[addRsIndex].issueTime = clock;
		addRs[addRsIndex].operation = instruction.OprType;
		CalInstruction inst = (CalInstruction) instruction;
		System.out.println("Issue CAL: "+ inst.OprType +" F"+inst.registerD+" "+ " F"+inst.registerS1 + " F"+inst.registerS2);
		
		if(registers[inst.registerS1].isWaiting) { //第一操作数未就绪
			addRs[addRsIndex].Qj = registers[inst.registerS1].stateFunc;
			addRs[addRsIndex].S1Ready = false;
		}
		else {	//第一操作数就绪
			addRs[addRsIndex].Qj = null;
			addRs[addRsIndex].Vj = registers[inst.registerS1].value;
			addRs[addRsIndex].S1Ready = true;
		}
		if(registers[inst.registerS2].isWaiting) { //第二操作数未就绪
			addRs[addRsIndex].Qk = registers[inst.registerS2].stateFunc;
			addRs[addRsIndex].S2Ready = false;
		}
		else { //第二操作数就绪
			addRs[addRsIndex].Qk = null;
			addRs[addRsIndex].Vk = registers[inst.registerS2].value;
			addRs[addRsIndex].S2Ready = true;
		}
		if(addRs[addRsIndex].S1Ready && addRs[addRsIndex].S2Ready) {//两个操作数均就绪
			addRs[addRsIndex].isReady = true;
			addRs[addRsIndex].readyTime = clock;
			System.out.println("Ready Inst " + inst.OprType +" " + inst.registerD +" "+inst.registerS1+" "+inst.registerS2);;
		}
		
		//更新指令信息(第一次发射的时间)
		if(instruction.issue == -1) {
			instruction.issue = clock;
		}
		//更新状态寄存器
		registers[inst.registerD].stateFunc = "addRS" + Integer.toString(addRsIndex);
		registers[inst.registerD].isWaiting = true;
		//更新nextInstIndex
		nextInstIndex ++;
		return;
	}
	
	/* Issue MUL,DIV
     * 1.遍历保留站,寻找空闲保留站,流出指令
     * 2.更新保留站信息
     * 3.更新指令信息(issue)
     * 4.更新状态寄存器信息
     * 5.更新nextInstIndex
     */
	public void IssueMUL(Instruction instruction) {
		int multRsIndex = -1;
		//寻找空闲保留站
		for(int i = 0; i < MultRsNum; i ++) {
			if(!multRs[i].isBusy) {
				multRsIndex = i;
				break;
			}
		}
		if(multRsIndex == -1) return; //未找到空闲保留站
		
		//更新multReservation
		multRs[multRsIndex].instruction = instruction;
		multRs[multRsIndex].isBusy = true;
		multRs[multRsIndex].issueTime = clock;
		multRs[multRsIndex].operation = instruction.OprType;
		CalInstruction inst = (CalInstruction) instruction;
		System.out.println("Issue CAL: "+ inst.OprType +" F"+inst.registerD+" "+ " F"+inst.registerS1 + " F"+inst.registerS2);
		
		if(registers[inst.registerS1].isWaiting) { //第一操作数未就绪
			multRs[multRsIndex].Qj = registers[inst.registerS1].stateFunc;
			multRs[multRsIndex].S1Ready = false;
		}
		else {	//第一操作数就绪
			multRs[multRsIndex].Qj = null;
			multRs[multRsIndex].Vj = registers[inst.registerS1].value;
			multRs[multRsIndex].S1Ready = true;
		}
		if(registers[inst.registerS2].isWaiting) { //第二操作数未就绪
			multRs[multRsIndex].Qk = registers[inst.registerS2].stateFunc;
			multRs[multRsIndex].S2Ready = false;
		}
		else { //第二操作数就绪
			multRs[multRsIndex].Qk = null;
			multRs[multRsIndex].Vk = registers[inst.registerS2].value;
			multRs[multRsIndex].S2Ready = true;
		}
		if(multRs[multRsIndex].S1Ready && multRs[multRsIndex].S2Ready) {//两个操作数均就绪
			multRs[multRsIndex].isReady = true;
			multRs[multRsIndex].readyTime = clock;
			System.out.println("Ready Inst " + inst.OprType +" " + inst.registerD +" "+inst.registerS1+" "+inst.registerS2);;
		}
		
		//更新指令信息(第一次发射的时间)
		if(instruction.issue == -1) {
			instruction.issue = clock;
		}
		//更新状态寄存器
		registers[inst.registerD].stateFunc = "multRS" + Integer.toString(multRsIndex);
		registers[inst.registerD].isWaiting = true;
		//更新nextInstIndex
		nextInstIndex ++;
		return;
	}
	
	/* Issue JUMP
     * 1.遍历保留站,寻找空闲保留站,流出指令
     * 2.更新保留站信息
     * 3.更新指令信息(issue)
     * 4.更新状态寄存器信息
     * 5.更新nextInstIndex
	 */
	public void IssueJUMP(Instruction instruction) {
		int jumpRsIndex = -1; //用加减法保留站
		for(int i = 0 ; i < AddRsNum; i ++) {
			if(!addRs[i].isBusy) {
				jumpRsIndex = i;
				break;
			}
		}
		if(jumpRsIndex == -1) return;	//无空闲保留站
		
		//更新AddReservation
		addRs[jumpRsIndex].instruction = instruction;
		addRs[jumpRsIndex].isBusy = true;
		addRs[jumpRsIndex].issueTime = clock;
		addRs[jumpRsIndex].operation = instruction.OprType;
		
		JumpInstruction jumpInst = (JumpInstruction)instruction;
		System.out.println("Issue JUMP: "+ jumpInst.OprType +" "+jumpInst.compare+" F"+jumpInst.registerNo + " "+jumpInst.jumpAddr);
		//JUMP,INTEGER,REGISTER,INTERGER
		addRs[jumpRsIndex].Qj = null;
		addRs[jumpRsIndex].Vj = jumpInst.compare;
		if(registers[jumpInst.registerNo].isWaiting) {	//REGISTER等待写回
			addRs[jumpRsIndex].Qk = registers[jumpInst.registerNo].stateFunc;
			addRs[jumpRsIndex].isReady = false;
		}
		else {	//操作数准备就绪
			addRs[jumpRsIndex].Qk = null;
			addRs[jumpRsIndex].Vk = registers[jumpInst.registerNo].value;
			addRs[jumpRsIndex].isReady = true;
			addRs[jumpRsIndex].readyTime = clock;
		}
		
		//更新指令信息
		if(instruction.issue == -1) {
			instruction.issue = clock;
		}
		//更新nextInstIndex
		nextInstIndex ++;
		return;
	}
	
	
	
	
	
	/*寄存器改名
	 *  + 更新保留站中等待寄存器
	 */
	public void ExecUpdateRegisters(String oldST, String newST) {
		for(int i = 0; i < RegisterNum; i ++) {
			if(registers[i].isWaiting && registers[i].stateFunc.equals(oldST)) {
				registers[i].stateFunc = newST;
			}
		}
	}
	public void ExecUpdateRs(String oldST, String newST) {
		for(int i = 0; i < AddRsNum; i ++) {
			if(oldST.equals(addRs[i].Qj)) {
				addRs[i].Qj = newST;
			}
			if(oldST.equals(addRs[i].Qk)) {
				addRs[i].Qk = newST;
			}
		}
		for(int i = 0; i < MultRsNum; i ++) {
			if(oldST.equals(multRs[i].Qj)) {
				multRs[i].Qj = newST;
			}
			if(oldST.equals(multRs[i].Qk)) {
				multRs[i].Qk = newST;
			}
		}
	}
	
	public void exec() {
		/*Exec ADD,SUB*/
		int cAddAvailable = CAddNum;
		for(int i = 0; i < CAddNum; i ++) {
			if(cadds[i].isBusy) {	//正在运行的加减法运算器
				cadds[i].remainRunTime --;
				if(cadds[i].remainRunTime == 1 && cadds[i].instruction.exec == -1) { //指令在当前周期第一次执行完成
					cadds[i].instruction.exec = clock;
				}
				cAddAvailable --;
			}
		}
		while(cAddAvailable != 0) {
			boolean AddReady = false;
            int execADDIndex = -1;
            int earlytime = Integer.MAX_VALUE;
            for (int i = 0; i < AddRsNum; i++) { 	//取最先就绪的指令
            	//System.out.println("addexec "+ i + " "+ addRs[i].isBusy + " "+ addRs[i].isReady + " "+addRs[i].isExec);
                if (addRs[i].isBusy && addRs[i].isReady && !addRs[i].isExec && addRs[i].issueTime < earlytime && addRs[i].issueTime != clock){
                	AddReady = true;				//存在就绪的ADD,MUL
                	execADDIndex = i;
                    earlytime = addRs[i].issueTime;
                }
            }
            //System.out.println("AddReady ="+AddReady);
            if(AddReady){					//指令序列中有就绪的ADD,MUL
            	cAddAvailable--;			//占用运算器资源
                for (int i = 0; i < CAddNum; i++) {
                    if(!cadds[i].isBusy){	//占用空闲运算器i
                    	//更新运算器i的信息
                    	cadds[i].remainRunTime = ADDTime;	//ADD,SUB运行时间3
                    	cadds[i].isBusy = true;
                    	cadds[i].instruction = addRs[execADDIndex].instruction;
                    	cadds[i].calRs = addRs[execADDIndex];
                    	cadds[i].result = addRs[execADDIndex].Vj + addRs[execADDIndex].Vk;
                        System.out.println("Start Exec ADD/SUB: addRs "+execADDIndex+" cadds "+i+" result = "+cadds[i].result);
                        //更新addRs信息
                        addRs[execADDIndex].isExec = true;
                        //关联cadds 和 addRs
                        cadds[i].calRs = addRs[execADDIndex];
                        break;
                    }
                }
            }
            else
                break;
		}
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
                        //关联cloads 和LoadBuffer
                        cloads[i].cloadBuffer = loadBuffers[execLoadIndex];
                        //更新stateFunc
                        String oldST = "loadBuffer" + Integer.toString(execLoadIndex);
                        String newST = "loader" + Integer.toString(i);
                        ExecUpdateRegisters(oldST,newST);
                        ExecUpdateRs(oldST,newST);
                        break;
                    }
                }
            }
            else
                break;
        }
		
	}
	
	/*Write Load
	 *  (1)寻找当前周期运行结束的指令
	 *  (2)更新运算资源的信息
	 *  (3)更新寄存器状态表信息
	 *  (4)更新保留站信息
	 */
	public void writeLoad() {
		for(int i = 0; i < CLoadNum; i ++) {
			if(cloads[i].isBusy && cloads[i].remainRunTime == 1) {
				//更新运算资源的信息
				cloads[i].isBusy = false;
				cloads[i].cloadBuffer.isBusy = false;
				cloads[i].cloadBuffer.isExec = false;
				cloads[i].cloadBuffer.writeTime = clock;
				cloads[i].cloadBuffer = null;
				cloads[i].remainRunTime = 0;
				if(cloads[i].instruction.write == -1)
                    cloads[i].instruction.write = clock;
				System.out.println("Write Issue: Load"+ ((LoadInstruction)cloads[i].instruction).registerNo + " result = "+ cloads[i].result);
				
				String finishLoad = "loader" + Integer.toString(i);
				int LoadResult = cloads[i].result;
				//更新寄存器状态表
				upDateRegisters(LoadResult,finishLoad);
				//更新保留站
				upDateReservation(LoadResult,finishLoad);
			}
		}
	}
	
	/*Write ADD/SUB */
	public void writeAdd() {
		for(int i = 0; i < CAddNum; i ++) {
			if(cadds[i].isBusy && cadds[i].remainRunTime == 1) {
				//更新运算资源的信息
				cadds[i].isBusy = false;
				cadds[i].remainRunTime = 0;
				//更新对应的保留站信息
				cadds[i].calRs.isBusy = false;
				cadds[i].calRs.isReady = cadds[i].calRs.S1Ready = cadds[i].calRs.S2Ready = false;
				cadds[i].calRs.isExec = false;
				cadds[i].calRs.readyTime = 0;
				cadds[i].calRs.Qj = cadds[i].calRs.Qk = null;
				cadds[i].calRs.Vj = cadds[i].calRs.Vk = 0;
				cadds[i].calRs.writeTime = clock;
				cadds[i].calRs = null;
				//更新指令写回信息
				if(cadds[i].instruction.write == -1)	//第一次在本周期写回的指令
					cadds[i].instruction.write = clock;
				
				CalInstruction addInst = (CalInstruction)cadds[i].instruction;
				System.out.println("Write Issue: ADD/SUB "+addInst.OprType+ " F" + addInst.registerD+ " F" + addInst.registerS1+" F" + addInst.registerS2 + " result = "+ cadds[i].result);
				
				String finishAdd = "adder" + Integer.toString(i);
				int AddResult = cadds[i].result;
				//更新寄存器状态表
				upDateRegisters(AddResult,finishAdd);
				//更新保留站
				upDateReservation(AddResult,finishAdd);
			}
		}
	}
	
	/*Write MUL/DIV */
	public void writeMult() {
		
	}
	
	/*Write JUMP */
	public void writeJump() {
		
	}
	
	public void write() {
		writeLoad();    //写回LD
		writeAdd();		//写回ADD/SUB
		writeMult();	//写回MUL/DIV
		writeJump();    //写回JUMP
	}
	
	
	
	
	
	//指令写回,更新寄存器状态表
	public void upDateRegisters(int result, String finishInst) {
		for(int i = 0; i < RegisterNum;i ++) {
			//寻找匹配的并等待写回的寄存器
			if(registers[i].isWaiting && registers[i].stateFunc.contentEquals(finishInst)) {
				registers[i].isWaiting = false;
				registers[i].stateFunc = null;
				registers[i].value = result;
				System.out.println("Register "+ i + " value = "+registers[i].value);
			}
		}		
	}
	
	//指令写回,更新保留站
	public void upDateReservation(int result , String finishInst) {
		//更新加减法保留站
		for(int i = 0; i < AddRsNum; i ++) {
			if(addRs[i].isBusy &&(addRs[i].Qj != null || addRs[i].Qk != null)) { //操作数等待就绪的指令
				if(finishInst.equals(addRs[i].Qj)) {	//等待该结果作为第一操作数的保留站
					addRs[i].Vj = result;
					addRs[i].Qj = null;
				}
				if(finishInst.equals(addRs[i].Qk)) { //等待该结果作为第二操作数的保留站
					addRs[i].Vk = result;
					addRs[i].Qk = null;
				}
				if(addRs[i].Qj == null && addRs[i].Qk == null) {	//两个操作数均就绪,可以进行计算
					addRs[i].isReady = true;
					addRs[i].readyTime = clock;
				}
			}
			//System.out.println("addRs "+ i + " Qj = "+addRs[i].Qj +" Qk = "+addRs[i].Qk + " Vj ="+addRs[i].Vj + " Vk = "+ addRs[i].Vk);
		}
		//更新乘除法保留站
		for(int i = 0; i < MultRsNum; i ++) {
			if(multRs[i].isBusy &&(multRs[i].Qj != null || multRs[i].Qk != null)) {
				if(finishInst.equals(multRs[i].Qj)) {	//等待该结果作为第一操作数的保留站
					multRs[i].Vj = result;
					multRs[i].Qj = null;
				}
				if(finishInst.equals(multRs[i].Qk)) { //等待该结果作为第二操作数的保留站
					multRs[i].Vk = result;
					multRs[i].Qk = null;
				}
				if(multRs[i].Qj == null && multRs[i].Qk == null) {	//两个操作数均就绪,可以进行计算
					multRs[i].isReady = true;
					multRs[i].readyTime = clock;
				}
			}
			//System.out.println("multRs "+ i + " Qj = "+multRs[i].Qj +" Qk = "+multRs[i].Qk + " Vj ="+multRs[i].Vj + " Vk = "+ multRs[i].Vk);
		}
	}
	
	public void printRs() {
		for(int i = 0; i < AddRsNum; i ++) {
			System.out.println("addRs "+ i + " "+ addRs[i].operation + " Qj = "+addRs[i].Qj +" Qk = "+addRs[i].Qk + " Vj ="+addRs[i].Vj + " Vk = "+ addRs[i].Vk);
		}
		for(int i = 0; i < MultRsNum; i ++) {
			System.out.println("multRs "+ i + " "+ multRs[i].operation +" Qj = "+multRs[i].Qj +" Qk = "+multRs[i].Qk + " Vj ="+multRs[i].Vj + " Vk = "+ multRs[i].Vk);
		}
	}
	
	public void printResult() {
		for(int i = 0; i < inst.length; i ++) {
			System.out.println(i + " "+ inst[i].OprType + " "
								+inst[i].issue + " "+inst[i].exec+ " "+inst[i].write);
		}
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
    ReserveStation calRs;	 //若为ADD,SUB,MUL,DIV指令所占用的Rs
    	
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
	boolean S1Ready;	//第一操作数就绪
	boolean S2Ready;	//第二操作数就绪
	
	int issueTime;		//指令流出时间
    int readyTime;		//操作数就绪时间
    int writeTime;		//指令写回时间
    
    
    ReserveStation(){
        this.issueTime = -1;
        this.readyTime = -1;
        this.writeTime = -1;
        this.isBusy = false;
        this.isExec = false;
        this.isReady = false;
        this.S1Ready = false;
        this.S2Ready = false;
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
	int writeTime;				//指令写回时间
//	int address;				//写入的寄存器地址
	
	LoadBuffer(){
		this.isBusy = false;
		this.isExec = false;
		this.issueTime = -1;
		this.writeTime = -1;
	}
}


/*寄存器状态类：RegisterStatus 
 * 32个寄存器（F0-F31）
 */
class RegisterStatus{
    String stateFunc;	//寄存器状态	
    int value;			//寄存器数值
    
    boolean isWaiting;	//是否在等待执行写回
    
    RegisterStatus(){
        stateFunc = null;
        value = 0;
        isWaiting = false;
    }
}



