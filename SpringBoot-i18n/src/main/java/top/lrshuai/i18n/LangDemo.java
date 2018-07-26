package top.lrshuai.i18n;

import java.util.Locale;

public class LangDemo {

	public static void main(String[] args) {
		
	//返回java所支持的全部国家和语言的数组
	  Locale[] localeList=Locale.getAvailableLocales();
	  //遍历数组的每个元素,依次获取所支持的国家和语言
	  for (int i=0;i<localeList.length;i++){
			// 打印出所支持的国家和语言
			System.out.println(localeList[i].getDisplayCountry()+"_"+localeList[i].getDisplayLanguage()+"=====>:"+localeList[i].getLanguage()+"_"+localeList[i].getCountry());
		}
	}
}
