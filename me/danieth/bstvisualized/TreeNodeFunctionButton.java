package me.danieth.bstvisualized;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeNodeFunctionButton extends JButton {
    
    public TreeNodeFunctionButton(final BstObjPanel bstObjPanel, final JFrame topFrame, final String command) {
        super(command);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bstObjPanel.pause();
                String extraWords = "";
                String defaultText = "";
                while (true) {
                    String response = JOptionPane.showInputDialog(topFrame,
                            "Please enter the data of the person you would like to "
                                    + command
                                    + "! " + extraWords + "\n(take your time - the visualization was paused)", defaultText);
                    if (response == null) {
                        break;
                    }
                    Person person;
                    try {
                        person = new Person(response.split(","));
                    } catch (IllegalArgumentException e1) {
                        extraWords = "(" + e1.getMessage() + ")";
                        defaultText = "first name, last name, age, state";
                        continue;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        continue;
                    }
                    bstObjPanel.addTaskToFront(new Task(command.toLowerCase(), person, bstObjPanel.treeShape.root, null));
                    break;
                }
                bstObjPanel.unpause();
            }
        });
    }
}
