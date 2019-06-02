public class Instruction {
	static OperationType Opr;
	int issue,exec,write;
	Instruction(){
		this.issue = -1;
		this.exec = -1;
		this.write = -1;
	}
	Instruction(String opr){
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
				System.out.println("Unknown Operation Type!");
		}
	}
	public static void PrintInst() {
		System.out.println(Opr);
	}
}
