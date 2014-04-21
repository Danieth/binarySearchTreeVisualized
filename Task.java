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

    public final static String DELETE = "delete";
    public final static String INSERT = "insert";
    public final static String FIND = "find";
    public final static String FIND_FOR_DELETE = "findForDelete";
    public final static String FIND_SUCCESSOR = "findSuccessor";
    public final static String MIN = "min";
    public final static String MAX = "max";
    public final static String DISPLAY = "display";
    public final static String PREORDER = "preOrder";
    public final static String INORDER = "inOrder";
    public final static String POSTORDER = "postOrder";
    private final String type;
    private final Object[] args;

    public Task(String type, Object... args) {
        this.type = type;
        if (args == null) {
            this.args = new Object[0];
        } else {
            this.args = args;
        }
    }
    /**
     * 
     * @param bst
     * @return The amount of time a thread should wait before the next task is executed
     */

    public int execute(BstObjPanel bst) {
        String message = null;
        Person person = null;
        if (args.length > 0) {
            if (args[0] instanceof Person) {
                person = (Person) args[0];
            } else if (args[0] instanceof String) {
                message = (String) args[0];
            }
        }
        NodeShape node = null;
        if (args.length > 1) {
            node = (NodeShape) args[1];
        }
        NodeShape parentNode = null;
        if (args.length > 2) {
            parentNode = (NodeShape) args[2];
        }

        switch (type) {
            case PREORDER:
                if(node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if(node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do a pre-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning pre-order scan of list,");
                    }
                }
                node.select(1000);
                bst.addToBuffer(node.getVal().toString());
                if(node.getLkid() != null) {
                   bst.addTaskToFront(new Task(PREORDER, null, node.getLkid()));
                }
                if(node.getRkid() != null) {
                    bst.addTaskToFront(new Task(PREORDER, null, node.getRkid()));
                }
                break;
            case INORDER:
                if(node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if(node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do an in-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning in-order scan of list,");
                    }
                }
                if(parentNode == null) {
                    if(node.getLkid() != null) {
                        bst.addTaskToFront(new Task(INORDER, null, node.getLkid()));
                    }
                    node.select(100);
                    bst.addTaskToFront(new Task(INORDER, null, null, node));
                    if(node.getRkid() != null) {
                        bst.addTaskToFront(new Task(INORDER, null, node.getRkid()));
                    }
                    return 100;
                } else {
                    bst.addToBuffer(parentNode.getVal()+"");
                    parentNode.select(1000);
                }
                break;
            case POSTORDER:
                if(node == bst.treeShape.root || (node != null && node.equals(bst.treeShape.root))) {
                    if(node == null) {
                        bst.buffer.clear();
                        bst.addToBuffer("The tree is empty, so you cannot do a post-order scan");
                        break;
                    } else {
                        bst.buffer.clear();
                        bst.addToBuffer("Beginning post-order scan of list,");
                    }
                }
                if(parentNode == null) {
                    node.select(100);
                    bst.addTaskToFront(new Task(POSTORDER, null, null, node));
                    if(node.getLkid() != null) {
                       bst.addTaskToFront(new Task(POSTORDER, null, node.getLkid()));
                    }
                    if(node.getRkid() != null) {
                        bst.addTaskToFront(new Task(POSTORDER, null, node.getRkid()));
                    }
                    return 100;
                } else {
                    bst.addToBuffer(parentNode.getVal()+"");
                    parentNode.select(1000);
                }
                break;
            case DELETE:
                if (bst.getTaskArgumentsSize() == 0) {
                    bst.addTaskToFront(this);
                    bst.addTaskToFront(new Task(FIND_FOR_DELETE, person));
                    bst.addTaskToFront(new Task(DISPLAY, "Searching for "
                            + person + " to delete them"));
                } else if (bst.getTaskArgumentsSize() == 2) { // We have the node with the person
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    if (node == null) {
                        // the node was not found
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Could not find the person " + person));
                    } else {
                        // node was found, so delete it

                        // re-add the arguments to the queue
                        bst.addTaskArgument(node, parentNode);

                        // find the successor
                        bst.addTaskToFront(new Task(DELETE, person));
                        bst.addTaskToFront(new Task(FIND_SUCCESSOR, null, node));
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Found the node containing " + person + " so now we will search for the in-order successor"));
                    }
                } else if (bst.getTaskArgumentsSize() == 3) { // We found the in order successor
                    node = (NodeShape) bst.getNextTaskArgument();
                    parentNode = (NodeShape) bst.getNextTaskArgument();
                    TreeNode inOrderSuccessorNode = (TreeNode) bst.getNextTaskArgument();
                    //bst.addTaskToFront(newTask);

                    if (inOrderSuccessorNode == null) {
                        bst.addTaskToFront(new Task(DISPLAY,
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
                            bst.treeShape.root = null;

                            // Either refactor code to include reference to the bst, or delete the shady way
                        }
                    } else if (inOrderSuccessorNode != null) {
                        bst.addTaskToFront(new Task(DISPLAY,
                                "Found the in-order successor, so now we will swap the values, and delete the in order successor node"));
                        node.setVal(inOrderSuccessorNode.getVal());
                        inOrderSuccessorNode.setVal(node.getVal());
                        bst.addTaskArgument(DELETE, person, node, parentNode);
                    }
                }
                break;
            case FIND_FOR_DELETE:
                //TODO prevent an infinite loop if the person we're looking for cannot be found
                if(bst.treeShape.root == null) {
                    bst.buffer.clear();
                    bst.addToBuffer("Finding " + person);
                    bst.addToBuffer("Root tree is empty and so we cannot find the person you are looking for.");
                    break;
                }
                if(node == bst.treeShape.root || node == null) {
                    node = bst.treeShape.root;
                    bst.buffer.clear();
                    bst.addToBuffer("Finding " + person);
                }
                if (person != null) {
                    node.select(1000);
                    int compare = node.getVal().compareTo(person);
                    if (compare == 0) {
                        bst.addToBuffer("Found " + person + " for delete");
                        bst.addTaskArgument(node);
                    } else if (compare < 0) {
                        bst.addToBuffer(node.getVal().toString() + " < " + person + " so we will continue down the right tree");
                        NodeShape n = (NodeShape) node.getLkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, node));
                        } else {
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, n));
                        }
                    } else {
                        bst.addToBuffer(node.getVal().toString() + " > " + person + " so we will continue down the left tree");
                        NodeShape n = (NodeShape) node.getRkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, node));
                        } else {
                            bst.addTaskToFront(new Task(FIND_FOR_DELETE, person, n));
                        }
                    }
                }
                break;
            case FIND_SUCCESSOR:
                //TODO finds the in order successor from the node given, add it with bst.addTaskArgument(inOrderSuccessor)
                if (node != null && node.getRkid() != null) {
                    bst.addTaskToFront(new Task(MIN, null, node.getRkid()));
                    bst.addTaskToFront(new Task(DISPLAY, "This node has a right child, so we will go to the right tree and find the minimum value"));
                } else if (node != null && node.getLkid() != null) {
                    bst.addTaskToFront(new Task(MAX, null, node.getLkid()));
                    bst.addTaskToFront(new Task(DISPLAY, "This node has a left child, so we will go to the left tree and find the maximum value"));
                }
                break;
            case MIN:
                if (node != null && node.getLkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addTaskToFront(new Task(MIN, null, node.getLkid()));
                }
                break;
            case MAX:
                if (node != null && node.getRkid() == null) { // Found
                    bst.addTaskArgument(node);
                } else if (node != null) {
                    bst.addTaskToFront(new Task(MAX, null, node.getRkid()));
                }
                break;
            case INSERT:
                if(node == bst.treeShape.root || node == null) {
                    node = bst.treeShape.root;
                    bst.buffer.clear();
                    bst.addToBuffer("Inserting Into the list " + person);
                }

                if(bst.getTaskArgumentsSize() == 1) { // actual insertion
                    bst.treeShape.insert();
                    if((boolean)bst.getNextTaskArgument()) {
                        bst.addToBuffer("The left tree was empty, so this node will point to the new node containing " + person);
                        node.setRkid(new NodeShape(person,null,null));
                        if (node.getRkid() instanceof NodeShape) {
                            ((NodeShape) node.getRkid()).select(1000);
                        }
                    } else {
                        bst.addToBuffer("The right tree was empty, so this node will point to the new node containing " + person);
                        node.setLkid(new NodeShape(person,null,null));
                        if (node.getLkid() instanceof NodeShape) {
                            ((NodeShape) node.getLkid()).select(1000);
                        }
                    }
                    break;
                } else {
                    if(node == null) {
                        bst.treeShape.insert();
                        bst.addToBuffer("Because the root of the BST was not pointing to anything, it will now point to " + person);
                        bst.treeShape.root = new NodeShape(person, null, null);
                        break;
                    } else {
                        node.select(1000);
//                        bst.addToBuffer("Comparing " + person + " to " + node.getVal());
                        if(node.getVal().compareTo(person) < 0) {
                            bst.addToBuffer(node.getVal().toString() + " < " + person + " so we will continue down the right tree");
                            NodeShape n = (NodeShape)node.getLkid();
                            if(n == null) {
                                bst.addTaskArgument(false);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else if(node.getVal().compareTo(person) > 0) {
                            bst.addToBuffer(node.getVal().toString() + " > " + person + " so we will continue down the left tree");
                            NodeShape n = (NodeShape)node.getRkid();
                            if(n == null) {
                                bst.addTaskArgument(true);
                                bst.addTaskToFront(new Task(INSERT, person, node));
                            } else {
                                bst.addTaskToFront(new Task(INSERT, person, n));
                            }
                        } else {
                            //Problem!
                        }
                    }
                }
                break;
            case FIND:
                //TODO prevent an infinite loop if the person we're looking for cannot be found
                if(bst.treeShape.root == null) {
                    bst.buffer.clear();
                    bst.addToBuffer("Finding " + person);
                    bst.addToBuffer("Root tree is empty and so we cannot find the person you are looking for.");
                    break;
                }
                if(node == bst.treeShape.root || node == null) {
                    node = bst.treeShape.root;
                    bst.buffer.clear();
                    bst.addToBuffer("Finding " + person);
                }
                if (person != null) {
                    node.select(1000);
                    int compare = node.getVal().compareTo(person);
                    if (compare == 0) {
                        bst.addToBuffer("Found " + person);
                    } else if (compare < 0) {
                        bst.addToBuffer(node.getVal().toString() + " < " + person + " so we will continue down the right tree");
                        NodeShape n = (NodeShape) node.getLkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addTaskToFront(new Task(FIND, person, node));
                        } else {
                            bst.addTaskToFront(new Task(FIND, person, n));
                        }
                    } else {
                        bst.addToBuffer(node.getVal().toString() + " > " + person + " so we will continue down the left tree");
                        NodeShape n = (NodeShape) node.getRkid();
                        if (n == null) {
                            bst.addTaskArgument(false);
                            bst.addTaskToFront(new Task(FIND, person, node));
                        } else {
                            bst.addTaskToFront(new Task(FIND, person, n));
                        }
                    }
                }
                break;
        }
        return 1000;
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
