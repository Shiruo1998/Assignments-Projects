package GroupProject;

/**
 * entity class; 
 * this class holds information of a user
 *
 */
public class User {
    private String 	qmnumber;
    private String fullname;
    private String email;
    private String judgelegal;
    private String start_time;
    private String daily_time;
    private String week_time;

    public User(String qmnumber, String fullname,String email,String judgelegal,String start_time,String daily_time, String week_time) {
        this.qmnumber = qmnumber;
        this.fullname = fullname;
        this.email = email;
        this.judgelegal=judgelegal;
        this.start_time=start_time;
        this.daily_time = daily_time;
        this.week_time=week_time;
  
    }
    /**
     * 
     * @return QM number
     */
    public String getQmnumber() {
        return qmnumber;
    }
    /**
     * @param qmnumber QM number
     */
    public void setQmnumber(String qmnumber) {
        this.qmnumber = qmnumber;
    }
    /**
     * 
     * @return user's full name
     */
    public String getFullname() {
        return fullname;
    }
    /**
     * 
     * @param fullname user's full name
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    /**
     * 
     * @return user's email
     */
    public String getEmail() {
        return email;
    }
    /**
     * 
     * @param email user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * 
     * @return legality
     */
    public String getJudgelegal() {
        return judgelegal;
    }
    /**
     * 
     * @param judgelegal legality
     */
    public void setJudgelegal(String judgelegal) {
        this.judgelegal = judgelegal;
    }
    /**
     * 
     * @return start time of this ride 
     */
 	public String getStart_time() {
 		return start_time;
 	}
 	/**
 	 * 
 	 * @param start_time start time of this ride
 	 */
 	public void setStart_time(String start_time) {
 		this.start_time = start_time;
 	}
 	/**
 	 * 
 	 * @return daily riding period
 	 */
 	public String getDaily_time() {
 		return this.daily_time;
 	}
 	/**
 	 * 
 	 * @param daily_time daily riding period
 	 */
 	public void setDaily_time(String daily_time) {
 		this.daily_time = daily_time;
 	}
 	/**
 	 * 
 	 * @return weekly riding period
 	 */
 	public String getWeek_time() {
		return week_time;
	}

 	/**
 	 * 
 	 * @param week_time weekly riding period
 	 */
	public void setWeek_time(String week_time) {
		this.week_time = week_time;
	}

}