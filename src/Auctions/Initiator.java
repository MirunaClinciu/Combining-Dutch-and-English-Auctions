package Auctions;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.ArrayList;


public class Initiator extends MainAuctions implements Runnable 
{	public
	//BlockingQueue<Messages> queue; 
	ArrayList<GoodsPrice> mygoods;
	ArrayList<Goods> biddergoods;

	
    public Initiator(ArrayList<GoodsPrice> Goods,ArrayList<Goods> Goods2)
    { 
    	this.mygoods=Goods;
    	this.biddergoods=Goods2;
    }  
    @Override
    public void run() 
    { 		
    	Messages msg;	
    	ArrayList<Messages> receivedMessages=null;
    	ArrayList<Messages> Proposals=new ArrayList<Messages>();
		ArrayList<Integer> SetofBidders=new ArrayList<Integer>();
		ArrayList<Integer> SetofBidderseng=new ArrayList<Integer>();
		ArrayList<Integer> ChooseBidder=new ArrayList<Integer>();
		ArrayList<Integer> SetofWinners=new ArrayList<Integer>();
		Integer countbidder=0;
		Integer Bidder;
		Integer Bid;	
		Integer mf = 0;
		//###### What type of auction? dutch or english
    	String auction="dutch";
    	//###### MyId
		Integer MyId= (int) Thread.currentThread().getId();
		//System.out.print("Bidder "+MyId+" started.\n");
		System.out.println("Auctioneer Id:"+MyId);  
		//###### Participants List contains ID of Participants
		Integer i;
		int Profit;
		double Profit2;
		ArrayList<Integer> ParticipantsList=new ArrayList<Integer>();
		//System.out.print(ParticipantsList.size()+"\n");
		Iterator<Integer> p=Participants.iterator();
		while(p.hasNext())
		{
			Integer m=p.next();
			ParticipantsList.add(m);
			System.out.print("Participant:"+m+" added.\n");
		}
		//###### AuctionId
		Integer AuctionId=0; 	
    	//#####Search in goods
    	GoodsPrice good;
    	Iterator <GoodsPrice> g=mygoods.iterator();
    	while (g.hasNext())
    	{
    		good=g.next();
    		String Good=good.Good;
        	Integer InitialPrice=good.InitialPrice;
        	Integer Reserve=good.Reserve;
        	Integer Decrease=good.Decrease;
        	System.out.print("InitialPrice="+InitialPrice+"\n");
        	Goods good2;
        	Iterator <Goods> g2=biddergoods.iterator();
        		good2=g2.next();
            	Integer InitialBid=good2.HighestPrice;
            	Integer Reservebidder=good2.Reservebidder;
            	System.out.print("InitialBid="+InitialBid+"\n");
            	

  
        	try 
        	{
        		Thread.sleep(3000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//###### Price <- InitialPrice
        	Integer Price = InitialPrice;
        	//###### For every auction it will be a different Id;
        	AuctionId++;
        	//######  For all Participant from the list 
        	Iterator <Integer> p2=Participants.iterator();
        	while (p2.hasNext())
        	{
        		Integer PId=p2.next();
        		//################# Inform start of the auction send message (inform-start-auction(MyId,PId,AuctionId,auction))
        		msg = new Messages("Inform-start-auction",MyId,PId,AuctionId,Good,auction);  
        		System.out.println("Inform-start-auction"+" MyId:"+MyId+" PId:"+PId+" AuctionId:"+AuctionId+"Good name:"+Good+" type of auction:"+auction);  
        		send(msg);
        		//################# Call for proposal send message (call-for-proposal(MyId,PId,AuctionId,Good,Price))
        		msg = new Messages("call-for-proposal",MyId,PId,AuctionId,Good,Price,auction);
        		System.out.println("Call for proposal"+" MyId:"+MyId+" PId:"+PId+" AuctionId:"+AuctionId+" Good name:"+Good+" Price:"+Price+" ");  
        		send(msg);
        	} // End the Participants Loop
        	boolean AuctionOn=true;	
        	while(AuctionOn)
        	{   	
        		try 
        		{
        			Thread.sleep(3000);
				} 
        		catch (InterruptedException e1) 
        		{
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
				}
			    receivedMessages=receiveMsgs(MyId);		
				Iterator <Messages> rmsg;
				rmsg=receivedMessages.iterator();
				if (receivedMessages.size()==0)
				{
					Price=Price-Decrease;
					System.out.println("Verify decreased price:"+Price);
					if (Price>=Reserve)
					{
				       	Iterator <Integer> p3=Participants.iterator();
				       	while (p3.hasNext())
				       	{
				       		Integer PId=p3.next();
			        		//################################# Call for proposal send message (call-for-proposal(MyId,PId,AuctionId,Good,Price))
			        		msg = new Messages("call-for-proposal",MyId,PId,AuctionId,Good,Price,auction);
			        		System.out.println("Call for proposal"+" MyId:"+MyId+" PId:"+PId+" AuctionId:"+AuctionId+" Good name:"+Good+" Price:"+Price+" ");  
			        		send(msg);
				       	}
					}	
					else
					{
						AuctionOn=false;	
						System.out.println("No Winner for:"+Good);
					}
				}
				else
				{
					while (rmsg.hasNext())
					{
						Messages temp2=rmsg.next();
						if ((temp2.type=="not-understood"))//remove "not-understood" messages from Msgs
						{
							try 
							{
								removeMsgs(temp2);
							}
							catch (InterruptedException e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else
						{
							if ((temp2.type=="proposal")&&(temp2.AuctionId!=AuctionId) )//remove "proposal" messages with different AuctionId % filter out messages sent by mistake
							{
								try {
										removeMsgs(temp2);
									} 										
								catch (InterruptedException e) 
									{
									// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
							else
							{
								if ((temp2.type=="proposal")&&(temp2.Offer<Price))//remove "proposal" messages with Offer<Price % filter out previous offers & chancers
								{
										try {
											removeMsgs(temp2);} 
										catch (InterruptedException e) 
										{
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
								}
							}
						}
						//System.out.print("type="+ temp.type+"\n");
			        	try 
			        	{
			        		Thread.sleep(3000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						//for all proposal (PId,MyId,AuctionId,Offer) exist in Msgs do
						Iterator <Messages> proposal;
						proposal=receivedMessages.iterator();
						//System.out.println(proposal);
					  	boolean ok=true;
						while (proposal.hasNext()&&(ok=true))
						{
							Messages prop=proposal.next();
							if ((prop.type=="Proposal"))
							{
								Proposals.add(prop);
								//	Setofbidders <- Setofbidders U PId % I add the PID in the Setofbidders
								SetofBidderseng.add(prop.PId);//Was PiD  it was prop.PId
				        		SetofBidders.add(prop.MyId);
								System.out.println("the whileset "+SetofBidders);
							}
							countbidder=SetofBidders.size();
							System.out.println(SetofBidderseng);							
							System.out.println("Number of bidders"+countbidder);

						
					  /*      	try 
					        	{
					        		Thread.sleep(6000);
								} catch (InterruptedException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}*/
							  	proposal=receivedMessages.iterator();
								//System.out.println("prop");
								while (proposal.hasNext())
								{
									Messages temp3=proposal.next();
									if ((temp3.type=="Proposal")&&(countbidder==1))
									{
							        	Iterator <Integer> p3=SetofBidderseng.iterator();
							        	while (p3.hasNext())
							        	{
							        		Integer PId=p3.next();
							        		Proposals.add(temp3);
							        		Bidder = temp3.MyId;
							        		Bid = temp3.Offer;
							        		System.out.println("Bid"+Bid);
							        		System.out.println("InitialBid"+InitialBid);
							        		System.out.println("Reserve"+Reserve);
							        		
							        		msg = new Messages("Accept",MyId,Bidder,AuctionId,Bid);
							        		System.out.println("ACCEPTED BIDDER WINNER"+temp3.MyId);
							        		System.out.println("Accept "+" MyId:"+MyId+" Bidder:"+Bidder+" AuctionId:"+AuctionId+" Bid:"+Bid);  
							        		send(msg);
							        		SetofWinners.add(Bidder);
							        		Profit=Bid-Reserve;
							        		//Profit=(int)(InitialBid*(Bid-Reserve));
							        		//double probab = 25/100;
							        		//Profit2=(double) ((double)Profit*probab);
							        		System.out.println("The Profit is:"+Profit);
							        		int saved=Reservebidder-Bid;
							        		System.out.println("Winner saved:"+Profit);
							        		msg = new Messages("The Winner",MyId,Bidder,AuctionId,Bidder);
							        		System.out.println("The Winner:"+" MyId:"+MyId+" PId:"+Bidder+" AuctionId:"+AuctionId+" Bidder:"+Bidder);  
							        		send(msg);
							        		ok=false;
							        		AuctionOn=false;
							            	//SetofBidderseng.clear();//AFTER EVERY GOOD
							        	}
								}
								else if ((countbidder>=2)&&(temp3.type=="Proposal"))
								{
								        	Iterator <Integer> p3=SetofBidderseng.iterator();
								        	while (p3.hasNext())
								        	{
												System.out.print("We have 2 or more winners=>start English Auction");
												System.out.print("      ");
												//System.out.print("    SET OF WINNERS  "+SetofBidders);
												try {
														englishauction (Good,Price,Reserve,SetofWinners,MyId,AuctionId);
													} catch (InterruptedException e) {
														// TODO Auto-generated catch block
														e.printStackTrace();
													};

								        	}
								}
								else if (countbidder==0)
								{
									AuctionOn=false;
									System.out.println("No Winner for:"+good);
								}
								}
								
							}
						}
					}
				}
        	SetofBidders.clear();//AFTER EVERY GOOD
        	SetofBidderseng.clear();//AFTER EVERY GOOD


        	}
    	}
   
    
						
									
				  
				  

@SuppressWarnings("null")
public void englishauction(String Good,Integer Price,Integer Reserve,ArrayList<Integer> SetofBidders,Integer MyId,Integer AuctionId) throws InterruptedException
{	
   Messages msg;	
   ArrayList<Messages> receivedMessages=null;
   ArrayList<Messages> Proposalsenglish =new ArrayList<Messages>();
   ArrayList<Integer> SetofWinnerseng2 = null;
   ArrayList<Integer> SetofWinnerseng = null;
   ArrayList<Integer> Choosebidder= null;
	ArrayList<Integer> SetofWinnersFlip=new ArrayList<Integer>();
	ArrayList<Integer> BidList=new ArrayList<Integer>();
   Integer Winner=null;
   Integer Offer=null;
   System.out.print("SET OF WINNERS  in the english "+SetofBidders);
   
   //###### What type of auction? dutch or english
   String auction="english";  	
   //System.out.print("Bidder "+MyId in English Auction+" started.\n");
   //###### MyId
   System.out.println("Auctioneer Id:"+MyId);
   System.out.println("Price good:"+Price);
   System.out.println("Reserve:"+Reserve);
   
	GoodsPrice good;
	Integer Increase=5;
	Iterator <GoodsPrice> g=mygoods.iterator();

   //###### For every auction it will be a different Id;
   AuctionId++;
   Integer Bidder=null;
   Integer Bid=null;

   //######  For all Participant from the list 
   Iterator <Integer> p2=SetofBidders.iterator();
   while (p2.hasNext())	
   {
	   Integer PId=p2.next();
	   //################# Inform start of the auction send message (inform-start-auction(MyId,PId,AuctionId,auction))
	   msg = new Messages("Inform-start-auction",MyId,PId,AuctionId,Good,auction);  
	   System.out.println("Inform-start-auction ENGLISH"+" MyId:"+MyId+" PId:"+PId+"Good name:"+Good+" AuctionId:"+AuctionId+" type of auction:"+auction);  
	   send(msg);
	   //################# Call for proposal send message (call-for-proposal(MyId,PId,AuctionId,Good,Price))
	   msg = new Messages("call-for-proposal",MyId,PId,AuctionId,Good,Price,auction);
	   System.out.println("Call for proposal ENGLISH"+" MyId:"+MyId+" PId:"+PId+" AuctionId:"+AuctionId+" Good name:"+Good+" Price:"+Price+" ");  
	   send(msg);
   } // End the Participants Loop	
   boolean AuctionOn=true;	
   while(AuctionOn)
   {   	
	   try 
	   {
		   Thread.sleep(3000);
	   } 
	   catch (InterruptedException e1) 
	   {
		   // TODO Auto-generated catch block
		   e1.printStackTrace();
	   }
	   receivedMessages=receiveMsgs(MyId);		
	   Iterator <Messages> rmsg;
	   rmsg=receivedMessages.iterator();
	   if (receivedMessages.size()==0)
	   {

		   Iterator <Integer> p3=SetofBidders.iterator();
		   while (p3.hasNext())
			       	{
			       		Integer PId=p3.next();
						msg = new Messages("Inform",MyId,PId,AuctionId,0);//no bids
						System.out.println("NO BIDS");
						send(msg); 
						AuctionOn=false;
			       	}
	   }
	   else
	   {
			//Price=Price+Increase;
		   if((Bid!=null)&&(Bidder!=null))
		   {
			   if (Bid>Price)//era bid
			   {	
				   
				   System.out.println("cONDITIE Bid>Price"+Bid+">"+Price);
				   msg = new Messages("Accept",MyId,Bidder,AuctionId,Bid);
				   System.out.println("Accept "+" MyId:"+MyId+" Bidder:"+Bidder+" AuctionId:"+AuctionId+" Bid:"+Bid);  
				   send(msg);
				   System.out.println("Winner"+Bidder);
				   System.out.println("Set of Bidders in english before remove"+SetofBidders);
				   SetofBidders.remove(Bidder);
				   System.out.println("Set of Bidders in english after remove"+SetofBidders);
				   msg = new Messages("Request",MyId,Bidder,AuctionId,Bid);
				   send(msg);  
				   Winner=Bidder;
				   msg = new Messages("Winner",MyId,Bidder,AuctionId,Winner,Bid);
				   System.out.println("The Winner:"+" MyId:"+MyId+" PId:"+Bidder+" AuctionId:"+AuctionId+" Winner:"+Winner+"Bid:"+Bid);  
				   System.out.println("The Winner:"+" MyId:"+MyId+" PId:"+Bidder+" AuctionId:"+AuctionId+" Winner:"+Winner+"Bid:"+Bid); 
				   send(msg); 
				   AuctionOn=false;
				   }
			   }
		   		else
		   		{
		   			while (rmsg.hasNext())
		   			{
		   				Messages temp3=rmsg.next();
		   				if ((temp3.type=="not-understood"))//remove "not-understood" messages from Msgs
		   				{
		   					try 
		   					{
		   						removeMsgs(temp3);
		   					}
		   					catch (InterruptedException e) 
		   					{
		   						// TODO Auto-generated catch block
		   						e.printStackTrace();
		   					}
		   				}
		   				else
		   				{
		   					if ((temp3.type=="proposal")&&(temp3.AuctionId!=AuctionId) )//remove "proposal" messages with different AuctionId % filter out messages sent by mistake
		   					{
		   						try {
		   							removeMsgs(temp3);
		   						} 										
		   						catch (InterruptedException e) 
		   						{
		   							// TODO Auto-generated catch block
		   							e.printStackTrace();
		   						}
		   					}
		   					else
		   					{
		   						if ((temp3.type=="proposal")&&(temp3.Offer<Price))//remove "proposal" messages with Offer<Price % filter out previous offers & chancers
		   						{
		   							try {
		   								removeMsgs(temp3);
		   								} 
		   							catch (InterruptedException e) 
		   							{
		   								// TODO Auto-generated catch block
		   								e.printStackTrace();
		   							}
		   						}
		   					}
		   				}
		   			}
		   		}
			   	try 
			   	{
			   		Thread.sleep(3000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				//for all proposal (PId,MyId,AuctionId,Offer) exist in Msgs do
			   	Iterator <Messages> proposal;
			   	proposal=receivedMessages.iterator();
			   	//System.out.println(proposal);
			   	//boolean ok=true;
			   	boolean ok=true;
			   	while (proposal.hasNext()&&(ok=true))
			   	{
			   		Messages prop=proposal.next();
			   		//Proposalsenglish.add(prop);
			   		Bidder=prop.MyId;
			   		System.out.println("Bidder proposal"+Bidder);
			   		Bid=prop.Offer;
			   		System.out.println("prop.Bid"+prop.Bid);
			   		System.out.println("Bid proposal"+Bid);
			   		SetofWinnersFlip.add(Bidder);
			   		BidList.add(Bid);
			   		System.out.println("BidList*****"+BidList);
			   		System.out.println("BidList***2"+BidList);
				   }	
					//System.out.println("prop");
				   if(SetofWinnersFlip.size()==2)
				   {
					  for (Integer w: BidList) 
					  {
						  for (Integer z: BidList) 
						  {
						
						      if(BidList.get(w) == BidList.get(z))
						      {
						          System.out.println(w + " Head ");
						          System.out.println(w + " I choose "+w+"Head");
						          System.out.println(z +" Tails");
						          CoinToss test = new CoinToss();
						          int choice=1;
								do{
						              if (choice == 1){
						              test.flip();
						          }
						              else if (choice > 1){
						              System.out.println("Not Correct ");
						              }
						          }while (choice != 0);
								AuctionOn=false;
						        	
						      }
						      else
						      {
						    	  if(BidList.get(w)> BidList.get(z))
						    	  {
						    		  Winner=BidList.get(w);
						    		  System.out.println(Winner);
						    		  AuctionOn=false;
						    	  }
						    	  
						    	  else
						    	  {
						    		  Winner=BidList.get(z);
						    		  System.out.println(Winner);
						    		  AuctionOn=false;
						    	  }
						      }
						    }
						}
				   }
			  //Price=Price+Increase;//Increase
			  System.out.println("Verify increased price:"+Price);
			  if(Price+Increase<Reserve)//price
			  {
				   AuctionOn=false;
			  }
			  if(Bid>=Price)
			  {
				   Price=Bid;
			  }
			  //######  For all Participant from the list 
			  Iterator <Integer> p3=SetofBidders.iterator();
			  while (p3.hasNext())
			  {
				   Integer PId=p3.next();
				   msg = new Messages("call-for-proposal",MyId,PId,AuctionId,Good,Price,auction);
				   System.out.println("Call for proposal"+" MyId:"+MyId+" PId:"+PId+" AuctionId:"+AuctionId+" Good name:"+Good+" Price:"+Price+" ");  
				   send(msg);
			  }
			}
   }
}

			
			
			
			
			
				

//#############################################################################
  public synchronized void send(Messages msg)
  {
  	try {
			queue.put(msg);
		} catch (InterruptedException e) 
  	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }   
//###############################################################################
      public synchronized ArrayList<Messages> receiveMsgs(Integer Threadid)
  {
  	ArrayList<Messages> Msgs=new ArrayList<Messages>();
      // Pid THE SAME with threadid
		Iterator<Messages> idsearch=queue.iterator();
  	while(idsearch.hasNext())
	    	{
	    		Messages ID=idsearch.next();
	    		System.out.println("The message myid is:"+ID.MyId+" AND pid is:"+ID.PId);
	    		if ((ID.PId==Threadid))
	    		{
	    			
	    			Msgs.add(ID);
	    		}
	    	}
		System.out.println("Mymsgs:"+Msgs.size());
  	return Msgs;
  }
//#################################################################################
//Remove messages from Msgs
  public synchronized void removeMsgs (Messages msg) throws InterruptedException
  {
  	queue.remove(msg);
  }

}