/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chessGame;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import processing.core.*;
import static processing.core.PConstants.*;

/**
 *
 * @author shiyaoli
 */
public class chess extends PApplet {

    //private boolean[][] has_piece = new boolean[8][8];
    private ArrayList<chessPieces> pieces = new ArrayList();
    private chessPieces selected_piece = new rook(1, 1, true);
    private boolean is_moving = false;
    private boolean is_whiteTurn = true;
    public king bk = new king(350,50,true);
    public king wk = new king(350,750,false);
    public boolean get_iswhiteTurn(){
        return is_whiteTurn;
    }
    public void settings() {
        size(900, 800);
        pieces.add(new rook(50, 50, true));
        pieces.add(new rook(750, 50, true));
        pieces.add(new knight(150, 50, true));
        pieces.add(new knight(650, 50, true));
        pieces.add(new bishop(250, 50, true));
        pieces.add(new bishop(550, 50, true));
        pieces.add(bk);
        pieces.add(new queen(450, 50, true));
        for (int i = 0; i < 8; i++) {
            pieces.add(new pawn(50 + i * 100, 150, true));
        }
        pieces.add(new rook(50, 750, false));
        pieces.add(new rook(750, 750, false));
        pieces.add(new knight(150, 750, false));
        pieces.add(new knight(650, 750, false));
        pieces.add(new bishop(250, 750, false));
        pieces.add(new bishop(550, 750, false));
        pieces.add(wk);
        pieces.add(new queen(450, 750, false));
        for (int i = 0; i < 8; i++) {
            pieces.add(new pawn(50 + i * 100, 650, false));
        }
    }

    public void setup() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect(i * 100, j * 100, 100, 100);
            }
        }
        fill(255, 255, 255);
        rect(800, 0, 100, 100);
        textSize(25);
        fill(0, 102, 153);
        this.text("Restart!", 805, 50);
