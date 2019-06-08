import javax.swing.*;
import java.awt.*;

// loadBuffer部件
public class LoadBufferUI extends JPanel{
	GridLayout gridLayout;
	JLabel[] labels;
	
	public LoadBufferUI() {
		super();
		//布局
		gridLayout = new GridLayout(4,3);
		this.setLayout(gridLayout);
		
		//颜色
		Color borderColor = new java.awt.Color(255,240,245);
		
		
		//Labels部件
		labels = new JLabel[12];
		for(int i = 0; i < 12; i ++) {
			labels[i] = new JLabel("", JLabel.CENTER);
			labels[i].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, borderColor));
			this.add(labels[i]);
		}
		
		labels[1].setText("Busy");
		labels[2].setText("Address");
		
		labels[3].setText("LB1");
		labels[6].setText("LB2");
		labels[9].setText("LB3");
	}
}
