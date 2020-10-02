package CA1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MenuController implements Initializable {
    //INSTANCES
    //MenuBar
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu file;
    @FXML
    private Menu help;
    @FXML
    private Menu edit;
    @FXML
    private MenuItem openImage;

    //Image
    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private Image image1, image2;
    WritableImage triColor;

    //Buttons
    @FXML
    private Button triColorButton, unionFindButton, whiteBloodCells, redBloodCells;

    //Slider
    @FXML
    private Slider hueSlider, saturationSlider, brightnessSlider;
    private double adjHue, adjSaturation, adjBrightness;

    //Arrays
    private int[] redData;
    private int[] purpleData;
    private int[] rootAccessData;

    //HashSet
    HashSet<Integer> redRoots;

    //Texts
    @FXML
    Text whiteCount;
    @FXML
    Text redCount;


    //METHODS
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // listener: get new target color from sliders and apply it
        ChangeListener<Number> listener = new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateColor();
            }

        };

        hueSlider.valueProperty().addListener(listener);
        saturationSlider.valueProperty().addListener(listener);
        brightnessSlider.valueProperty().addListener(listener);
    }


    //Show Number Of Red Blood Cells
    public void showRedBloodCells(ActionEvent e) {
        //set up the red data array
        //height width
        int width = (int) image1.getWidth();
        int height = (int) image1.getHeight();

        redData = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                redData[(y * width) + x] = (y * width) + x;
            }
        }

        //get the red cells
        PixelReader pixelReader = triColor.getPixelReader();

        //goes though the pixels in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixelReader.getColor(x, y).equals(Color.PURPLE) || pixelReader.getColor(x, y).equals(Color.WHITE)) {//if the pixel is purple or white -> data for that pixel is -1
                    redData[(y * width) + x] = -1;
                }

                //identify the red blood cells
                if (((y * width + x + 1) % width != 0) && (DisjointSetNode.find(redData, y * width + x) != -1) && (DisjointSetNode.find(redData, y * width + x + 1) != -1)) {
                    DisjointSetNode.union(redData, y * width + x, y * width + x + 1);
                }

                if (((y * width + x + width < redData.length) && (DisjointSetNode.find(redData, y * width + x) != -1) && DisjointSetNode.find(redData, y * width + x + width) != -1)) {
                    DisjointSetNode.union(redData, y * width + x, y * width + x + width);
                }
            }
        }

        //root access
        rootAccessArray();

        //display the red blood cells
        for (int i = 0; i < redData.length; i++) {
            if (i % (int) image1.getWidth() == 0)
                System.out.print(DisjointSetNode.find(redData, i) + " ");
        }

        HashSet<Integer> redRoots = new HashSet<>();
        for (int id = 0; id < redData.length; id++) {
            if (redData[id] != -1 && rootAccessData[DisjointSetNode.find(redData, id)] > 150) {
                redRoots.add(DisjointSetNode.find(redData, id));
            }
        }

        redCount.setText(String.valueOf(redRoots.size()));
    }

    //Show Number Of White Blood Cells
    public void showWhiteBloodCells(ActionEvent e) {
        //set up purple data array

        //height width
        int width = (int) image1.getWidth();
        int height = (int) image1.getHeight();

        purpleData = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                purpleData[(y * width) + x] = (y * width) + x;
            }
        }

        //get white blood cells
        PixelReader pixelReader = triColor.getPixelReader();
        //goes though the pixels in the image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixelReader.getColor(x, y).equals(Color.WHITE) || pixelReader.getColor(x, y).equals(Color.RED)) {//if the pixel is white or red -> data for that pixel is -1
                    purpleData[(y * width) + x] = -1;
                }

                //identify white blood cells
                if (((y * width + x + 1) % width != 0) && (DisjointSetNode.find(purpleData, y * width + x) != -1) && (DisjointSetNode.find(purpleData, y * width + x + 1) != -1)) {
                    DisjointSetNode.union(purpleData, y * width + x, y * width + x + 1);
                }

                if (((y * width + x + width < purpleData.length) && (DisjointSetNode.find(purpleData, y * width + x) != -1) && DisjointSetNode.find(purpleData, y * width + x + width) != -1)) {
                    DisjointSetNode.union(purpleData, y * width + x, y * width + x + width);
                }
            }
        }

        //root access
        rootAccessArray();

        //display purple grid
        for (int i = 0; i < purpleData.length; i++) {
            if (i % (int) image1.getWidth() == 0)
                System.out.println(DisjointSetNode.find(purpleData, i) + "");

        }

        HashSet<Integer> whiteRoots = new HashSet<>();
        for (int id = 0; id < purpleData.length; id++) {
            if (purpleData[id] != -1 && rootAccessData[DisjointSetNode.find(purpleData, id)] > 150) {
                whiteRoots.add(DisjointSetNode.find(purpleData, id));
            }
        }

        whiteCount.setText(String.valueOf(whiteRoots.size()));
    }


    //Update Color
    public void updateColor() {
        //get height and width
        int height = (int) image1.getHeight();
        int width = (int) image1.getWidth();

        //get pixel reader, writable image and pixel writer
        PixelReader pixelReader = image2.getPixelReader();
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        //initialize saturation, hue, and brightness
        adjSaturation = saturationSlider.valueProperty().doubleValue();
        adjBrightness = brightnessSlider.valueProperty().doubleValue();
        adjHue = hueSlider.valueProperty().doubleValue();

        //go through image
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color = pixelReader.getColor(x, y);
                pixelWriter.setColor(x, y, color);

                double hue = color.getHue() + adjHue;
                if (hue > 360.0) {
                    hue = hue - 360;
                } else if (hue < 0.0) {
                    hue = hue + 360.0;
                }

                double saturation = color.getSaturation() + adjSaturation;
                if (saturation > 1.0) {
                    saturation = 1.0;
                } else if (saturation < 0.0) {
                    saturation = 0.0;
                }

                double brightness = color.getBrightness() + adjBrightness;
                if (brightness > 1.0) {
                    brightness = 1.0;
                } else if (brightness < 0.0) {
                    brightness = 0.0;
                }

                double opacity = color.getOpacity();

                Color newColor = Color.hsb(hue, saturation, brightness, opacity);
                pixelWriter.setColor(x, y, newColor);

            }

        }

        //set the new image
        imageView2.setImage(writableImage);

    }

    //Open Image
    @FXML
    private void openTheImage(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();

        //set extension filters
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));


        //show open file dialog
        File file = fileChooser.showOpenDialog(null);
        try {
            //creates a buffered image from chosen image
            BufferedImage bufferedImage = ImageIO.read(file);

            //
            image1 = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView1.setImage(image1);
        } catch (IOException ioe) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ioe);
        }

        //make the image resizeable
        imageView1.setPreserveRatio(true);
        imageView1.setSmooth(true);
        imageView1.setCache(true);

    }


    public void turnToTriColor() {
        //pixel reader
        PixelReader pixelReader = image1.getPixelReader();

        //height width
        int width = (int) image1.getWidth();
        int height = (int) image1.getHeight();

        //initialize data array
        redData = new int[width * height];
        purpleData = new int[width * height];


        WritableImage triColorImage = new WritableImage(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //initialize hue and saturation
                double saturation = pixelReader.getColor(x, y).getSaturation();
                double hue = pixelReader.getColor(x, y).getHue();

                //set colors to red
                if (hue > 300 && hue < 340) {
                    triColorImage.getPixelWriter().setColor(x, y, Color.RED);
                }

                //purple colors
                else if (hue > 260 && hue < 300 && (saturation > 0.60 && saturation < 0.90)) {
                    triColorImage.getPixelWriter().setColor(x, y, Color.PURPLE);
                } else {
                    triColorImage.getPixelWriter().setColor(x, y, Color.WHITE);
                }
            }
        }
        //initialize red array
        for (int i = 0; i < redData.length; i++) {
            redData[i] = i;
        }

        //initialize purple array
        for (int i = 0; i < purpleData.length; i++) {
            purpleData[i] = i;
        }

        imageView2.setImage(triColorImage);
        triColor = triColorImage;
        image2 = triColorImage;
    }


    public void rootAccessArray() {
        //height width
        int width = (int) image1.getWidth();
        int height = (int) image1.getHeight();

        rootAccessData = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (DisjointSetNode.find(redData, (y * width + x)) != -1) {
                    rootAccessData[DisjointSetNode.find(redData, (y * width + x))]++;
                } else if (DisjointSetNode.find(purpleData, (y * width + x)) != -1) {
                    rootAccessData[DisjointSetNode.find(purpleData, (y * width + x))]++;
                }
            }
        }


        for (int i = 0; i < rootAccessData.length; i++) {
            if (i % (int) image1.getWidth() == 0)
                System.out.println(rootAccessData[i] + "");
        }
    }

