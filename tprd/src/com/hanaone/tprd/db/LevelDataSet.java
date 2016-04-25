package com.hanaone.tprd.db;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class LevelDataSet implements Parcelable {
	private int id;
	private int number;
	private String label;
	private List<SectionDataSet> sections;
	private int active;
	private FileDataSet txt;
	private List<FileDataSet> audio;	
	private FileDataSet pdf;
	private int score;
	private int maxScore;
	private int color;
	private int exam_id;

	public LevelDataSet() {

	}
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
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<SectionDataSet> getSections() {
		return sections;
	}
	public void setSections(List<SectionDataSet> sections) {
		this.sections = sections;
	}
	
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	
	public List<FileDataSet> getAudio() {
		return audio;
	}
	public void setAudio(List<FileDataSet> audio) {
		this.audio = audio;
	}
	public FileDataSet getPdf() {
		return pdf;
	}
	public void setPdf(FileDataSet pdf) {
		this.pdf = pdf;
	}
	
	public FileDataSet getTxt() {
		return txt;
	}
	public void setTxt(FileDataSet txt) {
		this.txt = txt;
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
	
	public int getExam_id() {
		return exam_id;
	}
	public void setExam_id(int exam_id) {
		this.exam_id = exam_id;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		
		dest.writeInt(id);
		dest.writeInt(number);
		dest.writeString(label);
		dest.writeTypedList(sections);
		dest.writeInt(active);
		dest.writeParcelable(txt, flags);
		dest.writeTypedList(audio);

		dest.writeParcelable(pdf, flags);
		dest.writeInt(score);
		dest.writeInt(maxScore);
		dest.writeInt(color);
		dest.writeInt(exam_id);
		
	}
	public static final Parcelable.Creator<LevelDataSet> CREATOR
	= new Parcelable.Creator<LevelDataSet>() {

		@Override
		public LevelDataSet createFromParcel(Parcel source) {
			
			return new LevelDataSet(source);
		}

		@Override
		public LevelDataSet[] newArray(int size) {
			return new LevelDataSet[size];
		}


	};
	protected LevelDataSet(Parcel in){
	
		id = in.readInt();
		number = in.readInt();
		label = in.readString();
		sections = new ArrayList<SectionDataSet>();
		in.readTypedList(sections, SectionDataSet.CREATOR);
		
		active = in.readInt();
		
		txt = in.readParcelable(FileDataSet.class.getClassLoader());
		audio = new ArrayList<FileDataSet>();
		in.readTypedList(audio, FileDataSet.CREATOR);
		pdf = in.readParcelable(FileDataSet.class.getClassLoader());
		
		score = in.readInt();
		maxScore = in.readInt();
		color = in.readInt();
		
		exam_id = in.readInt();
	}	
	
}
