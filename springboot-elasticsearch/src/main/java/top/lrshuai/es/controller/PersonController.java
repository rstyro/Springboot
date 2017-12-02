package top.lrshuai.es.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import top.lrshuai.es.entity.Person;
import top.lrshuai.es.service.PersonService;

@RestController
public class PersonController {
	
	@Autowired
	private PersonService personservice;
	
	/**
	 * 新增
	 * @param person
	 * @return
	 */
	@PostMapping("/save/person")
	public Object savePerson(@RequestParam(name = "name") String name,
			@RequestParam(name = "sex") String sex,
			@RequestParam(name = "age") Integer age,
			@RequestParam(name = "introduce") String introduce,
			@RequestParam(name = "birthday")@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date birthday
			) {
		System.out.println("name="+name);
		System.out.println("sex="+sex);
		System.out.println("age="+age);
		System.out.println("introduce="+introduce);
		System.out.println("birthday="+birthday);
		Person person = new Person(name, age, sex, birthday, introduce);
		return personservice.savePerson(person);
	}
	
	
	
	/**
	 * 更新
	 * @param id 更新的数据id
	 * @param person 更新对象
	 * @return
	 */
	@PostMapping("/update/person/{id}")
	public Object updatePerson(@PathVariable("id") String id
			,@RequestParam(name="name") String name
			,@RequestParam(name="sex") String sex
			,@RequestParam(name="age") int age
			,@RequestParam(name="introduce") String introduce
			,@RequestParam(name="birthday") @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")Date birthday
			) {
		Person person = new Person(name, age, sex, birthday, introduce);
		person.setId(id);
		return personservice.updatePerson(person);
	}
	
	/**
	 * 删除
	 * @param id 删除的数据id
	 * @return
	 */
	@PostMapping("/del/person/{id}")
	public Object delPerson(@PathVariable("id") String id) {
		return personservice.delPerson(id);
	}
	
	/**
	 * 获取数据
	 * @param id 想要获取的数据
	 * @return
	 */
	@GetMapping("/person/{id}")
	public Object getPerson(@PathVariable("id") String id) {
		return personservice.findPerson(id);
	}
	
	/**
	 * 聚合查询
	 * @param person 
	 * @return
	 */
	@PostMapping("/query/person/_search")
	public Object queryPerson(@RequestParam(name="name",required=false) String name
			,@RequestParam(name="age",required=false,defaultValue="0") Integer age
			,@RequestParam(name="introduce",required=false) String introduce) {
		Person person = new Person(name, age, null, null, introduce);
		return personservice.queryPerson(person);
	}
	
}
