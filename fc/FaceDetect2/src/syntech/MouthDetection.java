package syntech;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import static org.opencv.objdetect.Objdetect.CASCADE_SCALE_IMAGE;



public class MouthDetection {

        static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}


    public static void main(String[] args) {

            // Load both FACE & MOUTH Classifier
            CascadeClassifier faceCascade = new CascadeClassifier();
            CascadeClassifier mouthCascade = new CascadeClassifier();

            // CHECK IF THE CLASSIFIER IS WELL LOADED
            if(!faceCascade.load("fc\\FaceDetect2\\xml\\face.xml")){
                System.out.println("could not load haarcascade_frontalface_alt.xml");
                return;
            }
            if(!mouthCascade.load("fc\\FaceDetect2\\xml\\Mouth.xml")){
                System.out.println("could not load mouth.xml");
                return;
            }

            //Class for video capturing from video files, image sequences or cameras
            // The number 0  is the ID of your device's camera
            VideoCapture videoCapture = new VideoCapture(0);

            // Create an empty matrix  to store image data of grayscale or color images and histograms
            Mat frame = new Mat();

            //Usage of Scalar to specify an RGB Color
            Scalar green = new Scalar(0, 128, 0);
            Scalar red = new Scalar(0, 0, 255);

            // Camera exception error
            if(!videoCapture.isOpened()){
                System.out.println("No cam found");
                return;
            }

            //star reading the frames
            while (true) {
                //read each frame and stock it into Frame matrix
                videoCapture.read(frame);

                // create Gray matrix for storing grayScale frame
                Mat gray = new Mat();


                // Once we’ve loaded the classifiers we are ready to start the detection;
                // First of all we need to convert the frame in grayscale and equalize the
                // histogram to improve the results
                Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
                Imgproc.equalizeHist(gray, gray);

                //matrix That will containing our large objects in our case FACES
                MatOfRect faces = new MatOfRect();

                // get the width and height for each frame
                double w = frame.width();
                double h = frame.height();

//                The detectMultiScale function detects objects of different sizes in the input image.
//                The detected objects are returned as a list of rectangles. The parameters are:
//
//               ## image Matrix of the type CV_8U containing an image where objects are detected.
//               ## objects Vector of rectangles where each rectangle contains the detected object.
//               ## scaleFactor Parameter specifying how much the image size is reduced at each image scale.
//               ## minNeighbors Parameter specifying how many neighbors each candidate rectangle should have to retain it.
//               ## flags Parameter with the same meaning for an old cascade as in the function cvHaarDetectObjects. It is not used for a new cascade.
//               ## minSize Minimum possible object size. Objects smaller than that are ignored.
//               ## maxSize Maximum possible object size. Objects larger than that are ignored.
//               ---- So the result of the detection is going to be in the objects parameter or in our case faces.-----

                faceCascade.detectMultiScale(gray, faces, 1.1, 2,CASCADE_SCALE_IMAGE,new Size(30, 30), new Size(w, h));



                //  put this result in an array of rects and draw them on the frame
                Rect[] faceRects = faces.toArray();

                // do the same on each on each detected frames
                if(faceRects.length>0) {
                    for (Rect r : faceRects) {

                        // startingPoint an endingPoint of the rectangle
                        Point startingPoint = new Point(r.tl().x-1,r.br().y);
                        Point endingPoint = new Point(r.br().x,r.br().y+25);


                        // font size depending on the rectangle
                        double fontScale = (r.br().x/r.tl().x)/4;
                        Mat face = new Mat(frame,r);


                        //MOUTH DETECTION CODE STARTS HERE
                        // same as face detection above
                        MatOfRect mouth = new MatOfRect();
                        double v=mouth.width();
                        double c=mouth.height();
                        mouthCascade.detectMultiScale(face,mouth,1.9,2,CASCADE_SCALE_IMAGE,new Size(10, 10), new Size(v, c));
                        Rect[] mouthRects = mouth.toArray();

                        if(mouthRects.length>=1 && faceRects.length==1) {
                            for (Rect m : mouthRects) {
                                //209, 195, 0
                                Point tl = new Point(r.tl().x + m.tl().x, r.tl().y + m.tl().y);
                                Point br = new Point(r.tl().x + m.br().x, r.tl().y + m.br().y);
                            }
                        }

                        //MOUTH DETECTION CODE ENDS HERE


                        //  Detection of the Mask
                        if(mouthRects.length <1 ){
                            Imgproc.rectangle(frame,r.tl(),r.br(),green,3);
                            Imgproc.rectangle(frame,startingPoint,endingPoint,green,-1);
                             Imgproc.putText(frame,"Thank YOU ! ",new Point(r.tl().x+5,r.br().y+18),Core.FONT_HERSHEY_TRIPLEX,fontScale,new Scalar(255, 255, 255),1);
                        }
                        else if(mouthRects.length >1  ){
                            Imgproc.rectangle(frame,r.tl(),r.br(),red,3);
                            Imgproc.rectangle(frame,startingPoint,endingPoint,red,-1);

                            Imgproc.putText(frame,"Wear Your FaceMask !",new Point(r.tl().x+5,r.br().y+18),Core.FONT_HERSHEY_TRIPLEX,fontScale,new Scalar(255, 255, 255),1);


                        }
                    }
                }
                HighGui.imshow("Feature Detection",frame);
                if (HighGui.waitKey(30)>=0) break;
            }
        }
    }