//        for (int i = 0; i < 2; i++) {
//            for (int j = 0; j < 8; j++) {
//                has_piece[i][j] = true;
//            }
//        }
//        for (int i = 6; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                has_piece[i][j] = true;
//            }
//        }
    }

    public void draw() {
        if(!bk.is_alive){
            new notice("Black","White");
            restart();
        }
        if(!wk.is_alive){
            new notice("White","black");
            restart();
        }
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).is_moved) {
                pieces.get(i).draw(this);
                pieces.get(i).is_moved = false;
            }
        }
    }

    public void mousePressed() {
        if (Math.abs(mouseX - 850) < 50 && Math.abs(mouseY - 50) < 50) {
            restart();
            return;
        }
        
        if (findPiece(mouseX, mouseY) != null && !findPiece(mouseX, mouseY).is_selected && !is_moving && ((is_whiteTurn && !findPiece(mouseX, mouseY).get_isBlack()) || (!is_whiteTurn && findPiece(mouseX, mouseY).get_isBlack()))) {
            is_moving = true;
            selected_piece = findPiece(mouseX, mouseY);
            selected_piece.is_selected = true;
            return;
        }
        if (selected_piece.is_selected) {
            if(selected_piece.name == "Pawn" && !(selected_piece.get_isBlack()) && mouseY < 100 && selected_piece.moveLegal(this, mouseX, mouseY)){
                new upgrade(selected_piece);
                if (((int) (selected_piece.x / 100) + (int) (selected_piece.y / 100) + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect((int) (selected_piece.x / 100) * 100, (int) (selected_piece.y / 100) * 100, 100, 100);
                selected_piece.x = (int) (mouseX / 100) * 100 + 50;
                selected_piece.y = (int) (mouseY / 100) * 100 + 50;
                is_whiteTurn = false;
                is_moving = false;
                selected_piece.is_selected = false;
                if(check(bk) && bk.is_alive){
                    new checkNotice("Black");
                }
                if(check(wk) && wk.is_alive){
                    new checkNotice("White");
                }
                return;
            }
            if(selected_piece.name == "Pawn" && (selected_piece.get_isBlack()) && mouseY > 700 && selected_piece.moveLegal(this, mouseX, mouseY)){
                new upgrade(selected_piece);
                if (((int) (selected_piece.x / 100) + (int) (selected_piece.y / 100) + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect((int) (selected_piece.x / 100) * 100, (int) (selected_piece.y / 100) * 100, 100, 100);
                selected_piece.x = (int) (mouseX / 100) * 100 + 50;
                selected_piece.y = (int) (mouseY / 100) * 100 + 50;
                is_whiteTurn = true;
                is_moving = false;
                selected_piece.is_selected = false;
                if(check(bk) && bk.is_alive){
                    new checkNotice("Black");
                }
                if(check(wk) && wk.is_alive){
                    new checkNotice("White");
                }
                return;
            }
            if(selected_piece.name == "King" && selected_piece.neverMove && (int)(mouseX/100) == 1 && findPiece(50,mouseY)!=null && findPiece(50,mouseY).name == "Rook" && findPiece(50,mouseY).neverMove){
                System.out.println(selected_piece.get_isBlack());
                
                if(findPiece(150, mouseY)==null && findPiece(250, mouseY)==null && !checkpos(150, mouseY, selected_piece.get_isBlack()) && !checkpos(250, mouseY, selected_piece.get_isBlack())){
                    if(mouseY < 100){
                        fill(200,170,170);
                    }
                    else{
                        fill(128,0,0);
                    }
                    rect(0,(int) (mouseY / 100) * 100,100,100);
                    if(mouseY < 100){
                        fill(128,0,0);
                    }
                    else{
                        fill(200,170,170);
                    }
                    rect(300,(int) (mouseY / 100) * 100,100,100);
                    selected_piece.is_moved = true;
                    findPiece(50,mouseY).is_moved = true;
                    selected_piece.x = 150;
                    findPiece(50,mouseY).x = 250;
                    selected_piece.is_selected = false;
                    is_moving = false;
                    is_whiteTurn = !is_whiteTurn;
                    if(check(bk) && bk.is_alive){
                        new checkNotice("Black");
                    }
                    if(check(wk) && wk.is_alive){
                        new checkNotice("White");
                    }
                    return;
                }
            }
            if(selected_piece.name == "King" && selected_piece.neverMove && (int)(mouseX/100) == 5 && findPiece(750,mouseY)!=null && findPiece(750,mouseY).name == "Rook" && findPiece(750,mouseY).neverMove){
                System.out.println(selected_piece.get_isBlack());
                if(findPiece(450, mouseY)==null && findPiece(550, mouseY)==null && findPiece(650,mouseY) == null && !checkpos(450, mouseY, selected_piece.get_isBlack()) && !checkpos(550, mouseY, selected_piece.get_isBlack()) && !checkpos(550, mouseY, selected_piece.get_isBlack())){
                    if(mouseY < 100){
                        fill(128,0,0);
                    }
                    else{
                        fill(200,170,170);
                    }
                    rect(700,(int) (mouseY / 100) * 100,100,100);
                    if(mouseY < 100){
                        fill(128,0,0);
                    }
                    else{
                        fill(200,170,170);
                    }
                    rect(300,(int) (mouseY / 100) * 100,100,100);
                    selected_piece.is_moved = true;
                    findPiece(750,mouseY).is_moved = true;
                    selected_piece.x = 550;
                    findPiece(750,mouseY).x = 450;
                    selected_piece.is_selected = false;
                    is_moving = false;
                    is_whiteTurn = !is_whiteTurn;
                    if(check(bk) && bk.is_alive){
                        new checkNotice("Black");
                    }
                    if(check(wk) && wk.is_alive){
                        new checkNotice("White");
                    }
                    return;
                }
            }
            if (selected_piece.moveLegal(this, mouseX, mouseY) && findPiece(mouseX, mouseY) == null) {
                if (((int) (selected_piece.x / 100) + (int) (selected_piece.y / 100) + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect((int) (selected_piece.x / 100) * 100, (int) (selected_piece.y / 100) * 100, 100, 100);
                //has_piece[(int) (selected_piece.y / 100)][(int) (selected_piece.x / 100)] = false;
                selected_piece.x = (int) (mouseX / 100) * 100 + 50;
                selected_piece.y = (int) (mouseY / 100) * 100 + 50;
                is_moving = false;
                //has_piece[(int) (mouseY / 100)][(int) (mouseX / 100)] = true;
                selected_piece.is_moved = true;
                selected_piece.is_selected = false;
                is_whiteTurn = !is_whiteTurn;
                if(check(bk) && bk.is_alive){
                    new checkNotice("Black");
                }
                if(check(wk) && wk.is_alive){
                    new checkNotice("White");
                }
            } else if (selected_piece.moveLegal(this, mouseX, mouseY) && findPiece(mouseX, mouseY) != null && selected_piece.get_isBlack() != findPiece(mouseX, mouseY).get_isBlack()) {
                if (((int) (selected_piece.x / 100) + (int) (selected_piece.y / 100) + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect((int) (selected_piece.x / 100) * 100, (int) (selected_piece.y / 100) * 100, 100, 100);

                if (((int) (mouseX / 100) + (int) (mouseY / 100) + 1) % 2 == 1) {
                    fill(200, 170, 170);
                } else {
                    fill(128, 0, 0);
                }
                rect((int) (mouseX / 100) * 100, (int) (mouseY / 100) * 100, 100, 100);
                if (Math.abs(mouseY - selected_piece.y) < 50 && mouseY - selected_piece.y < 150 && Math.abs(mouseX - selected_piece.x) > 50 && findPiece(mouseX, mouseY).is_passant && selected_piece.get_isBlack() != findPiece(mouseX, mouseY).get_isBlack()) {
                    if (selected_piece.get_isBlack()) {
                        selected_piece.x = findPiece(mouseX, mouseY).x;
                        selected_piece.y = findPiece(mouseX, mouseY).y + 100;
                    } else {
                        selected_piece.x = findPiece(mouseX, mouseY).x;
                        selected_piece.y = findPiece(mouseX, mouseY).y - 100;
                    }
                    findPiece(mouseX, mouseY).is_alive = false;
                    is_moving = false;
                    selected_piece.is_moved = true;
                    selected_piece.is_selected = false;
                    is_whiteTurn = !is_whiteTurn;
                    if(check(bk) && bk.is_alive){
                        new checkNotice("Black");
                    }
                    if(check(wk) && wk.is_alive){
                        new checkNotice("White");
                    }
                    return;
                }
                findPiece(mouseX, mouseY).is_alive = false;
                selected_piece.x = (int) (mouseX / 100) * 100 + 50;
                selected_piece.y = (int) (mouseY / 100) * 100 + 50;
                is_moving = false;
                selected_piece.is_moved = true;
                selected_piece.is_selected = false;
                is_whiteTurn = !is_whiteTurn;
                if(check(bk) && bk.is_alive){
                    new checkNotice("Black");
                }
                if(check(wk) && wk.is_alive){
                    new checkNotice("White");
                }
            } else {
                is_moving = false;
                selected_piece.is_selected = false;
            }
            return;
        }
        return;
    }

    public chessPieces findPiece(float x, float y) {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).is_alive && Math.abs(pieces.get(i).x - x) < 50 && Math.abs(pieces.get(i).y - y) < 50) {
                return pieces.get(i);
            }
        }
        chessPieces n = null;
        return n;
    }
    public chessPieces find_passantPiece() {
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).is_alive && pieces.get(i).is_passant) {
                return pieces.get(i);
            }
        }
        chessPieces n = null;
        return n;
    }
    private boolean check(king k){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).is_alive && pieces.get(i).get_isBlack() != k.get_isBlack() && pieces.get(i).moveLegal(this, k.x, k.y)){
                return true;
            }
        }
        return false;
    }
    public boolean checkpos(float x, float y, boolean isblk){
        for(int i = 0; i < pieces.size(); i++){
            if(pieces.get(i).is_alive && pieces.get(i).get_isBlack() != isblk && pieces.get(i).moveLegal(this, x, y)){
                System.out.println(i);
                return true;
            }
        }
        return false;
    }
    public void restart() {
        is_whiteTurn = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (findPiece(100 * i + 50, 100 * j + 50) != null) {
                    //System.out.println("cover"+" "+i+" "+j);
                    if ((i + j + 1) % 2 == 1) {
                        fill(200, 170, 170);
                    } else {
                        fill(128, 0, 0);
                    }
                    rect(i * 100, j * 100, 100, 100);
                }
            }
        }
        for (int i = 0; i < pieces.size(); i++) {
            if (!pieces.get(i).is_alive) {
                pieces.get(i).is_alive = true;
            }
            if (!pieces.get(i).is_moved) {
                pieces.get(i).is_moved = true;
            }
            if (pieces.get(i).name == "Pawn") {
                pieces.get(i).is_firstStep = true;
            }
            if (!pieces.get(i).neverMove) {
                pieces.get(i).neverMove = true;
            }
        }
        pieces.get(0).x = 50;
        pieces.get(0).y = 50;
        pieces.get(1).x = 750;
        pieces.get(1).y = 50;
        pieces.get(2).x = 150;
        pieces.get(2).y = 50;
        pieces.get(3).x = 650;
        pieces.get(3).y = 50;
        pieces.get(4).x = 250;
        pieces.get(4).y = 50;
        pieces.get(5).x = 550;
        pieces.get(5).y = 50;
        pieces.get(6).x = 350;
        pieces.get(6).y = 50;
        pieces.get(7).x = 450;
        pieces.get(7).y = 50;
        for (int i = 8; i < 16; i++) {
            pieces.get(i).x = 50 + 100 * (i - 8);
            pieces.get(i).y = 150;
        }
        pieces.get(16).x = 50;
        pieces.get(16).y = 750;
        pieces.get(17).x = 750;
        pieces.get(17).y = 750;
        pieces.get(18).x = 150;
        pieces.get(18).y = 750;
        pieces.get(19).x = 650;
        pieces.get(19).y = 750;
        pieces.get(20).x = 250;
        pieces.get(20).y = 750;
        pieces.get(21).x = 550;
        pieces.get(21).y = 750;
        pieces.get(22).x = 350;
        pieces.get(22).y = 750;
        pieces.get(23).x = 450;
        pieces.get(23).y = 750;
        for (int i = 24; i < 32; i++) {
            pieces.get(i).x = 50 + 100 * (i - 24);
            pieces.get(i).y = 650;
        }
    }
    class upgrade extends JFrame {
    public upgrade(chessPieces cps){
        super("Upgrade");
        this.setLocation(600, 250);
        this.setSize(400,400);
        Container c = this.getContentPane();
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(4,1,5,10));
        JButton Q = new JButton("Queen");
        Q.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
                for(int i = 0; i < pieces.size(); i++){
                    if(cps.x == pieces.get(i).x && cps.y == pieces.get(i).y && pieces.get(i).is_alive){
                        pieces.remove(i);
                        pieces.add(new queen(cps.x, cps.y, cps.get_isBlack()));
                        break;
                    }
                }
                setVisible(false);
            }           
        });
        JButton K = new JButton("Knight");
        K.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
                for(int i = 0; i < pieces.size(); i++){
                    if(cps.x == pieces.get(i).x && cps.y == pieces.get(i).y && pieces.get(i).is_alive){
                        pieces.remove(i);
                        pieces.add(new knight(cps.x, cps.y, cps.get_isBlack()));
                        break;
                    }
                }
                setVisible(false);
            }           
        });
        JButton B = new JButton("Bishop");
        B.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
                for(int i = 0; i < pieces.size(); i++){
                    if(cps.x == pieces.get(i).x && cps.y == pieces.get(i).y && pieces.get(i).is_alive){
                        pieces.remove(i);
                        pieces.add(new bishop(cps.x, cps.y, cps.get_isBlack()));
                        break;
                    }
                }
                setVisible(false);
            }           
        });
        JButton R = new JButton("Rook");
        R.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) { 
                for(int i = 0; i < pieces.size(); i++){
                    if(cps.x == pieces.get(i).x && cps.y == pieces.get(i).y && pieces.get(i).is_alive){
                        pieces.remove(i);
                        pieces.add(new rook(cps.x, cps.y, cps.get_isBlack()));
                        break;
                    }
                }
                setVisible(false);
            }           
        });
        p.add(Q);
        p.add(K); 
        p.add(B);
        p.add(R);
        c.add(p, BorderLayout.NORTH);
        this.setVisible(true);
    }
    
}
    class notice extends JFrame{
        public notice(String lcolor, String wcolor){
            super("Game over!");
            this.setSize(600,600);
            this.setLocation(600, 250);
            Container c = getContentPane();
            JTextField tf = new JTextField();
            tf.setText(lcolor+" "+"king has lost,"+" "+wcolor+" "+"player won!");
            tf.setEditable(false);
            JButton b = new JButton("confirm");
            b.setPreferredSize(new Dimension(50,50));
            b.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
                
            });
            c.add(tf,BorderLayout.NORTH);
            c.add(b,BorderLayout.CENTER);
            setVisible(true);
        }
    }
    class checkNotice extends JFrame{
        public checkNotice(String color){
            super("Check!");
            this.setSize(600,600);
            this.setLocation(600, 250);
            Container c = getContentPane();
            JTextField tf = new JTextField();
            tf.setText("The" + " "+ color + " "+"king"+" "+"has been checked");
            tf.setEditable(false);
            JButton b = new JButton("confirm");
            b.setPreferredSize(new Dimension(50,50));
            b.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
                
            });
            c.add(tf,BorderLayout.NORTH);
            c.add(b,BorderLayout.CENTER);
            setVisible(true);
        }
    }
    public static void main(String[] args) {
        PApplet.main("chessGame.chess");
    }
}

