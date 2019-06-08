import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JPanel{
	GridLayout gridLayout;
	public JLabel[][] labels;
	
	public RegisterUI() {
		super();
		//布局
		gridLayout = new GridLayout(6,17);
		this.setLayout(gridLayout);
		
		//颜色
		Color borderColor = new java.awt.Color(255,240,245);
		
		
		//Labels部件
		labels = new JLabel[6][17];	
		for(int i = 0 ; i < 6; i ++) {
			for(int j = 0; j < 17; j++) {
				labels[i][j] = new JLabel("", JLabel.CENTER);
				labels[i][j].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, borderColor));
				this.add(labels[i][j]);
			}
		}
		
		labels[1][0].setText("Status");
		labels[2][0].setText("Value");
		labels[4][0].setText("Status");
		labels[5][0].setText("Value");
		
		for(int j = 1; j <17; j ++) {
			labels[0][j].setText("F"+ j);
			labels[3][j].setText("F"+ (16+j));
		}
	}
}
