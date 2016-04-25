package com.hanaone.tprd.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hanaone.tprd.Constants;
import com.hanaone.tprd.R;
import com.hanaone.tprd.db.ChoiceDataSet;
import com.hanaone.tprd.db.FileDataSet;
import com.hanaone.tprd.db.QuestionDataSet;
import com.hanaone.tprd.db.SectionDataSet;
import com.hanaone.tprd.util.ImageUtils;
import com.hanaone.tprd.util.PreferenceHandler;

public class ListSectionAdapter extends BaseAdapter implements DownloadListener{
	private Context mContext;
	private LayoutInflater mInflater;
	private ListAdapterListener mListener;
	private List<SectionDataSet> mDataSet;
	private List<Item> mItems;
	//private ArrayList<ResultDataSet> mResults;
	private boolean cheat;
	private boolean isShowHint;
	public ListSectionAdapter(Context mContext, ListAdapterListener mListener) {
		this.mContext = mContext;
		this.mListener = mListener;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		String mode = PreferenceHandler.getQuestionModePreference(mContext);
		cheat = false;
		if(Constants.QUESTION_MODE_PRACTICE.equals(mode) 
				|| Constants.QUESTION_MODE_REVIEW.equals(mode)){
			isShowHint = PreferenceHandler.getHintDisplayPreference(mContext);
			cheat = true;
		}
		

	}

