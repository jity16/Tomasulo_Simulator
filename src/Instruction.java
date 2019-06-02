public class Instruction {
	public OperationType Opr;
	public int issue,exec,write;
	Instruction(){
	}
	Instruction(String opr){
		this.issue = -1;
		this.exec = -1;
		this.write = -1;
		switch(opr) {
			case "LD":
				this.Opr = OperationType.LD;
				break;
			case "ADD":
				this.Opr = OperationType.ADD;
				break;
			case "SUB":
				this.Opr = OperationType.SUB;
				break;
			case "MUL":
				this.Opr = OperationType.MUL;
				break;
			case "DIV":
				this.Opr = OperationType.DIV;
				break;
			case "JUMP":
				this.Opr = OperationType.JUMP;
				break;
			default:
		}
	}
	public void PrintInst() {
		System.out.println(Opr);
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