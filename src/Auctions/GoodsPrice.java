package Auctions;

public class GoodsPrice 
{
    
	public
    String Good;
    int InitialPrice;
    int Reserve;
    int Decrease;
    int Increase;
  
    public GoodsPrice(String Good, int InitialPrice, int Reserve, int Decrease,int Increase)
    {
    	this.Good=Good;
    	this.InitialPrice=InitialPrice;
    	this.Reserve=Reserve;
    	this.Decrease=Decrease;
    	this.Increase=Increase;
    }
    

}