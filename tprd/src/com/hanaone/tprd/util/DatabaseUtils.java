package com.hanaone.tprd.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class DatabaseUtils {
	
//	public static <T, S> S convertObject(T model, Class<S> c){
//		try {
//			Constructor<S> cstr = c.getConstructor();
//			S obj = cstr.newInstance();
//			
//			Field[] fieldsModel = model.getClass().getDeclaredFields();
//			
//			Field[] fieldsPojo = obj.getClass().getDeclaredFields();
//			for(Field fieldPojo: fieldsPojo)
//				for(Field fieldModel: fieldsModel)
//					if(fieldPojo.getName().equals(fieldModel.getName()))
//					{
//						fieldPojo.setAccessible(true);
//						fieldModel.setAccessible(true);
//						fieldPojo.set(obj, fieldModel.get(model));
//					}
//			return obj;
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return null;
//		
//	}
}
