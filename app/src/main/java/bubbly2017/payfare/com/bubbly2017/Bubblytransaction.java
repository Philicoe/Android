package bubbly2017.payfare.com.bubbly2017;
final class Bubblytransaction {

    private String strTRXData ;
    private String strCardSN;
    private boolean bIsUploaded ;
    private int iDX;


    public Bubblytransaction(String strTRXData,String strCardSN,boolean bIsUploaded,int iDX){
        this.strTRXData = strTRXData;
        this.bIsUploaded = bIsUploaded;
        this.iDX = iDX;
        this.strCardSN =strCardSN;
    }


    public Bubblytransaction(){
        this.strTRXData = "";
        this.bIsUploaded = false;
        this.iDX = 0;
        this.strCardSN ="";
    }

    public String getStrTRXData(){return  strTRXData;}
    public String getstrCardSN(){return  strCardSN;}

    public boolean getbIsUploaded(){return  bIsUploaded;}
    public int getiDX(){return  iDX;}

    // return a string representation of this point
    public String toString() {
        return "(" + strTRXData + ", " + strCardSN + ", " + bIsUploaded + ", " + iDX + ")";
    }


    //to use:
    /*
    Bubblytransaction bTRX = new Bubblytransaction("data",true,0 );
    String strRes = bTRX.getStrTRXData(); etc

     */


}
