/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package syntech;


import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;


/**
 *
 * @author ccs
 */
public class Home extends javax.swing.JFrame {

    /**
     * Creates new form Home
     */

    String source = "fc\\FaceDetect2\\xml\\haarcascade_frontalface_alt.xml"; //paste here
    String source2 = "fc\\FaceDetect2\\xml\\haarcascade_mcs_mouth.xml";
  //  String source = "C:\\Users\\ccs\\Desktop\\trafic\\vehicle_detection_haarcascades-master\\cars.xml";
    CascadeClassifier faceDetector = new CascadeClassifier(source);
    CascadeClassifier mouthDetector = new CascadeClassifier(source2);


    public Home() {
        initComponents();
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        lblnumber = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(153, 255, 255));

        jButton1.setBackground(new java.awt.Color(204, 255, 204));
        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
               jButton1ActionPerformed(evt);
              ;//  MouthDetection mouthDetection = new MouthDetection(evt);
            }
        });

        lblnumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblnumber.setText("No of face");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 289, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE)
                        .addComponent(lblnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed


        (new Thread(){
            public void run(){
                VideoCapture capture = new VideoCapture(0);//0 mean your default web cam
          //    VideoCapture capture = new VideoCapture("C:\\Users\\KURAPIKA.dll\\Desktop\\videoplayback.mp4");
                MatOfRect rostros = new MatOfRect();
                MatOfByte mem = new MatOfByte();

                Mat frame = new Mat();
                Mat frame_gray = new Mat();
                Scalar col4 = new Scalar(178, 138, 17);


                //MatOfRect ros = new MatOfRect();


                Rect[] facesArray;


                Graphics g;
                BufferedImage buff = null;
                CascadeClassifier mouthCascade = new CascadeClassifier();
                if(!mouthCascade.load("fc\\FaceDetect2\\xml\\Mouth.xml")){
                    System.out.println("could not load mouth.xml");
                    return;
                }


                while(capture.read(frame)){
                    if(frame.empty()){
                        System.out.println("No detection");
                        break;
                    }else{
                        try {
                            g = jPanel1.getGraphics();
                            Imgproc.cvtColor(frame, frame_gray, Imgproc.COLOR_BGR2GRAY);
                            Imgproc.equalizeHist(frame_gray, frame_gray);
                            double w = frame.width();
                            double h = frame.height();
                            faceDetector.detectMultiScale(frame_gray, rostros, 1.1, 2,CASCADE_SCALE_IMAGE, new Size(30, 30), new Size(w, h) );
                            facesArray = rostros.toArray();

                            System.out.println("number Of Faces detected: "+facesArray.length);

                            for (int i = 0; i < facesArray.length; i++) {


                                Point center = new Point((facesArray[i].x + facesArray[i].width * 0.5),
                                        (facesArray[i].y + facesArray[i].height * 0.5));
                                Imgproc.ellipse(frame,
                                        center,
                                        new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5),
                                        0,
                                        0,
                                        360,
                                        new Scalar(255, 0, 255), 4, 8, 0);

                                Mat faceROI = frame_gray.submat(facesArray[i]);
                                Imgproc.rectangle(frame,
                                        new Point(facesArray[i].x,facesArray[i].y),
                                        new Point(facesArray[i].x+facesArray[i].width,facesArray[i].y+facesArray[i].height),
                                        new Scalar(123, 213, 23, 220));
                              //  Imgproc.putText(frame, "Width: "+faceROI.width()+" Height: "+faceROI.height()+" X = "+facesArray[i].x+
                               //         " Y = "+facesArray[i].y, new Point(facesArray[i].x, facesArray[i].y-20), 1, 1, new Scalar(255,255,255));
                                Imgproc.putText(frame, "This is human   ", new Point(facesArray[i].x, facesArray[i].y-20), Core.FONT_HERSHEY_TRIPLEX, 1, new Scalar(255,255,255));
                                //MOUTH DETECTION CODE STARTS HERE

                            }
                            MatOfRect mouth = new MatOfRect();
                            double v=mouth.width();
                            double c=mouth.height();
//                            mouthCascade.detectMultiScale(face,mouth,1.5,5,CASCADE_SCALE_IMAGE,new Size(10, 10), new Size(v, c));
//                            Rect[] mouthRects = mouth.toArray();
//
//                            if(mouthRects.length>=1 && faceRects.length==1) {
//                                for (Rect m : mouthRects) {
//                                    //209, 195, 0
//                                    Point tl = new Point(r.tl().x + m.tl().x, r.tl().y + m.tl().y);
//                                    Point br = new Point(r.tl().x + m.br().x, r.tl().y + m.br().y);
//
//                                    //  Imgproc.rectangle(frame, tl, br, col4, 2);
//                                    //  Imgproc.putText(frame,"Mouth",new Point(tl.x, br.y),Core.FONT_HERSHEY_TRIPLEX,0.5,new Scalar(255, 255, 255),1);
//                                    // Imgproc.line(frame, br, new Point(frame.width() - 155, br.y), col4, 2);
//                                    // Imgproc.putText(frame, "wear your face mask", new Point(frame.width() - 150, br.y), Core.FONT_HERSHEY_TRIPLEX, 0.5, new Scalar(255, 255, 255), 1);
//                                    // Imgproc.putText(frame," face mask detected" ,new Point(r.tl().x+5,r.br().y+18),Core.FONT_HERSHEY_TRIPLEX,fontScale,new Scalar(255, 255, 255),1);
//                                }
//                            }
//                        else if(mouthRects.length<1 && faceRects.length==1){
//                            Imgproc.putText(frame,"wear your face mask",new Point(r.tl().x+5,r.br().y+18),Core.FONT_HERSHEY_TRIPLEX,fontScale,new Scalar(255, 255, 255),1);
//
//
//                        }

                            int no= facesArray.length;

                            lblnumber.setText(String.valueOf(no));

                             Imgcodecs.imencode(".bmp", frame, mem);
                            BufferedImage im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
                            buff = im;
                            g.drawImage(buff, 0, 0, jPanel1.getWidth(), jPanel1.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(), null);







                        } catch (Exception ex) {

                        }
                    }

                }
            }

            public void main(String[] args) {
                System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            };


        }).start();


    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblnumber;
    // End of variables declaration//GEN-END:variables
}
