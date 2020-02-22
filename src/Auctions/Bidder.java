package Auctions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Bidder extends MainAuctions implements Runnable{
public //BlockingQueue<Messages> queue;
ArrayList<Goods> biddergoods;

    public Bidder(ArrayList<Goods> Goods)
    {
        //this.queue=q;
        this.biddergoods=Goods;   
    }

	@Override
    public void run() 
    {
    	Messages msg = null;	
    	CurrentAuction ca=null;
    	ArrayList<Messages> receivedMessages=new ArrayList<Messages>();
    	ArrayList<CurrentAuction> CurrentAuctions=new ArrayList<CurrentAuction>();
    	
		Integer PId= (int) Thread.currentThread().getId();
		sendParticipantsId(PId);
		//Participants.add(PId);	
		System.out.println("PId:"+PId); 
		Integer Budget=100;
		Integer Offer=0;
		
		while ((Budget > Offer)&&(biddergoods.size()!=0))
		{
	       try 
	       {
	    	   Thread.sleep(3000);
			} 
	       	catch (InterruptedException e1) 
	       	{// TODO Auto-generated catch block
					e1.printStackTrace();
			}
	       	receivedMessages= receive(PId);
			Iterator <Messages> rmsg;
			rmsg=receivedMessages.iterator();
			while (rmsg.hasNext())
			{
				Messages temp2=rmsg.next();
				//System.out.print(temp2);
			    if (temp2.type=="Inform-start-auction")
			    {
			    	ca = new CurrentAuction(temp2.AuctioneerId,temp2.AuctionId,temp2.auction);  
			    	CurrentAuctions.add(ca);
			    	//System.out.println(CurrentAuctions);
			    }
			    else if(temp2.type=="call-for-proposal")
			    {
			    	Iterator <CurrentAuction> search;
			    	search=CurrentAuctions.iterator();
			    	boolean ok=false;
			    	while((search.hasNext())&&(!ok))
			    	{
			    		CurrentAuction temp3=search.next();
			    		if(temp2.AuctionId==temp3.AuctionId)
			    		{
			    			ok=true;
			    		}
			    	}
			    	//System.out.println("ok is:"+ok+" for bidder:"+PId);
			    	if(ok==true)
			    	{
				    	Iterator <Goods> good;
				    	good=biddergoods.iterator();
				    	while(good.hasNext())
				    	{
				    		Goods goodsearch=good.next();
				    		boolean flag;
				    		if (goodsearch.Good==temp2.Good)
				    		{
					    		if (temp2.auction=="english")
					    		{
					    			if((temp2.Price+goodsearch.Increase)<=goodsearch.HighestPrice)
					    			{
					    				//Offer=temp2.Price+(temp2.Price*goodsearch.Increase);
					    				Offer=temp2.Price+goodsearch.Increase;
					    				System.out.println("The offer Price<=HighestPrice is:"+Offer);
									    msg = new Messages("Proposal",PId,temp2.MyId,temp2.AuctionId,Offer);
									    System.out.println("Proposal english"+"Bidder Id:"+PId+" Auctioneer Id:"+temp2.MyId+" AuctionId:"+temp2.AuctionId+" Offer:"+Offer);  //-
									    send(msg);
					    			}
					    			else
					    			{
					    				Offer=goodsearch.HighestPrice;
					    				System.out.println("The offer equal to HighestPrice:"+Offer);
									    msg = new Messages("Proposal",PId,temp2.MyId,temp2.AuctionId,Offer);
									    System.out.println("Proposal english"+"Bidder Id:"+PId+" Auctioneer Id:"+temp2.MyId+" AuctionId:"+temp2.AuctionId+" Offer:"+Offer);  //-
									    send(msg);
					    			}

					    		}
					    		else if (temp2.auction=="dutch")
					    		{
					    			//System.out.println("Bidder acoution type received:"+temp2.auction);
					    			if((temp2.Price<=goodsearch.HighestPrice)&&(Offer!=temp2.Price))
								    {
					    				Offer=temp2.Price;
								        msg = new Messages("Proposal",PId,temp2.MyId,temp2.AuctionId,Offer);
								        System.out.println("Proposal dutch"+"Bidder Id:"+PId+" Auctioneer Id:"+temp2.MyId+" AuctionId:"+temp2.AuctionId+" Offer:"+Offer);  //-Pid and Myid
								        send(msg);
								    }
					    		}
					    	}
				    		
				    	}
				    }
			    }
			    if (temp2.type=="Reject")
						{
						}
				if(temp2.type=="Accept")
						{
							Budget=Budget-Offer;
						}
				if(temp2.type=="Inform")
						{
							//System.out.println();
						}
			}
		}
    }
	   
				    	
		       	

	    public synchronized void send(Messages msg)
	    {    	try {
				queue.put(msg);
				//System.out.println("MESSAGE: myid:"+msg.MyId+" and urid:"+msg.PId);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public synchronized void sendParticipantsId(Integer PId)
	    {    	try {
				Participants.put(PId);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    public synchronized ArrayList<Messages> receive(Integer Threadid)
	    {
	    	ArrayList<Messages> Msgs=new ArrayList<Messages>();
	// Pid THE SAME with threadid
			Iterator<Messages> idsearch=queue.iterator();
	    	while(idsearch.hasNext())
		    	{
		    		Messages ID=idsearch.next();
		    			Msgs.add(ID);
		    			queue.remove(ID);
		    	}
	    	return Msgs;
	    }

}
	     
