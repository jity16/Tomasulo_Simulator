public class Instruction {
	public OperationType OprType;
	public int issue,exec,write;
	Instruction(){
	}
	Instruction(String opr){
		this.issue = -1;
		this.exec = -1;
		this.write = -1;
		switch(opr) {
			case "LD":
				this.OprType = OperationType.LD;
				break;
			case "ADD":
				this.OprType = OperationType.ADD;
				break;
			case "SUB":
				this.OprType = OperationType.SUB;
				break;
			case "MUL":
				this.OprType = OperationType.MUL;
				break;
			case "DIV":
				this.OprType = OperationType.DIV;
				break;
			case "JUMP":
				this.OprType = OperationType.JUMP;
				break;
			default:
		}
	}
	public void setType(OperationType opr) {
		this.OprType = opr;
	}
	public OperationType getType() {
		return this.OprType;
	}
	public void PrintInst() {
		System.out.println(this.OprType);
	}
}
class LoadInstruction extends Instruction {
    public int registerNo;
    public int loadAddr;
    LoadInstruction(String[] inst){
        super(inst[0]);
        //Opr = getType(inst[0]);
//        System.out.println("in load"+" "+Opr);
        registerNo = Integer.parseInt(inst[1].replaceAll("F", ""));
        loadAddr = Integer.parseUnsignedInt(inst[2].replaceAll("0x", "").toLowerCase(), 16);
    }
}
class CalInstruction extends Instruction {
	public int registerD, registerS1, registerS2;
    CalInstruction(String[] inst){
        super(inst[0]);
        //Opr = getType(inst[0]);
//        System.out.println("in cal"+" "+Opr);
        registerD = Integer.parseInt(inst[1].replaceAll("F", ""));;
        registerS1 = Integer.parseInt(inst[2].replaceAll("F", ""));;
        registerS2 = Integer.parseInt(inst[3].replaceAll("F", ""));;
    }
}

class JumpInstruction extends Instruction {
    int compare;
    int registerNo;
    int jumpAddr;
    JumpInstruction(String[] inst){
        super(inst[0]);
        //Opr = getType(inst[0]);
//        System.out.println("in jump"+" "+Opr);
        compare = Integer.parseUnsignedInt(inst[1].replaceAll("0x", "").toLowerCase(), 16);
        registerNo = Integer.parseInt(inst[2].replaceAll("F", ""));
        jumpAddr = Integer.parseUnsignedInt(inst[3].replaceAll("0x", "").toLowerCase(), 16);
    }
}