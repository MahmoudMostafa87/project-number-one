package anything.mavenproject;

import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

//import java.util.Scanner;



public class Mavenproject {
    
    
  
    
public static void main(String[] args) {
        
        JFrame frame = new JFrame("Paint Brush");
        PaintBrush paintBrush = new PaintBrush();
        frame.add(paintBrush);
        
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        paintBrush.setBackground(Color.WHITE);


        
//first way
//    NewThread s=new NewThread();
//    Thread th=new Thread(s);
//    th.start();
    
//secound way
//NewThread newth=new NewThread();
//newth.start();



//    Thread th=new Thread(s);
//    th.start();
//Shape1 s=new Square();

//Shape s2=new Square2();








//create ref From class Shape name s to show all method in Shape but s point on object From Square this method abstract
/*
show all method in class shape but this implement from Class Square
*/

//will check this method is found in ref if yes compaile run 
//if nt compaile will return compaile error
//but when create method will create method in object from class Square
/*Shape1 s=new Square(3);
s.creatShape();*/




//will check this method is found in ref if yes compaile run 
//if nt compaile will return compaile error
//but when create method will create method in object from class Square
//Square sq=new Square(3);
//sq.creatShape();


//automatically TYPE Casting 
//Shape1 shape=new Square("mahmoud");


//this cast because of  Shape1 is perant for Square is child
//manually casting
//Square square=(Square) new Shape1();//but will return runtime error
//Square square=new Shape1();
//System.out.println(shape.name);//mahmoud
//
//
//Square s=new Square("mahmoud");
////s.setName("mm");
//System.out.println("this name from child  "+s.name);//output mm without comment
//System.out.println("this name from parent "+s.getName());//output mm without comment





//Square sq;
//
//Square sq1=new Square(3);
//
//sq1=null;
//
//Square sq2=new Square(3);
//Square sq3=sq1;
//System.out.println(sq3==sq1);//is point on the same place in heap
//System.out.println(sq1==sq2);//not the same this not location this compare address not value
//System.out.println(sq1.area()==sq2.area());//this compare between value not address
//System.out.println(sq1);//address to pointer  name = sq1
//System.out.println(sq2);//address to pointer  name = sq2
//System.out.println(sq3);//address to pointer  name = sq3


}
}

    

