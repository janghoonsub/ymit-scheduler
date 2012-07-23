package wb.ml.schoolevents;

//PageControl �⺻Ʋ
public interface IPageControl {
	public void setPageSize(int size);
	
	public int getPageSize();
	
	public void setPageIndex(int index);

	public int getCurrentPageIndex(); 
}
