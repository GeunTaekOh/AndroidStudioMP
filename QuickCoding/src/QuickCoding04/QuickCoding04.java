package QuickCoding04;

public class QuickCoding04 {
	public static BankAccount account = new BankAccount();

	static int money = 10;
	public static void main(String[] args){

		Thread depositMan = new Thread(){
			
			public void run(){
				for(int i=0; i<10; i++){
				synchronized(this){
				account.add(money);
				notify();
				}
				}
			}		
		};
		Thread withdrawMan = new Thread(){
			
			public void run(){
				synchronized(this){
				while(account.getBalance()>0){
					account.delete(money);	
				}
				if(account.getBalance()<0){
					try{
						wait();
					}catch(InterruptedException e){
					}
				}
			}
			}
		};
		depositMan.start();
		withdrawMan.start();
		
		
	}
	
}
	class BankAccount{
		private int balance = 100;
		
		public int getBalance(){
			return balance;
		}
		public void withDraw(int amount){
			balance-=amount;
		}
		public void add(int money){
			balance+=money;
			System.out.println("총합 : "+balance +"       "+money +"만큼 입금");
		}
		public void delete(int money){
			balance -= money;
			System.out.println("총합 : "+balance+"       "+money+"만큼 출금");
		}
	}