abstract class chessPieces {

    public float x, y;
    public boolean is_alive;
    private boolean is_black;
    public boolean is_selected;
    public boolean is_moved;
    public String name;
    protected boolean is_firstStep = true;
    public boolean is_passant = false;
    protected boolean neverMove = true;
    public chessPieces(float x, float y, boolean is_black, String name) {
        this.is_moved = true;
        this.is_black = is_black;
        this.is_alive = true;
        this.x = x;
        this.y = y;
        this.is_selected = false;
        this.name = name;
    }
    
    protected boolean has_blockSlash(chess c, float x, float y) {
        if (c.findPiece(x, y) != null && c.findPiece(x, y).is_black == this.is_black) {
            return true;
        }
        if ((this.x < (int) (x / 100) * 100) && (this.y > (int) (y / 100) * 100 + 100)) {
            float stepx = this.x + 100;
            float stepy = this.y - 100;
            while (stepx < (int) (x / 100) * 100) {
                if (c.findPiece(stepx, stepy) != null) {
                    return true;
                }
                stepx += 100;
                stepy -= 100;
            }
        }
        if ((this.x > (int) (x / 100) * 100 + 100) && (this.y < (int) (y / 100) * 100)) {
            float stepx = this.x - 100;
            float stepy = this.y + 100;
            while (stepx > (int) (x / 100) * 100 + 100) {
                if (c.findPiece(stepx, stepy) != null) {
                    return true;
                }
                stepx -= 100;
                stepy += 100;
            }
        }
        if ((this.x > (int) (x / 100) * 100 + 100) && (this.y > (int) (y / 100) * 100 + 100)) {
            float stepx = this.x - 100;
            float stepy = this.y - 100;
            while (stepx > (int) (x / 100) * 100 + 100) {
                if (c.findPiece(stepx, stepy) != null) {
                    return true;
                }
                stepx -= 100;
                stepy -= 100;
            }
        }
        if ((this.x < (int) (x / 100) * 100) && (this.y < (int) (y / 100) * 100)) {
            float stepx = this.x + 100;
            float stepy = this.y + 100;
            while (stepx < (int) (x / 100) * 100) {
                if (c.findPiece(stepx, stepy) != null) {
                    return true;
                }
                stepx += 100;
                stepy += 100;
            }
        }
        return false;
    }

