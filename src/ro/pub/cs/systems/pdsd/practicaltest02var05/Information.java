package ro.pub.cs.systems.pdsd.practicaltest02var05;

public class Information {

	private DateTime timeStamp;
	private String val;
	
	public Information(DateTime timeStamp, String val){
		this.timeStamp = timeStamp; 
		this.val = val;
	}
	
	public DateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(DateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	
	
	
}
