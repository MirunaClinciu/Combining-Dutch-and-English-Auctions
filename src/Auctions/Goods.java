package Auctions;

public class Goods
{
    
	public
    String Good;
    int HighestPrice;
    int Increase;
    int Reservebidder;
    
    public Goods(String Good, int HighestPrice, int Reservebidder, int Increase)
    {
    	this.Good=Good;
    	this.HighestPrice=HighestPrice;
    	this.Increase=Increase;
    	this.Reservebidder=Reservebidder;
    }
    
}