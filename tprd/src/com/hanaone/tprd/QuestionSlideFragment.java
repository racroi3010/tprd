package com.hanaone.tprd;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.hanaone.tprd.adapter.DatabaseAdapter;
import com.hanaone.tprd.adapter.DownloadFileAdapter;
import com.hanaone.tprd.adapter.DownloadInfo;
import com.hanaone.tprd.adapter.DownloadListener;
import com.hanaone.tprd.adapter.ListAdapterListener;
import com.hanaone.tprd.db.ChoiceDataSet;
import com.hanaone.tprd.db.FileDataSet;
import com.hanaone.tprd.db.QuestionDataSet;
import com.hanaone.tprd.db.ResultDataSet;
import com.hanaone.tprd.db.SectionDataSet;
import com.hanaone.tprd.util.ImageUtils;
import com.hanaone.tprd.util.PreferenceHandler;

import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionSlideFragment extends Fragment implements DownloadListener {
	private static final String ARG_PAGE = "page";
	private static final String ARG_Listener = "listener";
	private ArrayList<SectionDataSet> mSections;
//	private boolean cheat;
	public QuestionSlideFragment() {
		
	}

//	public QuestionSlideFragment(List<SectionDataSet> sections) {
//		this.mSections = sections;
//	}
	
	public static QuestionSlideFragment create(ArrayList<SectionDataSet> sections){
		QuestionSlideFragment fragment = new QuestionSlideFragment();
		//mSections = sections;
		Bundle data = new Bundle();
		data.putParcelableArrayList(ARG_PAGE, sections);
		fragment.setArguments(data);
		
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSections = getArguments().getParcelableArrayList(ARG_PAGE);
		String mode = PreferenceHandler.getQuestionModePreference(getActivity());	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup sectionsView = (ViewGroup) inflater.inflate(R.layout.layout_question_fragment, container, false);
		
		LinearLayout layoutSections = (LinearLayout) sectionsView.findViewById(R.id.layout_sections);
		
		for(final SectionDataSet section: mSections){
			ViewGroup sectionView = (ViewGroup) inflater.inflate(R.layout.layout_question_section, layoutSections, false);
			TextView txtSectionQuestion = (TextView) sectionView.findViewById(R.id.txt_section_question);
			
			LinearLayout layoutQuestions = (LinearLayout) sectionView.findViewById(R.id.layout_questions);
			
			final TextView txtSectionHint = (TextView) sectionView.findViewById(R.id.txt_section_hint);
		
			
			if(section.getHint() == null || section.getHint().isEmpty()){
				txtSectionHint.setVisibility(TextView.GONE);
			} else {
				txtSectionHint.setVisibility(LinearLayout.VISIBLE);
				txtSectionHint.setText(section.getHint());
			}
			ImageView imgSectionQuestion = (ImageView) sectionView.findViewById(R.id.img_section_question);
			if(Constants.FILE_TYPE_IMG.equals(section.getType())){
				imgSectionQuestion.setVisibility(ImageView.VISIBLE);	
				
				if(section.getImg() !=  null && section.getImg().getPathLocal() != null && new File(section.getImg().getPathLocal()).exists()){
					imgSectionQuestion.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(section.getImg().getPathLocal(), 300, 300));
				} else {
					// set default image;
					imgSectionQuestion.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.image_unknown));
					
					imgSectionQuestion.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showDownloadDialog(section.getImg());
						}
					});
				}				
			} else {
				imgSectionQuestion.setVisibility(ImageView.GONE);	
			}				
			
			List<QuestionDataSet> questions = section.getQuestions();
			String txt = "";
			if(questions != null && !questions.isEmpty()){
				txt += " # [" + questions.get(0).getNumber() + "~" + questions.get(questions.size() - 1).getNumber() + "] ";
			}
			txt += section.getText();
			
			
			txtSectionQuestion.setText(txt);			
			
			if(questions != null){
				for(final QuestionDataSet question: questions){
					ViewGroup questionView = (ViewGroup) inflater.inflate(R.layout.layout_question_question, layoutQuestions, false);
					
					TextView txtNumber = (TextView) questionView.findViewById(R.id.txt_question_number);
					TextView txtQuestionHint = (TextView) questionView.findViewById(R.id.txt_question_hint);
					
					final List<Button> btnChoices = new ArrayList<Button>(4);
					final List<TextView> txtChoices = new ArrayList<TextView>(4);
					final List<ImageView> imgChoices = new ArrayList<ImageView>(4);
					
					btnChoices.add((Button) questionView.findViewById(R.id.btn_question_choice_1));
					btnChoices.add((Button) questionView.findViewById(R.id.btn_question_choice_2));
					btnChoices.add((Button) questionView.findViewById(R.id.btn_question_choice_3));
					btnChoices.add((Button) questionView.findViewById(R.id.btn_question_choice_4));
					
					
					txtChoices.add((TextView) questionView.findViewById(R.id.txt_question_choice_1));
					txtChoices.add((TextView) questionView.findViewById(R.id.txt_question_choice_2));
					txtChoices.add((TextView) questionView.findViewById(R.id.txt_question_choice_3));
					txtChoices.add((TextView) questionView.findViewById(R.id.txt_question_choice_4));	
	
					imgChoices.add((ImageView) questionView.findViewById(R.id.img_question_choice_1));
					imgChoices.add((ImageView) questionView.findViewById(R.id.img_question_choice_2));
					imgChoices.add((ImageView) questionView.findViewById(R.id.img_question_choice_3));
					imgChoices.add((ImageView) questionView.findViewById(R.id.img_question_choice_4));
					
					
					TextView txtQuestionTxt = (TextView) questionView.findViewById(R.id.txt_question_txt);
					ImageView imgQuestion = (ImageView) questionView.findViewById(R.id.img_question);					
					
					//final LinearLayout layoutQuestionHint = (LinearLayout) questionView.findViewById(R.id.layout_question_hint);
					if(question.getHint() == null || question.getHint().isEmpty()){
						txtQuestionHint.setVisibility(LinearLayout.GONE);
					} else {
						txtQuestionHint.setVisibility(LinearLayout.VISIBLE);
						txtQuestionHint.setText(question.getHint());
					}
						
					
					txt = question.getText();
					txtQuestionTxt.setVisibility(TextView.VISIBLE);
					if( txt != null && !txt.isEmpty()){
						txtQuestionTxt.setText(txt + " (" + question.getMark() + "점)");
					} else {
						txtQuestionTxt.setText("(" + question.getMark() + "점)");
					}
					
					if(Constants.FILE_TYPE_IMG.equals(question.getType())){
						imgQuestion.setVisibility(ImageView.VISIBLE);	
						if(question.getImg() !=  null && question.getImg().getPathLocal() != null && new File(question.getImg().getPathLocal()).exists()){
							imgQuestion.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(question.getImg().getPathLocal(), 300, 300));
						} else {
							// set default image;
							imgQuestion.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.image_unknown));
							
							imgQuestion.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showDownloadDialog(question.getImg());
								}
							});
						}	
					} else {
						imgQuestion.setVisibility(ImageView.GONE);
					}
					
					
					txtNumber.setText(question.getNumber() + ". ");
					
					
					List<ChoiceDataSet> choices = question.getChoices();
					if(choices != null){
						for(int i = 0; i < 4; i ++){
							final ChoiceDataSet choice = choices.get(i);
							if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
								txtChoices.get(i).setVisibility(TextView.GONE);
								imgChoices.get(i).setVisibility(ImageView.VISIBLE);	
								if(choice.getImg() !=  null && choice.getImg().getPathLocal() != null && new File(choice.getImg().getPathLocal()).exists()){
									imgChoices.get(i).setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(choice.getImg().getPathLocal(), 200, 200));		
								} else {
									// set default image;
									imgChoices.get(i).setImageBitmap(ImageUtils.decodeSampledBitmapFromResource(getActivity().getResources(), R.drawable.image_unknown, 100, 100));
									imgChoices.get(i).setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View v) {
											showDownloadDialog(choice.getImg());
										}
									});
								}
													
							}else {
								txtChoices.get(i).setVisibility(TextView.VISIBLE);
								txtChoices.get(i).setText(choice.getContent());
								imgChoices.get(i).setVisibility(ImageView.GONE);						
							}
						}		
						
					}
					for(int i = 0; i < btnChoices.size(); i ++){
						Button btn = btnChoices.get(i);
						final int pos = i + 1;
						btn.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								onChoose(question, pos, btnChoices);
							}
						});
					}				
					// check choice
					Button btn = null;
					if(question.getChoice() > 0){
						btn = btnChoices.get(question.getChoice() - 1);
						btn.setTextColor(getResources().getColor(R.color.WHITE));
						btn.setBackgroundResource(R.drawable.circle_number_black);
					}									
					
					layoutQuestions.addView(questionView);
				}
			}
			
			
			layoutSections.addView(sectionView);
		}
		

		
		return sectionsView;
	}
	
	private void onChoose(QuestionDataSet question, int btn
			, List<Button> btns){
		question.setChoice(btn);
		for(int i = 0; i < btns.size(); i ++){
			if((i + 1) == btn){
				btns.get(i).setBackgroundResource(R.drawable.circle_number_black);	
				btns.get(i).setTextColor(getActivity().getResources().getColor(R.color.WHITE));
			} else {
				btns.get(i).setBackgroundResource(R.drawable.circle_number_trans);	
				btns.get(i).setTextColor(getActivity().getResources().getColor(R.color.BLACK));				
			}
		}
	}
	public void showDownloadDialog(final FileDataSet file){
		final Dialog dialog = new Dialog(getContext());
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
		
		new DownloadFileAdapter(file, info, getContext(), new DatabaseAdapter(getContext()), dialog, this).execute();
		
	}

	@Override
	public void onFinishNotify(boolean flag) {
		if(flag){
			FragmentTransaction tr = getFragmentManager().beginTransaction();
			tr.replace(R.id.scr_layout_sections, this);
			tr.commit();
			
		}
	}	
	
}
