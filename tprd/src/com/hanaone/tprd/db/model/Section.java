package com.hanaone.tprd.db.model;

import com.hanaone.tprd.db.Pojo;
import com.hanaone.tprd.db.SectionDataSet;

import android.provider.BaseColumns;

public class Section implements Model {
	protected int id;
	protected int number;
	protected String text;
	protected String hint;
	protected int exam_level_id;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

	public int getExam_level_id() {
		return exam_level_id;
	}
	public void setExam_level_id(int exam_level_id) {
		this.exam_level_id = exam_level_id;
	}



	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}



	public static abstract class SectionEntry implements BaseColumns{
		public static final String TABLE_NAME = "section";
		public static final String COLUMN_NAME_NUMBER = "number";
		public static final String COLUMN_NAME_TEXT = "text";
		public static final String COLUMN_NAME_HINT = "hint";
		public static final String COLUMN_NAME_EXAM_LEVEL_ID = "exam_level_id";
	}



	@Override
	public SectionDataSet toPojo() {
		SectionDataSet section = new SectionDataSet();
		section.setId(id);
		section.setNumber(number);
		section.setText(text);
		section.setHint(hint);
		return section;
	}	
}
