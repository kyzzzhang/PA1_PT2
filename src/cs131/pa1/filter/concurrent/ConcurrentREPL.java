package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		LinkedList<Thread> threads = new LinkedList<Thread>();
		LinkedList<String> commandsList =  new LinkedList<String>();
		LinkedList<Thread> lastThreads = new LinkedList<Thread>();
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if (command.trim().equals("repl_jobs")) {
				int number = 1;
				for (Thread t: threads) {
					if (t.isAlive()) {
						System.out.println(number + ". " + t.getName() + " & ");
						number++;
					}
				}
			} else if(!command.trim().equals("")) {
				//building the filters list from the command
				String[] commandList = command.split("\\s+");
				String symbol = commandList[commandList.length-1];
				boolean backgroundMode = false;
				if (symbol.equals("&")) {
					backgroundMode = true;
					int symbolIndex = command.indexOf(symbol);
					command = command.substring(0, symbolIndex);
				}
				
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				Thread last = new Thread();
				while(filterlist != null) {
//					System.out.println(filterlist.toString());
					Thread nextFilter = new Thread(filterlist);
//					threads.add(nextFilter);
//					if(filterlist.getNext() == null) {
//						lastThreads.add(nextFilter);
//					}
					nextFilter.start();
					try {
						nextFilter.join();
					} catch (InterruptedException e) {}
					filterlist = (ConcurrentFilter) filterlist.getNext();
					if ( filterlist.getNext() == null) {
						last = new Thread(last);
					}
					
				}
				if (backgroundMode) {
					LinkedList<Thread> nextLine = new LinkedList<Thread>();
					String concurrentCommand = s.nextLine();
					for (Thread t: threads) {
						try {
							t.join();
						} catch (InterruptedException e) {
							System.out.println("Interrupted.");
						} 
					}
				}
				
			} else if (command.contains("kill")) {
				String[] killList = command.split("\\s+");
				int killNumber = Integer.parseInt(killList[1]);
				if (lastThreads.get(killNumber-1).isAlive()) {
					if(killNumber > 1) {
						int fromIndex = threads.indexOf(lastThreads.get(killNumber-2))+1;
						int endIndex = threads.indexOf(lastThreads.get(killNumber-1));
						for(int i = fromIndex; i <= endIndex; i++) {
							if(threads.get(i).isAlive()) {
								threads.get(i).interrupt();
							}
							
						}
					}
					
				}
//				int alive = 1;
//				for (Thread t: threads) {
//					if (t.isAlive()){
//						if (alive == killNumber) {
//							t.interrupt();
//						}
//					}
//				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}

