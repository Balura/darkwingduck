package my.risktool.darkwingduck.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import my.risktool.darkwingduck.GeneralFunctions;
import my.risktool.darkwingduck.dao.ThreatCatalogueDao;
import my.risktool.darkwingduck.model.ThreatCatalogue;

public class ThreatCatalogueService {

	private static ThreatCatalogueDao threatCatalogueDao;
	private final HashMap<Integer, ThreatCatalogue> threatCatalogueHashmap = new HashMap<>();

	public ThreatCatalogueService() {
		threatCatalogueDao = new ThreatCatalogueDao();
	}

	public static void persist(ThreatCatalogue entity) {
		threatCatalogueDao.openCurrentSessionwithTransaction();
		threatCatalogueDao.persist(entity);
		threatCatalogueDao.closeCurrentSessionwithTransaction();
	}

	public void update(ThreatCatalogue entity) {
		threatCatalogueDao.openCurrentSessionwithTransaction();
		threatCatalogueDao.update(entity);
		threatCatalogueDao.closeCurrentSessionwithTransaction();
	}

	public static ThreatCatalogue findById(int id) {
		threatCatalogueDao.openCurrentSession();
		ThreatCatalogue threat = threatCatalogueDao.findById(id);
		threatCatalogueDao.closeCurrentSession();
		return threat;
	}

	public void delete(int id) {
		threatCatalogueDao.openCurrentSessionwithTransaction();
		ThreatCatalogue threat = threatCatalogueDao.findById(id);
		threatCatalogueDao.delete(threat);
		threatCatalogueDao.closeCurrentSessionwithTransaction();
	}

	public List<ThreatCatalogue> findAllForHashmap() {
		threatCatalogueDao.openCurrentSession();
		List<ThreatCatalogue> threats = threatCatalogueDao.findAll();
		threatCatalogueDao.closeCurrentSession();
		return threats;
	}
	
//	public List<ThreatCatalogue> findAll() {
//		threatCatalogueDao.openCurrentSession();
//		List<ThreatCatalogue> threats = threatCatalogueDao.findAll();
//		threatCatalogueDao.closeCurrentSession();
//		return threats;
//	}

	public synchronized List<ThreatCatalogue> findAll() {
		return findAll(null);
	}
	
	public List<ThreatCatalogue> findAllThreats() {
		threatCatalogueDao.openCurrentSession();
		List<ThreatCatalogue> threats = threatCatalogueDao.findAllThreats();
		threatCatalogueDao.closeCurrentSession();
		return threats;
	}

	public synchronized List<ThreatCatalogue> findAll(String stringFilter) {
		threatCatalogueDao.openCurrentSession();
		populateHashMap();
		ArrayList<ThreatCatalogue> arrayList = new ArrayList<>();
//		System.out.println("threats" + threatCatalogue);
		for (ThreatCatalogue threat : threatCatalogueHashmap.values()) {
			try {
				boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
						|| threat.toString().toLowerCase().contains(stringFilter.toLowerCase());
				if (passesFilter) {
					arrayList.add(threat.clone());
				}
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(ThreatCatalogueService.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Collections.sort(arrayList, new Comparator<ThreatCatalogue>() {

			@Override
			public int compare(ThreatCatalogue t1, ThreatCatalogue t2) {
				return (int) (t1.getId() - t2.getId());
			}
		});
		threatCatalogueDao.closeCurrentSession();
		System.out.println("Arraylist" + arrayList);
		return arrayList;
	}

	public void deleteAll() {
		threatCatalogueDao.openCurrentSessionwithTransaction();
		threatCatalogueDao.deleteAll();
		threatCatalogueDao.closeCurrentSessionwithTransaction();
	}

	public ThreatCatalogueDao threatDao() {
		return threatCatalogueDao;
	}

	public void save(ThreatCatalogue entity) {
		if (entity.getId() > 0) {
			update(entity);
		} else {
			persist(entity);
		}
	}

	private HashMap<Integer, ThreatCatalogue> populateHashMap() {
		
		List<ThreatCatalogue> threatList = findAllForHashmap();
		threatCatalogueHashmap.clear();
		
		System.out.println("Hashmap" + threatList);
		for (ThreatCatalogue threat : threatList) {
			ThreatCatalogue t = new ThreatCatalogue();
			t.setId(threat.getId());
			t.setThreat(threat.getThreat());
			threatCatalogueHashmap.put(threat.getId(), threat);
		}

		return threatCatalogueHashmap;
	}

}
