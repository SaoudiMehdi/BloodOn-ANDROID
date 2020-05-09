package stock;

public class Stock {

    String type,hopital;
    String qte;
    int img;
    public Stock(String hopital,String type, String qte, int img){
      this.type=type;
      this.qte=qte;
      this.img=img;
      this.hopital=hopital;
    }

    public String getType() {
        return type;
    }

    public int getImg() {
        return img;
    }

    public String getQte(){
        return qte;
    }
    public String getHopital(){
        return hopital;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setImg(int img) {
        this.img = img;
    }
    public  void setQte(String qte){
        this.qte=qte;
    }
    public void setHopital(String hopital){
        this.hopital=hopital;
    }
}
