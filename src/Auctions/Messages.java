package Auctions;

public class Messages {
	
	public
    String type;
    int MyId;
    int PId;
    int AuctionId;
    String Good;
    int Price;
    String auction;
    int Bidder;
    int Bid;
    int AuctioneerId;
    int Offer;

 // Inform start auction for Dutch Initiator, English Initiator and for the Bidder
    //msg = new Messages("Inform-start-auction",MyId,PId,AuctionId,Good,auction); 
   public Messages(String typeofmessage, int MyId, int PId, int AuctionId, String Good,String typeofauction)
    {
    	this.type=typeofmessage;
    	this.MyId=MyId;
    	this.PId=PId;
    	this.AuctionId=AuctionId;
    	this.Good=Good;
    	this.auction=typeofauction;
    }
 // Inform and Call for proposal for Dutch and English Auction
    public Messages(String typeofmessage, int MyId, int PId, int AuctionId, String Good, int Price,String auction)
    {
    	this.type=typeofmessage;
    	this.MyId=MyId;
    	this.PId=PId;
    	this.AuctionId=AuctionId;
    	this.Good=Good;
    	this.Price=Price;
    	this.auction=auction;
    }
    // proposal,accept and reject, inform messages for Dutch Initiator, English Initiator and Bidder also request
    public Messages(String typeofmessage, int MyId,int PId ,int AuctionId, int Offer) 
    {
    	this.type=typeofmessage;
    	this.PId=PId;
    	this.MyId=MyId;
    	this.AuctionId=AuctionId;
    	this.Offer=Offer;
    }

    // WINNER English ("Winner",MyId,PId,AuctionId,Winner,Bid);
    public Messages(String typeofmessage, int MyId, int PId, int AuctionId, int Bidder, int Bid)
    {
    	this.type=typeofmessage;
    	this.PId=PId;
    	this.MyId=MyId;
    	this.Bidder=Bidder;
    	this.Bid=Bid;
    }
    //not-understood for the Bidder ("not-understood",AuctioneerId,AuctionId,Good,Price,auction);  
    public Messages(String typeofmessage, int AuctioneerId,int AuctionId, String Good,int Price, String auction)
    {
    	this.type=typeofmessage;
    	this.AuctioneerId=AuctioneerId;
    	this.AuctionId=AuctionId;
    	this.Good=Good;
    	this.Price=Price;
    	this.auction=auction;
    }



}