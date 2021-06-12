package com.inhatc.domain;

import java.sql.Date;
import java.sql.Timestamp;

public class BoardVO {
	
	private Integer bno;
	private String title;
	private String content;
	private String writer;
	private Timestamp regedit;
	private int viewcnt;
	
	public Integer getBno() {
		return bno;
	}
	public void setBno(Integer bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Timestamp getRegedit() {
		return regedit;
	}
	public void setRegedit(Timestamp regedit) {
		this.regedit = regedit;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	
	@Override
	public String toString() {
		return "BoardVO [bno=" + bno + ", title=" + title + ", content=" + content + ", writer=" + writer + ", regedit="
				+ regedit + ", viewcnt=" + viewcnt + "]";
	}
	
	

	
}
