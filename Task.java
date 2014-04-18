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

public class Task {

    private final static String delete = "delete";
    private final static String insert = "insert";
    private final static String find = "find";
    private final static String findForDelete = "findForDelete";
    private final static String findSuccessor = "findSuccessor";
    private final static String min = "min";
    private final static String max = "max";
    private final static String display = "display";
    private final String type;
    private final Object[] args;

    public Task(String type, Object... args) {
        this.type = type.toLowerCase();
        if (args == null) {
            this.args = new Object[0];
        } else {
            this.args = args;
        }
    }

    public void exectute(BstObjPanel bst) {
        String message = null;
        Person person = null;
        if (args.length > 0) {
            if (args[0] instanceof Person) {
                person = (Person) args[0];
            } else if (args[0] instanceof String) {
                message = (String) args[0];
            }
        }
        TreeNode node = null;
        if (args.length > 1) {
            node = (TreeNode) args[1];
        }
        TreeNode parentNode = null;
        if (args.length > 2) {
            parentNode = (TreeNode) args[2];
        }

        switch (type) {
            case delete:
                if (bst.getTaskArgumentsSize() == 0) {
                    bst.addTaskToFront(this);
                    bst.addTaskToFront(new Task(findForDelete, person));
                    bst.addTaskToFront(new Task(display, "Searching for "
                            + person + " to delete them"));
                } else if (bst.getTaskArgumentsSize() == 2) { // We have the node with the person
                    node = (TreeNode) bst.getNextTaskArgument();
                    parentNode = (TreeNode) bst.getNextTaskArgument();
                    if (node == null) {
                        // the node was not found
                        bst.addTaskToFront(new Task(display,
                                "Could not find the person " + person));
                    } else {
                        // node was found, so delete it

                        // re-add the arguments to the queue
                        bst.addTaskArgument(node, parentNode);

                        // find the successor
                        bst.addTaskToFront(new Task(delete, person));
                        bst.addTaskToFront(new Task(findSuccessor, null, node));
                        bst.addTaskToFront(new Task(display,
                                "Found the node containing " + person + " so now we will search for the in-order successor"));
                    }
                } else if (bst.getTaskArgumentsSize() == 3) { // We found the in order successor
                    node = (TreeNode) bst.getNextTaskArgument();
                    parentNode = (TreeNode) bst.getNextTaskArgument();
                    TreeNode inOrderSuccessorNode = (TreeNode) bst.getNextTaskArgument();
                    //bst.addTaskToFront(newTask);

                    if (inOrderSuccessorNode == null) {
                        bst.addTaskToFront(new Task(display,
                                "The value has no in order successor, so we delete the reference to it from the parent"));
                        if (parentNode != null) {
                            if (parentNode.getLkid() != null && parentNode.getLkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setLkid(null);
                            } else if (parentNode.getRkid() != null && parentNode.getRkid().getVal()
                                    .compareTo(node.getVal()) == 0) {
                                parentNode.setRkid(null);
                            }
                        } else {
                            // extreme edge case, the node is the first node, and it is barren,
                            // therefore, we could simply go to bst and tell it to delete the
                            // reference to it, but that seems a bit shady

                            // Either refactor code to include reference to the bst, or delete the shady way
                        }
                    } else if (inOrderSuccessorNode != null) {
                        bst.addTaskToFront(new Task(display,
                                "Found the in-order successor, so now we will swap the values, and delete the in order successor node"));
                        node.setVal(inOrderSuccessorNode.getVal());
                        inOrderSuccessorNode.setVal(node.getVal());
                        bst.addTaskArgument(delete, person, node, parentNode);
                    }
                }
                break;
            case findForDelete:
                //TODO special case of find. Finds the value and adds it with bst.addTaskArgument(foundNode)

                break;
            case findSuccessor:
                //TODO finds the in order successor from the node given, add it with bst.addTaskArgument(inOrderSuccessor)
                if (node.getRkid() != null) {
                    bst.addTaskToFront(new Task(min, null, node.getRkid()));
                    bst.addTaskToFront(new Task(display, "This node has a right child, so we will go to the right tree and find the minimum value"));
                } else if (node.getLkid() != null) {
                    bst.addTaskToFront(new Task(max, null, node.getLkid()));
                    bst.addTaskToFront(new Task(display, "This node has a left child, so we will go to the left tree and find the maximum value"));
                }
                break;
            case min:
                if (node.getLkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else {
                    bst.addTaskToFront(new Task(min, null, node.getLkid()));
                }
                break;
            case max:
                if (node.getRkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else {
                    bst.addTaskToFront(new Task(max, null, node.getRkid()));
                }
                break;
            case insert:
                // TODO person, person to insert
                // node, currentNode

                break;

            case find:

                break;
        }
    }
    // Tasks
    /*
     * TODO build task class
     * 
     * Other Important Tasks
     * 
     * Print results Modify Shapes change color of shape delete a shape
     */

}
