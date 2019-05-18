package com.otm.test;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.CountProjection;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.EnhancedProjection;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.PropertyProjection;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.criterion.SimpleProjection;

import com.otm.entities.Flat;
import com.otm.helper.SessionFactoryRegistry;

public class HQLTest {
	public static void main(String[] args) {
		SessionFactory sessionFactory = null;
		Session session = null;

		try {
			sessionFactory = SessionFactoryRegistry.getSessionFactory();
			session = sessionFactory.openSession();

			// showFlats(session);
			// showFlatsByType(session, "2 BHK");
			// showFlatsByTypeSqftAndFacing(session, "2 BHK", 1250, 1350,
			// "East");
			// showFlatsByTypesAndSqft(session, Arrays.asList(new String[] { "2
			// BHK", "3 BHK" }), 1350);
			// showNoOfFlatsByFlatType(session, "2 BHK");
			// showNoOfFlatsInBlocks(session);
			// showNoOfFlatsByFlatTypeAndFacing(session);
			// showFlatsByFloor(session, 9);
			// showFlatsByBlockName(session, "Marine");
			// showFlatsAndBlocksByFlatTypeSqftFacingAndUnits(session, "2 BHK",
			// "East", 1250, 300);
			// showFlatsBetweenFloors(session, 6, 13);
			// showFlatsByTypeNamedQuery(session, "3 BHK");

			// showFlatsC(session);
			// showFlatsTypeTypeC(session, "2 BHK");
			// showFlatsByTypeSqftAndFacingC(session, "2 BHK", 1250, 1350,
			// "East");
			// showFlatsByTypesAndSqftC(session, Arrays.asList(new String[] { "2
			// BHK", "3 BHK" }), 1350);
			// showNoOfFlatsByFlatTypeC(session, "2 BHK");
			// showNoOfFlatsInBlocksC(session);
			// showNoOfFlatsByFlatTypeAndFacingC(session);
			//showFlatsByBlockNameC(session, "Marine");
			//showFlatsAndBlocksByFlatTypeSqftFacingAndUnitsC(session, "2 BHK","East", 1250, 200);
			showFlatsByFlatTypeDC(session, "3 BHK");
		} finally {
			if (session != null) {
				session.close();
			}
			SessionFactoryRegistry.closeSessionFactory();
		}
	}

