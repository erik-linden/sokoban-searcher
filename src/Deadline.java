import java.util.Date;

public class Deadline {
    
    Deadline(Date pDate)
    {
        mDate=pDate;
    }

    public Deadline(int i) {
    	mDate = new Date(new Date().getTime()+i);
	}

	long TimeUntil()
    {
        return mDate.getTime()-(new Date()).getTime();
    }
        
    private Date mDate;
}
