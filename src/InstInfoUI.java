import javax.swing.*;
import java.awt.*;

public class InstInfoUI extends JPanel{
	GridLayout gridLayout;
    JLabel[][] labels;

    public InstInfoUI() {
        super();
        gridLayout = new GridLayout(3, 2);
        this.setLayout(gridLayout);
        labels = new JLabel[3][2];
        //颜色
      	Color borderColor = new java.awt.Color(255,240,245);
        
        for (int i = 0; i < 3; i++) {
        	for(int j = 0 ; j < 2; j ++) {
	            labels[i][j] = new JLabel("", JLabel.CENTER);
	            labels[i][j].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, borderColor));
	            this.add(labels[i][j]);
        	}
        }
        labels[0][0].setText("Issue");
        labels[1][0].setText("Exec");
        labels[2][0].setText("Write");

    }
}
