package strobe.data;


public class Data {
    
    private double[] data = new double[2];

    public Data(){}
    public Data(double wavelength, double intensity) {
        data[0] = wavelength;
        data[1] = intensity;
    }
    public Data(double[] data){
        if(data.length==2){
            this.data = data;
        }
    }

    public double getWavelength() {
        return data[0];
    }
    public void setWavelength(double wavelength) {
        this.data[0] = wavelength;
    }

    public double getIntensity() {
        return data[1];
    }
    public void setIntensity(double intensity) {
        this.data[1] = intensity;
    }

    public double[] getData() {
        return data;
    }
    public void setData(double[] data) {
        if(data.length==2){
            this.data = data;
        }
    }
}
