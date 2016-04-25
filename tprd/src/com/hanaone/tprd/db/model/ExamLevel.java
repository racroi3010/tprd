package com.hanaone.tprd.db.model;

import android.provider.BaseColumns;

public class ExamLevel {
	private int id;
	private int exam_id;
	private int number;
	private String label;
	private int audio_id;
	private int pdf_id;	
	private int txt_id;
	private int score;
	private int maxScore;	
	private int active;
	private int color;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getAudio_id() {
		return audio_id;
	}
	public void setAudio_id(int audio_id) {
		this.audio_id = audio_id;
	}

	

	public int getPdf_id() {
		return pdf_id;
	}
	public void setPdf_id(int pdf_id) {
		this.pdf_id = pdf_id;
	}



	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	



	public int getTxt_id() {
		return txt_id;
	}
	public void setTxt_id(int txt_id) {
		this.txt_id = txt_id;
	}




	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}




	public int getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(int maxScore) {
		this.maxScore = maxScore;
	}




	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}




	public abstract class ExamLevelEntry implements BaseColumns{
		public static final String TABLE_NAME = "examlevel";
		public static final String COLUMN_NAME_EXAM_ID = "exam_id";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_LABEL = "label";	
		public static final String COLUMN_NAME_AUDIO_ID = "audio_id";
		public static final String COLUMN_NAME_PDF_ID = "pdf_id";	
		public static final String COLUMN_NAME_TXT_ID = "txt_id";	
		public static final String COLUMN_NAME_SCORE = "score";
		public static final String COLUMN_NAME_MAXSCORE = "maxscore";
		public static final String COLUMN_NAME_ACTIVE = "active";
		public static final String COLUMN_NAME_COLOR = "color";
		
	}	
}