    protected boolean has_blockHori(chess c, float x, float y) {
        if (this.x > (int) (x / 100) * 100 + 100) {
            float stepx = this.x - 100;
            while (stepx > (int) (x / 100) * 100 + 100) {
                if (c.findPiece(stepx, y) != null) {
                    return true;
                }
                stepx -= 100;
            }
        }
        if (this.x < (int) (x / 100) * 100) {
            float stepx = this.x + 100;
            while (stepx < (int) (x / 100) * 100) {
                if (c.findPiece(stepx, y) != null) {
                    return true;
                }
                stepx += 100;
            }
        }
        return false;
    }

    protected boolean has_blockVerti(chess c, float x, float y) {
        if (this.y > (int) (y / 100) * 100 + 100) {
            float stepy = this.y - 100;
            while (stepy > (int) (y / 100) * 100 + 100) {
                if (c.findPiece(x, stepy) != null) {
                    return true;
                }
                stepy -= 100;
            }
        }
        if (this.y < (int) (y / 100) * 100) {
            float stepy = this.y + 100;
            while (stepy < (int) (y / 100) * 100) {
                if (c.findPiece(x, stepy) != null) {
                    return true;
                }
                stepy += 100;
            }
        }
        return false;
    }

    public boolean get_isBlack() {
        return this.is_black;
    }
    public boolean get_nMove(){
        return neverMove;
    }
    abstract void draw(PApplet p);

