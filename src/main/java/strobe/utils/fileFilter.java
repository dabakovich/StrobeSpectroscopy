package strobe.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class fileFilter extends FileFilter{
    
    private String extension;
    
    public fileFilter(String extension){
        this.extension = extension;
    }
    
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().contains(extension);
    }

    @Override
    public String getDescription() {
        return "(*."+extension+")";
    }

}
