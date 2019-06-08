import javax.swing.*;
import java.awt.*;


public class ClockUI extends JPanel{
	GridLayout gridLayout;
    JButton button;
    JLabel clock; 
    JLabel tab;
    public ClockUI() {
        super();
        gridLayout = new GridLayout(1,4);
        this.setLayout(gridLayout);
        
        tab = new JLabel(" ");
        
        JLabel clockName = new JLabel("CLOCK");
        this.add(clockName);
        
        clock = new JLabel("1");
        this.add(clock);
        
        this.add(tab);
        
        button = new JButton("Next");
        this.add(button);
        
    }	
}