    abstract boolean moveLegal(chess c, float mousex, float mousey);
}

class queen extends chessPieces {

    public queen(float x, float y, boolean is_black) {
        super(x, y, is_black, "Queen");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_queen;
            b_queen = p.loadImage("kunkun1.jpg");
            p.imageMode(CENTER);
            p.image(b_queen, this.x, this.y, 70, 95);
        }
        if (!this.get_isBlack() && this.is_alive) {
            PImage w_queen;
            w_queen = p.loadImage("kunkun2.jpg");
            p.imageMode(CENTER);
            p.image(w_queen, this.x, this.y, 70, 95);
        }
    }

    public boolean moveLegal(chess c, float mousex, float mousey) {
        if (mousex > 0 && mousex < 800 && mousey > 0 && mousey < 800) {
            if (Math.abs(mousex - this.x) < 50 && !(has_blockVerti(c, mousex, mousey))) {
                return true;
            }
            if (Math.abs(mousey - this.y) < 50 && !(has_blockHori(c, mousex, mousey))) {
                return true;
            }
            if ((int)((Math.abs(mousex - this.x)-50)/100) == (int)((Math.abs(mousey - this.y)-50)/100) && !(has_blockSlash(c, mousex, mousey))) {
                return true;
        }
        }
        return false;
    }
}

