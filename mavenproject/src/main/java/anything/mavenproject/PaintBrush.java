package anything.mavenproject;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class PaintBrush extends JPanel {
    private Color currentColor = Color.BLACK;
    private String currentShape = "Line";
    private boolean fill = false;
    private boolean dashed = false;
    private boolean drawing = false;
    private boolean erasing = false;
    private ArrayList<ShapeInfo> shapes = new ArrayList<>();
    private ArrayList<Line2D.Double> lines = new ArrayList<>();
    private ArrayList<Color> lineColors = new ArrayList<>();
    private Point point1, point2;
    private BufferedImage image;

    public PaintBrush() {
        setFocusable(true);
        requestFocusInWindow();

        setupButtons();
        setupMouseListeners();
        
    }

    private void setupButtons() {
        // Color Buttons
        add(createColorButton("Red", Color.RED));
        add(createColorButton("Green", Color.GREEN));
        add(createColorButton("Blue", Color.BLUE));

        // Shape Buttons
        add(createShapeButton("Rectangle"));
        add(createShapeButton("Oval"));
        add(createShapeButton("Line"));

        // Free Hand Button
        Button draw = new Button("Free Hand");
        draw.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                setCurrentShape("FreeHand");
            }
        });
        add(draw);

        // Eraser Button
        Button erase = new Button("Eraser");
        erase.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            drawing = false;
            erasing = true;
            currentColor = getBackground();
            }
        });
        add(erase);

        // Clear All Button
        Button clear = new Button("Clear All");
        clear.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            lines.clear();
            lineColors.clear();
            shapes.clear();
            repaint();
            }
                }); 
        add(clear);

        // Dotted Checkbox
        Checkbox dashedCheckbox = new Checkbox("Dotted");
        dashedCheckbox.addItemListener(e -> dashed = dashedCheckbox.getState());/////////////
        add(dashedCheckbox);

        // Filled Checkbox
        Checkbox fillCheckbox = new Checkbox("Filled");
        fillCheckbox.addItemListener(new ItemListener(){
            
            @Override
            public void itemStateChanged(ItemEvent e) {
            fill = fillCheckbox.getState();
            }
                });
        add(fillCheckbox);

        // Undo Button (Bonus)
        Button undo = new Button("Undo");
        undo.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
              if (!shapes.isEmpty()) {
                shapes.remove(shapes.size() - 1);
                repaint();
            } else if (!lines.isEmpty()) {
                lines.remove(lines.size() - 1);
                lineColors.remove(lineColors.size() - 1);
                repaint();
            }
            }
        });
        add(undo);

        // Save Button (Bonus)
        Button save = new Button("Save");
        save.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                 saveImage();
            }
        });
            add(save);

        // Open Button (Bonus)
        Button open = new Button("Open");
        open.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });
        add(open);
    }

    private void setupMouseListeners() {
    addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            point1 = e.getPoint();
            if (erasing) {
                erase(e.getPoint());
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            point2 = e.getPoint();
            if (!erasing && !currentShape.equals("FreeHand")) {
                shapes.add(new ShapeInfo(currentShape, point1, point2, currentColor, fill, dashed));
                repaint();
            }
        }
    });
