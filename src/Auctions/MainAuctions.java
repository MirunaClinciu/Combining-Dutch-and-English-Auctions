package Auctions;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MainAuctions 
{
	public static BlockingQueue<Integer> Participants=new LinkedBlockingQueue<Integer>();
	//Messages Queue
	public static BlockingQueue<Messages> queue = new LinkedBlockingQueue<>();

	public static void main (String args[]) throws InterruptedException
	{
		//GoodsPrice(String Good, int InitialPrice, int Reserve, int Decrease, int Increase)
		GoodsPrice good1=new GoodsPrice("Mona Lisa",30,20,5,10);
		GoodsPrice good2=new GoodsPrice("Vitruvian Man",25,20,5,10);
		GoodsPrice good3=new GoodsPrice("Salvator Mundi",30,10,5,10);
			
		ArrayList<GoodsPrice> mygoods=new ArrayList<GoodsPrice>();
		mygoods.add(good1);
		mygoods.add(good2);
		mygoods.add(good3);
		
		//Bidder 1 Goods(String Good, int HighestPrice, int Reservebidder, int Increase)
		Goods goodbidder1=new Goods("Mona Lisa",20,10,2);
		Goods goodbidder2=new Goods("Vitruvian Man",25,10,5);//less reserve price
		Goods goodbidder3=new Goods("Salvator Mundi",10,10,5);//less reserve price
			
		ArrayList<Goods> biddergoods=new ArrayList<Goods>();
		biddergoods.add(goodbidder1);
		biddergoods.add(goodbidder2);
		biddergoods.add(goodbidder3);
		
		
		//Bidder 2 Goods(String Good, int HighestPrice, int Reservebidder, int Increase);
		Goods goodbidder21=new Goods("Mona Lisa",20,10,5);
		Goods goodbidder22=new Goods("Vitruvian Man",10,10,5);//less reserve price
		Goods goodbidder23=new Goods("Salvator Mundi",10,10,5);//less reserve price
			
		ArrayList<Goods> biddergoods2=new ArrayList<Goods>();
		biddergoods2.add(goodbidder21);
		biddergoods2.add(goodbidder22);
		biddergoods2.add(goodbidder23);      
		
		//Bidder 3 Goods(String Good, int HighestPrice, int Reservebidder,int Increase);
		Goods goodbidder31=new Goods("Mona Lisa",15,10,5);
		Goods goodbidder32=new Goods("Vitruvian Man",20,10,5);//less reserve price
		Goods goodbidder33=new Goods("Salvator Mundi",10,10,5);//less reserve price
			
		ArrayList<Goods> biddergoods3=new ArrayList<Goods>();
		biddergoods3.add(goodbidder31);
		biddergoods3.add(goodbidder32);
		biddergoods3.add(goodbidder33);  
	   
	        //Initiator initiator = new Initiator(queue, mygoods);
	        //Bidder bidder = new Bidder(queue, biddergoods);
        
	      /*  //starting producer to produce messages in queue
	        new Thread(initiator).start();
	        //starting consumer to consume messages from queue
	        new Thread(bidder).start();
	        System.out.println("Inform-start-auction");
	       */
	        //Thread t1=new Thread(initiator(mygoods));
	        Thread t2=new Thread(new Bidder(biddergoods));   
	        t2.start();
	       // Thread.sleep(3000);
	       Thread t3=new Thread(new Bidder(biddergoods2));   
	       t3.start();
	       // Thread.sleep(3000);
	   //     Thread t4=new Thread(new Bidder(biddergoods3));   
	   //     t4.start();
	       // Thread.sleep(3000);
	        Thread t1=new Thread(new Initiator(mygoods, biddergoods));   
	        t1.start();	        

	        //t1.start();      

	        
	 }

	private static Runnable initiator(ArrayList<GoodsPrice> mygoods) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
	