class king extends chessPieces {

    public king(float x, float y, boolean is_black) {
        super(x, y, is_black, "King");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_king;
            b_king = p.loadImage("chuanchuan1.jpg");
            p.imageMode(CENTER);
            p.image(b_king, this.x, this.y, 70, 95);
        }
        if (!(this.get_isBlack()) && this.is_alive) {
            PImage w_king;
            w_king = p.loadImage("chuanchuan2.jpg");
            p.imageMode(CENTER);
            p.image(w_king, this.x, this.y, 70, 95);
        }
    }

    @Override
    boolean moveLegal(chess c, float mousex, float mousey) {
        if (Math.abs(mousex - this.x) < 150 && Math.abs(mousey - this.y) < 150) {
            neverMove = false;
            return true;
        }
        return false;
    }
}

class bishop extends chessPieces {

    public bishop(float x, float y, boolean is_black) {
        super(x, y, is_black, "Bishop");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_bishop;
            b_bishop = p.loadImage("bl_bishop.png");
            p.imageMode(CENTER);
            p.image(b_bishop, this.x, this.y, 50, 95);
        }
        if (!(this.get_isBlack()) && this.is_alive) {
            PImage w_bishop;
            w_bishop = p.loadImage("wh_bishop.png");
            p.imageMode(CENTER);
            p.image(w_bishop, this.x, this.y, 50, 95);
        }
    }

    boolean moveLegal(chess c, float mousex, float mousey) {
        if ((int)((Math.abs(mousex - this.x)-50)/100) == (int)((Math.abs(mousey - this.y)-50)/100) && !(has_blockSlash(c, mousex, mousey))) {
                return true;
        }
        return false;
    }
}

