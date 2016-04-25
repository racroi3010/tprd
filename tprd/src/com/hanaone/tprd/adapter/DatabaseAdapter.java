package com.hanaone.tprd.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import android.content.Context;
import android.util.Log;

import com.hanaone.http.DownloadHelper;
import com.hanaone.tprd.Constants;
import com.hanaone.tprd.db.ChoiceDataSet;
import com.hanaone.tprd.db.ExamDataSet;
import com.hanaone.tprd.db.FileDataSet;
import com.hanaone.tprd.db.LevelDataSet;
import com.hanaone.tprd.db.QuestionDataSet;
import com.hanaone.tprd.db.SectionDataSet;
import com.hanaone.tprd.db.model.Choice;
import com.hanaone.tprd.db.model.ExamLevel;
import com.hanaone.tprd.db.model.Examination;
import com.hanaone.tprd.db.model.FileExtra;
import com.hanaone.tprd.db.model.Question;
import com.hanaone.tprd.db.model.Section;
import com.hanaone.tprd.db.sample.QuestionSample;
import com.hanaone.tprd.db.sample.SectionSample;
import com.hanaone.tprd.util.Config;
import com.hanaone.tprd.util.DatabaseUtils;

public class DatabaseAdapter{
	private Context mContext;
	private DatabaseHelper dbHelper;
	private DownloadHelper dlHelper;
	public DatabaseAdapter(Context context){
		this.mContext = context;
		this.dbHelper = DatabaseHelper.getInstance(mContext);
		this.dlHelper = new DownloadHelper(mContext);
	}
	
	public List<ExamDataSet> getAllExam(){
		List<Examination> examModels = dbHelper.selectAllExam();
		if(Config.LOGGING){
			Log.w("size of exam", examModels.size() + "");
		}
		List<ExamDataSet> list = new ArrayList<ExamDataSet>();
		for(Examination exam: examModels){
			ExamDataSet data = DatabaseUtils.convertObject(exam, ExamDataSet.class);
			List<LevelDataSet> levels = new ArrayList<LevelDataSet>();
			
			List<ExamLevel> levelModels = dbHelper.selectExamLevelByExamNumber(exam.getNumber());
			if(levelModels != null){
				for(ExamLevel lmodel: levelModels){
					LevelDataSet l = DatabaseUtils.convertObject(lmodel, LevelDataSet.class);
					
					l.setId(lmodel.getId());
					l.setNumber(lmodel.getNumber());
					l.setLabel(lmodel.getLabel());

					l.setActive(lmodel.getActive());
					
					FileExtra audio = dbHelper.selectFileById(lmodel.getAudio_id());
					List<FileDataSet> audios = new ArrayList<FileDataSet>();
					audios.add(DatabaseUtils.convertObject(audio, FileDataSet.class));				
					l.setAudio(audios);
					
					FileExtra pdf = dbHelper.selectFileById(lmodel.getPdf_id());
					l.setPdf(DatabaseUtils.convertObject(pdf, FileDataSet.class));	
					
					FileExtra txt = dbHelper.selectFileById(lmodel.getTxt_id());
					l.setTxt(DatabaseUtils.convertObject(txt, FileDataSet.class));
					
					levels.add(l);
				}
				
				
				data.setLevels(levels);
				
				list.add(data);
			}

			
		}
		return list;
	}
	public boolean checkExam(int examNumber){
		Examination exam = dbHelper.selectExamByNumber(examNumber);
		
		if(exam != null) return true;
		return false;
		
	}
	public void addExam(ExamDataSet examDataSet){
		//Examination exam = DatabaseUtils.examPojo2Model(examDataSet);
		Examination exam = DatabaseUtils.convertObject(examDataSet, Examination.class);		
		dbHelper.insert(exam);
		
		List<LevelDataSet> levels = examDataSet.getLevels();
		if(levels != null){
			for(LevelDataSet data: levels){
				
				ExamLevel examLevel = DatabaseUtils.convertObject(data, ExamLevel.class);
				examLevel.setExam_id(exam.getNumber());
				examLevel.setNumber(data.getNumber());
				examLevel.setLabel(data.getLabel());

				
				examLevel.setAudio_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getAudio().get(0), FileExtra.class)));
				examLevel.setPdf_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getPdf(), FileExtra.class)));
				examLevel.setTxt_id((int)dbHelper.insert(DatabaseUtils.convertObject(data.getTxt(), FileExtra.class)));
				
				examLevel.setActive(0);
				
				
				data.setId((int)dbHelper.insert(examLevel));
			}
		}
		
		
	}
