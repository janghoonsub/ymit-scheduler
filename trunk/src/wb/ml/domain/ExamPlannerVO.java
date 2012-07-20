package wb.ml.domain;

public class ExamPlannerVO {
	private int position;
	private String suject;
	private String cover;
	private String before;
	private String goal;
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getSuject() {
		return suject;
	}
	public void setSuject(String suject) {
		this.suject = suject;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getBefore() {
		return before;
	}
	public void setBefore(String before) {
		this.before = before;
	}
	public String getGoal() {
		return goal;
	}
	public void setGoal(String goal) {
		this.goal = goal;
	}
}
