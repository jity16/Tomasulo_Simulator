import javax.swing.*;
import java.awt.*;


public class ClockUI extends JPanel{
	FlowLayout flowLayout;
    JButton button;
    JLabel timerText, clock;    
    public ClockUI() {
        super();
        flowLayout = new FlowLayout();
        this.setLayout(flowLayout);
        
        button = new JButton("Next");
        this.add(button);
        timerText = new JLabel("Cycle: ");
        this.add(timerText);
        clock = new JLabel("1");
        this.add(clock);
    }	
}