class pawn extends chessPieces {

    public pawn(float x, float y, boolean is_black) {
        super(x, y, is_black, "Pawn");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_pawn;
            b_pawn = p.loadImage("bl_pawn.png");
            p.imageMode(CENTER);
            p.image(b_pawn, this.x, this.y, 50, 95);
        }
        if (!(this.get_isBlack()) && this.is_alive) {
            PImage w_pawn;
            w_pawn = p.loadImage("wh_pawn.png");
            p.imageMode(CENTER);
            p.image(w_pawn, this.x, this.y, 50, 95);
        }
    }

    boolean moveLegal(chess c, float mousex, float mousey) {
        if (mousex > 800 || mousey > 800) {
            return false;
        }
        if (!this.get_isBlack()) {
            if (is_firstStep) {
                if (Math.abs(mousex - this.x) < 50 && this.y > (int) (mousey / 100) * 100 + 100 && this.y - mousey < 250) {
                    is_firstStep = false;
                    if((c.findPiece(mousex - 100, mousey) != null && c.findPiece(mousex - 100, mousey).name == "Pawn" && c.findPiece(mousex - 100, mousey).get_isBlack() != this.get_isBlack()) || (c.findPiece(mousex + 100, mousey) != null && c.findPiece(mousex+100, mousey).name == "Pawn" && c.findPiece(mousex + 100, mousey).get_isBlack() != this.get_isBlack())){
                        is_passant = true;
                    }
                    return true;
                }
            }
            if (Math.abs(mousex - this.x) < 50 && this.y > (int) (mousey / 100) * 100 + 100 && this.y - mousey < 150 && c.findPiece(mousex, mousey) == null) {
                if (c.find_passantPiece() != null) {
                    c.find_passantPiece().is_passant = false;
                }
                return true;
            }
            if (this.y - mousey < 150 && Math.abs(mousex - this.x) > 50 && this.y > (int) (mousey / 100) * 100 + 100 && Math.abs(mousex - this.x) < 150 && this.y - mousey < 150 && c.findPiece(mousex, mousey) != null && c.findPiece(mousex, mousey).get_isBlack() != this.get_isBlack()) {
                if (is_passant) {
                    is_passant = false;
                }
                if(c.find_passantPiece() != null && (Math.abs(mousex - c.find_passantPiece().x) > 50 || Math.abs(mousey - c.find_passantPiece().y) > 50)){
                    c.find_passantPiece().is_passant = false;
                }
                return true;
            }
            if (Math.abs(mousex - this.x) < 150 && Math.abs(mousex - this.x) > 50 && Math.abs(mousey - this.y) < 50 && c.findPiece(mousex, mousey) != null && c.findPiece(mousex, mousey).is_passant) {
                return true;
            }
            return false;
        } else {
            if (is_firstStep) {
                if (Math.abs(mousex - this.x) < 50 && this.y < (int) (mousey / 100) * 100 && mousey - this.y < 250) {
                    is_firstStep = false;
                    if((c.findPiece(mousex - 100, mousey) != null && c.findPiece(mousex - 100, mousey).name == "Pawn" && c.findPiece(mousex - 100, mousey).get_isBlack() != this.get_isBlack()) || (c.findPiece(mousex + 100, mousey) != null && c.findPiece(mousex+100, mousey).name == "Pawn" && c.findPiece(mousex + 100, mousey).get_isBlack() != this.get_isBlack())){
                        is_passant = true;
                    }
                    return true;
                }
            }
            if (Math.abs(mousex - this.x) < 50 && this.y < (int) (mousey / 100) * 100 && mousey - this.y < 150 && c.findPiece(mousex, mousey) == null) {
                if (c.find_passantPiece() != null) {
                    c.find_passantPiece().is_passant = false;
                }
                return true;
            }
            if (mousey - this.y < 150 && Math.abs(mousex - this.x) > 50 && this.y < (int) (mousey / 100) * 100  && Math.abs(mousex - this.x) < 150 && mousey - this.y < 150 && c.findPiece(mousex, mousey) != null && c.findPiece(mousex, mousey).get_isBlack() != this.get_isBlack()) {
                if (is_passant) {
                    is_passant = false;
                }
                if(c.find_passantPiece() != null && (Math.abs(mousex - c.find_passantPiece().x) > 50 || Math.abs(mousey - c.find_passantPiece().y) > 50)){
                    c.find_passantPiece().is_passant = false;
                }
                return true;
            }
            if (Math.abs(mousex - this.x) < 150 && Math.abs(mousex - this.x) > 50 && Math.abs(mousey - this.y) < 50 && c.findPiece(mousex, mousey) != null && c.findPiece(mousex, mousey).is_passant) {
                is_passant = false;
                return true;
            }
            return false;
        }
    }
}