	public void setmDataSet(List<SectionDataSet> mDataSet) {
		this.mDataSet = mDataSet;
		
		mItems = new ArrayList<ListSectionAdapter.Item>();
		for(int i = 0; i < this.mDataSet.size(); i ++){
			SectionDataSet section = this.mDataSet.get(i);
			mItems.add(new SectionItem(section, i));
			for(int j = 0; j < section.getQuestions().size(); j ++){
				mItems.add(new QuestionItem(section, section.getQuestions().get(j), i, j));
			}
		}
		
		this.notifyDataSetChanged();
	}
//	public void setResults(ArrayList<ResultDataSet> results){
//		this.mResults = results;
//	}
	@Override
	public int getCount() {
		if(mItems != null){
			return mItems.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mItems != null && mItems.size() > position){
			return mItems.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = mItems.get(position).getView(mContext, mInflater, convertView, parent, position);
		
		return convertView;
	}
	private void onChoose(QuestionDataSet question, int btn
			, List<Button> btns){
		question.setChoice(btn);
		for(int i = 0; i < btns.size(); i ++){
			if((i + 1) == btn){
				btns.get(i).setBackgroundResource(R.drawable.circle_number_black);	
				btns.get(i).setTextColor(mContext.getResources().getColor(R.color.WHITE));
			} else {
				btns.get(i).setBackgroundResource(R.drawable.circle_number_trans);	
				btns.get(i).setTextColor(mContext.getResources().getColor(R.color.BLACK));				
			}
		}
	}
	
	public interface Item{
		public int getViewType();
		public View getView(Context context, LayoutInflater inflater, View convertView, ViewGroup parent, int position);		
	}
	public class SectionItem implements Item{
		private SectionDataSet section;
		private int sectionNumber;
		public SectionItem(SectionDataSet section, int sectionNumber) {
			this.section = section;
			this.sectionNumber = sectionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent, int position) {
			final SectionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof SectionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_section, parent, false);
				
				holder = new SectionViewHolder();
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_section_question);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_section_hint);
				holder.btnHint = (Button) convertView.findViewById(R.id.btn_section_hint);

				
				convertView.setTag(holder);
			} else {
				holder = (SectionViewHolder) convertView.getTag();

			}
			

			
			// fill data
			if(section.getHint() == null || section.getHint().isEmpty()){
				holder.txtHint.setVisibility(TextView.GONE);
				holder.btnHint.setVisibility(Button.GONE);
			} else {
				holder.txtHint.setText(section.getHint());
				if(cheat){
					holder.btnHint.setVisibility(Button.VISIBLE);
					holder.btnHint.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(holder.txtHint.getVisibility() == TextView.VISIBLE){
								holder.txtHint.setVisibility(TextView.GONE);
								holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
							} else {
								holder.txtHint.setVisibility(TextView.VISIBLE);
								holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
							}
						}
					});
					if(isShowHint){
						holder.txtHint.setVisibility(LinearLayout.VISIBLE);
						holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);					
					} else {
						holder.txtHint.setVisibility(LinearLayout.GONE);
						holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
					}							
				} else {
					holder.btnHint.setVisibility(Button.GONE);
					holder.txtHint.setVisibility(LinearLayout.GONE);
				}
		
			}		
			List<QuestionDataSet> questions = section.getQuestions();

			String txt = "";
			if(questions != null && questions.size() > 1){
				txt += " # [" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
			} else {
				txt += " # [" + questions.get(0).getNumber() + "]";
			}
			txt += section.getText();	
			holder.txtQuestion.setText(txt);			
			
			
			return convertView;
		}
		
		private class SectionViewHolder{
			TextView txtQuestion;
			Button btnHint;
			TextView txtHint;	
		}
	}
	public class QuestionItem implements Item{
		private SectionDataSet section;
		private QuestionDataSet question;
		private int sectionNumber;
		private int questionNumber;
		public QuestionItem(SectionDataSet section, QuestionDataSet question, int sectionNumber, int questionNumber) {
			this.question = question;
			this.section = section;
			this.sectionNumber = sectionNumber;
			this.questionNumber = questionNumber;
		}

		@Override
		public int getViewType() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(Context context, LayoutInflater inflater,
				View convertView, ViewGroup parent, int position) {
			final QuestionViewHolder holder;
			if(convertView == null || !(convertView.getTag() instanceof QuestionViewHolder)){
				convertView = inflater.inflate(R.layout.layout_question_question, parent, false);
				
				holder = new QuestionViewHolder();
				
				
				holder.txtNumber = (TextView) convertView.findViewById(R.id.txt_question_number);
				holder.txtHint = (TextView) convertView.findViewById(R.id.txt_question_hint);
				
				holder.btnChoices = new ArrayList<Button>(4);
				
				holder.btnChoices.add((Button) convertView.findViewById(R.id.btn_question_choice_1));
				holder.btnChoices.add((Button) convertView.findViewById(R.id.btn_question_choice_2));
				holder.btnChoices.add((Button) convertView.findViewById(R.id.btn_question_choice_3));
				holder.btnChoices.add((Button) convertView.findViewById(R.id.btn_question_choice_4));
				
				holder.txtChoices = new ArrayList<TextView>(4);	
				
				holder.txtChoices.add((TextView) convertView.findViewById(R.id.txt_question_choice_1));
				holder.txtChoices.add((TextView) convertView.findViewById(R.id.txt_question_choice_2));
				holder.txtChoices.add((TextView) convertView.findViewById(R.id.txt_question_choice_3));
				holder.txtChoices.add((TextView) convertView.findViewById(R.id.txt_question_choice_4));	
				
				holder.txtQuestion = (TextView) convertView.findViewById(R.id.txt_question_txt);
				holder.imgQuestion = (ImageView) convertView.findViewById(R.id.img_question);
				
				holder.imgChoices = new ArrayList<ImageView>(4);
				holder.imgChoices.add((ImageView) convertView.findViewById(R.id.img_question_choice_1));
				holder.imgChoices.add((ImageView) convertView.findViewById(R.id.img_question_choice_2));
				holder.imgChoices.add((ImageView) convertView.findViewById(R.id.img_question_choice_3));
				holder.imgChoices.add((ImageView) convertView.findViewById(R.id.img_question_choice_4));
				
				
				holder.btnHint = (Button) convertView.findViewById(R.id.btn_question_hint);
				holder.layoutHint = (LinearLayout) convertView.findViewById(R.id.layout_question_hint);
				
				
				convertView.setTag(holder);
								
			} else {
				holder = (QuestionViewHolder) convertView.getTag();
							
			}		
			
			for(int i = 0; i < holder.btnChoices.size(); i ++){
				Button btn = holder.btnChoices.get(i);
				btn.setBackgroundResource(R.drawable.circle_number_trans);
				btn.setText((i + 1) + "");
				btn.setTextColor(mContext.getResources().getColor(R.color.BLACK));
			}	
			
			if(question.getHint() == null || question.getHint().isEmpty()){
				holder.btnHint.setVisibility(Button.GONE);
				holder.layoutHint.setVisibility(LinearLayout.GONE);
			} else {
				holder.txtHint.setText(question.getHint());
				
				if(cheat){
					holder.btnHint.setVisibility(Button.VISIBLE);
					if(isShowHint){
						holder.layoutHint.setVisibility(LinearLayout.VISIBLE);
						holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);	
						
					} else {
						holder.layoutHint.setVisibility(LinearLayout.GONE);
						holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
					}
					holder.btnHint.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							if(holder.layoutHint.getVisibility() == LinearLayout.VISIBLE){
								holder.layoutHint.setVisibility(LinearLayout.GONE);
								holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_black);
							} else {
								holder.layoutHint.setVisibility(LinearLayout.VISIBLE);
								holder.btnHint.setBackgroundResource(R.drawable.ic_image_wb_sunny_cyan);
							}
						}
					});					
				} else {
					holder.btnHint.setVisibility(Button.GONE);
					holder.layoutHint.setVisibility(LinearLayout.GONE);
				}


										
			}
			
			String questionTxt = question.getText(); 
			if(questionTxt != null && !questionTxt.isEmpty()){
				holder.txtQuestion.setText(questionTxt + " (" + question.getMark() + "점)");
			} else {
				holder.txtQuestion.setText("(" + question.getMark() + "점)");
			}
			
			
			holder.txtNumber.setText(question.getNumber() + ". ");
			
			
			List<ChoiceDataSet> choices = question.getChoices();
			if(choices != null){
				for(int i = 0; i < choices.size(); i ++){
					final ChoiceDataSet choice = choices.get(i);
					if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
						holder.txtChoices.get(i).setVisibility(TextView.GONE);
						holder.imgChoices.get(i).setVisibility(ImageView.VISIBLE);	
						if(choice.getImg() !=  null && choice.getImg().getPathLocal() != null && new File(choice.getImg().getPathLocal()).exists()){
							holder.imgChoices.get(i).setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choice.getImg().getPathLocal(), 200, 200));		
						} else {
							// set default image;

							holder.imgChoices.get(i).setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(mContext.getResources(), R.drawable.image_unknown, 100, 100));
							holder.imgChoices.get(i).setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showDownloadDialog(choice.getImg());
								}
							});
						}
											
					}else {
						holder.txtChoices.get(i).setVisibility(TextView.VISIBLE);
						holder.txtChoices.get(i).setText(choice.getContent());
						holder.imgChoices.get(i).setVisibility(ImageView.GONE);						
					}
				}			
				
			}
			for(int i = 0; i < holder.btnChoices.size(); i ++){
				Button btn = holder.btnChoices.get(i);
				final int pos = i + 1;
				btn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						onChoose(question, pos, holder.btnChoices);
					}
				});
			}			
				
	
			// check choice
			Button btn = null;
			if(question.getChoice() > 0){
				btn = holder.btnChoices.get(question.getChoice() - 1);
				btn.setTextColor(mContext.getResources().getColor(R.color.WHITE));
				btn.setBackgroundResource(R.drawable.circle_number_black);
			}
			
			// check result
			
			if(Constants.QUESTION_MODE_REVIEW.equals(PreferenceHandler.getQuestionModePreference(mContext)) ){

				
				//ResultDataSet result = mResults.get(question.getNumber() - 1);

				if(question.getAnswer() > 0){
					btn = holder.btnChoices.get(question.getAnswer() - 1);
					btn.setBackgroundResource(R.drawable.circle_number_green);
				}			
				
				if(question.getChoice() > 0){
					btn = holder.btnChoices.get(question.getChoice() - 1);
					if(question.getChoice() != question.getAnswer()){
						btn.setBackgroundResource(R.drawable.circle_number_red);
					}					
				}														
					
			}			
			return convertView;
		}
		private class QuestionViewHolder{
			TextView txtNumber;
			TextView txtQuestion;
			ImageView imgQuestion;
			Button btnHint;
			LinearLayout layoutHint;
			TextView txtHint;		
			List<Button> btnChoices;
			List<TextView> txtChoices;
			List<ImageView> imgChoices;
		}
	}
	public void showDownloadDialog(final FileDataSet file){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_dialog_download_cancel);
		dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		dialog.show();
		dialog.setCancelable(false);
		
		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});	
		
		DownloadInfo info = new DownloadInfo();
		info.setPrgBar((ProgressBar)dialog.findViewById(R.id.prg_dialog_download));
		info.setTxtPer((TextView)dialog.findViewById(R.id.txt_dialog_file_progress));
		info.setTxtSize((TextView)dialog.findViewById(R.id.txt_dialog_file_size));
		
		new DownloadFileAdapter(file, info, mContext, new DatabaseAdapter(mContext), dialog, this).execute();
		
	}

	@Override
	public void onFinishNotify(boolean flag) {
		if(flag){
			this.notifyDataSetChanged();
		}
	}	
}
