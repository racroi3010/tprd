package com.hanaone.tprd.db.sample;


public class SectionSample{
	protected int id;
	protected int number;
	protected float startAudio;
	protected float endAudio;
	protected String text;
	protected String hint;
	protected int exam_level_id;	
	private int audio;


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
	public float getStartAudio() {
		return startAudio;
	}
	public void setStartAudio(float startAudio) {
		this.startAudio = startAudio;
	}
	public float getEndAudio() {
		return endAudio;
	}
	public void setEndAudio(float endAudio) {
		this.endAudio = endAudio;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public int getExam_level_id() {
		return exam_level_id;
	}
	public void setExam_level_id(int exam_level_id) {
		this.exam_level_id = exam_level_id;
	}
	public int getAudio() {
		return audio;
	}
	public void setAudio(int audio) {
		this.audio = audio;
	}




//	public static abstract class SectionEntry implements BaseColumns{
//		public static final String TABLE_NAME = "section";
//		public static final String COLUMN_NAME_NUMBER = "number";
//		public static final String COLUMN_NAME_START_AUDIO = "start_audio";
//		public static final String COLUMN_NAME_END_AUDIO = "end_audio";
//		public static final String COLUMN_NAME_TEXT = "text";
//		public static final String COLUMN_NAME_HINT = "hint";
//		public static final String COLUMN_NAME_EXAM_LEVEL_ID = "exam_level_id";
//	}	
}
