package strobe.utils;

import org.jfree.data.xy.XYSeries;
import strobe.data.Data;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DataUtils {
    
    public DataUtils(){}
    
    public static void readData(XYSeries dataGraphList, String filePathName){
        BufferedReader br;
        
        try {
            br = new BufferedReader(new FileReader(filePathName));
            
            String str;
            
            while((str = br.readLine())!=null){
                if(str.contains(",")){
                    str = str.replace(",", ".");
                }
                String[] strData = str.split("  ");
                
                double[] data = new double[strData.length];
                
                for(int i = 0; i<strData.length; i++){
                    data[i] = Double.parseDouble(strData[i]);
                }
                Data d = new Data(data);
                dataGraphList.add(d.getWavelength(), d.getIntensity());
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataUtils.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataUtils.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    public static void writeData(XYSeries dataGraphList, String filePathName){
        BufferedWriter bw;
        
        try {
            bw = new BufferedWriter(new FileWriter(filePathName));

            for(int i = 0; i<dataGraphList.getMaximumItemCount(); i++){
                bw.append(dataGraphList.getX(i)+"  "+dataGraphList.getY(i));
                bw.newLine();
                i++;
                if(i%50==0){
                    bw.flush();
                }
            }
            
            bw.flush();
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(DataUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void writeSettings(){
        
    }
    public static void readSettings(){
        
    }
}
