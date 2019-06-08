import javax.swing.*;
import java.awt.*;

// 运算器部件
public class CalculatorUI extends JPanel{
	GridLayout gridLayout;
	public JLabel[][] labels;
	
	public CalculatorUI() {
		super();
		//布局
		gridLayout = new GridLayout(8,3);
		this.setLayout(gridLayout);
		
		//颜色
		Color borderColor = new java.awt.Color(255,240,245);
		
		
		//Labels部件
		labels = new JLabel[8][3];
		for(int i = 0; i < 8; i ++) {
			for(int j = 0 ; j < 3; j ++) {
				labels[i][j] = new JLabel("", JLabel.CENTER);
				labels[i][j].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, borderColor));
				this.add(labels[i][j]);
			}
		}
			
		
		labels[0][1].setText("Instruction");
		labels[0][2].setText("Timeleft");
		
		labels[1][0].setText("adder1");
		labels[2][0].setText("adder2");
		labels[3][0].setText("adder3");
		labels[4][0].setText("multer1");
		labels[5][0].setText("multer2");
		labels[6][0].setText("loader1");
		labels[7][0].setText("loader2");
	}
}

