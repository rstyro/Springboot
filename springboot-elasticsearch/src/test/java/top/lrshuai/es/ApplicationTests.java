package top.lrshuai.es;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import top.lrshuai.es.entity.Person;
import top.lrshuai.es.service.PersonService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private PersonService personService;
	
	@Test
	public void testSavePerson() {
		String name = "大帅哥";
		String introduce = "加一个超级大帅哥";
		Person person = new Person(name, 23, "男", new Date(), introduce);
		System.out.println(personService.savePerson(person));
	}
	
	@Test
	public void testUpdatePerson() {
		String name = "靓女";
		int age = 24;
		String sex = "女";
		String introduce = "这是一个非常非常非常非常漂亮的女孩。";
		Date birthday = new Date();
		Person person = new Person(name, age, sex, birthday, introduce);
		person.setId("mjupFmABhhkOZSWoch9i");
		personService.updatePerson(person);
	}
	
	@Test
	public void testFindPerson() {
		String id = "mjupFmABhhkOZSWoch9i";
		System.out.println(personService.findPerson(id));
	}
	
	@Test
	public void testDelPerson() {
		String id = "mjupFmABhhkOZSWoch9i";
		System.out.println(personService.delPerson(id));
	}
	
	@Test
	public void testQueryPerson() {
		Person person = new Person();
		person.setName("帅");
		person.setIntroduce("人");
//		person.setAge(27);
		Object obj  = personService.queryPerson(person);
		System.out.println(obj);
	}

}
