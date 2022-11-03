package pingpong_v1;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author Nguyen Duong Phu Trong CE160324
 */
public class PingPong extends javax.swing.JFrame {
    public static final int _ScenceWidth = 800;
    public static final int _ScenceHeight = 400;
    public static final int _RacketWidth = 6;
    public static final int _RacketHeight = 40;
    
    public static final int _BallSize = 20;
    public static final int _BallRadius = _BallSize/2;
    
    public static final int _ScenceWidth2 = _ScenceWidth/2;
    public static final int _ScenceHeight2 =_ScenceHeight/2 ;
    public static final int _RacketWidth2 = _RacketWidth/2;
    public static final int _RacketHeight2 = _RacketHeight/2;
    
    public static boolean checkEnd = false;
    
    int redPoint , bluePoint;
    
    int xRed,yRed;
    boolean rUp,rDown,rRight,rLeft;
    JLabel red;
    
    int xBlue,yBlue;
    boolean bUp,bDown,bRight,bLeft;
    JLabel blue;
    
    int speedRacket;
    int speedBall;
    
    int time;
    Thread timer;
    Thread game;
    
    int xBall,yBall,dxBall,dyBall;
    JLabel ball;
    /**
     * Creates new form PingPong
     */
    public  ImageIcon resizeicon(String imgpath, int width , int height){
        return new ImageIcon(new ImageIcon(getClass().getResource(imgpath)).getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
    }
    public void init(){
     redPoint = bluePoint = 0;    
    
    rUp=rDown=rRight=rLeft=false;
    bUp=bDown=bRight=bLeft=false;
    
    speedRacket=2;
    speedBall=3;
    
    time=0;
    
    xRed=10;
    yRed=_ScenceHeight2-_RacketHeight2;    
    
    xBlue=_ScenceWidth-_RacketWidth-10;
    yBlue=_ScenceHeight2-_RacketHeight2;   
    
    xBall=_ScenceWidth2-_BallRadius;
    yBall=_ScenceHeight2-_BallRadius;
    dxBall= Randomizer.Random(0,1)==0?-1:1;
    dyBall=Randomizer.Random(0,1)==0?-1:1;

    }
    public void reset(){
     redPoint =bluePoint=0;    
    
    rUp=rDown=rRight=rLeft=false;
    bUp=bDown=bRight=bLeft=false;
    
    speedRacket=2;
    speedBall=3;
    
    xBall=_ScenceWidth2-_BallRadius;
    yBall=_ScenceHeight2-_BallRadius;
    
    dxBall= Randomizer.Random(0,1)==0?-1:1;
    dyBall=Randomizer.Random(0,1)==0?-1:1;

    }
    public void initScence(){
        lblRedLabel.setIcon(resizeicon("/img/playerRed.png", 30, 30));
        lblRedLabel.setText("");
        lblBlueLabel.setIcon(resizeicon("/img/playerBlue.png", 30, 30));
        lblBlueLabel.setText("");
        
        pnlScence.setLayout(null);
        pnlScence.removeAll();
        pnlScence.revalidate();
        pnlScence.repaint();
        pnlScence.setSize(_ScenceWidth, _ScenceHeight);
        
        red= new JLabel();
        red.setOpaque(true);
        red.setBackground(Color.RED);
        red.setBounds(xRed,yRed,_RacketWidth,_RacketHeight);
        
        blue= new JLabel();
        blue.setOpaque(true);
        blue.setBackground(Color.BLUE);
        blue.setBounds(xBlue,yBlue,_RacketWidth,_RacketHeight);
        
        ball= new JLabel();
        ball.setIcon(resizeicon("/img/ball.png",_BallSize , _BallSize));
        ball.setBounds(xBall,yBall,_BallSize , _BallSize);
        
        pnlScence.add(red);
        pnlScence.add(blue);
        pnlScence.add(ball);
    }
    
    public void runTimer(){
        timer = new Thread(){
            public void run(){
                Thread.currentThread().setPriority(MIN_PRIORITY);
                while(true){
                    try {
                    ++time;
                    lblTime.setText(String.format("%02d:%02d:%02d",time/3600,time/60%60,time%60));
                    Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PingPong.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        timer.start();
    }
    public void gameOver(){
        if(xBall<=0){
            bluePoint++;
            lblMessage.setText("The BLUE player is the winner. Please press SPACEPAR top replay!");
            lblMessage.setForeground(Color.blue);
            checkEnd=true;
        }else if(xBall>=_ScenceWidth-_BallSize){
            redPoint++;
            lblMessage.setText("The RED player is the winner. Please press SPACEPAR top replay!");
            lblMessage.setForeground(Color.red);
            checkEnd=true;
        }
        lblRedpoint.setText(redPoint+"");
        lblBluePoint.setText(bluePoint+"");
        timer.stop();
        game.stop();
    }
    public boolean isGameOver(){
      return xBall<=0|| xBall>=_ScenceWidth-_BallSize;
    }
    
    public boolean isHitRedRacket(){
        int bX = xBall+_BallRadius;
        int bY = yBall +_BallRadius;
        int dX=xBall-(xRed+_RacketWidth);
        if(0<=dX&&dX<=_BallRadius)
            if(yRed-_BallRadius<=bY&&bY<=yRed+_RacketHeight+_BallRadius)
                return true;
            return false;        
    }
    public boolean isHitBlueRacket(){
        int bX = xBall+_BallRadius;
        int bY = yBall +_BallRadius;
        
        int dX=xBlue-(bX+_BallRadius);
        if(0<=dX&&dX<=_BallRadius)
            if(yBlue-_BallRadius<=bY&&bY<=yBlue+_RacketHeight+_BallRadius)
                return true;
            return false;        
    }
    public void gamePlaying(){
        game = new Thread(){
            public void run(){
                while(true){
                    try {
                        //hien thi vi tri cua cay vot do
                        if(rLeft && xRed > 0) xRed -= speedRacket;
                        if(rRight) xRed += speedRacket;
                        if(rUp && yRed > 0) yRed -= speedRacket;
                        if(rDown && yRed < 358) yRed += speedRacket;
                        red.setBounds(xRed,yRed,_RacketWidth,_RacketHeight);
                        
                        //hien thi vi tri cua cay vot xanh
                        if(bLeft) xBlue -= speedRacket;
                        if(bRight && xBlue < _ScenceWidth) xBlue += speedRacket;
                        if(bUp && yBlue > 0) yBlue -= speedRacket;
                        if(bDown && yBlue < 358) yBlue += speedRacket;
                        blue.setBounds(xBlue,yBlue,_RacketWidth,_RacketHeight);
                        
                        //hien thi vi tri cua trai banh
                        xBall+=dxBall*speedBall;
                        if(isGameOver())
                            gameOver();
                        else if(isHitBlueRacket()||isHitRedRacket()){
                            dxBall=-dxBall;
                        }

                        yBall+=dyBall*speedBall;
                         if(yBall<=0){
                            yBall=0;
                            dyBall=-dyBall;
                        }
                        if(yBall>=_ScenceHeight-_BallSize){
                            yBall=_ScenceHeight-_BallSize;
                            dyBall=-dyBall;
                        }
                        ball.setBounds(xBall,yBall,_BallSize , _BallSize);
                        
                        Thread.sleep(10);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PingPong.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
            }
        };
        game.start();
    }
    
    public void replay(){
        reset();
        initScence();
        runTimer();
        gamePlaying();
        checkEnd=false;
    }
    
    public PingPong() {
        initComponents();
        //canh giữaframe trên màn hình
        this.setLocationRelativeTo(null);
        //thiết đặt ảnh cho người chơi
        init();
        initScence();
        runTimer();
        gamePlaying();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pnlGameInfor = new javax.swing.JPanel();
        lblRedLabel = new javax.swing.JLabel();
        lblRedpoint = new javax.swing.JLabel();
        lblBlueLabel = new javax.swing.JLabel();
        lblBluePoint = new javax.swing.JLabel();
        lblTimeLabel = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        pnlScence = new javax.swing.JPanel();

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Super PingPong");
        setName("frmPingPong"); // NOI18N
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        pnlGameInfor.setBorder(javax.swing.BorderFactory.createTitledBorder("Game Information"));

        lblRedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/playerRed.png"))); // NOI18N

        lblRedpoint.setText("0");

        lblBlueLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/playerBlue.png"))); // NOI18N

        lblBluePoint.setText("0");

        lblTimeLabel.setText("Time: ");

        lblTime.setText("00:00:00");

        lblMessage.setForeground(new java.awt.Color(255, 153, 51));

        javax.swing.GroupLayout pnlGameInforLayout = new javax.swing.GroupLayout(pnlGameInfor);
        pnlGameInfor.setLayout(pnlGameInforLayout);
        pnlGameInforLayout.setHorizontalGroup(
            pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGameInforLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRedpoint)
                .addGap(97, 97, 97)
                .addComponent(lblBlueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBluePoint, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGameInforLayout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addComponent(lblMessage))
                    .addGroup(pnlGameInforLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(lblTimeLabel)
                        .addGap(18, 18, 18)
                        .addComponent(lblTime)))
                .addContainerGap())
        );
        pnlGameInforLayout.setVerticalGroup(
            pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGameInforLayout.createSequentialGroup()
                .addGroup(pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlGameInforLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblRedpoint)
                            .addComponent(lblRedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlGameInforLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblBlueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMessage)))
                    .addGroup(pnlGameInforLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlGameInforLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblBluePoint, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTimeLabel)
                            .addComponent(lblTime))))
                .addContainerGap())
        );

        pnlScence.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlScence.setForeground(new java.awt.Color(51, 255, 51));
        pnlScence.setPreferredSize(new java.awt.Dimension(800, 400));

        javax.swing.GroupLayout pnlScenceLayout = new javax.swing.GroupLayout(pnlScence);
        pnlScence.setLayout(pnlScenceLayout);
        pnlScenceLayout.setHorizontalGroup(
            pnlScenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 796, Short.MAX_VALUE)
        );
        pnlScenceLayout.setVerticalGroup(
            pnlScenceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 396, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(pnlScence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(pnlGameInfor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlGameInfor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlScence, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        // TODO add your handling code here:
        int key= evt.getKeyCode();
        if(key==KeyEvent.VK_A) rLeft=true;
        if(key==KeyEvent.VK_D) rRight=true;
        if(key==KeyEvent.VK_W) rUp=true;
        if(key==KeyEvent.VK_S) rDown=true;
        
        if(key==KeyEvent.VK_LEFT) bLeft=true;
        if(key==KeyEvent.VK_RIGHT)bRight=true;
        if(key==KeyEvent.VK_UP)   bUp=true;
        if(key==KeyEvent.VK_DOWN) bDown=true;
        
        if(key==KeyEvent.VK_SPACE && checkEnd){
            replay();
        }
        
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        // TODO add your handling code here:
        int key= evt.getKeyCode();
        if(key==KeyEvent.VK_A) rLeft=false;
        if(key==KeyEvent.VK_D) rRight=false;
        if(key==KeyEvent.VK_W) rUp=false;
        if(key==KeyEvent.VK_S) rDown=false;
        
        if(key==KeyEvent.VK_LEFT) bLeft=false;
        if(key==KeyEvent.VK_RIGHT)bRight=false;
        if(key==KeyEvent.VK_UP)   bUp=false;
        if(key==KeyEvent.VK_DOWN) bDown=false;
        
        
    }//GEN-LAST:event_formKeyReleased

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PingPong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PingPong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PingPong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PingPong.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PingPong().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblBlueLabel;
    private javax.swing.JLabel lblBluePoint;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblRedLabel;
    private javax.swing.JLabel lblRedpoint;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTimeLabel;
    private javax.swing.JPanel pnlGameInfor;
    private javax.swing.JPanel pnlScence;
    // End of variables declaration//GEN-END:variables
}