class knight extends chessPieces {

    public knight(float x, float y, boolean is_black) {
        super(x, y, is_black, "Knight");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_knight;
            b_knight = p.loadImage("bl_knight.png");
            p.imageMode(CENTER);
            p.image(b_knight, this.x, this.y, 50, 95);
        }
        if (!(this.get_isBlack()) && this.is_alive) {
            PImage w_knight;
            w_knight = p.loadImage("wh_knight.png");
            p.imageMode(CENTER);
            p.image(w_knight, this.x, this.y, 50, 95);
        }
    }

    boolean moveLegal(chess c, float mousex, float mousey) {
        if (mousex > 800 || mousey > 800) {
            return false;
        }
        if ((Math.abs(mousey - this.y) > 150 && Math.abs(mousey - this.y) < 250 && Math.abs(mousex - this.x) > 50 && Math.abs(mousex - this.x) < 150) || (Math.abs(mousey - this.y) > 50 && Math.abs(mousey - this.y) < 150 && Math.abs(mousex - this.x) > 150 && Math.abs(mousex - this.x) < 250)) {
            return true;
        }
        return false;
    }
}

class rook extends chessPieces {

    public rook(float x, float y, boolean is_black) {
        super(x, y, is_black, "Rook");
    }

    @Override
    public void draw(PApplet p) {
        if (this.get_isBlack() && this.is_alive) {
            PImage b_rook;
            b_rook = p.loadImage("bl_rook.png");
            p.imageMode(CENTER);
            p.image(b_rook, this.x, this.y, 50, 95);
        }
        if (!(this.get_isBlack()) && this.is_alive) {
            PImage w_rook;
            w_rook = p.loadImage("wh_rook.png");
            p.imageMode(CENTER);
            p.image(w_rook, this.x, this.y, 50, 95);
        }
    }

    boolean moveLegal(chess c, float mousex, float mousey) {
        if (Math.abs(mousex - this.x) < 50 && !(has_blockVerti(c, mousex, mousey))) {
            neverMove = false;
            return true;
        }
        if (Math.abs(mousey - this.y) < 50 && !(has_blockHori(c, mousex, mousey))) {
            neverMove = false;
            return true;
        }
        return false;
    }
}


