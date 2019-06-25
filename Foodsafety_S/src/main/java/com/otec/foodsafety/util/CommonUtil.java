package com.otec.foodsafety.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CommonUtil {

	/**
	 * 
	 * @param source
	 * @param target
	 * @throws Exception
	 */
	public static void copyBean(Object source, Object target) throws Exception {
        copyBean(source, target, true);
	}

    /**
     *
     * @param source
     * @param target
     * @param ignoreNullValue
     * @throws Exception
     */
	public static void copyBean(Object source, Object target, boolean ignoreNullValue) throws Exception {
		if (source == null || target == null)
			return;
		PropertyDescriptor[] source_pds = BeanUtils
				.getPropertyDescriptors(source.getClass());
		for (PropertyDescriptor spd : source_pds) {
			PropertyDescriptor targetPd = BeanUtils.getPropertyDescriptor(
					target.getClass(), spd.getName());
			Method readMethod = spd.getReadMethod();
			Object value = readMethod.invoke(source);
			// 如果ignoreNullValue等于true，忽略源对象中的null数值的字段。
            if (ignoreNullValue && value == null)
                continue;
			if (targetPd == null)
				continue;
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod == null)
				continue;
			writeMethod.invoke(target, value);
		}
	}

	public static boolean listIsEmpty(List<?> list) {
		if (list == null || list.size() == 0) {
			return true;
		}
		return false;
	}

	public static String list2String(List<String> list) {
		return StringUtils.join(list, ",");
	}
	
	public static List<String> string2List(String str) {
		return Arrays.asList(StringUtils.split(str, ","));
	}
	
	public static List<String> string2List(String str, char separatorChar) {
		return Arrays.asList(StringUtils.split(str, separatorChar));
	}

	public static String bean2Xml(Object object, Class<?> load)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(load);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);
		StringWriter xml = new StringWriter();
		marshaller.marshal(object, xml);
		return xml.toString();
	}

	public static Object xml2Bean(String xml, Class<?> load)
			throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(load);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return unmarshaller.unmarshal(string2Reader(xml));
	}

	public static void upload(byte[] bytes, String dirName, String fileName) {
		try {
			File dir = new File(dirName);
			if (!dir.exists()) {
				dir.mkdir();
			}
			OutputStream os;
			os = new FileOutputStream(dirName + File.separator + fileName);
			os.write(bytes);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	public static String reader2String(Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		StringBuffer buffer = new StringBuffer();
		String line = " ";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	public static Reader string2Reader(String str) {
		Reader r = new StringReader(str);
		return r;
	}
	
	/**
	 * 将时间精确到毫秒+img做为文件名
	 * 
	 * @param fileNameInput
	 * @return
	 */
	public static String rename(String fileNameInput) {
		String fileNameOutput = null;
		String file[] = fileNameInput.split("\\.");
		fileNameOutput = UUID.randomUUID().toString() + "_img." + file[1];
		return fileNameOutput;
	}

}
