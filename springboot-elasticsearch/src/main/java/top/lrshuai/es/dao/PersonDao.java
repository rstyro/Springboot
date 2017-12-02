package top.lrshuai.es.dao;


import top.lrshuai.es.entity.Person;

public interface PersonDao {
	public String save(Person person);
	public String update(Person person);
	public String deltele(String id);
	public Object find(String id);
	public Object query(Person person);
}