	private static void showFlats(Session session) {
		Query flatsQuery = null;
		List<Flat> flats = null;

		flatsQuery = session.createQuery("from Flat");
		flats = flatsQuery.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsC(Session session) {
		Criteria c = null;
		c = session.createCriteria(Flat.class);
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByType(Session session, String flatType) {
		Query query = null;
		List<Flat> flats = null;

		query = session.createQuery("from Flat f where f.flatType = ?");
		query.setString(0, flatType);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsTypeTypeC(Session session, String flatType) {
		Criteria c = null;

		//c = session.createCriteria(Flat.class).add(Restrictions.eq("flatType", flatType));
		c=session.createCriteria(Flat.class);
		SimpleExpression simpleExpression = Restrictions.eq("flatType", flatType);
		c=c.add(simpleExpression);
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByTypeSqftAndFacing(Session session, String flatType, int startSize, int maxSize,
			String facing) {
		Query query = null;
		List<Flat> flats = null;

		query = session.createQuery("from Flat f where f.flatType = ? and f.sqft between ? and ? and f.facing = ?");
		query.setString(0, flatType);
		query.setInteger(1, startSize);
		query.setInteger(2, maxSize);
		query.setString(3, facing);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByTypeSqftAndFacingC(Session session, String flatType, int startSize, int maxSize,
			String facing) {
		Criteria c = null;

		c = session.createCriteria(Flat.class);
		Criterion eq1 = Restrictions.eq("facing", facing);
		Criterion between = Restrictions.between("sqft", startSize, maxSize);
		SimpleExpression eq2 = Restrictions.eq("flatType", flatType);
		
		Criterion and1 = Restrictions.and(between,eq2);
		Criterion and2 = Restrictions.and(eq1,and1);
		
	    c = c.add(and2);
		
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByTypesAndSqft(Session session, List<String> flatTypes, int sqft) {
		Query query = null;
		List<Flat> flats = null;
		query = session.createQuery("from Flat f where f.flatType in (:flats) and f.sqft > :sqft");
		query.setParameterList("flats", flatTypes);
		query.setParameter("sqft", sqft);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByTypesAndSqftC(Session session, List<String> flatTypes, int sqft) {
		Criteria c = null;

		c = session.createCriteria(Flat.class)
				.add(Restrictions.and(Restrictions.in("flatType", flatTypes), Restrictions.gt("sqft", sqft)));
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showNoOfFlatsByFlatType(Session session, String flatType) {
		Query query = null;

		query = session.createQuery("select count(f) from Flat f where f.flatType = ?");
		query.setString(0, flatType);
		System.out.println(query.list().get(0));
	}

	private static void showNoOfFlatsByFlatTypeC(Session session, String flatType) {
		Criteria c = null;

		c = session.createCriteria(Flat.class);
		Criterion eq = Restrictions.eq("flatType", flatType);
		c = c.add(eq);
		Projection count = Projections.count("flatNo");
		c.setProjection(count);
		
		
		List<Integer> fc = c.list();
		System.out.println(fc.get(0));
	}

	private static void showNoOfFlatsInBlocks(Session session) {
		Query query = null;

		query = session.createQuery("select f.block.blockNo, count(f) from Flat f group by f.block.blockNo");
		List<Object[]> records = query.list();
		for (Object[] row : records) {
			System.out.println("block : " + row[0] + " count : " + row[1]);
		}
	}

	private static void showNoOfFlatsInBlocksC(Session session) {
		Criteria c = session.createCriteria(Flat.class);
		Projection groupProperty = Projections.groupProperty("block.blockNo");
		Projection count = Projections.count("flatNo");
		Projection projectionList = Projections.projectionList().add(groupProperty).add(count);
		
		c.setProjection(projectionList);
		List<Object[]> records = c.list();
		for (Object[] row : records) {
			System.out.println("blockNo : " + row[0] + " count : " + row[1]);
		}

	}

	private static void showNoOfFlatsByFlatTypeAndFacing(Session session) {
		Query query = null;
		List<Object[]> records = null;

		query = session.createQuery("select f.flatType, f.facing, count(f) from Flat f group by f.flatType, f.facing");
		records = query.list();
		for (Object[] row : records) {
			System.out.println("flatType : " + row[0] + " facing : " + row[1] + " count : " + row[2]);
		}

	}

	private static void showNoOfFlatsByFlatTypeAndFacingC(Session session) {
		Criteria c = session.createCriteria(Flat.class)
				.setProjection(Projections.projectionList().add(Projections.groupProperty("flatType"))
						.add(Projections.groupProperty("facing")).add(Projections.count("flatNo")));
		List<Object[]> records = c.list();
		for (Object[] row : records) {
			System.out.println("flatType : " + row[0] + " facing : " + row[1] + " count : " + row[2]);
		}
	}

	private static void showFlatsByFloor(Session session, int floor) {
		Query query = null;
		List<com.otm.bo.Flat> flats = null;

		query = session
				.createQuery("select new com.otm.bo.Flat(f.flatType, f.facing, f.sqft) from Flat f where f.floor >= ?");
		query.setInteger(0, floor);
		flats = query.list();
		for (com.otm.bo.Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByBlockName(Session session, String blockName) {
		Query query = null;
		List<Flat> flats = null;

		// query = session.createQuery("select f from Flat f inner join f.block
		// b where b.blockName = ?");
		query = session.createQuery("select f from Flat f where f.block.blockName = ?");
		query.setString(0, blockName);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByBlockNameC(Session session, String blockName) {
		Criteria c = session.createCriteria(Flat.class).createAlias("block", "b")
				.add(Restrictions.eq("b.blockName", blockName));
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsAndBlocksByFlatTypeSqftFacingAndUnits(Session session, String flatType, String facing,
			int sqft, int units) {
		Query query = null;
		List<Object[]> records = null;

		query = session.createQuery(
				"select f, b from Flat f inner join f.block b where f.flatType = ? and f.facing = ? and f.sqft >= ? and b.units >= ?");
		
		query.setString(0, flatType);
		query.setString(1, facing);
		query.setInteger(2, sqft);
		query.setInteger(3, units);
		records = query.list();
		for (Object[] row : records) {
			System.out.println(row[0] + "-" + row[1]);
		}
	}

	private static void showFlatsAndBlocksByFlatTypeSqftFacingAndUnitsC(Session session, String flatType, String facing,
			int sqft, int units) {
		Criteria c = session.createCriteria(Flat.class).createAlias("block", "b")
				.add(
						Restrictions.and(
								Restrictions.eq("flatType", flatType), Restrictions.eq("facing", facing),
						        Restrictions.ge("sqft", sqft), Restrictions.ge("b.units", units))
						
						);
	
		List<Flat> flats = c.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsBetweenFloors(Session session, int minFloor, int maxFloor) {
		Query query = null;
		List<Flat> flats = null;

		query = session.createQuery("select f from Flat f where f.floor between :minFloor and :maxFloor");
		query.setParameter("minFloor", minFloor);
		query.setParameter("maxFloor", maxFloor);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}

	private static void showFlatsByTypeNamedQuery(Session session, String flatType) {
		Query query = null;
		List<Flat> flats = null;
		query = session.getNamedQuery("sqlFlatsByType");
		query.setParameter("flatType", flatType);
		flats = query.list();
		for (Flat f : flats) {
			System.out.println(f);
		}
	}
	
	private static void showFlatsByFlatTypeDC(Session session, String flatType ) {
		DetachedCriteria dc = DetachedCriteria.forClass(Flat.class).add(Restrictions.eq("flatType", flatType));
		
		Criteria c = dc.getExecutableCriteria(session);
		List<Flat> flats = c.list();
		for(Flat f : flats) {
			System.out.println(f);
		}
	}
}