package com.jgalante.balance.persistence;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import com.jgalante.balance.entity.Account;
import com.jgalante.balance.entity.Category;
import com.jgalante.balance.entity.Transaction;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.persistence.CrudDAO;
import com.jgalante.crud.util.Util;

public class TransactionDAO extends CrudDAO {

	private static final long serialVersionUID = 1L;
	
	@Override
	public <T extends BaseEntity> List<T> searchAll(Map<String, Boolean> sort) {
		getQueryParam().reset();
		addJoinFields("account");
		addJoinFields("category");
		addJoinFields("category.parent");
		Map<String, Boolean> tmpSort = new LinkedHashMap<String, Boolean>();
		if (sort != null) {
			tmpSort.putAll(sort);
		}
		tmpSort.put("id", false);
		return super.searchAll(tmpSort);
	}
	
	@Override
	public <T extends BaseEntity> List<T> search(int first, int pageSize,
			Map<String, Boolean> sort, Map<String, Object> filters) {
		addJoinFields("account");
		addJoinFields("category");
		addJoinFields("category.parent");
		return super.search(first, pageSize, sort, filters);
	}
	
//	public BigDecimal currentBalance() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
//		sb.append(Transaction.class.getName());
//		sb.append(" AS t ");
//		sb.append(" LEFT JOIN t.category c ");
//		sb.append(" WHERE ");
//		sb.append(" t.transactionDate <= :tDate");
//		sb.append(" AND t.account.type.id <> 3");
//		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
//		query.setParameter("tDate", Calendar.getInstance(), TemporalType.DATE);
//		return query.getSingleResult();
//	}
	
	public BigDecimal currentBalance(Account account, Category category) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" LEFT JOIN t.account a ");
		sb.append(" WHERE ");
		sb.append(" t.transactionDate <= :tDate ");
		sb.append(" AND t.account.type.id <> 3");
		if (account != null || category != null) {
			sb.append(" AND ");
			if (account != null) {
				sb.append(" a.id = ");
				sb.append(account.getId());
			}
			if (category != null) {
				if (account != null) {
					sb.append(" AND ");
				}
				if (category.getParent() == null)
					sb.append(" c.parent.id = ");
				else
					sb.append(" c.id = ");
				
				sb.append(category.getId());
			}
		}
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("tDate", Calendar.getInstance(), TemporalType.DATE);
		return query.getSingleResult();
	}

	public BigDecimal periodBalance(Account account, Calendar endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.account a ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" WHERE ");
		sb.append(" (t.transactionDate <= :dtEnd) ");
		if (account != null) {
			sb.append(" AND ");
			if (account != null) {
				sb.append(" a.id = ");
				sb.append(account.getId());
			}
		}
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
//		query.setParameter("dtStart", Util.beginOfMonth(startDate), TemporalType.DATE);
		Calendar date = Util.endOfMonth(endDate);
		Calendar today = Calendar.getInstance();
		query.setParameter("dtEnd", (date.compareTo(today) > 0 ? today : date), TemporalType.DATE);
		return query.getSingleResult();
	}
	
	public BigDecimal periodBalance(Account account, Category category, Calendar startDate, Calendar endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" LEFT JOIN t.account a ");
		sb.append(" WHERE ");
		sb.append(" (t.transactionDate >= :dtStart) ");
		sb.append(" AND (t.transactionDate <= :dtEnd) ");
		sb.append(" AND t.account.type.id <> 3");
		if (account != null || category != null) {
			sb.append(" AND ");
			if (account != null) {
				sb.append(" a.id = ");
				sb.append(account.getId());
			}
			if (category != null) {
				if (account != null) {
					sb.append(" AND ");
				}
				if (category.getParent() == null)
					sb.append(" c.parent.id = ");
				else
					sb.append(" c.id = ");
				sb.append(category.getId());
			}
		}
		
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("dtStart", Util.beginOfMonth(startDate), TemporalType.DATE);
		query.setParameter("dtEnd", Util.endOfMonth(endDate), TemporalType.DATE);
		return query.getSingleResult();
	}
	
	public BigDecimal periodBalanceForCreditCard(Account account, Category category, Calendar startDate, Calendar endDate) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT SUM(CASE c.positive WHEN 1 THEN t.value ELSE (t.value * -1) END) FROM ");
		sb.append(Transaction.class.getName());
		sb.append(" AS t ");
		sb.append(" LEFT JOIN t.account a ");
		sb.append(" LEFT JOIN t.category c ");
		sb.append(" WHERE ");
		sb.append(" (t.transactionDate >= :dtStart) ");
		sb.append(" AND (t.transactionDate <= :dtEnd) ");
		sb.append(" AND a.id = :accountId");
		sb.append(" AND c.positive is FALSE ");
		
		if (category != null) {
			sb.append(" AND ");
			if (category.getParent() == null)
				sb.append(" c.parent.id = ");
			else
				sb.append(" c.id = ");
			sb.append(category.getId());
		}
		
		TypedQuery<BigDecimal> query = getEntityManager().createQuery(sb.toString(),BigDecimal.class);
		query.setParameter("accountId", account.getId());
		query.setParameter("dtStart", Util.beginOfMonth(startDate), TemporalType.DATE);
		Calendar date = Util.endOfMonth(endDate);
		Calendar today = date;
		query.setParameter("dtEnd", (date.compareTo(today) > 0 ? today : date), TemporalType.DATE);
		return query.getSingleResult();
	}

	@Override
	public Class<? extends BaseEntity> getEntityClass() {
		if (super.getEntityClass() == null) {
			super.setEntityClass(Transaction.class);
		}
		return super.getEntityClass();
	}
}
