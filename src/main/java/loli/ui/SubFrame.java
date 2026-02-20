package loli.ui;

import loli.Exchange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class SubFrame extends JFrame {

    private final Exchange exchange;
    private int mainWidth = 1000;
    private int mainHeight = 1000;

    public SubFrame(){
        exchange = new Exchange(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        addComponentListener(new ComponentAdapter(){
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                mainWidth = e.getComponent().getWidth();
                mainHeight = e.getComponent().getHeight();
                resizeElements();
            }
        });

        setSize(mainWidth, mainHeight);

        // Create main panel on top of Frame
        // Add main panel to the center of Frame
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 2, 2));

        getContentPane().add(mainPanel);

        setJMenuBar(exchange.createMainMenu());

        mainPanel.add(exchange.getTopLayeredPane());
        mainPanel.add(exchange.getBottomLayeredPane());
    }

    private void resizeElements(){
        // Top
        for(Component component : exchange.getTopLayeredPane().getComponents()){
            if(component instanceof JPanel panel){
                panel.setSize(exchange.getTopLayeredPane().getSize());
                panel.updateUI();
            }
        }
        // Bottom
        for(Component component : exchange.getBottomLayeredPane().getComponents()){
            if(component instanceof JPanel panel){
                panel.setSize(exchange.getTopLayeredPane().getSize());
                panel.updateUI();
            }
        }
    }

}
