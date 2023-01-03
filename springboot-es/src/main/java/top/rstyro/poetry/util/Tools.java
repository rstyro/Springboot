package top.rstyro.poetry.util;

import com.spreada.utils.chinese.ZHConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Tools {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String s = "豳风";
        System.out.println("s="+s);
        String convert = ZHConverter.convert(s, 1);
        System.out.println(convert);
    }

    public static String cnToSimple(String content){
        if(StringUtils.isEmpty(content)){
            return content;
        }
        return ZHConverter.convert(content,1);
    }

    public static List<String> cnToSimple(Collection<?> list){
        if(ObjectUtils.isEmpty(list)){
            return new ArrayList<>();
        }
        return list.stream().map(i->Tools.cnToSimple((String)i)).collect(Collectors.toList());
    }


}
