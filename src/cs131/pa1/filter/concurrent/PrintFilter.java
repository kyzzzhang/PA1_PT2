package cs131.pa1.filter.concurrent;

public class PrintFilter extends ConcurrentFilter {
	public PrintFilter() {
		super();
	}
	
	public void process() {
		while(!isDone()) {
			processLine(input.poll());
		}
	}
	
	public String processLine(String line) {
		if(line !=null && !line.equals("COMPLETED")) {
			System.out.println(line);
		}
		return null;
	}
}
