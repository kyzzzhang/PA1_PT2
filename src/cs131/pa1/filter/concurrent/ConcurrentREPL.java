package cs131.pa1.filter.concurrent;

import cs131.pa1.filter.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConcurrentREPL {

	static String currentWorkingDirectory;
	
	public static void main(String[] args){
		currentWorkingDirectory = System.getProperty("user.dir");
		Scanner s = new Scanner(System.in);
		System.out.print(Message.WELCOME);
		String command;
		List<Thread> threadList = new LinkedList<Thread>();
		while(true) {
			//obtaining the command from the user
			System.out.print(Message.NEWCOMMAND);
			command = s.nextLine();
			if(command.equals("exit")) {
				break;
			} else if(!command.trim().equals("")) {
				//building the filters list from the command
				ConcurrentFilter filterlist = ConcurrentCommandBuilder.createFiltersFromCommand(command);
				while(filterlist != null) {
//					filterlist.process();
					Thread nextFilter = new Thread(filterlist);
					threadList.add(nextFilter);
					nextFilter.start();
					filterlist = (ConcurrentFilter) filterlist.getNext();
				}
				for(Thread t : threadList) {
					try {
						t.join();
					} catch (InterruptedException e) {
						System.out.println("Interrupted.");
					} 
				}
			}
		}
		s.close();
		System.out.print(Message.GOODBYE);
	}

}
