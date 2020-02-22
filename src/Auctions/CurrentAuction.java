package Auctions;

public class CurrentAuction 
{

	public
    int AuctioneerId;
    int AuctionId;
    String auction;
  
    public CurrentAuction(int AuctioneerId, int AuctionId, String auction)
    {
    	this.AuctioneerId=AuctioneerId;
    	this.AuctionId=AuctionId;
    	this.auction=auction;
    }
    
}