//	public String getLevelLabel(int levelId){
//		ExamLevel examLevelModel = dbHelper.selectExamLevelById(levelId);
//		Level levelModel = dbHelper.selectLevelById(examLevelModel.getLevel_id());
//		if(levelModel != null){
//			return levelModel.getLabel();					
//		}		
//		return null;
//	}
	public LevelDataSet getLevel(int levelId){
		
		
		ExamLevel examLevelModel = dbHelper.selectExamLevelById(levelId);
		LevelDataSet levelDataSet = DatabaseUtils.convertObject(examLevelModel, LevelDataSet.class);
		
		levelDataSet.setNumber(examLevelModel.getNumber());
		levelDataSet.setLabel(examLevelModel.getLabel());
		
		levelDataSet.setActive(examLevelModel.getActive());
		
		FileExtra audio = dbHelper.selectFileById(examLevelModel.getAudio_id());
		List<FileDataSet> audios = new ArrayList<FileDataSet>();
		audios.add(DatabaseUtils.convertObject(audio, FileDataSet.class));
		levelDataSet.setAudio(audios);
		
		FileExtra pdf = dbHelper.selectFileById(examLevelModel.getPdf_id());
		levelDataSet.setPdf(DatabaseUtils.convertObject(pdf, FileDataSet.class));	
		
		FileExtra txt = dbHelper.selectFileById(examLevelModel.getTxt_id());
		levelDataSet.setTxt(DatabaseUtils.convertObject(txt, FileDataSet.class));	
		
		List<Section> sections = dbHelper.selectSectionByExamLevelId(examLevelModel.getId());
		List<SectionDataSet> sectiondatas = new ArrayList<SectionDataSet>();
		for(Section section:sections){
			SectionDataSet sectionData = DatabaseUtils.convertObject(section, SectionDataSet.class);
			
			List<Question> questionModels = dbHelper.selectQuestionBySectionId(section.getId());
			List<QuestionDataSet> questionDatas = new ArrayList<QuestionDataSet>();
			for(Question questionModel: questionModels){
				QuestionDataSet questionData = DatabaseUtils.convertObject(questionModel, QuestionDataSet.class);
				List<Choice> choiceModels = dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDatas = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choice = DatabaseUtils.convertObject(choiceModel, ChoiceDataSet.class);
					if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
						FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
						if(imgModel != null){
							choice.setImg(DatabaseUtils.convertObject(imgModel, FileDataSet.class));	
						} else {
							choice.setImg(new FileDataSet());
						}
											
					}

					
					choiceDatas.add(choice);
					
				}
				questionData.setChoices(choiceDatas);
				questionDatas.add(questionData);
			}
			sectionData.setQuestions(questionDatas);
			
			sectiondatas.add(sectionData);
		}
		levelDataSet.setSections(sectiondatas);
		
		return levelDataSet;
	}
	public int updateLevelActive(int levelId, boolean active){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			if(active){
				levelModel.setActive(1);
			} else {
				levelModel.setActive(0);
			}
			return this.dbHelper.update(levelModel);
		}
		return -1;
	}
	public int updateLevelAudio(int levelId, String audioLocal, String audioRemote){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			FileExtra file = new FileExtra();
			file.setName("audio_" + levelId);
			file.setPathLocal(audioLocal);
			file.setPathRemote(audioRemote);
			file.setType(Constants.FILE_TYPE_MP3);
			
			long audioId = this.dbHelper.insert(file);
			if(audioId != -1){
				levelModel.setAudio_id((int)audioId);
				return this.dbHelper.update(levelModel);
			}
		}
		
		return -1;
	}
	public int updateLevelTxt(int levelId, String txtLocal, String txtRemote){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			FileExtra file = new FileExtra();
			file.setName("audio_" + levelId);
			file.setPathLocal(txtLocal);
			file.setPathRemote(txtRemote);
			file.setType(Constants.FILE_TYPE_MP3);
			
			long audioId = this.dbHelper.insert(file);
			if(audioId != -1){
				levelModel.setTxt_id((int)audioId);
				return this.dbHelper.update(levelModel);
			}
		}
		
		return -1;
	}
	public int updateLevelScore(int levelId, int score){
		ExamLevel levelModel = this.dbHelper.selectExamLevelById(levelId);
		if(levelModel != null){
			levelModel.setScore(score);
			return this.dbHelper.update(levelModel);
		}
		
		return -1;		
	}
	public void addSection(SectionDataSet data, int levelId){
		Section model = DatabaseUtils.convertObject(data, Section.class);
		model.setExam_level_id(levelId);	
		long sectionId = dbHelper.insert(model);
		
		
		List<QuestionDataSet> questions = data.getQuestions();
		for(QuestionDataSet questionData: questions){
			Question questionModel = DatabaseUtils.convertObject(questionData, Question.class);
			questionModel.setSection_id((int)sectionId);
			long questionId = dbHelper.insert(questionModel);
			
			List<ChoiceDataSet> choiceDatas = questionData.getChoices();
			for(ChoiceDataSet choiceData: choiceDatas){
				Choice choice = DatabaseUtils.convertObject(choiceData, Choice.class);
				choice.setQuestion_id((int)questionId);
				
				
				
				if(Constants.FILE_TYPE_IMG.equals(choiceData.getType())){
					FileDataSet img = choiceData.getImg();
					FileExtra imgModel = DatabaseUtils.convertObject(img, FileExtra.class);
					long fileId = dbHelper.insert(imgModel);
					choice.setFile_id((int)fileId);
				}

				
				
				
//				// image type
//				if(Constants.FILE_TYPE_IMG.equals(questionData.getChoiceType())){
//					// download
//					String path = Constants.getPath(mContext, Constants.PATH_FILE);
//					
//					path += "/img_" + sectionId + "_" + questionId + "_" +  choiceData.getLabel() + ".jpg";
//					try {
//						this.dlHelper.downloadFile(choice.getText(), path);
//						choice.setText(path);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
				
				dbHelper.insert(choice);
			}
		}
		
	}
	
	public LevelDataSet generateSampleTest(int level){
		LevelDataSet levelData = new LevelDataSet();
		levelData.setNumber(level);
		levelData.setLabel(level + "");
		levelData.setActive(Constants.STATUS_ACTIVE);
		levelData.setScore(100);		
		
		List<SectionDataSet> sections = new ArrayList<SectionDataSet>();
		List<FileDataSet> audios = new ArrayList<FileDataSet>();
		
		levelData.setSections(sections);
		levelData.setAudio(audios);
		
		Random random = new Random();
		
		// question 1 - 20
		for(int i = 1; i < 21; i ++){
			SectionDataSet section = new SectionDataSet();			
			section.setNumber(i);
			List<QuestionDataSet> questionDatasets = new ArrayList<QuestionDataSet>();
				
			
			List<QuestionSample> questionModels = this.dbHelper.selectionQuestionByExamLevelAndQuestionNumber(35, level, i);
			
			if(questionModels != null && questionModels.size() > 0){
				int rdIdx = random.nextInt(questionModels.size());
				
				QuestionSample questionModel = questionModels.get(rdIdx);
				QuestionDataSet questionDataset = DatabaseUtils.convertObject(questionModel, QuestionDataSet.class);			
				
				List<Choice> choiceModels = this.dbHelper.selectChoiceByQuestionId(questionModel.getId());
				List<ChoiceDataSet> choiceDataset = new ArrayList<ChoiceDataSet>();
				for(Choice choiceModel: choiceModels){
					ChoiceDataSet choice = DatabaseUtils.convertObject(choiceModel, ChoiceDataSet.class);
					if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
						FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
						choice.setImg(DatabaseUtils.convertObject(imgModel, FileDataSet.class));						
					}
					
					choiceDataset.add(choice);											
				}
				Section sectionModel = this.dbHelper.selectSectionByQuestionId(questionModel.getId());
				questionDataset.setChoices(choiceDataset);				
				questionDatasets.add(questionDataset);
				
				section = DatabaseUtils.convertObject(sectionModel, SectionDataSet.class);
				
				section.setQuestions(questionDatasets);	
				section.setStartAudio(questionDataset.getStartAudio());
				section.setEndAudio(questionDataset.getEndAudio());
				

				FileExtra audio = this.dbHelper.selectFileById(questionModel.getAudio());
				boolean checkAudio = false;
				for(FileDataSet fileTemp: audios){
					if(fileTemp.getId() == audio.getId()){
						audios.add(fileTemp);
						checkAudio = true;
						break;
					}
				}				
				if(!checkAudio){
					FileDataSet fileData = DatabaseUtils.convertObject(audio, FileDataSet.class);
					fileData.setId(audio.getId());
					audios.add(fileData);
				}
				

				sections.add(section);
			}		
			
			
		}	
		
		// question 21 - 30
		for(int i = 6; i < 21; i ++){
			List<SectionSample> sectionModels = this.dbHelper.selectSectionByExamLevelandSectionNumber(35, level, i);
			if(sectionModels != null && sectionModels.size() >  0){
				int rdIdx = random.nextInt(sectionModels.size());
				SectionSample sectionModel = sectionModels.get(rdIdx);			
				SectionDataSet section = DatabaseUtils.convertObject(sectionModel, SectionDataSet.class);
				
				List<Question> questionModels = this.dbHelper.selectQuestionBySectionId(sectionModel.getId());
				List<QuestionDataSet> questionDatasets = new ArrayList<QuestionDataSet>();
				for(Question questionModel: questionModels){
					QuestionDataSet questionDataset = DatabaseUtils.convertObject(questionModel, QuestionDataSet.class);
					
					List<Choice> choiceModels = this.dbHelper.selectChoiceByQuestionId(questionModel.getId());
					List<ChoiceDataSet> choiceDataset = new ArrayList<ChoiceDataSet>();
					for(Choice choiceModel: choiceModels){
						ChoiceDataSet choice = DatabaseUtils.convertObject(choiceModel, ChoiceDataSet.class);
						if(Constants.FILE_TYPE_IMG.equals(choice.getType())){
							FileExtra imgModel = this.dbHelper.selectFileById(choiceModel.getFile_id());
							choice.setImg(DatabaseUtils.convertObject(imgModel, FileDataSet.class));						
						}
						
						choiceDataset.add(choice);
						
					}
					
					questionDataset.setChoices(choiceDataset);
					
					
					questionDatasets.add(questionDataset);
				}
				
				section.setQuestions(questionDatasets);
				FileExtra audio = this.dbHelper.selectFileById(sectionModel.getAudio());
				boolean checkAudio = false;
				for(FileDataSet fileTemp: audios){
					if(fileTemp.getId() == audio.getId()){
						audios.add(fileTemp);
						checkAudio = true;
						break;
					}
				}				
				if(!checkAudio){
					FileDataSet fileData = DatabaseUtils.convertObject(audio, FileDataSet.class);
					fileData.setId(audio.getId());
					audios.add(fileData);
				}
				
				sections.add(section);
				
			}
		}
		
		
		return levelData;
	}
	public boolean checkLevel(int level){
		List<QuestionSample> questionModels = this.dbHelper.selectionQuestionByExamLevelAndQuestionNumber(35, level, 1);	
		if(questionModels.size() == 0){
			return false;
		}
		return true;
	}
	public int updateFile(FileDataSet file){
		if(file == null) return -1;
		FileExtra fileModel = DatabaseUtils.convertObject(file, FileExtra.class);
		if(fileModel != null){
			return this.dbHelper.update(fileModel);
		}
		return -1;
	}
	
}
