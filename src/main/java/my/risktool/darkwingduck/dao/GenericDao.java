package my.risktool.darkwingduck.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {
//	T findById(ID id, boolean lock);
//	
//	List<T> findByExample(T exampleInstance);
//
//	T makePersistent(T entity);

//	void makeTransient(T entitiy);

	public void persist(T entity);

	public void update(T entity);

	public T findById(int id);

	public void delete(T entity);

	public List<T> findAll();

	public void deleteAll();

}
