package top.lrshuai.es.service;

import java.util.Map;

import top.lrshuai.es.entity.Person;

public interface PersonService {
	public Map<String, Object> savePerson(Person p);
	public Map<String, Object> updatePerson(Person p);
	public Map<String, Object> delPerson(String id);
	public Map<String, Object> findPerson(String id);
	public Map<String, Object> queryPerson(Person p);
}
