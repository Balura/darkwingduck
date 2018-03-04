package my.risktool.darkwingduck.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import my.risktool.darkwingduck.GeneralFunctions;
import my.risktool.darkwingduck.dao.ThreatCategoryDao;
import my.risktool.darkwingduck.model.ThreatCategory;

public class ThreatCategoryService {

	private static ThreatCategoryDao threatCategoryDao;
	private final HashMap<Integer, ThreatCategory> threatCategoryHashmap = new HashMap<>();

	public ThreatCategoryService() {
		threatCategoryDao = new ThreatCategoryDao();
	}

	public static void persist(ThreatCategory entity) {
		threatCategoryDao.openCurrentSessionwithTransaction();
		threatCategoryDao.persist(entity);
		threatCategoryDao.closeCurrentSessionwithTransaction();
	}

	public void update(ThreatCategory entity) {
		threatCategoryDao.openCurrentSessionwithTransaction();
		threatCategoryDao.update(entity);
		threatCategoryDao.closeCurrentSessionwithTransaction();
	}

	public static ThreatCategory findById(int id) {
		threatCategoryDao.openCurrentSession();
		ThreatCategory threatCategory = threatCategoryDao.findById(id);
		threatCategoryDao.closeCurrentSession();
		return threatCategory;
	}

	public void delete(int id) {
		threatCategoryDao.openCurrentSessionwithTransaction();
		ThreatCategory threat = threatCategoryDao.findById(id);
		threatCategoryDao.delete(threat);
		threatCategoryDao.closeCurrentSessionwithTransaction();
	}

	public List<ThreatCategory> findAllForHashmap() {
		threatCategoryDao.openCurrentSession();
		List<ThreatCategory> threats = threatCategoryDao.findAll();
		threatCategoryDao.closeCurrentSession();
		return threats;
	}
	
//	public List<ThreatCategory> findAll() {
//		threatCategoryDao.openCurrentSession();
//		List<ThreatCategory> threats = threatCategoryDao.findAll();
//		threatCategoryDao.closeCurrentSession();
//		return threats;
//	}

	public synchronized List<ThreatCategory> findAll() {
		return findAll(null);
	}
	
	public List<ThreatCategory> findAllThreatCategories() {
		threatCategoryDao.openCurrentSession();
		List<ThreatCategory> categories = threatCategoryDao.findAll();
		threatCategoryDao.closeCurrentSession();
		return categories;
	}

	public synchronized List<ThreatCategory> findAll(String stringFilter) {
		threatCategoryDao.openCurrentSession();
		populateHashMap();
		ArrayList<ThreatCategory> arrayList = new ArrayList<>();
//		System.out.println("threats" + threatCategory);
		for (ThreatCategory threat : threatCategoryHashmap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| threat.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(threat.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ThreatCategoryService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<ThreatCategory>() {

			@Override
			public int compare(ThreatCategory t1, ThreatCategory t2) {
				return (int) (t1.getId() - t2.getId());
			}
		});
		threatCategoryDao.closeCurrentSession();
		System.out.println("Arraylist" + arrayList);
		return arrayList;
	}

	public void deleteAll() {
		threatCategoryDao.openCurrentSessionwithTransaction();
		threatCategoryDao.deleteAll();
		threatCategoryDao.closeCurrentSessionwithTransaction();
	}

	public ThreatCategoryDao threatDao() {
		return threatCategoryDao;
	}

	public void save(ThreatCategory entity) {
		if (entity.getId() > 0) {
			update(entity);
		} else {
			persist(entity);
		}
	}

	private HashMap<Integer, ThreatCategory> populateHashMap() {
		
		List<ThreatCategory> categoryList = findAllForHashmap();
		threatCategoryHashmap.clear();
		
		System.out.println("Hashmap" + categoryList);
		for (ThreatCategory category : categoryList) {
			ThreatCategory t = new ThreatCategory();
			t.setId(category.getId());
			t.setCategory(category.getCategory());
			threatCategoryHashmap.put(category.getId(), category);
		}

		return threatCategoryHashmap;
	}

}