addMouseMotionListener(new MouseMotionAdapter() {
        @Override
        public void mouseDragged(MouseEvent e) {
            point2 = e.getPoint();
            if (currentShape.equals("FreeHand")) {
                lines.add(new Line2D.Double(point1, point2));
                lineColors.add(currentColor);
                point1 = point2;
                repaint();
            } else if (erasing) {
                erase(e.getPoint());
            }
        }
    });
}


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }

        // Draw shapes
        for (ShapeInfo shape : shapes) {
            g2d.setColor(shape.getColor());
            if (shape.isDashed()) {
                float[] dash = {10.0f};
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
            } else {
                g2d.setStroke(new BasicStroke(1));
            }

            if (shape.getType().equals("Rectangle")) {
                drawRectangle(g2d, shape);
            } else if (shape.getType().equals("Oval")) {
                drawOval(g2d, shape);
            } else if (shape.getType().equals("Line")) {
                g2d.draw(new Line2D.Double(shape.getStart(), shape.getEnd()));
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            g2d.setColor(lineColors.get(i));
            g2d.draw(lines.get(i));
        }
    }

    private void drawRectangle(Graphics2D g2d, ShapeInfo shape) {
        int x = Math.min(shape.getStart().x, shape.getEnd().x);
        int y = Math.min(shape.getStart().y, shape.getEnd().y);
        int width = Math.abs(shape.getStart().x - shape.getEnd().x);
        int height = Math.abs(shape.getStart().y - shape.getEnd().y);
        if (shape.isFilled()) {
            g2d.fillRect(x, y, width, height);
        } else {
            g2d.drawRect(x, y, width, height);
        }
    }

    private void drawOval(Graphics2D g2d, ShapeInfo shape) {
        int x = Math.min(shape.getStart().x, shape.getEnd().x);
        int y = Math.min(shape.getStart().y, shape.getEnd().y);
        int width = Math.abs(shape.getStart().x - shape.getEnd().x);
        int height = Math.abs(shape.getStart().y - shape.getEnd().y);
        if (shape.isFilled()) {
            g2d.fillOval(x, y, width, height);
        } else {
            g2d.drawOval(x, y, width, height);
        }
    }

private void erase(Point point) {
    int eraseSize = 20;
    for (int i = shapes.size() - 1; i >= 0; i--) {
        ShapeInfo shape = shapes.get(i);
        if (isPointInShape(point, shape)) {
            shapes.remove(i);
        }
    }
    for (int i = lines.size() - 1; i >= 0; i--) {
        Line2D.Double line = lines.get(i);
        if (isPointOnLine(point, line)) {
            lines.remove(i);
            lineColors.remove(i);
        }
    }
    Graphics g = getGraphics();
    g.setColor(getBackground());
    g.fillRect(point.x - eraseSize / 2, point.y - eraseSize / 2, eraseSize, eraseSize);
    repaint();

};

private boolean isPointInShape(Point point, ShapeInfo shape) {
    int x = Math.min(shape.getStart().x, shape.getEnd().x);
    int y = Math.min(shape.getStart().y, shape.getEnd().y);
    int width = Math.abs(shape.getStart().x - shape.getEnd().x);
    int height = Math.abs(shape.getStart().y - shape.getEnd().y);
    return x <= point.x && point.x <= x + width && y <= point.y && point.y <= y + height;
}

private boolean isPointOnLine(Point point, Line2D.Double line) {
    double distance = line.ptLineDist(point);
    return distance <= 5; 
}

    private Button createColorButton(String label, Color color) {
        Button button = new Button(label);
        button.setBackground(color);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            currentColor = color;
            erasing = false; 
            }
    });
        return button;
    }

    private Button createShapeButton(String shape) {
        Button button = new Button(shape);
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                        setCurrentShape(shape);
            }
        });
        return button;
    }

    private void setCurrentShape(String shape) {
        currentShape = shape;
        drawing = true;
        erasing = false;
    }

    private void saveImage() {
        try {
            BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            paint(g);
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                ImageIO.write(image, "png", fileChooser.getSelectedFile());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openImage() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                image = ImageIO.read(fileChooser.getSelectedFile());
                repaint();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private class ShapeInfo {
        private String type;
        private Point start, end;
        private Color color;
        private boolean filled;
        private boolean dashed;
        
        public ShapeInfo(String type, Point start, Point end, Color color, boolean filled, boolean dashed) {
            this.type = type;
            this.start = start;
            this.end = end;
            this.color = color;
            this.filled = filled;
            this.dashed = dashed;
        }

        public String getType() {
            return type;
        }

        public Point getStart() {
            return start;
        }

        public Point getEnd() {
            return end;
        }

        public Color getColor() {
            return color;
        }

        public boolean isFilled() {
            return filled;
        }

        public boolean isDashed() {
            return dashed;
        }
    }
}