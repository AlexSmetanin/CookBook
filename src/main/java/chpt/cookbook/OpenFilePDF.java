package chpt.cookbook;
import java.awt.*;
import java.io.File;

public class OpenFilePDF {
        public static void openFile(String filename) {
            System.setProperty("java.awt.headless", "false");
            try {
                File file = new File(filename);
                if (file.exists()) {
                    Desktop.getDesktop().open(file);
                } else {
                    System.out.println("File is not exists");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
}