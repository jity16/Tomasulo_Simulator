import javax.swing.*;
import java.awt.*;

public class ReservationUI extends JPanel{
    GridLayout gridLayout;
    JLabel[] labels;
    int row = 10, col = 7;

    public ReservationUI() {
        super();
        gridLayout = new GridLayout(row, col);
        this.setLayout(gridLayout);
        labels = new JLabel[row*col];
        //颜色
      	Color borderColor = new java.awt.Color(255,240,245);
        
        for (int i = 0; i < labels.length; i++) {
            labels[i] = new JLabel("", JLabel.CENTER);
//            labels[i].setPreferredSize(new Dimension(10, 10));
            labels[i].setBorder(BorderFactory.createMatteBorder(1, 5, 2, 1, borderColor));
            this.add(labels[i]);
        }
        labels[1].setText("Busy");
        labels[2].setText("Op");
        labels[3].setText("Vj");
        labels[4].setText("Vk");
        labels[5].setText("Qj");
        labels[6].setText("Qk");
        String prefix = "Ars ";
        for (int i = 0; i < 6; i++) {
            labels[7+i*7].setText(prefix+(i+1));
        }
        prefix = "Mrs ";
        for (int i = 0; i < 3; i++) {
            labels[49+i*7].setText(prefix+(i+1));
        }
    }
}
