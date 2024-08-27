package assignment;


import java.awt.event.*;
import java.util.*;

import assignment.Piece.PieceType;

public class JBrainTetris extends JTetris {
    private AIBrain brain; 
    private javax.swing.Timer timer2; 
    public JBrainTetris(){

        super();
        brain = new AIBrain();
        
        timer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e){
            }
        });

        timer2 = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                tick(Board.Action.NOTHING);
            }
        });

    }

        public static void main(String []args){
            createGUI(new JBrainTetris());
        }


        @Override
        public void tick(Board.Action verb){
            if(!gameOn){
                return; 
            }
            
            if(verb != Board.Action.DOWN){
                verb = brain.nextMove(board);
            }
            super.tick(verb);
        }
    
        @Override
        public void startGame(){
            super.startGame();
            timer2.start();
        }

        @Override 
        public void stopGame(){
            super.stopGame();
            timer2.stop();
        }
    }









