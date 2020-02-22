package Auctions;

public class Proposalsenglish {		
		public
	    String type;
	    int MyId;
	    int PId;
	    int AuctionId;
	    int AuctioneerId;
	    int Offer;
	  
	 // Inform start auction for Dutch Initiator, English Initiator and for the Bidder
	    public Proposalsenglish(String typeofmessage, int PId, int MyId, int AuctionId, int Offer)
	    {
	    	this.type=typeofmessage;
	    	this.MyId=MyId;
	    	this.PId=PId;
	    	this.AuctionId=AuctionId;
	    	this.Offer=Offer;

	    }

}
