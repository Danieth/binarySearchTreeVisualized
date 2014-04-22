package me.danieth.bstvisualized;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BstObjPanel extends JPanel implements Runnable {

    private final static double ZOOM_MIN = 5;
    private final static double ZOOM_MAX = 0.2;
    final public BstObjShape treeShape = new BstObjShape();
    private final ConcurrentLinkedDeque<Task> tasksToExecute = new ConcurrentLinkedDeque<>();
    private final LinkedList<Object> taskArguments = new LinkedList<>();
    private double x;
    private double y;
    private double mouseX;
    private double mouseY;
    private NodeShape mouseIsOverNode = null;
    /**
     * If true, the panel will not allow zooming. If false, use the scroll wheel to zoom in and out.
     */
    private boolean zoomIsLocked = false;
    /**
     * If true, the panel will not allow panning. If false, the panel will pan when the mouse clicks, and then drags.
     */
    private boolean screenIsLocked = false;
    /**
     * A zoom value of 1 means that the images are being displayed normally.
     */
    private double zoom = 1;
    /**
     * The last x value for the mouse before the affine transform was applied, and the frame was updated
     */
    private double lastX;
    /**
     * The last y value for the mouse before the affine transform was applied, and the frame was updated
     */
    private double lastY;

    private int tasksCompleted = 0;

    private PersonGenerator personGenerator = new PersonGenerator();

    private int speed = 0;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public ArrayList<String> buffer = new ArrayList<>(64);
    private boolean selectOn = true;

    final JLabel[] data = new JLabel[20];
    final String[] defaultDataText =
            {"Number of nodes: ", "Tasks completed: ", "Tasks in queue: ", "Speed: ", "Paused: "};
    private int tasksInQueue = 0;

    public BstObjPanel() {
        setOpaque(false);
        setDoubleBuffered(true);
        setVisible(true);
        setFocusable(true);
        int i = 0;
        for (; i < defaultDataText.length; i++) {
            data[i] = new JLabel(defaultDataText[i] + '0');
        }
        for (; i < data.length; i++) {
            data[i] = new JLabel("");
        }

        addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL
                        && !zoomIsLocked) {
                    incrementZoom(.05 * -(double) e.getWheelRotation());
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (!screenIsLocked) {
                    lastX = e.getX();
                    lastY = e.getY();
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                updateMouse();
                repaint();
            }

            public void mouseDragged(MouseEvent e) {
                if (!screenIsLocked) {
                    final int newMouseX = e.getX();
                    final int newMouseY = e.getY();

                    Point2D adjPreviousPoint = getAffineTranslatedPoint(new Point2D.Double(
                            lastX, lastY));

                    lastX = newMouseX;
                    lastY = newMouseY;

                    Point2D adjNewPoint = getAffineTranslatedPoint(new Point2D.Double(
                            newMouseX, newMouseY));

                    double newX = adjNewPoint.getX() - adjPreviousPoint.getX();
                    double newY = adjNewPoint.getY() - adjPreviousPoint.getY();

                    mouseX += newX;
                    mouseY += newY;
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        final BstObjPanel bstObjPanel = new BstObjPanel();

        final Thread thread = new Thread(bstObjPanel);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                final JFrame frame = new JFrame("Binary Search Tree Display") {
                    @Override
                    public void dispose() {
                        bstObjPanel.running = false;
                        super.dispose();
                    }
                };
                JFrame.setDefaultLookAndFeelDecorated(true);
                frame.setLayout(new GridBagLayout());
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create the weights for the main graphical panel
                GridBagConstraints c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.ipadx = 500;
                c.ipady = 500;
                c.gridwidth = 100;
                c.gridheight = 100;
                c.gridx = GridBagConstraints.RELATIVE;
                c.anchor = GridBagConstraints.LINE_START;
                c.gridy = 0;
                c.weighty = 1.0;
                c.weightx = 1.0;
                // add the panel with weights
                frame.add(bstObjPanel, c);

                // Create the weights for the main graphical panel
                c = new GridBagConstraints();
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 0; // 0.125
                c.weighty = 1.0;
                c.ipadx = 0;
                c.ipady = 50;
                c.gridwidth = 1;
                c.gridheight = 1;
                c.gridx = GridBagConstraints.RELATIVE;
                c.anchor = GridBagConstraints.LINE_START;

                final JPanel dataPanel = new JPanel() {
                    {
                        setLayout(new GridLayout(bstObjPanel.data.length, 1));
                        for (int i = 0; i < bstObjPanel.data.length; i++) {
                            if (bstObjPanel.data[i] != null) {
                                add(bstObjPanel.data[i]);
                            }
                        }
                    }
                };
                final JPanel buttonPanel = new JPanel() {
                    {
                        setLayout(new GridLayout(0, 1));
                        add(new JButton("Exit") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        frame.dispose();
                                    }
                                });
                            }
                        });
                        add(new TreeNodeFunctionButton(bstObjPanel, frame,
                                "Insert"));
                        add(new JButton("Insert Median Node (If Empty)") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (bstObjPanel.treeShape.root == null) {
                                            bstObjPanel.personGenerator.reset();
                                            bstObjPanel.paused = true;
                                            bstObjPanel.addTaskToEnd(new Task("insert", bstObjPanel.personGenerator
                                                    .generateMedianPerson(), bstObjPanel.treeShape.root));
                                            bstObjPanel.paused = false;
                                        }
                                    }
                                });
                            }
                        });
                        add(new JButton("Insert 25 Nodes Now") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (bstObjPanel.treeShape.root == null) {
                                            bstObjPanel.personGenerator.reset();
                                        }
                                        int s = bstObjPanel.speed;
                                        bstObjPanel.speed = 100;
                                        int i = 24;
                                        bstObjPanel.selectOn = false;
                                        bstObjPanel.addTaskToEnd(new Task("insert", bstObjPanel.personGenerator
                                                .generateMedianPerson(), bstObjPanel.treeShape.root));
                                        while (i > 0) {
                                            bstObjPanel.addTaskToEnd(new Task("insert", bstObjPanel.personGenerator
                                                    .generateRandomPerson(), bstObjPanel.treeShape.root));
                                            i--;
                                        }
                                        synchronized (this) {
                                            try {
                                                this.wait(150);
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                        bstObjPanel.selectOn = true;
                                        bstObjPanel.buffer.clear();
                                        bstObjPanel.speed = s;
                                        bstObjPanel.updateData();
                                    }
                                });
                            }
                        });
                        add(new JButton("Insert N Random Nodes") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.paused = true;
                                        if (bstObjPanel.treeShape.root == null) {
                                            bstObjPanel.personGenerator.reset();
                                            bstObjPanel.paused = false;
                                        }
                                        String extra = "";
                                        while (true) {
                                            String res = JOptionPane
                                                    .showInputDialog("Please type the number of nodes you want to randomely insert" + ((extra
                                                            .length() == 0) ? "" : "\n(" + extra + ")"), "50");
                                            if (res == null) {
                                                bstObjPanel.paused = false;
                                                return;
                                            }
                                            try {
                                                int i = Integer.parseInt(res);
                                                while (i > 0) {
                                                    bstObjPanel
                                                            .addTaskToEnd(new Task("insert", bstObjPanel.personGenerator
                                                                    .generateRandomPerson(), bstObjPanel.treeShape.root));
                                                    i--;
                                                }
                                            } catch (Exception e1) {
                                                extra = "Error: Must input a number";
                                                continue;
                                            }
                                            break;
                                        }
                                        bstObjPanel.paused = false;
                                    }
                                });
                            }
                        });
                        add(new JButton("Pre-Order") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel
                                                .addTaskToEnd(new Task("preOrder", null, bstObjPanel.treeShape.root));
                                    }
                                });
                            }
                        });
                        add(new JButton("In-Order") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.addTaskToEnd(new Task("inOrder", null, bstObjPanel.treeShape.root));
                                    }
                                });
                            }
                        });
                        add(new JButton("Post-Order") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel
                                                .addTaskToEnd(new Task("postOrder", null, bstObjPanel.treeShape.root));
                                    }
                                });
                            }
                        });
                        add(new TreeNodeFunctionButton(bstObjPanel, frame,
                                "Find"));
                        add(new TreeNodeFunctionButton(bstObjPanel, frame,
                                "Delete"));
                        add(new JButton("Clear Tree") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.treeShape.root = null;
                                        bstObjPanel.treeShape.resetNodeCount();
                                        bstObjPanel.updateData();
                                    }
                                });
                            }
                        });
                        add(new JButton("Clear Buffer") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.paused = true;
                                        bstObjPanel.buffer.clear();
                                        bstObjPanel.paused = false;
                                    }
                                });
                            }
                        });
                        add(new JButton("Clear Tasks") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.paused = true;
                                        bstObjPanel.tasksToExecute.clear();
                                        bstObjPanel.updateData();
                                        bstObjPanel.paused = false;
                                    }
                                });
                            }
                        });
                        add(new JButton("Speed Up") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (bstObjPanel.speed < 10 - 1) {
                                            bstObjPanel.speed++;
                                            bstObjPanel.updateData();
                                        }
                                    }
                                });
                            }
                        });
                        add(new JButton("Slow Down") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (bstObjPanel.speed > -10 + 1) {
                                            bstObjPanel.speed--;
                                            bstObjPanel.updateData();
                                        }
                                    }
                                });
                            }
                        });
                        add(new JButton("Reset Speed") {
                            {
                                this.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.speed = 0;
                                        bstObjPanel.updateData();
                                    }
                                });
                            }
                        });
                        add(new JButton("Lock Zoom") {
                            {
                                this.addActionListener(new ActionListener() {
                                    private String base = " Zoom";
                                    private String[] values = {"Lock",
                                            "Unlock"};
                                    private int v = 0;

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        ((JButton) e.getSource())
                                                .setText(values[v = ((v + 1) % values.length)]
                                                        + base);
                                        bstObjPanel.toggleZoom();
                                    }
                                });
                            }
                        });
                        add(new JButton("Lock Panning") {
                            {
                                this.addActionListener(new ActionListener() {
                                    private String base = " Panning";
                                    private String[] values = {"Lock",
                                            "Unlock"};
                                    private int v = 0;

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        ((JButton) e.getSource())
                                                .setText(values[v = ((v + 1) % values.length)]
                                                        + base);
                                        bstObjPanel.toggleScreenPanning();
                                    }
                                });
                            }
                        });
                        add(new JButton("Pause") {
                            {
                                this.addActionListener(new ActionListener() {

                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        bstObjPanel.paused = !bstObjPanel.paused;
                                        bstObjPanel.updateData();
                                    }
                                });
                            }
                        });
                    }
                };
                JPanel sidePanel = new JPanel() {
                    {
                        setLayout(new GridLayout(2, 2));
                        add(buttonPanel);
                        add(dataPanel);
                    }
                };
                frame.add(sidePanel, c);
                frame.setUndecorated(true);

                Dimension screenSize = Toolkit.getDefaultToolkit()
                        .getScreenSize();
                frame.setSize(screenSize.width, screenSize.height);
                frame.setExtendedState(Frame.MAXIMIZED_BOTH);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        System.gc();
        thread.run();
        System.gc();
    }

    boolean pauseBetweenTasks = false;

    public void run() {
        while (running) {
            final int delay;
            if (!tasksToExecute.isEmpty()) {
                delay = tasksToExecute.poll().execute(this);
                tasksInQueue--;
                tasksCompleted++;
                updateData();
            } else {
                System.gc();
                delay = 15;
            }
            if (pauseBetweenTasks) {
                System.gc();
                paused = true;
                updateData();
                while (paused) {
                    updateMouse();
                    repaint();
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }

            int i = 0;
            for (; i < delay - (delay * speed) / 10 || paused; i += 10) {
                updateMouse();
                repaint();
                try {
                    do {
                        Thread.sleep(10);
                        if (paused) {
                            System.gc();
                            updateMouse();
                            repaint();
                        }
                    } while (paused);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateMouse() {
        if (treeShape.root != null) {
            NodeShape current = null;
            if (mouseIsOverNode != null) {
                current = mouseIsOverNode;
            }
            mouseIsOverNode = treeShape.root
                    .contains(normalize(new Point2D.Double(x, y)));
            if (current == null || !current.equals(mouseIsOverNode)) {
                if (current != null) {
                    current.unselect();
                }
                if (mouseIsOverNode != null) {
                    mouseIsOverNode.select();
                }
            }
        } else {
            if (mouseIsOverNode != null) {
                mouseIsOverNode.unselect();
            }
            mouseIsOverNode = null;
        }
    }

    private void updateData() {
        data[0].setText(defaultDataText[0] + treeShape.numberOfNodes());
        data[1].setText(defaultDataText[1] + tasksCompleted);
        data[2].setText(defaultDataText[2] + tasksInQueue);
        data[3].setText(defaultDataText[3] + (speed + 10));
        data[4].setText(defaultDataText[4] + ((paused) ? "Yes" : "No"));
    }

    public void toggleZoom() {
        zoomIsLocked = !zoomIsLocked;
    }

    public void toggleScreenPanning() {
        screenIsLocked = !screenIsLocked;
    }

    public void incrementZoom(double amount) {
        zoom = Math.min(ZOOM_MIN, Math.max(ZOOM_MAX, zoom + amount));
    }

    public void paintComponent(Graphics g) {
        Graphics2D gd = (Graphics2D) g.create();

        // Set constants for rendering
        gd.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gd.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gd.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        // Draw anything you do not want to be affected by transformations, aka,
        // anything you would like to be drawn over the Binary Search Tree.
        // Useful for debugging

        if (buffer.size() > 0) {
            gd.setFont(new Font("Georgia", Font.BOLD, 10));
            gd.drawString(buffer.get(0), 30, 30);
            gd.setFont(new Font("Georgia", 0, 10));
            for (int i = 1; i < buffer.size(); i++) {
                gd.drawString(buffer.get(i), 30, 30 + i * 12);
            }
        }

        // Apply transform for data
        gd.setTransform(getAffineTransform());

        // Apply translation for data
        gd.translate(30, -getHeight() / 2 + 50);
        // Draw the Binary Search tree
        treeShape.draw(gd);

        // Dispose of the graphics

        gd.dispose();
    }

    private AffineTransform getAffineTransform() {
        AffineTransform tx = new AffineTransform();

        tx.translate(getWidth() / 2, getHeight() / 2);
        tx.scale(zoom, zoom);
        tx.translate(mouseX, mouseY);

        return tx;
    }

    private Point2D normalize(Point2D point) {
        return getStaticallyTranslatedPoint(getAffineTranslatedPoint(point));
    }

    private Point2D getStaticallyTranslatedPoint(Point2D point) {
        return new Point2D.Double(point.getX() - 30, point.getY()
                + (getHeight() / 2 + 50) - 98);
    }

    private Point2D getAffineTranslatedPoint(Point2D point) {
        try {
            return getAffineTransform().inverseTransform(point, null);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void pause() {
        this.paused = true;
        updateData();
    }

    public void unpause() {
        this.paused = false;
        updateData();
    }

    public void addTaskToFront(Task newTask) {
        tasksInQueue++;
        tasksToExecute.addFirst(newTask);
    }

    public void addTaskToEnd(Task newTask) {
        tasksInQueue++;
        tasksToExecute.addLast(newTask);
    }

    public Object getNextTaskArgument() {
        return taskArguments.poll();
    }

    /**
     * Added in order to the end of the queue
     */
    public void addTaskArgument(Object... c) {
        Collections.addAll(taskArguments, c);
    }

    public int getTaskArgumentsSize() {
        return taskArguments.size();
    }

    public void addToBuffer(String s) {
        if (buffer.size() == 60) {
            String firstValue = buffer.get(0);
            buffer.clear();
            buffer.add(firstValue);
        }
        buffer.add(s);
    }

    public boolean isSelectingOn() {
        return selectOn;
    }
}
