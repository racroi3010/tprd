package com.hanaone.tprd.db;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestionDataSet implements Parcelable{
	protected int id;
	protected int number;
	protected int mark;
	protected String text;
	protected List<ChoiceDataSet> choices;
	protected int answer;
	protected int choice;
	protected String type;

	protected String hint;
	protected float startAudio;
	protected float endAudio;
	public QuestionDataSet() {
		choice = -1;
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
	public int getMark() {
		return mark;
	}
	public void setMark(int mark) {
		this.mark = mark;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<ChoiceDataSet> getChoices() {
		return choices;
	}
	public void setChoices(List<ChoiceDataSet> choices) {
		this.choices = choices;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	
	public int getChoice() {
		return choice;
	}
	public void setChoice(int choice) {
		this.choice = choice;
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
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		
		dest.writeInt(id);
		dest.writeInt(number);
		dest.writeInt(mark);
		dest.writeString(text);
		dest.writeTypedList(choices);
		dest.writeInt(answer);
		dest.writeInt(choice);
		dest.writeString(type);
		dest.writeString(hint);
		dest.writeFloat(startAudio);
		dest.writeFloat(endAudio);
		
	}
	public static final Parcelable.Creator<QuestionDataSet> CREATOR
	= new Parcelable.Creator<QuestionDataSet>() {

		@Override
		public QuestionDataSet createFromParcel(Parcel source) {
			
			return new QuestionDataSet(source);
		}

		@Override
		public QuestionDataSet[] newArray(int size) {
			return new QuestionDataSet[size];
		}


	};
	protected QuestionDataSet(Parcel in){	
		id = in.readInt();
		number = in.readInt();
		mark = in.readInt();
		
		text = in.readString();
		choices = new ArrayList<ChoiceDataSet>();
		in.readTypedList(choices, ChoiceDataSet.CREATOR);
		
		answer = in.readInt();
		choice = in.readInt();
		type = in.readString();
		hint = in.readString();
		startAudio = in.readFloat();
		endAudio = in.readFloat();
	}		
}
