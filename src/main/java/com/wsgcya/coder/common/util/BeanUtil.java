package com.wsgcya.coder.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class BeanUtil {
	public static void copyProperties(Object source, Object target) throws BeansException {
		BeanUtils.copyProperties(source, target);
	}
	public static void copyToMap(Object source, Map<String,Object> target) throws BeansException {
		BeanUtil.copyToMap(source, target,(String[]) null);
	}
	public static Map<String,Object> convertToMap(Object source) throws BeansException {
		Map<String,Object> map = new HashMap<>();
		BeanUtil.copyToMap(source, map);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static List<Map> convertToMapList(List<Object> list) throws BeansException {
		List<Map> mapList = new ArrayList<>();
		for(Object source: list){
			Map<String,Object> map = new HashMap<>();
			BeanUtil.copyToMap(source, map);
			mapList.add(map);
		}

		return mapList;
	}

	private static void copyToMap(Object source, Map<String,Object> target,String... ignoreProperties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = source.getClass();

		PropertyDescriptor[] sourcePds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

		for (PropertyDescriptor sourcePd : sourcePds) {
			Method readMethod = sourcePd.getReadMethod();
			if (readMethod != null && (ignoreProperties == null || (!ignoreList.contains(sourcePd.getName())))) {

				try {
					if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
						readMethod.setAccessible(true);
					}
					Object value = readMethod.invoke(source);
					String name = readMethod.getName().substring(3);
					String fieldName = name.substring(0,1).toLowerCase()+name.substring(1);
					if("class".equals(fieldName)){
						continue;
					}
					target.put(fieldName, value);
				}
				catch (Throwable ex) {
					throw new FatalBeanException(
							"Could not copy property '" + sourcePd.getName() + "' from source to target", ex);
				}

			}
		}
	}

}