//    public void unionFindBySize()
//    {
//        DisjointSetNode<Integer> a=new DisjointSetNode<>(8);
//        c.parent=a;
//    }
//
//
//    public static void unionBySize(DisjointSetNode<?> p, DisjointSetNode<?> q) {
//        DisjointSetNode<?> rootp=find(p);
//        DisjointSetNode<?> rootq=find(q);
//        DisjointSetNode<?> biggerRoot=rootp.size>=rootq.size ? rootp : rootq;
//        DisjointSetNode<?> smallerRoot=biggerRoot==rootp ? rootq : rootp;
//        smallerRoot.parent=biggerRoot;
//        biggerRoot.size+=smallerRoot.size;
//    }
//
//    public static void unionBySize(int[] a, int p, int q) {
//        int rootp=find(a,p);
//        int rootq=find(a,q);
//        int biggerRoot=a[rootp]<a[rootq] ? rootp : rootq;
//        int smallerRoot=biggerRoot==rootp ? rootq : rootp;
//        int smallSize=a[smallerRoot];
//        a[smallerRoot]=biggerRoot;
//        a[biggerRoot]+=smallSize; //Value of merged root recalculated as the (negative)
//        //total number of elements in the merged set
//    }
//
//    //find
//    public static int find(int[] a, int id) {
//        while(a[id] >=0 ) id=a[id];
//        return id;
//    }


}
