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
		String command;
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(!command.trim().equals("") && ! (command.trim().equals("repel_jobs")) && !(command.trim().contains("kill"))) {
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
				
				while(filterlist != null) {
//					System.out.println(filterlist.toString());
					Thread nextFilter = new Thread(filterlist);
					threads.add(nextFilter);
					nextFilter.start();
					//System.out.println(nextFilter.getState());
					try {
						nextFilter.join();
					} catch (InterruptedException e) {}
					//System.out.println(nextFilter.getState());
						
					filterlist = (ConcurrentFilter) filterlist.getNext();
//					System.out.println(filterlist);
				}
				//System.out.println("out1");
				
				if (backgroundMode) {
					for (Thread t: threads) {
						try {
							t.join();
						} catch (InterruptedException e) {
							System.out.println("Interrupted.");
						} 
					}
				}
//				System.out.println("out2");
				
			} else if (command.trim().equals("repl_jobs")) {
				int number = 1;
				for (Thread t: threads) {
					if (t.isAlive()) {
						System.out.println(number + ". " + t.getName() + " & ");
						number++;
					}
				}
			} else if (command.contains("kill")) {
				String[] killList = command.split("//s+");
				int killNumber = Integer.parseInt(killList[1]);
				int alive = 1;
				for (Thread t: threads) {
					if (t.isAlive()){
						if (alive == killNumber) {
							t.interrupt();
						}
					}
				}
			}
		}
//		System.out.println("out3");
		s.close();
		System.out.print(Message.GOODBYE);
	}

}

