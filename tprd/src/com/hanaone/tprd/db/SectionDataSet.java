package com.hanaone.tprd.db;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class SectionDataSet implements Parcelable {
	protected int id;
	protected int number;
	protected float startAudio;
	protected float endAudio;
	protected String text;
	protected String hint;
	protected List<QuestionDataSet> questions;
	public SectionDataSet() {

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
	public List<QuestionDataSet> getQuestions() {
		return questions;
	}
	public void setQuestions(List<QuestionDataSet> questions) {
		this.questions = questions;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
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
		dest.writeFloat(startAudio);
		dest.writeFloat(endAudio);
		dest.writeString(text);
		dest.writeString(hint);
		dest.writeTypedList(questions);
		
	}
	public static final Parcelable.Creator<SectionDataSet> CREATOR
	= new Parcelable.Creator<SectionDataSet>() {

		@Override
		public SectionDataSet createFromParcel(Parcel source) {
			
			return new SectionDataSet(source);
		}

		@Override
		public SectionDataSet[] newArray(int size) {
			return new SectionDataSet[size];
		}


	};
	protected SectionDataSet(Parcel in){
		id = in.readInt();
		number = in.readInt();
		startAudio = in.readFloat();
		endAudio = in.readFloat();
		text = in.readString();
		hint = in.readString();
		
		questions = new ArrayList<QuestionDataSet>();
		in.readTypedList(questions, QuestionDataSet.CREATOR);
	}		
}
