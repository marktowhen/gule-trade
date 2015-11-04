package com.jingyunbank.etrade.base;

public class Page {

	private int offset;//
	private int size;//page size
	private long total;//total item count
	private long pcount;//=total/size
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public long getPcount() {
		return pcount;
	}
	public void setPcount(long pcount) {
		this.pcount = pcount;
	}
	
}
