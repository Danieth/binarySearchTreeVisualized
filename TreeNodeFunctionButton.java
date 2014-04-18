import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Clarifications:
 * This code is all the work of Daniel Ackerman. I used no other code but
 * my own in the process of creating this solution. No one else, to the best of
 * my efforts, and to the extent of my knowledge, has viewed this work except
 * for myself, and my professors. I have collaborated with no one, received help
 * from no one, and talked to no one about this solution. All code here is the
 * individual work of Daniel Ackerman.
 * 
 * Copyright (c) <2013> <Daniel Ackerman>
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above clarifications, copyright notice, and this permission
 * notice shall be included in all copies of the Software.
 *
 */

public class TreeNodeFunctionButton extends JButton {
    
    public TreeNodeFunctionButton(final BstObjPanel bstObjPanel, final JFrame topFrame, final String command) {
        super(command);
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bstObjPanel.pause();
                while (true) {
                    String response = JOptionPane.showInputDialog(topFrame,
                            "Please enter the data of the person you would like to "
                                    + command
                                    + " as follows: firstName,lastName,age,state\n(Take your time - the visualization was paused)"
                    );
                    Person person;
                    try {
                        person = new Person(response);
                    } catch (IllegalArgumentException e1) {
                        continue;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        continue;
                    }
                    if (response != null) {
                        bstObjPanel.addTaskToEnd(new Task(command, person, bstObjPanel.treeShape.root, null));
                        break;
                    }
                }
                // until here
                bstObjPanel.unpause();
            }
        });
    }
}
