package my.risktool.darkwingduck.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import my.risktool.darkwingduck.GeneralFunctions;
import my.risktool.darkwingduck.dao.ThreatDao;
import my.risktool.darkwingduck.model.Threat;

public class ThreatService {

	private static ThreatDao threatDao;
	private final HashMap<Integer, Threat> threatHashmap = new HashMap<>();

	public ThreatService() {
		threatDao = new ThreatDao();
	}

	public static void persist(Threat entity) {
		threatDao.openCurrentSessionwithTransaction();
		threatDao.persist(entity);
		threatDao.closeCurrentSessionwithTransaction();
	}

	public void update(Threat entity) {
		threatDao.openCurrentSessionwithTransaction();
		threatDao.update(entity);
		threatDao.closeCurrentSessionwithTransaction();
	}

	public static Threat findById(int id) {
		threatDao.openCurrentSession();
		Threat threat = threatDao.findById(id);
		threatDao.closeCurrentSession();
		return threat;
	}

	public void delete(int id) {
		threatDao.openCurrentSessionwithTransaction();
		Threat threat = threatDao.findById(id);
		threatDao.delete(threat);
		threatDao.closeCurrentSessionwithTransaction();
	}

	public List<Threat> findAllForHashmap() {
		threatDao.openCurrentSession();
		List<Threat> threats = threatDao.findAll();
		threatDao.closeCurrentSession();
		return threats;
	}
	
//	public List<Threat> findAll() {
//		threatDao.openCurrentSession();
//		List<Threat> threats = threatDao.findAll();
//		threatDao.closeCurrentSession();
//		return threats;
//	}

	public synchronized List<Threat> findAll() {
		return findAll(null);
	}

	public synchronized List<Threat> findAll(String stringFilter) {
		threatDao.openCurrentSession();
		populateHashMap();
		ArrayList<Threat> arrayList = new ArrayList<>();
//		System.out.println("threats" + threatCatalogue);
		for (Threat threat : threatHashmap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| threat.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(threat.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ThreatService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<Threat>() {

			@Override
			public int compare(Threat t1, Threat t2) {
				return (int) (t1.getId() - t2.getId());
			}
		});
		threatDao.closeCurrentSession();
		System.out.println("Arraylist" + arrayList);
		return arrayList;
	}

	public void deleteAll() {
		threatDao.openCurrentSessionwithTransaction();
		threatDao.deleteAll();
		threatDao.closeCurrentSessionwithTransaction();
	}

	public ThreatDao threatDao() {
		return threatDao;
	}

	public void save(Threat entity) {
//		if (entity.getId() > 0) {
//			update(entity);
//		} else {
			persist(entity);
//		}
	}

	private HashMap<Integer, Threat> populateHashMap() {
		
		List<Threat> threatList = findAllForHashmap();
		threatHashmap.clear();
		
		System.out.println("Hashmap" + threatList);
		for (Threat threat : threatList) {
			Threat t = new Threat();
			t.setId(threat.getId());
			t.setThreat(threat.getThreat());
			threatHashmap.put(threat.getId(), threat);
		}

		return threatHashmap;
	}


}
